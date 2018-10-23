package com.cloudclass.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DownLoadFile extends Thread {
	Handler handler;
	public String url;
	boolean isStops;
	int id;
	public boolean isDown = false;

	public DownLoadFile(int id, String url, Handler handler) {
		this.id = id;
		this.handler = handler;
		this.url = url;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		downFile(url);
		super.run();
	}

	// public static String PATH_DIR = "";
	public static String MP4_DIR = "/sdcard/CloudClass/Video/";//
	public static String MP4 = ".mp4";

	public static boolean CreateDirectory() {
		Log.d("PATH_DIR", String.valueOf(MP4_DIR));
		File file = new File(MP4_DIR);
		boolean b = false;
		if (!file.exists()) {
			b = file.mkdirs();
			System.out.println("不存在，创建文件夹" + b + MP4_DIR);
			return b;
		} else {
			return true;
		}
	}

	public boolean isStops() {
		return isStops;
	}

	public void stops(boolean isStops) {
		this.isStops = isStops;

	}

	// 67861492
	public boolean downFile(String url) {
		url = url.replaceAll(" ", "%20");

		isStops = false;
		int progress = 0;
		if (!DownLoadFile.CreateDirectory()) {
			return false;
		}

		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response;

		// 用来获取下载文件的大小
		HttpClient client_test = new DefaultHttpClient();
		HttpGet request_test = new HttpGet(url);
		HttpResponse response_test = null;

		try {
			response_test = client_test.execute(request_test);
			HttpEntity entity = response_test.getEntity();
			long length = entity.getContentLength();
			InputStream is = entity.getContent();
			// FileOutputStream fileOutputStream = null;
			MP4 = url.substring(url.lastIndexOf('.'), url.length());
			String fileName = MD5Util.string2MD5(url) + MP4;
			if (is != null) {
				File file = new File(MP4_DIR, fileName); // Config.UPDATE_SAVENAME
				if (!file.exists()) {
					file.createNewFile();
				}

				Log.d("startlength", file.getPath()+",length:" + file.length());
				if (file.length() == length) {
					if (isDown) {
						handler.sendMessage(Message.obtain(handler, 2, 100, id));
					} else
						handler.sendMessage(Message.obtain(handler, 0, 100, id,file.getPath()));
					return true;
				} else if (file.length() < length) {

					if (isDown) {
						// 从文件的末尾开始添加数据
						Header header_size = new BasicHeader("Range", "bytes="
								+ file.length() + "-" + length);
						get.addHeader(header_size);
						response = client.execute(get);
						entity = response.getEntity();
						is = entity.getContent();
						// fileOutputStream = new FileOutputStream(file);
						RandomAccessFile fos = new RandomAccessFile(file, "rw");
						// 从文件的size以后的位置开始写入，其实也不用，直接往后写就可以。有时候多线程下载需要用
						fos.seek(file.length());
						byte[] buf = new byte[1024];
						int ch = -1;
						long count = file.length();
						while ((ch = is.read(buf)) != -1 && !isStops) {//
							fos.write(buf, 0, ch);
							count += ch;
							int temp = (int) ((count * 100) / length);
							if (progress != temp) {
								progress = temp;
								Log.d("progress", String.valueOf(progress));
								handler.sendMessage(Message.obtain(handler, 2,
										progress, id));
							}

						}
						// fos.flush();
						if (fos != null) {
							fos.close();
						}
					} else
						handler.sendMessage(Message.obtain(handler, 1, 0, id));

				}

			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
