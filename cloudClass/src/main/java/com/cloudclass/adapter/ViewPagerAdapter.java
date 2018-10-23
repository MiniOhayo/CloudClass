package com.cloudclass.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * @author Apathy、恒
 * 
 *         ViewPager适配器
 * */
public class ViewPagerAdapter extends PagerAdapter {

	private ImageView[] mImageViews;

	public ViewPagerAdapter(ImageView[] imageViews) {
		this.mImageViews = imageViews;

	}

	@Override
	public int getCount() {
		return mImageViews.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
	//	Log.d("PagerAdapter", "instantiateItem position" + position);

		((ViewPager) container).addView(mImageViews[position]);

		return mImageViews[position];
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		//Log.d("PagerAdapter", "destroyItem position" + position);

		((ViewPager) container).removeView(mImageViews[position]);

	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

}
