package com.cloudclass;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.cloudclass.entity.LessonDetailInfo;
import com.cloudclass.utils.DownLoadFile;

public class DownLoadService extends Service {
	// HashMap<String, DownLoadFile> map = new HashMap<String, DownLoadFile>();
	List<DownLoadFile> downLoadFiles = new ArrayList<DownLoadFile>();
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case 2:
				// 下载完成 播放
				if (msg.arg1 == 100) {
					Log.d("DownLoadService", "DownLoadService " + msg.arg2
							+ " 下载完成");
				}
				updataAndSendBroadcast(msg.arg2, msg.arg1);
				break;
			}
		}
	};

	public void updataAndSendBroadcast(int id, int progress) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MY_RECEIVER");
		intent.putExtra("id", id);
		intent.putExtra("progress", progress);
		sendBroadcast(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if (intent != null) {
			final LessonDetailInfo lessonDetailInfo = (LessonDetailInfo) intent
					.getSerializableExtra("info");
			if (lessonDetailInfo != null) {
				boolean isExist = false;
				for (int i = 0; i < downLoadFiles.size(); i++) {
					if (lessonDetailInfo.downloadurl
							.equals(downLoadFiles.get(i).url)) {
						isExist = true;
						break;
					}

				}
				if (!isExist) {
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							DownLoadFile downLoadFile = new DownLoadFile(
									lessonDetailInfo.id,
									lessonDetailInfo.downloadurl, handler);
							// map.put(lessonDetailInfo.id, downLoadFile);
							downLoadFile.isDown = true;
							downLoadFile.start();
							downLoadFiles.add(downLoadFile);
						}
					}, 1000);
				}

			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
