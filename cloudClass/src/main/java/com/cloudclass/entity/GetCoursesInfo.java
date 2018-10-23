package com.cloudclass.entity;

import java.util.ArrayList;
import java.util.List;

public class GetCoursesInfo {
	public	int pagenum;
	public	int pagecount;
	public	int pagesize;
	public	List<LessonInfo> data = new ArrayList<LessonInfo>();

	public GetCoursesInfo() {
		// TODO Auto-generated constructor stubs
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
		return data;
	}

	public void setData(List<LessonInfo> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "GetCoursesInfo [pagenum=" + pagenum + ", pagecount="
				+ pagecount + ", pagesize=" + pagesize + ", data=" + data.toArray() + "]";
	}

}
