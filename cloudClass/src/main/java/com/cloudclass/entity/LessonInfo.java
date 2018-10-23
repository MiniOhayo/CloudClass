package com.cloudclass.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程对象
 * 
 * @author Administrator
 * 
 */
public class LessonInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7075115909655374194L;
	/**
	 * 课程ID
	 */
	public int id;
	/**
	 * 课程名称
	 */
	public String name;
	/**
	 * 课程类型
	 */
	public String categoryname;
	/**
	 * 课程详细信息
	 */
	public String detail;
	/**
	 * 课程简介
	 */
	public String sintro;
	/**
	 * 播放次数
	 */
	public int playtimes;
	/**
	 * 收藏次数
	 */
	public int favorateNum;
	/**
	 * 评论次数
	 */
	public int commenNum;

	/**
	 * 图片链接
	 */
	public String image;

	public String updatedate;

	/**
	 * 导师信息
	 */
	public TutorInfo teacher = new TutorInfo();

	/**
	 * 时间段
	 */
	public String times;

	/**
	 * 标签集合
	 */
	public List<LabelInfo> labels = new ArrayList<LabelInfo>();

	/**
	 * 标记集合
	 */
	public List<TagsInfo> tags = new ArrayList<TagsInfo>();

	/**
	 * 时间标记
	 */
	public String timeTag;
	/**
	 * 学习时间
	 */
	public String learnprogress;
	/**
	 * 下载时间
	 */
	public String time;

	public int downLodProgress = 100;
	public String downLoadState = "播放";

	public LessonInfo() {
		// TODO Auto-generated constructor stub
	}

	// public LessonInfo(String times) {
	// this.times = times;
	// }

	public LessonInfo(String timeTag) {
		this.timeTag = timeTag;
	}

	public boolean isChecked = false;

	public int price;

	@Override
	public String toString() {
		return "LessonInfo [id=" + id + ", name=" + name + ", detail=" + detail
				+ ", sintro=" + sintro + ", playtimes=" + playtimes
				+ ", favorateNum=" + favorateNum + ", commenNum=" + commenNum
				+ ", image=" + image + ", teachers=" + teacher + "]";
	}

}
