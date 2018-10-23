package com.cloudclass.entity;

import java.util.ArrayList;
import java.util.List;

public class GetTeachersInfo {
	public int pagenum;
	public int pagecount;
	public int pagesize;
	public List<TutorInfo> data = new ArrayList<TutorInfo>();

	public GetTeachersInfo() {
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

	public List<TutorInfo> getData() {
		return data;
	}

	public void setData(List<TutorInfo> data) {
		this.data = data;
	}

}
