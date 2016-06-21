package com.razz.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

public class SongStat {

	@Id
    private String id;
	private Song song;
	private int count;
	private int rank;
	private List<Venue> venues;
	private long firstDtg;
	private long lastDtg;
	
	public SongStat(Song song) {
		this.song = song;
		count = -1;
		rank = -1;
		venues = new ArrayList<Venue>(0);
		firstDtg = -1;
		lastDtg = -1;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public List<Venue> getVenues() {
		return venues;
	}

	public void setVenues(List<Venue> venues) {
		this.venues = venues;
	}

	public long getFirstDtg() {
		return firstDtg;
	}

	public void setFirstDtg(long firstDtg) {
		this.firstDtg = firstDtg;
	}

	public long getLastDtg() {
		return lastDtg;
	}

	public void setLastDtg(long lastDtg) {
		this.lastDtg = lastDtg;
	}

	@Override
	public String toString() {
		return "SongStats [id=" + id + ", song=" + song + ", count=" + count + ", rank=" + rank + ", venues=" + venues
				+ ", firstDtg=" + firstDtg + ", lastDtg=" + lastDtg + "]";
	}

}
