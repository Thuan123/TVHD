package com.mytv.model;

import java.util.List;

public class LichChieu {
    private String name;
    private List<Chanel> chanelList;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LichChieu(String name, List<Chanel> chanelList) {
		super();
		this.name = name;
		this.chanelList = chanelList;
	}
	public List<Chanel> getChanelList() {
		return chanelList;
	}
	public void setChanelList(List<Chanel> chanelList) {
		this.chanelList = chanelList;
	}
}
