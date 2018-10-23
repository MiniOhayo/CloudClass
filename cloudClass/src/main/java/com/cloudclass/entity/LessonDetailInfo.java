package com.cloudclass.entity;

import java.io.Serializable;

public class LessonDetailInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2275775073901977073L;
	public int id;
	public String playurl;
	public String downloadurl;
	public String filesize;
	public String detail;
	public String commentscount;

	/**
	 * 导师信息
	 */
	public TutorInfo teacher = new TutorInfo();

	public LessonDetailInfo() {
	}

}
