package com.mytv.model;

import java.util.ArrayList;
import java.util.List;

public class Episode {
	private long id;
	private String name;
	private String EpisoDes;
	private String logoUrl;
	private List<Video> videoList = new ArrayList<Video>();
	private String duration;
	private String released;

	public long getId() {
		return id;
	}

	public Episode(long id, String name, String episoDes, String logoUrl,
			List<Video> videoList, String duration, String released) {
		super();
		this.id = id;
		this.name = name;
		EpisoDes = episoDes;
		this.logoUrl = logoUrl;
		this.videoList = videoList;
		this.duration = duration;
		this.released = released;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEpisoDes() {
		return EpisoDes;
	}

	public void setEpisoDes(String episoDes) {
		EpisoDes = episoDes;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public List<Video> getVideoList() {
		return videoList;
	}

	public void setVideoList(List<Video> videoList) {
		this.videoList = videoList;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getReleased() {
		return released;
	}

	public void setReleased(String released) {
		this.released = released;
	}
}
