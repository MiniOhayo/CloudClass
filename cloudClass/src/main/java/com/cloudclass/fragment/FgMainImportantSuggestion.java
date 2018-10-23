package com.cloudclass.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudclass.AcLessonDetail;
import com.cloudclass.AcLessonMore;
import com.cloudclass.AppMain;
import com.cloudclass.R;
import com.cloudclass.adapter.ViewPagerAdapter;
import com.cloudclass.entity.GetCoursesInfo;
import com.cloudclass.entity.LessonInfo;
import com.cloudclass.utils.ApiUtils;
import com.cloudclass.utils.NetUtils;

@SuppressLint("HandlerLeak")
public class FgMainImportantSuggestion extends Fragment implements
		OnClickListener {
	public static final int SAVE_OK = 2;
	public static final int SAVE_ERROE = 3;
	protected static final int AUTO = 4;
	protected static final int DELAY = 6 * 1000;
	protected static final int INITE_VIEWPAGER = 5;
	protected static final int INITE_CONTENT = 6;
	protected static final int INITE_CONTENT1 = 0;
	protected static final int INITE_CONTENT2 = 1;
	protected static final int INITE_CONTENT3 = 2;
	protected static final int INITE_CONTENT4 = 3;
	AppMain appMain;
	TextView titleName;
	public static boolean isPause = false;

	// private int[] mImageRes = { R.drawable.a, R.drawable.b, R.drawable.c,
	// R.drawable.d, R.drawable.e };
	int mImageRes;
	private ImageView[] mImageViews;
	private FragmentActivity con;
	private View view;
	private LinearLayout layoutDots;
	private ImageView[] mDots;
	private ViewPager mViewpager;
	private ViewPagerAdapter mViewPagerAdp;

	private LayoutInflater layoutInflater;
	private TextView[] tvMore = new TextView[4];
	private int[] tvMoreId = { R.id.tv_favorite_more, R.id.tv_recommend_more,
			R.id.tv_week_more, R.id.tv_vip_more };
	private RelativeLayout[] lessonViews = new RelativeLayout[4];

	private int[] llId = { R.id.rl_1, R.id.rl_2, R.id.rl_3, R.id.rl_4 };
	int width;
	int padding;
	// public ArrayList<Fragment> fragments = new ArrayList<Fragment>();;
	public Handler hander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AUTO:
				if (mViewpager != null) {
					if (mViewPagerAdp != null && mViewPagerAdp.getCount() != 1) {
						int index = mViewpager.getCurrentItem();
						Log.d("PagerAdapter", "getCurrentItem" + (index + 1));
						if (index < mViewPagerAdp.getCount() - 1) {
							mViewpager.setCurrentItem(index + 1);
						} else if (index == mViewPagerAdp.getCount() - 1) {
							mViewpager.setCurrentItem(0);
						}

					}

				}

				break;
			case INITE_VIEWPAGER:

				mImageViews[msg.arg1].setImageBitmap((Bitmap) msg.obj);
				if (mViewPagerAdp == null) {
					view.findViewById(R.id.viewPager_content).setVisibility(
							View.VISIBLE);
					mViewPagerAdp = new ViewPagerAdapter(mImageViews);
					mViewpager.setAdapter(mViewPagerAdp);
					mViewpager.setCurrentItem(0);
				}
				break;
			case INITE_CONTENT:
				GetCoursesInfo response = (GetCoursesInfo) msg.obj;
				if (response != null && response.data != null) {
					mImageRes = response.data.size();
					if (mImageRes != 0) {

						initViewPager(response.data);
						initDots();
					}

				}

				break;
			case INITE_CONTENT1:
				GetCoursesInfo response1 = (GetCoursesInfo) msg.obj;
				if (response1 == null) {
					break;
				}
				view.findViewById(R.id.ll_title1).setVisibility(View.VISIBLE);
				initeLessons(lessonViews[INITE_CONTENT1], response1.data,
						width, padding);
				break;
			case INITE_CONTENT2:
				GetCoursesInfo response2 = (GetCoursesInfo) msg.obj;
				if (response2 == null) {
					break;
				}
				view.findViewById(R.id.ll_title2).setVisibility(View.VISIBLE);
				initeLessons(lessonViews[INITE_CONTENT2], response2.data,
						width, padding);
				break;
			case INITE_CONTENT3:
				GetCoursesInfo response3 = (GetCoursesInfo) msg.obj;
				if (response3 == null) {
					break;
				}
				view.findViewById(R.id.ll_title3).setVisibility(View.VISIBLE);
				initeLessons(lessonViews[INITE_CONTENT3], response3.data,
						width, padding);
				break;
			case INITE_CONTENT4:
				GetCoursesInfo response4 = (GetCoursesInfo) msg.obj;
				int padding = (int) con.getResources().getDimension(
						R.dimen.lesson_padding);
				if (response4 == null) {
					break;
				}
				view.findViewById(R.id.ll_title4).setVisibility(View.GONE);
				initeLessons(lessonViews[INITE_CONTENT4], response4.data,
						width, padding);
				break;
			default:
				break;
			}
		}
	};
	private boolean isFresh;
	private TextView pageName;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		con = this.getActivity();
		appMain = (AppMain) con.getApplication();
		WindowManager wm = con.getWindowManager();
		width = wm.getDefaultDisplay().getWidth();
		padding = (int) con.getResources().getDimension(R.dimen.lesson_padding);
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (view == null) {
			initeView(inflater, container);
		}
		UpdatePhoto();
		Log.d("FgMainImportantSuggestion", "onCreateView----->");
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		return view;
	}

	private void initeView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fg_main_important_suggestion,
				container, false);
		layoutInflater = LayoutInflater.from(con);
		layoutDots = (LinearLayout) view.findViewById(R.id.dotLayout);
		for (int i = 0; i < tvMore.length; i++) {
			tvMore[i] = (TextView) view.findViewById(tvMoreId[i]);
			tvMore[i].setOnClickListener(new MoreClickListener(i));
		}

		getData();
		setClickLisenter();
	}

	private void initViewPager(List<LessonInfo> data) {
		mViewpager = (ViewPager) view.findViewById(R.id.viewPager);
		mViewpager.setOnPageChangeListener(new MyPageChangeListener());
		pageName = (TextView) view.findViewById(R.id.page_name);

		mImageViews = new ImageView[data.size()];

		List<String> imageUrls = new ArrayList<String>(data.size());
		for (int i = 0; i < data.size(); i++) {
			LessonInfo lessonInfo = data.get(i);
			imageUrls.add(lessonInfo.image);
			mImageViews[i] = new ImageView(con);
			mImageViews[i].setScaleType(ScaleType.FIT_XY);
			mImageViews[i].setTag(lessonInfo.name);
			mImageViews[i].setOnClickListener(new onLessonClick(lessonInfo));
		}

		NetUtils.loadImage(con, imageUrls, NetUtils.LARGE_IMAGE,
				new NetUtils.ImageCallBack() {

					@Override
					public void callBack(int index, String urls, Bitmap maps) {
						// TODO Auto-generated method stub

						// hander.sendMessage(INITE_VIEWPAGER,);
						hander.sendMessage(Message.obtain(hander,
								INITE_VIEWPAGER, index, 0, maps));
					}
				});

	}

	/**
	 * @author
	 * 
	 *         初始化ViewPager的底部小点
	 * 
	 * */
	private void initDots() {
		mDots = new ImageView[mImageRes];
		int w = (int) con.getResources().getDimension(R.dimen.dots_w_h);
		for (int i = 0; i < mImageRes; i++) {
			ImageView iv = new ImageView(con);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w, w);
			lp.leftMargin = 5;
			lp.rightMargin = 5;
			lp.topMargin = 5;
			lp.bottomMargin = 5;
			iv.setLayoutParams(lp);
			iv.setImageResource(R.drawable.dot_normal);
			layoutDots.addView(iv);
			mDots[i] = iv;
		}
		if (mImageRes > 0) {
			mDots[0].setImageResource(R.drawable.dot_selected);
			pageName.setText("" + mImageViews[0].getTag().toString());
		}

	}

	/**
 * 
 */
	public void UpdatePhoto() {
		isPause = false;
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000);
					while (!isPause) {
						hander.sendEmptyMessage(AUTO);
						Thread.sleep(DELAY);

					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.run();
			}
		}.start();
	}

	/**
	 * 获取数据
	 */
	void getData() {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				GetCoursesInfo response = ApiUtils.getHomeCarouselsCourses(1,
						10, hander);
				hander.sendMessage(Message.obtain(hander, INITE_CONTENT,
						response));
				GetCoursesInfo response1 = ApiUtils.getGuessFavoriteCourses(1,
						4, hander);
				hander.sendMessage(Message.obtain(hander, INITE_CONTENT1,
						response1));
				GetCoursesInfo response2 = ApiUtils.getRecommendedCourses(1, 4,
						hander);
				hander.sendMessage(Message.obtain(hander, INITE_CONTENT2,
						response2));
				GetCoursesInfo response3 = ApiUtils.getWeekTitleCourses(1, 4,
						hander);
				hander.sendMessage(Message.obtain(hander, INITE_CONTENT3,
						response3));

				// GetCoursesInfo response4 = ApiUtils.getVIPCourses(1, 4,
				// hander);
				// hander.sendMessage(Message.obtain(hander, INITE_CONTENT4,
				// response4));
				// String ss="";
				// ss=null;
				// String lsk[]=ss.split("");
				super.run();
			}
		}.start();
	}

	/**
	 * 猜你喜欢，小编推荐，本周主题，会员专属添加内容
	 * 
	 * @param parentView
	 * @param data
	 * @param w
	 * @param padding
	 */
	void initeLessons(RelativeLayout parentView, List<LessonInfo> data, int w,
			int padding) {
		if (data == null) {
			return;
		}
		int width = (w - 3 * padding) / 2;
		int j = 0;
		int k = 0;
		for (int i = 0; i < data.size(); i++) {
			if (i == 4) {
				return;
			}
			LessonInfo lessonInfo = data.get(i);
			View childView = layoutInflater.inflate(R.layout.lesson_item, null);
			FrameLayout content = (FrameLayout) childView
					.findViewById(R.id.img_content);
			LayoutParams contentParams = (LayoutParams) content
					.getLayoutParams();
			contentParams.width = width;
			content.setLayoutParams(contentParams);
			TextView name = (TextView) childView.findViewById(R.id.name);
			LayoutParams textParams = (LayoutParams) name.getLayoutParams();
			TextView time = (TextView) childView.findViewById(R.id.times);
			LayoutParams texttimeParams = (LayoutParams) time.getLayoutParams();
			RelativeLayout.LayoutParams childViewParams = new RelativeLayout.LayoutParams(
					width, contentParams.height + textParams.height
							+ texttimeParams.height);
			// childViewParams.width = width;
			// j * w, k * h + m
			int left = j * (width + padding);
			childViewParams.leftMargin = left;
			int top = (childViewParams.height + padding) * k;
			childViewParams.topMargin = top;
			// Log.d("initeLessons", "initeLessons:" + left + "," + top);
			// childView.setLayoutParams(childViewParams);
			// childViewParams.setMargins(left, top, 0, 0);
			ImageView img = (ImageView) childView.findViewById(R.id.img);
			ImageView label = (ImageView) childView.findViewById(R.id.label);
			TextView teacherName = (TextView) childView
					.findViewById(R.id.text_teacher);
			TextView price = (TextView) childView.findViewById(R.id.text_price);
			//
			if (lessonInfo.tags != null && lessonInfo.tags.size() > 0) {
				// Log.w("getTagsID", "getTagsID:" + lessonInfo.tags.get(0).code
				// + ",getTagsName:" + lessonInfo.tags.get(0).name);
				label.setImageResource(getTagsID(lessonInfo.tags.get(0).code));

			} else {
				label.setVisibility(View.GONE);
			}
			ImageView tag = (ImageView) childView.findViewById(R.id.tag);
			NetUtils.loadImage(con, lessonInfo.image, NetUtils.LARGE_IMAGE, img);
			name.setText("" + lessonInfo.name);
			time.setText("播放次数：" + lessonInfo.playtimes);
			if (lessonInfo.price != 0) {
				Log.w("price.setText", "¥" + lessonInfo.price);
				price.setText("¥" + lessonInfo.price);
			}
			teacherName.setText("" + lessonInfo.teacher.name);
			parentView.addView(childView, childViewParams);
			childView.setId(parentView.getId() + i);
			childView.setOnClickListener(new onLessonClick(lessonInfo, time));
			j++;
			if ((i + 3) % 2 == 0) {

				k++;
				j = 0;
			}
		}
	}

	public int getTagsID(String code) {

		if ("xm".equals(code)) {
			return R.drawable.tag_4;
		} else if ("dj".equals(code)) {
			return R.drawable.tag_1;
		} else if ("hy".equals(code)) {
			return R.drawable.tag_2;
		} else if ("mf".equals(code)) {
			return R.drawable.tag_3;
		}
		return 0;

	}

	class onLessonClick implements OnClickListener {
		LessonInfo lessonInfo;
		TextView textView;

		public onLessonClick(LessonInfo lessonInfo) {
			this.lessonInfo = lessonInfo;
		}

		public onLessonClick(LessonInfo lessonInfo, TextView textView) {
			this.lessonInfo = lessonInfo;
			this.textView = textView;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.d("startActivity", "startActivity" + lessonInfo.id);
			lessonInfo.playtimes++;
			startActivity(new Intent(con, AcLessonDetail.class).putExtra(
					"info", lessonInfo));

			if (textView != null) {
				textView.setText("播放次数：" + lessonInfo.playtimes);
			}

		}

	}

	void setClickLisenter() {
		for (int i = 0; i < llId.length; i++) {
			lessonViews[i] = (RelativeLayout) view.findViewById(llId[i]);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// startActivity(new Intent(con, AcLessonDetail.class).putExtra("id",
		// String.valueOf(id)));
		// switch (v.getId()) {
		//
		// case R.id.menu_btn:
		//
		// break;
		// case R.id.ll_0_0:
		//
		// break;
		//
		// }
	}

	class MyPageChangeListener implements OnPageChangeListener {
		/**
		 * @author
		 * 
		 *         设置ViewPager当前的底部小点
		 * 
		 * 
		 * */
		private void setCurrentDot(int currentPosition) {
			for (int i = 0; i < mDots.length; i++) {
				if (i == currentPosition) {
					pageName.setText("" + mImageViews[i].getTag().toString());
					mDots[i].setImageResource(R.drawable.dot_selected);
				} else {
					mDots[i].setImageResource(R.drawable.dot_normal);
				}
			}
		}

		@Override
		public void onPageScrollStateChanged(int status) {
			// TODO Auto-generated method stub
			switch (status) {
			case 1:// 手势滑动
				isScrolled = false;
				break;
			case 2:// 界面切换
				isScrolled = true;
				break;
			case 0:// 滑动结束

				// 当前为最后一张，此时从右向左滑，则切换到第一张
				if (mViewpager.getCurrentItem() == mViewpager.getAdapter()
						.getCount() - 1 && !isScrolled) {
					mViewpager.setCurrentItem(0);
				}
				// 当前为第一张，此时从左向右滑，则切换到最后一张
				else if (mViewpager.getCurrentItem() == 0 && !isScrolled) {
					mViewpager.setCurrentItem(mViewpager.getAdapter()
							.getCount() - 1);
				}
				break;
			}
		}

		boolean isScrolled = false;

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			setCurrentDot(arg0);
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		isPause = true;
		Log.d("FgMainImportantSuggestion", "onPause----->");
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		isPause = true;
		Log.d("FgMainImportantSuggestion", "onStop----->");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		isPause = true;
		Log.d("FgMainImportantSuggestion", "onDestroy----->");
		super.onDestroy();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (isFresh) {
			isFresh = false;

		}
		super.onResume();
	}

	class MoreClickListener implements OnClickListener {
		int i;

		public MoreClickListener(int i) {
			this.i = i;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(new Intent(con, AcLessonMore.class).putExtra("index",
					i));
		}

	}

}
