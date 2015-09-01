package com.mytv.model;
import java.util.ArrayList;
import java.util.List;

import com.mytv.model.Schedules;
public class Chanel {
    private String date;
    private String desc;
    private String Name;
    private long id;
    private String logoUrl;
    private String type;
    private List<Schedules> list=new ArrayList<Schedules>();
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public Chanel(String date, String name, long id, String logoUrl,
			List<Schedules> list) {
		super();
		this.date = date;
		Name = name;
		this.id = id;
		this.logoUrl = logoUrl;
		this.list = list;
		this.desc=null;
		this.type=null;
	}
	public Chanel(String date, String desc, String name, long id,
			String logoUrl, List<Schedules> list,String type) {
		super();
		this.date = date;
		this.desc = desc;
		Name = name;
		this.id = id;
		this.logoUrl = logoUrl;
		this.list = list;
		this.type=type;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public List<Schedules> getList() {
		return list;
	}
	public void setList(List<Schedules> list) {
		this.list = list;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
