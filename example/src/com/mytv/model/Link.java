package com.mytv.model;

public class Link {
      private long linkId;
      private int rank;
      private String url;
      private String createdTime;
      private String updatedTime;
	public long getLinkId() {
		return linkId;
	}
	public Link(long linkId, int rank, String url, String createdTime,
			String updatedTime) {
		super();
		this.linkId = linkId;
		this.rank = rank;
		this.url = url;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}
	public void setLinkId(long linkId) {
		this.linkId = linkId;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
