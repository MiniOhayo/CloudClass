package com.cloudclass.entity;

import java.io.Serializable;

public class UploadFileRet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7755372750037407233L;

	public boolean issuccess;
	public String errormsg;
	public long imageid;
	public String imageurl;

	public UploadFileRet() {
		// TODO Auto-generated constructor stub
	}

}
