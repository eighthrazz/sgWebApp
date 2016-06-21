package com.razz.model;

import org.springframework.data.annotation.Id;

public class Venue {

	@Id
    private String id;
	private String name;
	private String location;
	
	public Venue() {
		this("");
	}
	
	public Venue(String name) {
		this.name = name;
		this.location = "";
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
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Venue [id=" + id + ", name=" + name + ", location=" + location + "]";
	}

}
