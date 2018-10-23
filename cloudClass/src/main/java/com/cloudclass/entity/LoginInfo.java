package com.cloudclass.entity;

import java.io.Serializable;

public class LoginInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7575375770741387852L;
	public boolean issuccess;
	public String errormsg;
	public String id;
	public String headimage;
	public String name="";
	public String mobile="";
	public String pass;
	public boolean isThirdLogin=false;

	public LoginInfo() {
	}

	public boolean isIssuccess() {
		return issuccess;
	}

	public void setIssuccess(boolean issuccess) {
		this.issuccess = issuccess;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return "LoginInfo [issuccess=" + issuccess + ", errormsg=" + errormsg
				+ ", id=" + id + ", headimage=" + headimage + ", name=" + name
				+ ", mobile=" + mobile + ", pass=" + pass + "]";
	}



}
