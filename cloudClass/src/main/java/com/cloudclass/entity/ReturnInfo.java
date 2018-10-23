package com.cloudclass.entity;

/**
 * 请求返回
 * 
 * @author Administrator
 * 
 */
public class ReturnInfo {
	public boolean issuccess; // 是否成功
	public String errormsg;
	public String imageurl;
	public String vericode;
	public String mobile;

	public ReturnInfo() {
		// TODO Auto-generated constructor stub
	}

	public boolean getIssuccess() {
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

	@Override
	public String toString() {
		return "ReturnInfo [issuccess=" + issuccess + ", errormsg=" + errormsg
				+ "]";
	}

}
