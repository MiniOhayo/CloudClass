package com.example.provinceselector;

import java.io.Serializable;

public class SortModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3979976853081277951L;
	private String name = ""; // 显示的数据
	private String sortLetters = ""; // 显示数据拼音的首字母
	private String info = "";
	private String image = "";
	private String id = "";

	public SortModel() {
		super();
	}

	public SortModel(String id, String name, String sortLetters, String info,
			String image) {
		this.id = id;
		this.info = info;
		this.image = image;
		this.name = name;
		this.sortLetters = sortLetters;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
