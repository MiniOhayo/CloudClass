package com.cloudclass.entity;

import java.util.ArrayList;
import java.util.List;

public class UploadCommentsInfo {
	public List<UpLoadInfo> data = new ArrayList<UpLoadInfo>();

	public UploadCommentsInfo(List<UpLoadInfo> data) {
		this.data = data;
	}

	public UploadCommentsInfo() {
	}

}
