package com.cloudclass.entity;

import java.util.ArrayList;
import java.util.List;

public class GetTeacherDetailInfo {

	public int pagenum;
	public int pagecount;
	public int pagesize;
	public String detail;
	public long coursestotal;
	public List<LessonInfo> courses = new ArrayList<LessonInfo>();

	public GetTeacherDetailInfo() {
	}

	public int getPagenum() {
		return pagenum;
	}

	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}

	public int getPagecount() {
		return pagecount;
	}

	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public List<LessonInfo> getData() {
		return courses;
	}

	public void setData(List<LessonInfo> data) {
		this.courses = data;
	}


}
