package com.mytv.model;

public class Video {
	private long id;
	private int rank;
	private String videoUrl;
	private String createdTime;
	private String updatedTime;

	public Video(long id, int rank, String videoUrl, String createdTime,
			String updatedTime) {
		super();
		this.id = id;
		this.rank = rank;
		this.videoUrl = videoUrl;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

}
