package com.mytv.model;

import java.util.ArrayList;
import java.util.List;

public class Film {
	private long id;
	private String name;
	private String logoUrl;
	private String filmDesc;
	private String filmDura;
	private List<Episode> Epi = new ArrayList<Episode>();

	public Film(long id, String name, String logoUrl, String filmDesc,String filmDura, List<Episode> epi, String filmReleased) {
		super();
		this.id = id;
		this.name = name;
		this.logoUrl = logoUrl;
		this.filmDesc = filmDesc;
		this.filmDura = filmDura;
		Epi = epi;
		this.filmReleased = filmReleased;
	}

	private String filmReleased;

	public long getId() {
		return id;
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

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getFilmDesc() {
		return filmDesc;
	}

	public void setFilmDesc(String filmDesc) {
		this.filmDesc = filmDesc;
	}

	public String getFilmDura() {
		return filmDura;
	}

	public void setFilmDura(String filmDura) {
		this.filmDura = filmDura;
	}

	public List<Episode> getEpi() {
		return Epi;
	}

	public void setEpi(List<Episode> epi) {
		Epi = epi;
	}

	public String getFilmReleased() {
		return filmReleased;
	}

	public void setFilmReleased(String filmReleased) {
		this.filmReleased = filmReleased;
	}
}
