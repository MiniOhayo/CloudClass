package com.cloudclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class AcLogo extends Activity {
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

	public void initePath() {
		new Thread() {
			@Override
			public void run() {

				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						startActivity(new Intent(AcLogo.this, AcMain.class));
						finish();
					}
				}, 2000);
			}
		}.start();
	}

}
