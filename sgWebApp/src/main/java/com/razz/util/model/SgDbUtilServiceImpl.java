package com.razz.util.model;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razz.model.Event;
import com.razz.model.Flyer;
import com.razz.model.Song;
import com.razz.model.SongStat;
import com.razz.model.Venue;
import com.razz.model.repository.EventRepository;
import com.razz.model.repository.FlyerRepository;
import com.razz.model.repository.SongRepository;
import com.razz.model.repository.SongStatsRepository;
import com.razz.model.repository.VenueRepository;
import com.razz.util.spreadsheet.CsvReader;
import com.razz.util.spreadsheet.CsvTranslator;

@Service
public class SgDbUtilServiceImpl implements SgDbUtilService {

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private SongRepository songRepository;
	
	@Autowired
	private VenueRepository venueRepository;

	@Autowired
	private FlyerRepository flyerRepository;
	
	@Autowired
	private SongStatsRepository songStatsRepository;
	
	private LogListener logListener;
	private boolean running;
	
	public SgDbUtilServiceImpl() {
		this.logListener = null;
		running = false;
	}
	
	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void run(LogListener logListener) throws Exception {
		if(running)
			return;
		this.logListener = logListener;
		try {
			running = true;
			_run();
		}
		finally {
			running = false;
		}
	}
	
	private synchronized void _run() throws Exception {
		log("running...");
		
		//
		final List<Event> events = getEvents();
		
		//
		eventRepository.deleteAll();
		songRepository.deleteAll();
		venueRepository.deleteAll();
		flyerRepository.deleteAll();
		songStatsRepository.deleteAll();
		
		//
		for(Event event : events) {
			//
			final List<Song> songs = event.getSongs();
			for(Song song : songs) {
				final String name = song.getName();
				final boolean containsSong = songRepository.findByName(name) != null;
				if(!containsSong) {
					songRepository.save(song);
				}
			}
			
			//
			final Venue venue = event.getVenue();
			final String venueName = venue.getName();
			final boolean containsVenue = venueRepository.findByName(venueName) != null;
			if(!containsVenue) {
				venueRepository.save(venue);
			}
			
			//
			final Flyer flyer = event.getFlyer();
			final byte[] imageData = flyer.getImageData();
			if(imageData != null && imageData.length > 0)
				flyerRepository.save(flyer);
			
			//
			eventRepository.save(event);
		}
		
		//
		final List<Song> allSongs = songRepository.findAll();
		final List<Event> allEvents = eventRepository.findAll();
		final SongStatBuilder songStatBuilder = new SongStatBuilder(allSongs, allEvents);
		log("building song stats...");
		songStatBuilder.calculate();
		for(Song song : allSongs) {
			final boolean containsSong = songStatsRepository.findBySong(song) != null;
			if(!containsSong) {
				final SongStat songStats = songStatBuilder.get(song);
				songStatsRepository.save(songStats);
			}
		}
		final List<SongStat> allSongStat = songStatsRepository.findAll();
		
		//
		log( String.format("total events: %s%n", allEvents.size()) );
		log( String.format("total songs: %s%n", allSongs.size()) );
		log( String.format("total songStats: %s%n", allSongStat.size()) );
	}
	
	private void log(String text) {
		System.out.format("%s%n", text);
		logListener.log(text);
	}
	
	List<Event> getEvents() {
		//
		final CsvTranslator<Event> eventTranslator = new CsvTranslator<Event>() {
			public Event translate(Map<String, List<String>> data) {
				//
				final List<String> venueVal = data.get("#VENUE");
				final String venueName = venueVal.get(0);
				final Venue venue = new Venue(venueName);
				
				//
				final List<String> dateVal = data.get("#DATE");
				final String dateStr = dateVal.get(0);
				final String dateFormat = "MM/dd/yyyy";
				final SimpleDateFormat dateFormater = new SimpleDateFormat(dateFormat);
				long dateMillis = 0;
				try {
					final Date date = dateFormater.parse(dateStr);
					dateMillis = date.getTime();
				}
				catch(Exception e) {
					e.printStackTrace();
				}

				//
				final List<String> songVal = data.get("#SONG");
				final List<Song> songs = new ArrayList<>();
				for(String s : songVal) {
					final Song song = new Song(s);
					songs.add(song);
				}
				
				//
				final byte[] flyerData = getFlyer(dateMillis);
				final Flyer flyer = new Flyer(flyerData);
				final String description = null;
				
				//
				final Event event = new Event(dateMillis, venue, songs, flyer, description);
				return event;
			}
		};
		
		//
		final char keyIdentifier = '#';
		final CsvReader<Event> eventReader = new CsvReader<>(keyIdentifier, eventTranslator);
		final List<Event> eventList = new ArrayList<>();
		try {
			final File file = Paths.get(System.getProperty("user.dir"), "data", "SgDb.csv").toFile();
			eventList.addAll( eventReader.read(file) );
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return eventList;
	}
	
	byte[] getFlyer(long dateMillis) {
		//
		if(dateMillis <= 0)
			return null;
		
		//
		final String dateFormat = "yyyy_MM_dd";
		final SimpleDateFormat dateFormater = new SimpleDateFormat(dateFormat);
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateMillis);
		final String flyerFileName = dateFormater.format(calendar.getTime()).concat(".jpg");
		final File file = Paths.get(System.getProperty("user.dir"), "data", "FLYERS", flyerFileName).toFile();
		if( !file.exists() ) {
			log( String.format("%s does not exist%n", file) );
			return null;
		}
		
		// else
		byte[] flyer = null;
		try(FileInputStream fis = new FileInputStream(file)) {
			final long length = file.length();
			flyer = new byte[(int)length];
			fis.read(flyer);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return flyer;
	}
	
	public interface LogListener {
		public void log(String text);
	}
	
}
