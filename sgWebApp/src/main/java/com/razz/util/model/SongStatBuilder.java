package com.razz.util.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.razz.model.Event;
import com.razz.model.Song;
import com.razz.model.SongStat;

public class SongStatBuilder {

	private final List<Song> allSongs;
	private final List<Event> allEvents;
	private final Map<Song, SongStat> songStatsMap;

	public SongStatBuilder(List<Song> allSongs, List<Event> allEvents) {
		this.allSongs = allSongs;
		this.allEvents = allEvents;
		songStatsMap = new HashMap<Song, SongStat>();
	}

	public void calculate() {
		synchronized (songStatsMap) {
			//
			songStatsMap.clear();

			//
			for(Song song : allSongs) {
				final SongStat songStats = new SongStat(song);
				songStatsMap.put(song, songStats);
			}

			//
			calculateCountAndRank();
			calculateFirstLastPlayed();
		}
	}

	public SongStat get(Song song) {
		synchronized (songStatsMap) {
			final SongStat songStats = songStatsMap.get(song);
			return songStats;
		}
	}

	void calculateCountAndRank() {
		//
		final Map<String,Integer> countMap = getCountMap();
		
		//
		final Map<String,Integer> rankMap = getRankMap(countMap);

		//
		for(Song song : allSongs) {
			final String key = song.getName();
			final int count = countMap.get(key);
			final int rank = rankMap.get(key);
			final SongStat songStats = songStatsMap.get(song);
			songStats.setCount(count);
			songStats.setRank(rank);
		}
	}
	
	Map<String,Integer> getCountMap() {
		final Map<String,Integer> countMap = new HashMap<>();
		for(Event event : allEvents) {
			final List<Song> songList = event.getSongs();
			for(Song song : songList) {
				final String key = song.getName();
				if( !countMap.containsKey(key) )
					countMap.put(key, 0);
				final int count = countMap.get(key) + 1;
				countMap.put(key, count);
			}
		}
		return countMap;
	}
	
	Map<String,Integer> getRankMap(Map<String,Integer> countMap) {
		//
		final Map<String,Integer> sortedCountMap = sortByValue(countMap);
		
		//
		final int mapLength = sortedCountMap.size();
		final List<String> keyListDecending = new ArrayList<>(mapLength);
		for(String name : sortedCountMap.keySet()) {
			keyListDecending.add(name);
		}
		Collections.reverse(keyListDecending);
		
		//
		int rank = 1;
		int previousCount = -1;
		final Map<String,Integer> rankMap = new HashMap<>();
		for(String name : keyListDecending) {
			final int count = sortedCountMap.get(name);
			if(count < previousCount)
				rank++;
			System.out.format("name=%s, count=%s, previousCount=%s, rank=%s%n", 
					name, count, previousCount, rank);
			rankMap.put(name, rank);
			previousCount = count;
		}
		return rankMap;
	}
	
	static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
		List<Map.Entry<K, V>> list =
				new LinkedList<>( map.entrySet() );
		Collections.sort( list, new Comparator<Map.Entry<K, V>>()
		{
			@Override
			public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
			{
				return (o1.getValue()).compareTo( o2.getValue() );
			}
		} );

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list)
		{
			result.put( entry.getKey(), entry.getValue() );
		}
		return result;
	}
	
	void calculateFirstLastPlayed() {
		//
		final Map<String,SongDtg> songDtgMap = getSongDtgMap();
		
		//
		for(Song song : songStatsMap.keySet()) {
			final String key = song.getName();
			final SongDtg songDtg = songDtgMap.get(key);
			final SongStat songStat = songStatsMap.get(song);
			songStat.setFirstDtg(songDtg.firstDtg);
			songStat.setLastDtg(songDtg.lastDtg);
		}
	}
	
	Map<String,SongDtg> getSongDtgMap() {
		//
		final Map<String,SongDtg> songDtgMap = new HashMap<>();
		
		//
		for(Event event : allEvents) {
			final List<Song> songList = event.getSongs();
			for(Song song : songList) {
				final String key = song.getName();
				if( !songDtgMap.containsKey(key) )
					songDtgMap.put(key, new SongDtg());
				final SongDtg songDtg = songDtgMap.get(key);
				final long eventDtg = event.getDate();
				if(eventDtg < songDtg.firstDtg)
					songDtg.firstDtg = eventDtg;
				if(eventDtg > songDtg.lastDtg)
					songDtg.lastDtg = eventDtg;
			}
		}
		return songDtgMap;
	}
	
	class SongDtg {
		long firstDtg = Long.MAX_VALUE;
		long lastDtg = Long.MIN_VALUE;
	}


}
