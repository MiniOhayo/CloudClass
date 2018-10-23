package com.cloudclass.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cloudclass.AcLessonDetail;
import com.cloudclass.AcSearch;
import com.cloudclass.AppMain;
import com.cloudclass.R;
import com.cloudclass.adapter.MainImportantFreeAdapter;
import com.cloudclass.adapter.SearchItemAdapter;
import com.cloudclass.entity.GetCoursesInfo;
import com.cloudclass.entity.GetLessonTypeInfo;
import com.cloudclass.entity.LessonInfo;
import com.cloudclass.entity.LessonTypeInfo;
import com.cloudclass.utils.ApiUtils;
import com.cloudclass.utils.ViewUtils;
import com.cloudclass.view.Show;
import com.example.provinceselector.ClearEditText;

public class FgMainAllLesson extends Fragment implements OnClickListener,
		OnItemClickListener, OnCheckedChangeListener {
	protected static final int INITE_CONTENT = 6;
	protected static final int SEARCH_CONTENT = 7;
	protected static final int SEARCH_NEXT_CONTENT = 8;
	public static final int SAVE_OK = 2;
	public static final int SAVE_ERROE = 3;
	AppMain appMain;
	TextView titleName;
	int mode = 0;// 0为搜索 1为搜索结果
	InputMethodManager imm;
	private FragmentActivity con;
	private ProgressDialog progressDialog;
	private View view;// ,llSearch;
	private View footer;// listview的footer
	private TextView farther;// footer的文本
	private ProgressBar progressBar;// footer的progressBar
	// boolean isHot=true;

	RadioGroup rgIsHot;

	RadioButton rbHot, rbNew;
	private int pageSize;
	private int pagecount;

	boolean isGetData = false;

	int Domestic = 0;// 0为全部，1位国内，2，为国外

	// public ArrayList<Fragment> fragments = new ArrayList<Fragment>();;
	public Handler hander = new Handler() {

		@SuppressLint("NewApi")
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case SEARCH_CONTENT:

				GetCoursesInfo response1 = (GetCoursesInfo) msg.obj;

				if (response1 != null && response1.data != null
						&& response1.data.size() > 0) {
					pageSize = response1.pagesize;
					pagecount = response1.pagecount;
					lessonList.addAll(response1.data);
					mainImportantFreeAdapter.notifyDataSetChanged();
					ViewUtils.setListViewHeightBasedOnChildren(lessonListView);
				} else
					Show.toast(con, "抱歉，没有搜到相关课程");
				progressBar.setVisibility(View.INVISIBLE);
				farther.setVisibility(View.VISIBLE);
				break;
			case SEARCH_NEXT_CONTENT:

				GetCoursesInfo response = (GetCoursesInfo) msg.obj;

				if (response != null && response.data != null
						&& response.data.size() > 0) {
					// pageSize = response.pagesize;
					// pagecount = response.pagecount;
					lessonList.addAll(response.data);
					mainImportantFreeAdapter.notifyDataSetChanged();
					ViewUtils.setListViewHeightBasedOnChildren(lessonListView);

				} else
					Show.toast(con, "抱歉，没有搜到相关课程");
				progressBar.setVisibility(View.INVISIBLE);
				farther.setVisibility(View.VISIBLE);
				break;
			case INITE_CONTENT:
				GetLessonTypeInfo response2 = (GetLessonTypeInfo) msg.obj;
				if (response2 != null && response2.data != null) {
					typeList.add(new LessonTypeInfo("", "全部", true));
					typeList.addAll(response2.data);
					searchItemAdapter.notifyDataSetChanged();
					ViewUtils.setListViewHeightBasedOnChildren(listView);
				}

				break;
			}
			// progressDialog.dismiss();
		}
	};
	private SearchItemAdapter searchItemAdapter;
	private GridView listView;
	private ArrayList<LessonTypeInfo> typeList;
	private String type = "";
	private TextView etSearch;
	// private Button btn_cancel;
	private ListView lessonListView;
	private ArrayList<LessonInfo> lessonList;
	private MainImportantFreeAdapter mainImportantFreeAdapter;
	private RadioGroup rg_isDomestic;
	private RadioButton rb_isDomestic;

	private int pageNum = 1;
	private ScrollView myScrollView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		con = this.getActivity();

		appMain = (AppMain) con.getApplication();
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		if (view == null) {
			initeView(inflater, container);
		}

		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);

		}
		return view;

	}

	private void initeView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fg_main_all_lesson, container, false);
		// view.findViewById(R.id.menu_btn).setOnClickListener(this);

		myScrollView = (ScrollView) view.findViewById(R.id.myScrollView);
		titleName = (TextView) view.findViewById(R.id.title_name);
		// llSearch=view.findViewById(R.id.ll_search);
		listView = (GridView) view.findViewById(R.id.gvType);
		// btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		// btn_cancel.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		typeList = new ArrayList<LessonTypeInfo>();
		searchItemAdapter = new SearchItemAdapter(con, typeList);
		searchItemAdapter.setAllLessonType(true);
		listView.setAdapter(searchItemAdapter);

		etSearch = (TextView) view.findViewById(R.id.filter_edit);
		lessonListView = (ListView) view.findViewById(R.id.lvLesson);
		rgIsHot = (RadioGroup) view.findViewById(R.id.rg_isNew_isHot);
		rbHot = (RadioButton) view.findViewById(R.id.isHot);
		rg_isDomestic = (RadioGroup) view.findViewById(R.id.rg_isDomestic);
		rb_isDomestic = (RadioButton) view.findViewById(R.id.rb_isDomestic);
		etSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(con, AcSearch.class));
			}
		});
		lessonListView.setOnItemClickListener(this);
		lessonList = new ArrayList<LessonInfo>();
		rgIsHot.setOnCheckedChangeListener(this);
		rg_isDomestic.setOnCheckedChangeListener(this);
		mainImportantFreeAdapter = new MainImportantFreeAdapter(lessonList, con);
		footer = inflater.inflate(R.layout.listview_footer, null);
		progressBar = (ProgressBar) footer.findViewById(R.id.main_pb);
		farther = (TextView) footer.findViewById(R.id.TextView_farther);
		farther.setText("更多");
		lessonListView.addFooterView(footer);
		lessonListView.setAdapter(mainImportantFreeAdapter);
		myScrollView.setOnTouchListener(new TouchListenerImpl());
		imm = (InputMethodManager) con
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		getData();
		footer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isGetData) {
					if (progressBar.getVisibility() == View.INVISIBLE) {
						if (pageSize * pagecount <= lessonList.size()) {
							farther.setVisibility(View.VISIBLE);
							progressBar.setVisibility(View.INVISIBLE);
							farther.setText("最后一页了");
						} else {
							progressBar.setVisibility(View.VISIBLE);
							farther.setVisibility(View.VISIBLE);
							// nextPage();
							Log.d("onScrollStateChanged",
									"onScrollStateChanged,加载更多。。。");
							pageNum++;
							searchNextPage(pageNum, Domestic);

						}
					}
				}

			}
		});

		search("", "", Domestic, rbHot.isChecked());

	}

	private class TouchListenerImpl implements OnTouchListener {
		int tmpScrollViewMeasuredHeight = 0;

		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:

				break;
			case MotionEvent.ACTION_MOVE:
				int scrollY = view.getScrollY();
				int height = view.getHeight();
				int scrollViewMeasuredHeight = myScrollView.getChildAt(0)
						.getMeasuredHeight();
				if (scrollY == 0) {
					System.out.println("滑动到了顶端 view.getScrollY()=" + scrollY);
				}
				if (((scrollY + height) > scrollViewMeasuredHeight - 100)
						&& ((scrollY + height) <= scrollViewMeasuredHeight)) {
					if (tmpScrollViewMeasuredHeight != scrollViewMeasuredHeight) {

						if (!isGetData) {
							tmpScrollViewMeasuredHeight = scrollViewMeasuredHeight;
							System.out
									.println("滑动到了底部 scrollViewMeasuredHeight="
											+ scrollViewMeasuredHeight);
							if (progressBar.getVisibility() == View.INVISIBLE) {

								if (pageSize * pagecount <= lessonList.size()) {
									farther.setVisibility(View.VISIBLE);
									progressBar.setVisibility(View.INVISIBLE);
									farther.setText("最后一页了");
								} else {
									progressBar.setVisibility(View.VISIBLE);
									farther.setVisibility(View.VISIBLE);
									// nextPage();
									Log.d("onScrollStateChanged",
											"onScrollStateChanged,加载更多。。。");
									pageNum++;
									searchNextPage(pageNum, Domestic);

								}
							}
						}
					}

				}

				break;

			default:
				break;
			}
			return false;
		}

	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.menu_btn:

			break;
		case R.id.btn_cancel:
			if (mode == 0) {
				// String searchStr=etSearch.getText().toString().trim();

				mode = 1;
				search(type, etSearch.getText().toString(), Domestic,
						rbHot.isChecked());
				imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
			} else {
				mode = 0;
				// btn_cancel.setText("搜索");
				// llSearch.setVisibility(View.VISIBLE);
				// lessonListView.setVisibility(View.GONE);
				imm.showSoftInput(etSearch, 0);
			}
			break;
		}
	}

	void search(final String type, final String keyword, final int isDomestic,
			final boolean isHot) {
		mode = 1;
		// btn_cancel.setText("取消");
		// llSearch.setVisibility(View.GONE);
		// lessonListView.setVisibility(View.VISIBLE);
		lessonList.clear();
		isGetData = true;
		pageNum = 1;
		farther.setText("更多");
		new Thread() {
			@Override
			public void run() {
				String DomesticStr = "";
				if (isDomestic == 1) {
					DomesticStr = "国内";
				} else if (isDomestic == 2) {
					DomesticStr = "国外";
				}

				GetCoursesInfo response = ApiUtils.getCoursesByCondition(1,
						type, keyword, DomesticStr, isHot, hander);
				isGetData = false;
				hander.sendMessage(Message.obtain(hander, SEARCH_CONTENT,
						response));
				super.run();
			}
		}.start();
	}

	void searchNextPage(final int page, final int isDomestic) {
		mode = 1;
		isGetData = true;
		new Thread() {
			@Override
			public void run() {
				String DomesticStr = "";
				if (isDomestic == 1) {
					DomesticStr = "国内";
				} else if (isDomestic == 2) {
					DomesticStr = "国外";
				}
				GetCoursesInfo response = ApiUtils.getCoursesByCondition(page,
						type, "", DomesticStr, rbHot.isChecked(), hander);
				isGetData = false;
				hander.sendMessage(Message.obtain(hander, SEARCH_NEXT_CONTENT,
						response));
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
				GetLessonTypeInfo response = ApiUtils
						.getAllCourseCategories(hander);
				hander.sendMessage(Message.obtain(hander, INITE_CONTENT,
						response));

				super.run();
			}
		}.start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (parent.getId() == R.id.lvLesson) {
			LessonInfo info = lessonList.get(position);
			startActivity(new Intent(con, AcLessonDetail.class).putExtra(
					"info", info));
		} else {
			for (int i = 0; i < typeList.size(); i++) {
				if (i != position) {
					typeList.get(i).isCheck = false;
				}
			}
			boolean isChecked = typeList.get(position).isCheck;
			typeList.get(position).isCheck = !isChecked;
			if (typeList.get(position).isCheck) {
				type = typeList.get(position).code;
			} else
				type = "";
			searchItemAdapter.updateListView(typeList);
			search(type, etSearch.getText().toString(), Domestic,
					rbHot.isChecked());
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if (group.getId() == R.id.rg_isDomestic) {
			if (checkedId == R.id.rb_isAbroad) {
				Domestic = 2;
			} else if (checkedId == R.id.rb_isDomestic) {
				Domestic = 1;
			} else
				Domestic = 0;

		}
		search(type, etSearch.getText().toString(), Domestic, rbHot.isChecked());
	}
}
