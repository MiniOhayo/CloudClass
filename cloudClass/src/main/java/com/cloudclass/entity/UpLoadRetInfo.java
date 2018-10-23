package com.cloudclass.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 上传参数
 * 
 * @author Administrator
 * 
 */
public class UpLoadRetInfo {
	public List<UpLoadInfo> data = new ArrayList<UpLoadInfo>();

	public UpLoadRetInfo() {
	}

	public List<UpLoadInfo> getData() {
		return data;
	}

	public void setData(List<UpLoadInfo> data) {
		this.data = data;
	}

}
