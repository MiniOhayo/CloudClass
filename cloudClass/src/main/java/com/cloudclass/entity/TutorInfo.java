package com.cloudclass.entity;

import java.io.Serializable;

public class TutorInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8839550920362737942L;
	// String
	// id、图片(headimage)、名称(name)、职位(job)、首字母(firstletter)、是否推荐(isrecommend<Y/N>
	public	String id;
	public	String headimage;
	public	String name="";
	public	String job="";
	public String firstletter;
	public String isrecommend;
	public String detail;

	// 导师ID(id)、图片(headimage)、名称(name)、职位(job)、首字母(firstletter)、是否推荐(isrecommend<Y/N>
	public TutorInfo() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHeadimage() {
		return headimage;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getFirstletter() {
		return firstletter;
	}

	public void setFirstletter(String firstletter) {
		this.firstletter = firstletter;
	}

	public String getIsrecommend() {
		return isrecommend;
	}

	public void setIsrecommend(String isrecommend) {
		this.isrecommend = isrecommend;
	}

}
