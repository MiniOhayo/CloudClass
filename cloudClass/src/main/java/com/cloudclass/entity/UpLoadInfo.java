package com.cloudclass.entity;

import java.io.Serializable;

public class UpLoadInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6177024429432727731L;
	public String userid;
	public String courseid;
	public boolean delflag;
	public String learnprogress;
	public String content;

	public UpLoadInfo() {

	}

	public UpLoadInfo(String userid, String courseid, boolean delflag) {
		this.userid = userid;
		this.courseid = courseid;
		this.delflag = delflag;
	}

	public UpLoadInfo(String userid, String courseid, String content) {
		this.userid = userid;
		this.courseid = courseid;
		this.content = content;
	}

	public UpLoadInfo(String userid, String courseid, String learnprogress,
			boolean delflag) {
		this.userid = userid;
		this.courseid = courseid;
		this.delflag = delflag;
		this.learnprogress = learnprogress;
	}

}
