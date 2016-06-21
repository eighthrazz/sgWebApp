package com.razz.model;

import org.springframework.data.annotation.Id;

public class Flyer {

	@Id
    private String id;
	private byte[] imageData;

	public Flyer() {
		this(new byte[]{});
	}
	
	public Flyer(byte[] imageData) {
		this.imageData = imageData;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	@Override
	public String toString() {
		return "Flyer [id=" + id + ", flyer.lenth=" + (imageData != null ? imageData.length : 0) + "]";
	}
	
}
