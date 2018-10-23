package com.cloudclass.entity;

import java.io.Serializable;

public class TagsInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3629288942471693146L;
	public String code;
	public String name;

	public TagsInfo() {
		// TODO Auto-generated constructor stub
	}

	public TagsInfo(String code, String name) {
		this.code = code;
		this.name = name;
	}

	@Override
	public String toString() {
		return "TagsInfo [code=" + code + ", name=" + name + "]";
	}

}
