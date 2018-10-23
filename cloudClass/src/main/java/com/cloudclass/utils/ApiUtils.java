package com.cloudclass.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloudclass.entity.GetCommentInfo;
import com.cloudclass.entity.GetCoursesInfo;
import com.cloudclass.entity.GetLessonTypeInfo;
import com.cloudclass.entity.GetSearchKeyword;
import com.cloudclass.entity.GetTeacherDetailInfo;
import com.cloudclass.entity.GetTeachersInfo;
import com.cloudclass.entity.LessonDetailInfo;
import com.cloudclass.entity.LoginInfo;
import com.cloudclass.entity.ReturnInfo;
import com.cloudclass.entity.TutorInfo;
import com.cloudclass.entity.UpLoadRetInfo;

public class ApiUtils {

	/**
	 * Post请求
	 * 
	 * @param baseURL
	 * @param pairList
	 * @return
	 */
	public static String httpPost(String baseURL, String pairList,Handler handler) {
		try {
			// URL使用基本URL即可，其中不需要加参数
			HttpPost httpPost = new HttpPost(baseURL);
			// 将请求体内容加入请求中
			httpPost.setEntity(new StringEntity(pairList, HTTP.UTF_8));
			// 需要客户端对象来发送请求
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
			// 读取超时
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, 20000);
			// 发送请求
			HttpResponse response = httpClient.execute(httpPost);
			// 显示响应
			if (null == response) {
				return "";
			}

			HttpEntity httpEntity = response.getEntity();
			try {
				InputStream inputStream = httpEntity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));
				String result = "";
				String line = "";
				while (null != (line = reader.readLine())) {
					result += line;
				}
				// System.out.println(result);
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				if (handler != null) {
					handler.sendEmptyMessage(ViewUtils.NET_ERROR);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			if (handler != null) {
				handler.sendEmptyMessage(ViewUtils.NET_ERROR);
			}
		}
		return "";
	}

	/**
	 * 获取首页轮播：getHomeCarouselsCourses：任意参数
	 * 
	 * @return
	 */
	public static GetCoursesInfo getHomeCarouselsCourses(int pagenum,
			int pagesize, Handler handler) {
		// List<GetCoursesInfo> lessonInfos = new ArrayList<GetCoursesInfo>();
		GetCoursesInfo getInfo = null;
		String baseURL = CommenData.SERVER + "getHomeCarouselsCourses";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("pagenum", pagenum);
		jsonObject.put("pagesize", pagesize);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getHomeCarouselsCourses", ":" + jsonStr);
			getInfo = JSON.parseObject(jsonStr, GetCoursesInfo.class);
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return getInfo;

	}

	/**
	 * 获取猜你喜欢：getGuessFavoriteCourses：任意参数
	 * 
	 * @return
	 */
	public static GetCoursesInfo getGuessFavoriteCourses(int pagenum,
			int pagesize, Handler handler) {
		String baseURL = CommenData.SERVER + "getGuessFavoriteCourses";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("pagenum", pagenum);
		jsonObject.put("pagesize", pagesize);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getGuessFavoriteCourses", ":" + jsonStr);
			GetCoursesInfo getInfo = JSON.parseObject(jsonStr,
					GetCoursesInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 获取小编推荐：getRecommendedCourses：任意参数
	 * 
	 * @return
	 */
	public static GetCoursesInfo getRecommendedCourses(int pagenum,
			int pagesize, Handler handler) {
		String baseURL = CommenData.SERVER + "getRecommendedCourses";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("pagenum", pagenum);
		jsonObject.put("pagesize", pagesize);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getGuessFavoriteCourses", ":" + jsonStr);
			GetCoursesInfo getInfo = JSON.parseObject(jsonStr,GetCoursesInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 获取本周主题：getWeekTitleCourses：任意参数
	 * 
	 * @return
	 */
	public static GetCoursesInfo getWeekTitleCourses(int pagenum, int pagesize,
			Handler handler) {
		String baseURL = CommenData.SERVER + "getWeekTitleCourses";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("pagenum", pagenum);
		jsonObject.put("pagesize", pagesize);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getGuessFavoriteCourses", ":" + jsonStr);
			GetCoursesInfo getInfo = JSON.parseObject(jsonStr,
					GetCoursesInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 获取会员专属：getVIPCourses：任意参数
	 * 
	 * @return
	 */
	public static GetCoursesInfo getVIPCourses(int pagenum, int pagesize,
			Handler handler) {
		String baseURL = CommenData.SERVER + "getVIPCourses";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("pagenum", pagenum);
		jsonObject.put("pagesize", pagesize);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getGuessFavoriteCourses", ":" + jsonStr);
			GetCoursesInfo getInfo = JSON.parseObject(jsonStr,
					GetCoursesInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 搜索：getCoursesByCondition：按价格
	 * 
	 * @return
	 */
	public static GetCoursesInfo getCoursesByCondition(int pagenum,
			int startprice, int endprice, Handler handler) {
		String baseURL = CommenData.SERVER + "getCoursesByCondition";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("pagenum", pagenum);
		jsonObject.put("endprice", endprice);
		jsonObject.put("startprice", startprice);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getCoursesByCondition", ":" + jsonStr);
			GetCoursesInfo getInfo = JSON.parseObject(jsonStr,
					GetCoursesInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;
	}

	/**
	 * 搜索：getCoursesByCondition：
	 * 
	 * @return
	 */
	public static GetCoursesInfo getCoursesByCondition(int pagenum,
			String categorycode, String keyword, Handler handler) {
		String baseURL = CommenData.SERVER + "getCoursesByCondition";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("pagenum", pagenum);
		if (!TextUtils.isEmpty(categorycode)) {
			jsonObject.put("categorycode", categorycode);
		}
		jsonObject.put("keyword", keyword);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getCoursesByCondition", ":" + jsonStr);
			GetCoursesInfo getInfo = JSON.parseObject(jsonStr,
					GetCoursesInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;
	}

	/**
	 * 搜索：getCoursesByCondition：
	 * 
	 * @return
	 */
	public static GetCoursesInfo getCoursesByCondition(int pagenum,
			Handler handler) {
		String baseURL = CommenData.SERVER + "getCoursesByCondition";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("endprice", 0);
		jsonObject.put("pagenum", pagenum);
		jsonObject.put("isnewest", true);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getCoursesByCondition", ":" + jsonStr);
			GetCoursesInfo getInfo = JSON.parseObject(jsonStr,
					GetCoursesInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;
	}

	/**
	 * 搜索：getCoursesByCondition：
	 * 
	 * @return
	 */
	public static GetCoursesInfo getCoursesByCondition(int pagenum,
			String categorycode, String keyword, String coursesrc,
			boolean ishot, Handler handler) {
		String baseURL = CommenData.SERVER + "getCoursesByCondition";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("pagenum", pagenum);
		jsonObject.put("pagesize", 10);
		if (!TextUtils.isEmpty(categorycode)) {
			jsonObject.put("categorycode", categorycode);
		}
		if (!TextUtils.isEmpty(coursesrc)) {
			jsonObject.put("coursesrc", coursesrc);
		}

		jsonObject.put("keyword", keyword);
		if (ishot) {
			jsonObject.put("ishot", true);
		} else
			jsonObject.put("isnewest", true);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getCoursesByCondition", ":" + jsonStr);
			GetCoursesInfo getInfo = JSON.parseObject(jsonStr,
					GetCoursesInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;
	}

	/**
	 * 搜索：getCoursesByCondition：
	 * 
	 * @return
	 */
	public static GetCoursesInfo getCoursesByCondition(int pagenum,
			String categorycode, String keyword, boolean isnewest,
			boolean ishot, int startprice, int endprice, Handler handler) {
		String baseURL = CommenData.SERVER + "getCoursesByCondition";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("pagenum", pagenum);
		jsonObject.put("categorycode", categorycode);
		jsonObject.put("keyword", keyword);
		jsonObject.put("isnewest", isnewest);
		jsonObject.put("ishot", ishot);
		jsonObject.put("endprice", endprice);
		jsonObject.put("startprice", startprice);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getCoursesByCondition", ":" + jsonStr);
			GetCoursesInfo getInfo = JSON.parseObject(jsonStr,
					GetCoursesInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;
	}

	/**
	 * 获取所有导师：getAllTeachersByPage 任意参数
	 * 
	 * @return
	 */
	public static GetTeachersInfo getAllTeachersByPage(Handler handler) {
		String baseURL = CommenData.SERVER + "getAllTeachersByPage";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("pagenum", 1);

		jsonObject.put("pagesize", 200);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getAllTeachersByPage", ":" + jsonStr);
			GetTeachersInfo getInfo = JSON.parseObject(jsonStr,
					GetTeachersInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 获取所有课程分类：getAllCourseCategories 任意参数
	 * 
	 * @return
	 */
	public static GetLessonTypeInfo getAllCourseCategories(Handler handler) {
		String baseURL = CommenData.SERVER + "getAllCourseCategories";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getAllCourseCategories", ":" + jsonStr);
			GetLessonTypeInfo getInfo = JSON.parseObject(jsonStr,
					GetLessonTypeInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 指定课程获取评论：getCommentsByCourseAndPage 课程ID(id)、页码(pagenum)
	 * 
	 * @return
	 */
	public static GetCommentInfo getCommentsByCourseAndPage(String id,
			int pagenum, Handler handler) {
		String baseURL = CommenData.SERVER + "getCommentsByCourseAndPage";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("id", id);
		jsonObject.put("pagenum", pagenum);
		jsonObject.put("pagesize", 10);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getAllCourseCategories", ":" + jsonStr);
			GetCommentInfo getInfo = JSON.parseObject(jsonStr,
					GetCommentInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 获取课程详细信息(包括评论总数)：getDetailByCourse 课程ID(id)
	 * 
	 * @param id
	 * 
	 * @return
	 */
	public static LessonDetailInfo getDetailByCourse(String id, Handler handler) {
		String baseURL = CommenData.SERVER + "getDetailByCourse";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getDetailByCourse", ":" + jsonStr);
			LessonDetailInfo getInfo = JSON.parseObject(jsonStr,
					LessonDetailInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;
	}

	/**
	 * 获取导师详细信息：getDetailByTeacher
	 * 
	 * @param id
	 *            导师ID
	 * 
	 * @return
	 */
	public static GetTeacherDetailInfo getDetailByTeacher(String id,
			int pagenum, Handler handler) {
		String baseURL = CommenData.SERVER + "getDetailByTeacher";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("id", id);
		jsonObject.put("pagenum", pagenum);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getAllCourseCategories", ":" + jsonStr);
			GetTeacherDetailInfo getInfo = JSON.parseObject(jsonStr,
					GetTeacherDetailInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 获取搜索关键字：getSearchKeywords：任意参数
	 * 
	 * @return
	 */
	public static GetSearchKeyword getSearchKeywords(Handler handler) {
		String baseURL = CommenData.SERVER + "getSearchKeywords";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getSearchKeywords", ":" + jsonStr);
			GetSearchKeyword getInfo = JSON.parseObject(jsonStr,
					GetSearchKeyword.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 上传搜索关键字：uploadSearchKeywords：关键字(keyword)、搜索次数(times)
	 * 
	 * @param keyword
	 * @param times
	 * 
	 * @return
	 */
	public static String uploadSearchKeywords(String keyword, String times,
			Handler handler) {
		String baseURL = CommenData.SERVER + "uploadSearchKeywords";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("keyword", keyword);
		jsonObject.put("times", times);
		Log.i("jsonObjectPara", jsonObject.toString());
		return httpPost(baseURL, jsonObject.toString(), handler);

	}

	/**
	 * 用户登录：userLogin：手机号或用户名(code)、密码(password)
	 * 
	 * @param code
	 * @param password
	 * 
	 * @return
	 */
	public static LoginInfo userLogin(String code, String password,
			Handler handler) {
		String baseURL = CommenData.SERVER + "userLogin";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("code", code);
		jsonObject.put("password", password);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("userLogin", ":" + jsonStr);
			LoginInfo getInfo = JSON.parseObject(jsonStr, LoginInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 用户登录 { "ptype": "QQ", "pid": "81dc9bdb52d04dc20036dbd8313ed055",
	 * "pnickname":"张三" }
	 * 
	 * @param ptype
	 * @param pid
	 * @param pnickname
	 * @param handler
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static boolean isMessyCode(String strName) {
		Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
		Matcher m = p.matcher(strName);
		String after = m.replaceAll("");
		String temp = after.replaceAll("\\p{P}", "");
		char[] ch = temp.trim().toCharArray();
		float chLength = ch.length;
		float count = 0;
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!Character.isLetterOrDigit(c)) {

				if (!isChinese(c)) {
					count = count + 1;
					System.out.print(c);
				}
			}
		}
		float result = count / chLength;
		if (result > 0.4) {
			return true;
		} else {
			return false;
		}

	}

	public static LoginInfo otherUserLogin(String ptype, String pid,
			String pnickname, Handler handler)
			throws UnsupportedEncodingException {
		if ("Wechat".equals(ptype)) {
			ptype = "微信";
		}
		if ("SinaWeibo".equals(ptype)) {
			ptype = "微博";
		}
		String baseURL = CommenData.SERVER + "userOtherLogin";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("ptype", ptype);
		jsonObject.put("pid", pid);
		if (!"微博".equals(ptype)) {
			jsonObject.put("pnickname", new String(pnickname.getBytes("gbk"),
					"utf-8"));
		} else
			jsonObject.put("pnickname", pnickname);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("otherUserLogin", ":" + jsonStr);
			LoginInfo getInfo = JSON.parseObject(jsonStr, LoginInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 忘记密码
	 * 
	 * @param code
	 * @param mobile
	 * @param password
	 * 
	 * @return
	 */
	public static ReturnInfo forgetpwd(String mobile, String newpassword,
			Handler handler) {
		String baseURL = CommenData.SERVER + "forgetpwd";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("mobile", mobile);
		jsonObject.put("password", newpassword);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("userLogin", ":" + jsonStr);
			ReturnInfo getInfo = JSON.parseObject(jsonStr, ReturnInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 用户注册：userRegis：用户名(code)、手机号(mobile)、密码(password)
	 * 
	 * @param code
	 * @param mobile
	 * @param password
	 * 
	 * @return
	 */
	public static ReturnInfo userRegis(String code, String mobile,
			String password, Handler handler) {
		String baseURL = CommenData.SERVER + "userRegis";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("code", code);
		jsonObject.put("mobile", mobile);
		jsonObject.put("password", password);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("userLogin", ":" + jsonStr);
			ReturnInfo getInfo = JSON.parseObject(jsonStr, ReturnInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 获取个人基本信息：getUserDetail：用户ID(id)
	 * 
	 * @param id
	 * 
	 * @return
	 */
	public static LoginInfo getUserDetail(String id, Handler handler) {
		String baseURL = CommenData.SERVER + "getUserDetail";

		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		// NameValuePair nameValuePair=;
		pairList.add(new BasicNameValuePair("id", id));
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("id", id);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getUserDetail", ":" + jsonStr);
			LoginInfo getInfo = JSON.parseObject(jsonStr, LoginInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 上传个人基本信息：uploadUserDetail：用户ID(id)、头像(headimage)、昵称(name)、手机号(mobile)
	 * 
	 * @param id
	 * @param headimage
	 * @param name
	 * @param mobile
	 * 
	 * @return
	 */
	public static ReturnInfo uploadUserDetail(String id, String headimage,
			String name, String password, String mobile, Handler handler) {
		String baseURL = CommenData.SERVER + "uploadUserDetail";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("id", id);
		if (!TextUtils.isEmpty(headimage)) {
			jsonObject.put("headimage", headimage);
		}
		if (!TextUtils.isEmpty(name)) {
			jsonObject.put("name", name);
		}
		if (!TextUtils.isEmpty(password)) {
			jsonObject.put("password", password);
		}
		if (!TextUtils.isEmpty(mobile)) {
			jsonObject.put("mobile", mobile);
		}
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("userLogin", ":" + jsonStr);
			ReturnInfo getInfo = JSON.parseObject(jsonStr, ReturnInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 上传评论(支持批量)：uploadUserComments：用户ID(userid)、课程ID(courseid)、评论内容(content)
	 * 
	 * @param userid
	 * @param courseid
	 * @param content
	 * 
	 * @return
	 */
	public static ReturnInfo uploadUserComments(UpLoadRetInfo upLoadRetInfo,
			Handler handler) {
		String baseURL = CommenData.SERVER + "uploadUserComments";
		String jsonObject = JSON.toJSONString(upLoadRetInfo);
		Log.i("jsonObjectPara", jsonObject);
		String jsonStr = httpPost(baseURL, jsonObject, handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("uploadUserComments", ":" + jsonStr);
			ReturnInfo getInfo = JSON.parseObject(jsonStr, ReturnInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 获取播放记录：getPlayRecordsByUser:用户ID(id)、页码(pagenum)
	 * 
	 * @param id
	 * @param pagenum
	 * 
	 * @return
	 */
	public static GetCoursesInfo getPlayRecordsByUser(String id, int pagenum,
			Handler handler) {
		String baseURL = CommenData.SERVER + "getPlayRecordsByUser";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("id", id);
		jsonObject.put("pagenum", pagenum);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getPlayRecordsByUser", ":" + jsonStr);
			GetCoursesInfo getInfo = JSON.parseObject(jsonStr,
					GetCoursesInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 获取收藏记录：getFavoriteRecordsByUser：用户ID
	 * 
	 * @return
	 */
	public static GetCoursesInfo getFavoriteRecordsByUser(String id,
			int pagenum, Handler handler) {
		String baseURL = CommenData.SERVER + "getFavoriteRecordsByUser";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("pagenum", pagenum);
		jsonObject.put("id", id);
		jsonObject.put("pagesize", 10);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getDownloadRecordsByUser", ":" + jsonStr);
			GetCoursesInfo getInfo = JSON.parseObject(jsonStr,
					GetCoursesInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 获取下载记录：getDownloadRecordsByUser：用户ID
	 * 
	 * @return
	 */
	public static GetCoursesInfo getDownloadRecordsByUser(String id,
			int pagenum, Handler handler) {
		String baseURL = CommenData.SERVER + "getDownloadRecordsByUser";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("id", id);
		jsonObject.put("pagenum", pagenum);
		jsonObject.put("pagesize", 10);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getDownloadRecordsByUser", ":" + jsonStr);
			GetCoursesInfo getInfo = JSON.parseObject(jsonStr,
					GetCoursesInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 获取我的课程：getMyCoursesByUser：用户ID
	 * 
	 * @return
	 */
	public static GetCoursesInfo getMyCoursesByUser(String id, int pagenum,
			Handler handler) {
		String baseURL = CommenData.SERVER + "getMyCoursesByUser";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("id", id);
		jsonObject.put("pagenum", pagenum);
		jsonObject.put("pagesize", 10);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("getDownloadRecordsByUser", ":" + jsonStr);
			GetCoursesInfo getInfo = JSON.parseObject(jsonStr,
					GetCoursesInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 上传播放记录(支持批量)：uploadPlayRecordsByUser：用户ID(userid)、课程ID(courseid)、学习进度(
	 * learnprogress)、删除标识(delflag)
	 * 
	 * @param userid
	 * @param courseid
	 * @param learnprogress
	 * @param delflag
	 * 
	 * @return
	 */
	public static ReturnInfo uploadPlayRecordsByUser(
			UpLoadRetInfo upLoadRetInfo, Handler handler) {
		String baseURL = CommenData.SERVER + "uploadPlayRecordsByUser";
		String jsonObject = JSON.toJSONString(upLoadRetInfo);
		Log.i("jsonObjectPara", jsonObject);
		String jsonStr = httpPost(baseURL, jsonObject, handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("uploadPlayRecordsByUser", ":" + jsonStr);
			ReturnInfo getInfo = JSON.parseObject(jsonStr, ReturnInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 上传收藏记录(支持批量)：uploadFavoriteRecordsByUser：用户ID
	 * 
	 * @return
	 */
	public static ReturnInfo uploadFavoriteRecordsByUser(
			UpLoadRetInfo upLoadRetInfo, Handler handler) {
		String baseURL = CommenData.SERVER + "uploadFavoriteRecordsByUser";
		String jsonObject = JSON.toJSONString(upLoadRetInfo);
		Log.i("jsonObjectPara", jsonObject);
		String jsonStr = httpPost(baseURL, jsonObject, handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("uploadDownloadRecordsByUser", ":" + jsonStr);
			ReturnInfo getInfo = JSON.parseObject(jsonStr, ReturnInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 上传下载记录(支持批量)：uploadDownloadRecordsByUser：用户ID(
	 * 
	 * @return
	 */
	public static ReturnInfo uploadDownloadRecordsByUser(
			UpLoadRetInfo upLoadRetInfo, Handler handler) {
		String baseURL = CommenData.SERVER + "uploadDownloadRecordsByUser";

		String jsonObject = JSON.toJSONString(upLoadRetInfo);
		Log.i("jsonObjectPara", jsonObject);
		String jsonStr = httpPost(baseURL, jsonObject, handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("uploadDownloadRecordsByUser", ":" + jsonStr);
			ReturnInfo getInfo = JSON.parseObject(jsonStr, ReturnInfo.class);
			Log.d("uploadDownloadRecordsByUser", ":" + getInfo.toString());
			return getInfo;

		}
		return null;

	}

	/**
	 * 上传我的课程(支持批量)：uploadMyCoursesByUser：用户ID
	 * 
	 * @return
	 */
	public static ReturnInfo uploadMyCoursesByUser(UpLoadRetInfo upLoadRetInfo,
			Handler handler) {
		String baseURL = CommenData.SERVER + "uploadMyCoursesByUser";
		String jsonObject = JSON.toJSONString(upLoadRetInfo);
		Log.i("jsonObjectPara", jsonObject);
		String jsonStr = httpPost(baseURL, jsonObject, handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("uploadMyCoursesByUser", ":" + jsonStr);
			ReturnInfo getInfo = JSON.parseObject(jsonStr, ReturnInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 上传意见反馈：uploadFeedbackByUser：用户ID(id)、联系方式(contactinfo)、反馈内容(content)
	 * 
	 * @param id
	 * @param contactinfo
	 * @param content
	 * 
	 * @return
	 */
	public static ReturnInfo uploadFeedbackByUser(String id,
			String contactinfo, String content, Handler handler) {
		String baseURL = CommenData.SERVER + "uploadFeedbackByUser";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("id", id);
		jsonObject.put("contactinfo", contactinfo);
		jsonObject.put("content", content);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("uploadFeedbackByUser", ":" + jsonStr);
			ReturnInfo getInfo = JSON.parseObject(jsonStr, ReturnInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 获取意见反馈界面内容：getFeedbackContent 任意参数
	 * 
	 * @return
	 */
	public static TutorInfo getFeedbackContent(Handler handler) {
		String baseURL = CommenData.SERVER + "getFeedbackContent";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("uploadFeedbackByUser", ":" + jsonStr);
			TutorInfo getInfo = JSON.parseObject(jsonStr, TutorInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}

	/**
	 * 获取意见反馈界面内容：getFeedbackContent 任意参数
	 * 
	 * @return
	 */
	public static TutorInfo getClientInfo(Handler handler) {
		String baseURL = CommenData.SERVER + "getClientInfo";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("uploadFeedbackByUser", ":" + jsonStr);
			TutorInfo getInfo = JSON.parseObject(jsonStr, TutorInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}
	/**
	 * 
	 * 
	 * @return
	 */
	public static ReturnInfo sendVerify(String phone,Handler handler) {
		String baseURL = CommenData.SERVER + "vericode";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubcls", "a");
		jsonObject.put("mobile", phone);
		Log.i("jsonObjectPara", jsonObject.toString());
		String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
		if (!TextUtils.isEmpty(jsonStr)) {
			Log.d("uploadFeedbackByUser", ":" + jsonStr);
			ReturnInfo getInfo = JSON.parseObject(jsonStr, ReturnInfo.class);
			return getInfo;
			// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
		}
		return null;

	}
	public static ReturnInfo uploadImage(byte[] headimage, Handler handler) {
		String baseURL = CommenData.SERVER + "uploadImage";
		JSONObject jsonObject = new JSONObject();
		if (headimage != null && headimage.length != 0) {
			jsonObject.put("headimage", headimage);
			String jsonStr = httpPost(baseURL, jsonObject.toString(), handler);
			if (!TextUtils.isEmpty(jsonStr)) {
				Log.d("userLogin", ":" + jsonStr);
				ReturnInfo getInfo = JSON
						.parseObject(jsonStr, ReturnInfo.class);
				return getInfo;
				// Log.d("getHomeCarouselsCourses", ":" + getInfo.toString());
			}
		}

		return null;

	}

	public static void uploadImage(String uploadFile, Handler handler) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			String baseURL = CommenData.SERVER + "image/upload";
			URL url = new URL(baseURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设置传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file1\";filename=\"" + "image.jpg" + "\"" + end);
			ds.writeBytes(end);
			/* 取得文件的FileInputStream */
			FileInputStream fStream = new FileInputStream(uploadFile);
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();

			ds.close();
			/* 取得Response内容 */
			int res = con.getResponseCode();
			if (res == 200) {
				InputStream is = con.getInputStream();
				int ch;
				StringBuffer b = new StringBuffer();
				while ((ch = is.read()) != -1) {
					b.append((char) ch);
				}
				is.close();

				handler.sendMessage(Message.obtain(handler, ViewUtils.UPLOAD_S,
						b.toString()));
			}
			con.disconnect();

		} catch (Exception e) {
			// showDialog("上传失败"+e);
			e.printStackTrace();
			handler.sendEmptyMessage(ViewUtils.UPLOAD_FAILD);
		}

	}
}
