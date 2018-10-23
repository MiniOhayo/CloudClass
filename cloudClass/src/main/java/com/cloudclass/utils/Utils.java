package com.cloudclass.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.cloudclass.entity.DayType;
import com.cloudclass.entity.LoginInfo;

@SuppressLint("SimpleDateFormat")
public class Utils {

	public static DayType getDayType(Long time) {
		long currentTime = System.currentTimeMillis();
		long temp = currentTime - time;
		if (temp < 3600 * 24) {
			return DayType.today;
		} else if (temp < 3600 * 24 * 7) {
			return DayType.week;
		} else if (temp < 3600 * 24 * 30) {
			return DayType.month;
		} else
			return DayType.moreLong;

	}

	public static DayType getDayType(String time) {
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//String today="2015-05-17 16:08:54";
		long times;
		try {
			//long	todays = sdf.parse(today).getTime();
			times = sdf.parse(time).getTime();
			//long temps=todays-times;
			long temp = currentTime - times;
			if (temp < 3600 * 24*1000) {
				return DayType.today;
			} else if (temp < 3600 * 24 * 7*1000) {
				return DayType.week;
			} else if (temp< 3600 * 24 * 30*1000) {
				return DayType.month;
			} else
				return DayType.moreLong;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return DayType.moreLong;

	}

	public static LoginInfo readLoginInfo(String path) {
		LoginInfo loginInfo = null;
		String readNodeList = Utils.readFile(path);
		if (!TextUtils.isEmpty(readNodeList)) {
			loginInfo = JSON.parseObject(readNodeList, LoginInfo.class);
			Log.d("loginInfo", "readNodeList size:" + loginInfo.toString());
		}
		return loginInfo;
	}

	public static boolean writeLoginInfo(LoginInfo loginInfo, String path) {
		String jsonString = null;
		if (loginInfo == null)
			jsonString = "";
		else
			jsonString = JSON.toJSONString(loginInfo);
		return writeFile(path, jsonString);
	}

	public static boolean writeFile(String xmlName, String content) {

		File file = new File(xmlName);
		BufferedWriter writer;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			if (TextUtils.isEmpty(content)) {
				file.delete();
			} else {
				// mFile = new FileOutputStream(file);
				OutputStreamWriter write = new OutputStreamWriter(
						new FileOutputStream(file), "UTF-8");
				writer = new BufferedWriter(write);
				// PrintWriter writer = new PrintWriter(new BufferedWriter(new
				// FileWriter(filePathAndName)));
				// PrintWriter writer = new PrintWriter(new
				// FileWriter(filePathAndName));
				writer.write(content);
				writer.close();
			}
		} catch (Exception e) {

			System.out.println("保存记录出错");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static String readFile(String xmlName) {

		StringBuilder sb = new StringBuilder();
		File file = new File(xmlName);
		// FileInputStream mFile = null;
		BufferedReader reader;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			// mFile = new FileInputStream(file);

			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));
			// reader.readLine()
			String temp;
			while ((temp = reader.readLine()) != null) {

				sb.append(temp);

			}
			// mFile.close();
		} catch (Exception e) {

			System.out.println("读取记录出错");
			e.printStackTrace();
		} finally {
			// try {
			// if (mFile != null) {
			// mFile.close();
			// }
			//
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}
		return sb.toString();

	}
}
