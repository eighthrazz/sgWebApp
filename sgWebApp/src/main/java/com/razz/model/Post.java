package com.razz.model;

import org.springframework.data.annotation.Id;

public class Post {

	public static enum Type {
		facebook("facebook"),
		twitter("twitter");
		
		final String key;
		
		private Type(String key) {
			this.key = key;
		}
		
		public String getKey() {
			return key;
		}
		
		@Override 
		public String toString() {
			return key;
		}
	}
	
	@Id
	private String id;
	private Type type;
	private long date;
	private String message;
	private String link;
	private String imageUrl;
	
	public Post() {
		this(null, null, -1L, null, null, null);
	}
	
	public Post(String id, Type type, long date, String message, String link, String imageUrl) {
		this.id = id;
		this.type = type;
		this.date = date;
		this.message = message;
		this.link = link;
		this.imageUrl = imageUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (date ^ (date >>> 32));
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		if (date != other.date)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imageUrl == null) {
			if (other.imageUrl != null)
				return false;
		} else if (!imageUrl.equals(other.imageUrl))
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", type=" + type + ", date=" + date + ", message=" + message + ", link=" + link
				+ ", imageUrl=" + imageUrl + "]";
	}
	
}
