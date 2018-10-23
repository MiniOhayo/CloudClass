package com.cloudclass.entity;

import java.io.Serializable;

/**
 * 标签
 * @author Administrator
 *
 */
public class LabelInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -610788100476774308L;
	public String code;
	public String name;

	public LabelInfo() {
		// TODO Auto-generated constructor stub
	}

	public LabelInfo(String code, String name) {
		this.code = code;
		this.name = name;
	}

	@Override
	public String toString() {
		return "LabelInfo [code=" + code + ", name=" + name + "]";
	}

}
