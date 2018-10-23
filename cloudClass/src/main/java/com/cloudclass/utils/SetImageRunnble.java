package com.cloudclass.utils;

import com.cloudclass.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class SetImageRunnble implements Runnable {
	Bitmap bmp;
	ImageView imageView;
	Context activity;

	public SetImageRunnble(Context activity, ImageView imageView, Bitmap bmp) {
		this.activity = activity;
		this.imageView = imageView;
		this.bmp = bmp;
	}

	public SetImageRunnble(ImageView imageView, Bitmap bmp) {
		this.imageView = imageView;
		this.bmp = bmp;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (bmp != null) {
			imageView.setImageBitmap(bmp);
		} else {
			if (activity != null) {
				imageView.setBackgroundResource(R.drawable.defult);
			}
		}

		imageView.setScaleType(ScaleType.FIT_XY);
		// imageView.setBackgroundDrawable(new BitmapDrawable(bmp));
	}
}