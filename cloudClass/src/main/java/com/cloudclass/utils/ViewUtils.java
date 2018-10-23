package com.cloudclass.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ViewUtils {
	public static final int NET_ERROR = -1;
	public static final int UPLOAD_FAILD = -2;
	public static final int UPLOAD_S = -3;
	public static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case NET_ERROR:
				// Show.toast(con, id)
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	public static void setListViewHeightBasedOnChildren(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	@SuppressLint("NewApi")
	public static void setListViewHeightBasedOnChildren(GridView listView) {

		ListAdapter listAdapter = listView.getAdapter();
		int num = listView.getNumColumns();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		if (listAdapter.getCount() > 0) {
			View listItem = listAdapter.getView(0, null, listView);
			listItem.measure(0, 0);
			int height = listItem.getMeasuredHeight();
			totalHeight = ((listAdapter.getCount()-1)  / num+1) * height;
			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = totalHeight;
			listView.setLayoutParams(params);
		}

	}

	// 获取屏幕的宽度
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	// 获取屏幕的高度
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}
}
