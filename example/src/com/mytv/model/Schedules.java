package com.mytv.model;

import java.util.ArrayList;
import java.util.List;

public class Schedules {
    private String time;
    private String titles;
    private String name;
    private int id;
    private String des;
    private String logoUrl;
    private String chanelType;
    private int iDelete;
    
    private List<Link> linkList=new ArrayList<Link>();
    
	public Schedules(String time, String titles) {
		super();
		this.time = time;
		this.titles = titles;
		this.setLinkList(null);
		this.setName(null);
		this.setId(0);
		this.setDes(null);
		this.setLogoUrl(null);
		this.setChanelType(null);
		this.setiDelete(0);
	}

    
   
	
	public Schedules() {
		super();
	}




	public Schedules(String name, String logoUrl, String chanelType) {
		super();
		this.name = name;
		this.logoUrl = logoUrl;
		this.chanelType = chanelType;
		this.setiDelete(0);
	}




	public Schedules(String time, String titles, String name, int id,
			String des, String logoUrl, List<Link> linkList,String chanelType) {
		super();
		this.time = time;
		this.titles = titles;
		this.name = name;
		this.id = id;
		this.des = des;
		this.logoUrl = logoUrl;
		this.linkList = linkList;
		this.chanelType=chanelType;
		this.setiDelete(0);
	}



	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTitles() {
		return titles;
	}
	public void setTitles(String titles) {
		this.titles = titles;
	}

	public List<Link> getLinkList() {
		return linkList;
	}

	public void setLinkList(List<Link> linkList) {
		this.linkList = linkList;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getDes() {
		return des;
	}


	public void setDes(String des) {
		this.des = des;
	}


	public String getLogoUrl() {
		return logoUrl;
	}


	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}



	public String getChanelType() {
		return chanelType;
	}



	public void setChanelType(String chanelType) {
		this.chanelType = chanelType;
	}




	public int getiDelete() {
		return iDelete;
	}




	public void setiDelete(int iDelete) {
		this.iDelete = iDelete;
	}
    
}
