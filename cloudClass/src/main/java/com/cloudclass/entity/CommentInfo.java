package com.cloudclass.entity;

public class CommentInfo {
	// 用户图片(headimage)、用户昵称(name)、评论内容(content)、评论时间(time)
	public String headimage;
	public String name;
	public String content;
	public long time;
	public	String times;
	public CommentInfo() {
		// TODO Auto-generated constructor stub
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getTime() {
		return time;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


}
