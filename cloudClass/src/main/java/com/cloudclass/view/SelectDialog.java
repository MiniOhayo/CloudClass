package com.cloudclass.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SelectDialog extends Dialog implements
		android.view.View.OnClickListener {

	public Context context;
	int layout;
	public LayoutInflater layoutInflate;
	public View view = null;
	public int width;
	public int height;

	public SelectDialog(Context context) {
		super(context);
		this.context = context;
	}

	public SelectDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public SelectDialog(Context context, int theme, View view) {
		super(context, theme);
		this.context = context;
		this.view = view;

	}

	public SelectDialog(Context context, int theme, View view, int width) {
		super(context, theme);
		this.context = context;
		this.view = view;
		this.width = width;

	}

	public SelectDialog(Context context, int theme, int d) {
		super(context, theme);
		this.context = context;
		this.width = width;

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (view != null) {
			this.setContentView(view);
			WindowManager wm = (WindowManager) getContext().getSystemService(
					Context.WINDOW_SERVICE);
			width = wm.getDefaultDisplay().getWidth();
			Window w = getWindow();
			WindowManager.LayoutParams lp = w.getAttributes();
			lp.width = width;
			lp.x = 0;
			final int cMakeBottom = -1000;
			lp.y = cMakeBottom;
			lp.gravity = Gravity.BOTTOM;
			onWindowAttributesChanged(lp);
			setCanceledOnTouchOutside(true);
		}
	}

	@Override
	public void onClick(View v) {

	}

}