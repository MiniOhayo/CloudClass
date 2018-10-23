package com.cloudclass;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.cloudclass.fragment.FgMainAllLesson;
import com.cloudclass.fragment.FgMainImportantLesson;
import com.cloudclass.fragment.FgMainMyLesson;
import com.cloudclass.view.MyDialog;

public class AcMain extends FragmentActivity implements
		OnCheckedChangeListener, OnClickListener {
	protected static final int REFRESH = 0;
	private static final String TAG = AcMain.class.getName();

	public static int currentPage = -1;
	FgMainImportantLesson fgMainImportantLesson;
	FgMainAllLesson fgMainAllLesson;
	FgMainMyLesson fgMainMyLesson;
	Fragment tempfragment;

	private RadioGroup RgMenus;
	private UpdateReceiver upReceiver;
	private AppMain appMain;
	public static Context mContext;
	public boolean isChooseToPlay = false;// 是否从播放界面跳去列表页面选择播放
	long firstClick = 0;
	long secondClick = 0;
	// TextView tvTitle;
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case REFRESH:

				break;
			}
		}
	};
	private MyDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_main);
		Log.i("onCreate", "onCreate------->");
		mContext = this;
		initView();
		// 注册
		upReceiver = new UpdateReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.MY_RECEIVER");
		registerReceiver(upReceiver, filter);
	}

	@Override
	protected void onResume() {
		handler.sendEmptyMessage(REFRESH);
		super.onResume();
	}

	private void initView() {
		// tvTitle = (TextView) findViewById(R.id.title_name);

		RgMenus = (RadioGroup) findViewById(R.id.tab_menu);
		fgMainImportantLesson = new FgMainImportantLesson();
		fgMainAllLesson = new FgMainAllLesson();
		fgMainMyLesson = new FgMainMyLesson();
		fragmentTransaction(fgMainImportantLesson);

		RgMenus.setOnCheckedChangeListener(this);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	public void setCurrentItem(int index) {
		currentPage = index;
	}

	public void fragmentTransaction(Fragment fragment) {
		tempfragment = fragment;
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.main_layout, fragment);
		transaction.commit();
	}

	private class MyOnClickListener implements OnClickListener {
		private int index;

		public MyOnClickListener(int index) {
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			setCurrentItem(index);
			// fragmentTransaction(index);

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.menu_btn:

			break;

		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(upReceiver);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// return doubleClickToExit();
			showDialog(getString(R.string.exit_monitor), true);
			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 列表更新广播接收器
	 * 
	 * @author Administrator
	 * 
	 */
	public class UpdateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			handler.sendEmptyMessage(REFRESH);

		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if (checkedId == R.id.tab_1) {
			if ((tempfragment instanceof FgMainImportantLesson)) {
				return;
			}
			fragmentTransaction(fgMainImportantLesson);
			// tvTitle.setText(getString(R.string.main_important));
		} else if (checkedId == R.id.tab_2) {
			if ((tempfragment instanceof FgMainAllLesson)) {
				return;
			}
			fragmentTransaction(fgMainAllLesson);
			// tvTitle.setText(getString(R.string.main_all));
		} else if (checkedId == R.id.tab_3) {
			if ((tempfragment instanceof FgMainMyLesson)) {
				return;
			}
			fragmentTransaction(fgMainMyLesson);
			// tvTitle.setText(getString(R.string.main_my));
		}

	}

	public void showDialog(String exit_info, final boolean isExit) {
		if (dialog == null) {
			dialog = new MyDialog.Builder(AcMain.this)
					.setMessage(exit_info)
					.setPositiveButton(R.string.positive,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									finish();
									android.os.Process.killProcess(Process
											.myPid());

								}
							})
					.setNegativeButton(R.string.negative,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							}).create();
		}
		dialog.show();
	}

}
