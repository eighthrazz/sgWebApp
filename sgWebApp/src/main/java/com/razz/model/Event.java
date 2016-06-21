package com.razz.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

public class Event {

	@Id
    private String id;
	private long date;
	private Venue venue;
	private List<Song> songs;
	private Flyer flyer;
	private String description;
	
	public Event() {
		this(0L, new Venue(), new ArrayList<Song>(), new Flyer(), "");
	}
	
	public Event(long date, Venue venue, List<Song> songs, Flyer flyer, String description) {
		this.date = date;
		this.venue = venue;
		this.songs = songs;
		this.flyer = flyer;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}
	
	public Flyer getFlyer() {
		return flyer;
	}

	public void setFlyer(Flyer flyer) {
		this.flyer = flyer;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return String.format("Event [id=%s, date=%s, flyer=%s, %s, %s",
				id, date, flyer, venue, songs);
	}
	
}
