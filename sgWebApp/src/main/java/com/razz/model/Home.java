package com.razz.model;

public class Home {

	private String backgroundImage;
	private String facebookUrl;
	private String twitterUrl;
	private String youTubeUrl;
	private String googlePlayUrl;
	
	public Home() {
		backgroundImage = "";
		facebookUrl = "";
		twitterUrl = "";
		youTubeUrl = "";
		googlePlayUrl = "";
	}

	public Home(String backgroundImage, String facebookUrl, String twitterUrl, String youTubeUrl,
			String googlePlayUrl) {
		this.backgroundImage = backgroundImage;
		this.facebookUrl = facebookUrl;
		this.twitterUrl = twitterUrl;
		this.youTubeUrl = youTubeUrl;
		this.googlePlayUrl = googlePlayUrl;
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public String getFacebookUrl() {
		return facebookUrl;
	}

	public void setFacebookUrl(String facebookUrl) {
		this.facebookUrl = facebookUrl;
	}

	public String getTwitterUrl() {
		return twitterUrl;
	}

	public void setTwitterUrl(String twitterUrl) {
		this.twitterUrl = twitterUrl;
	}

	public String getYouTubeUrl() {
		return youTubeUrl;
	}

	public void setYouTubeUrl(String youTubeUrl) {
		this.youTubeUrl = youTubeUrl;
	}

	public String getGooglePlayUrl() {
		return googlePlayUrl;
	}

	public void setGooglePlayUrl(String googlePlayUrl) {
		this.googlePlayUrl = googlePlayUrl;
	}

	@Override
	public String toString() {
		return "Home [backgroundImage=" + backgroundImage + ", facebookUrl=" + facebookUrl + ", twitterUrl="
				+ twitterUrl + ", youTubeUrl=" + youTubeUrl + ", googlePlayUrl=" + googlePlayUrl + "]";
	}
	
}
