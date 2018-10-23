package com.cloudclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class AcLogo extends Activity {
	public boolean isRet = false;
	private Handler handler;
	AppMain appMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_page);

		appMain = (AppMain) getApplication();

		handler = new Handler();
		initePath();
	}

	void initePath() {

		new Thread() {
			@Override
			public void run() {

				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// if (appMain.loginResult != null
						// && !TextUtils
						// .isEmpty(appMain.loginResult.token)) {
						// startActivity(new Intent(AcLogo.this, AcMain.class));
						// } else
						startActivity(new Intent(AcLogo.this, AcMain.class));
						finish();
					}
				}, 3000);
			}
		}.start();

		// String readNodeList = LocalFile.readFile(StreamData.NODE_LIST);
		// Log.d("readFile", "readFile" + readNodeList);
		// if (!TextUtils.isEmpty(readNodeList)) {
		// List<DeviceInfo> nodelist = appMain.getNodeList();
		// nodelist = JSON.parseArray(readNodeList, DeviceInfo.class);
		// Log.d("parseArray", "parseArray size:" + nodelist.size());
		// if (nodelist != null) {
		// appMain.setNodeList(nodelist);
		// }

		// }
	}

}
