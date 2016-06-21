package com.razz.model;

import java.util.Comparator;

import org.springframework.data.annotation.Id;

public class Song implements Comparator<Song> {

	@Id
    private String id;
	private String name;
	
	public Song(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int compare(Song o1, Song o2) {
		return o1.getName().compareTo(o2.getName());
	}
	
	@Override
	public String toString() {
		return "Song [id=" + id + ", name=" + name + "]";
	}

}
