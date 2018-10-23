package com.cloudclass.utils;

public class CommenData {
	/*
	 * 课程：
		获取课程
			获取首页轮播：getHomeCarouselsCourses
			获取猜你喜欢：getGuessFavoriteCourses
			获取小编推荐：getRecommendedCourses
			获取本周主题：getWeekTitleCourses
			获取会员专属：getVIPCourses
			获取所有课程(分页)：getAllCoursesByPage
		获取所有导师：getAllTeachersByPage
		获取所有课程分类：getAllCourseCategories
		指定课程获取播放地址：getPlayURLByCourse
		指定课程获取下载地址：getDownloadURLByCourse
		指定课程获取评论(分页获取)：getCommentsByCourseAndPage
		获取课程详细信息(包括评论总数)：getDetailByCourse
		获取导师详细信息：getDetailByTeacher
		获取搜索关键字：getSearchKeywords
		上传搜索关键字：uploadSearchKeywords
		搜索课程(分类+关键字+按最新、按最热)：getCoursesByCondition
	用户：
		用户登录：userLogin
		用户注册：userRegis
		获取个人基本信息：getUserDetail
		上传个人基本信息：uploadUserDetail
		上传评论(支持批量)：uploadUserComments
		获取播放记录：getPlayRecordsByUser
		获取收藏记录：getFavoriteRecordsByUser
		获取下载记录：getDownloadRecordsByUser
		获取我的课程：getMyCoursesByUser
		上传播放记录(支持批量)：uploadPlayRecordsByUser
		上传收藏记录(支持批量)：uploadFavoriteRecordsByUser
		上传下载记录(支持批量)：uploadDownloadRecordsByUser
		上传我的课程(支持批量)：uploadMyCoursesByUser
		上传意见反馈：uploadFeedbackByUser
	 */
	public static String SERVER="http://139.129.14.75:8080/"; 
	public static String LOGIN_INFO_PATH=""; 
}
