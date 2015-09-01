package com.mytv.model;

import java.util.ArrayList;
import java.util.List;

public class FilmDowload {
	private String name;
	private String id;
	private List<Film> filmDowload = new ArrayList<Film>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FilmDowload(String name, String id, List<Film> filmDowload) {
		super();
		this.name = name;
		this.id = id;
		this.filmDowload = filmDowload;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Film> getFilmDowload() {
		return filmDowload;
	}

	public void setFilmDowload(List<Film> filmDowload) {
		this.filmDowload = filmDowload;
	}
}
