package com.razz.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.razz.model.Event;
import com.razz.model.Flyer;
import com.razz.model.Home;
import com.razz.model.Song;
import com.razz.model.SongStat;
import com.razz.model.repository.CalendarEventRepository;
import com.razz.model.repository.EventRepository;
import com.razz.model.repository.SongRepository;
import com.razz.model.repository.SongStatsRepository;
import com.razz.service.ImageService;
import com.razz.service.LoggerService;
import com.razz.util.image.ImageUtil;
import com.razz.util.model.EventUtil;
import com.razz.util.model.SgDbUtilService;
import com.razz.util.model.SgDbUtilServiceImpl;

@Controller
public class MainController {

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private SongRepository songRepository;
	
	@Autowired
	private SongStatsRepository songStatsRepository;
	
	@Autowired
	private CalendarEventRepository calendarEventRepository;
	
	@Autowired
	private SgDbUtilService sgDbUtilService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private LoggerService logger;
	
	private StringBuilder adminLog;
	
	public MainController() {
		adminLog = new StringBuilder();
	}
	
	@RequestMapping("/greeting")
	public String greeting(@RequestParam(value="name", required=false, defaultValue="World")String name, Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}
	
	@RequestMapping("/song")
	public String song(@RequestParam(value="name", required=true) String name, Model model) {
		final Song song = songRepository.findByName(name);
		final SongStat songStat = songStatsRepository.findBySong(song);
		logger.debug(getClass(), "song() %s, %s", song, songStat); 
		model.addAttribute("song", song);
		model.addAttribute("songStat", songStat);
		return "song";
	}
	
	@RequestMapping("/songs")
	public String songs(Model model) {
		final Sort sort = new Sort(Direction.ASC, "song.name");
		final List<SongStat> songStats = songStatsRepository.findAll(sort);
		model.addAttribute("songStats", songStats);
		return "songs";
	}

	@RequestMapping(value = "/timeline", method = RequestMethod.GET)
	public String events(Model model) {
		return "timeline";
	}
    
	@RequestMapping(value = "/events/{page}", method = RequestMethod.GET)
	public String events(@PathVariable("page") int page, Model model) {
		logger.debug(getClass(), "events.page=%s", page); 
		
		//
		model.addAttribute("page", page);
		model.addAttribute("nextPage", page+1);
		
		//
		final boolean reverse = true;
		final List<Event> events = getOrderedEvents(reverse);
		if(page < 0) {
			logger.debug(getClass(), "events.size=%s", events.size());
			model.addAttribute("events", events);
		}
		
		//
		else {
			//
			final PagedListHolder<Event> eventPageListHolder = new PagedListHolder<>(events);
			eventPageListHolder.setPageSize(4);
			eventPageListHolder.setPage(page);
			
			//
			logger.debug(getClass(), "events.size=%s", eventPageListHolder.getPageList().size());
			model.addAttribute("events", eventPageListHolder.getPageList());
		}
		
		//
		return "events :: resultsList";
	}
	
    List<Event> getOrderedEvents(boolean reverse) {
    	final List<Event> events = eventRepository.findAll();
    	Collections.sort(events, new Comparator<Event>() {
			public int compare(Event o1, Event o2) {
				if(reverse)
					return Long.compare(o2.getDate(), o1.getDate());
				else
					return Long.compare(o1.getDate(), o2.getDate());
			}
		});
    	return events;
    }
    
    @RequestMapping(value = "flyer", method = RequestMethod.GET, produces = "image/jpg")
    public @ResponseBody byte[] flyer(@RequestParam(value="eventId")String eventId)  {
        try {
        	final Event event = eventRepository.findOne(eventId);
    		final Flyer flyer = event.getFlyer();
    		final byte[] flyerData = flyer.getImageData() != null ? flyer.getImageData() : getNoImageAvailableJpeg();
    		logger.debug(getClass(), "eventId=%s, flyerId=%s, flyer.length=%s", 
    				event.getId(), flyer.getId(), flyerData != null ? flyerData.length : "null"); 
            return flyerData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    byte[] getNoImageAvailableJpeg() {
    	try {
    		final String text = "No Image Available";
    		return ImageUtil.generateJpg(text, 200, 50);
    	}
    	catch(Exception e) {
    		logger.error(getClass(), e, "getNoImageAvailableJpeg()");
    		return null;
    	}
    }
    
    @RequestMapping("/calendar")
	public String calendar(Model model) {
    	final List<Event> events = getPublishedGoogleCalendarEvents();
		logger.debug(getClass(), "calendar : events.size=%s%n", events.size()); 
    	model.addAttribute("events", events);
		return "calendar";
	}
    
    List<Event> getPublishedGoogleCalendarEvents() {
    	final List<Event> events = calendarEventRepository.findAll();
    	EventUtil.filterPublished(events);
		EventUtil.filterUpcomingDates(events);
		EventUtil.sortByDtgAscending(events);
		return events;
    }
    
    @RequestMapping("/admin")
	public String admin(Model model) {
		logger.debug(getClass(), "admin"); 
		return "admin";
	}
    
    @RequestMapping("/admin/reloadDatabase")
    public @ResponseBody void reloadDatabase() {
    	logger.debug(getClass(), "reloadDatabase");
    	try {
    		final SgDbUtilServiceImpl.LogListener logListener = new SgDbUtilServiceImpl.LogListener() {
    			public void log(String text) {
    				adminLog.append(text).append("\n");
    			}
    		}; 
    		sgDbUtilService.run(logListener);
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @RequestMapping("/admin/log")
	public @ResponseBody String log() {
		logger.debug(getClass(), "log"); 
		return adminLog.toString();
	}
    
    @RequestMapping( {"/", "/home"} )
    public String home(Model model) {
    	final Home home = getHomeModel();
    	logger.debug(getClass(), "home : %s", home);
    	model.addAttribute("home", home);
    	return "home";
    }
    
    @RequestMapping("/homeOld")
    public String homeOld(Model model) {
    	final Home home = getHomeModel();
    	logger.debug(getClass(), "homeOld : %s", home);
    	model.addAttribute("home", home);
    	return "homeOld";
    }
    
    Home getHomeModel() {
    	final String backgroundImage = getBackgroudImageFilename();
    	final String facebookUrl = "https://www.facebook.com/sgomaha";
    	final String twitterUrl = "https://twitter.com/satchelgrande";
    	final String youTubeUrl = "https://www.youtube.com/channel/UCRbDjEwINspjMBOmq7Vz-HA";
    	final String googlePlayUrl = "https://play.google.com/store/search?q=satchel+grande";
    	final Home home = new Home(backgroundImage, facebookUrl, twitterUrl, youTubeUrl, googlePlayUrl);
    	return home;
    }
    
    String getBackgroudImageFilename() {
    	String filename = "";
    	try {
    		filename = imageService.getRandomFileName("static/img/bg", "jpg");
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	return filename;
    }
    
    @RequestMapping("/news")
    public String news(Model model) {
    	logger.debug(getClass(), "news");
    	return "news";
    }
    
    @RequestMapping("/parallax")
    public String parallax(Model model) {
    	logger.debug(getClass(), "parallax");
    	final Home home = getHomeModel();
    	model.addAttribute("home", home);
    	return "parallax";
    }
    
}
