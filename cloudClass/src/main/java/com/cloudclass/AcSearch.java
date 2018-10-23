package com.cloudclass;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudclass.adapter.KeyWordAdapter;
import com.cloudclass.adapter.MainImportantFreeAdapter;
import com.cloudclass.adapter.SearchItemAdapter;
import com.cloudclass.entity.GetCoursesInfo;
import com.cloudclass.entity.GetSearchKeyword;
import com.cloudclass.entity.GetSearchKeyword.SearchKeywordInfo;
import com.cloudclass.entity.LessonInfo;
import com.cloudclass.utils.ApiUtils;
import com.cloudclass.utils.ViewUtils;
import com.cloudclass.view.Show;
import com.example.provinceselector.ClearEditText;

public class AcSearch extends Activity implements OnClickListener,
		OnItemClickListener {
	protected static final int INITE_CONTENT = 6;
	protected static final int SEARCH_CONTENT = 7;
	protected static final int SEARCH_NEXT_CONTENT = 8;
	private GridView listView;
	private ListView lessonListView;
	private List<SearchKeywordInfo> typeList;
	private List<LessonInfo> lessonList;
	private KeyWordAdapter searchItemAdapter;
	MainImportantFreeAdapter mainImportantFreeAdapter;
	private ClearEditText etSearch;
	int mode = 0;// 0为搜索 1为搜索结果
	InputMethodManager imm;
	private String type = "";
	private int pageSize;
	private int pagecount;
	private int pageNum;
	private View footer;// listview的footer
	private TextView farther;// footer的文本
	private ProgressBar progressBar;// footer的progressBar
	public Handler hander = new Handler() {

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
					imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
				} else
					Show.toast(AcSearch.this, "抱歉，没有搜到相关课程");

				break;
			case SEARCH_NEXT_CONTENT:

				GetCoursesInfo response = (GetCoursesInfo) msg.obj;

				if (response != null && response.data != null
						&& response.data.size() > 0) {
					pageSize = response.pagesize;
					pagecount = response.pagecount;
					lessonList.addAll(response.data);
					mainImportantFreeAdapter.notifyDataSetChanged();
					ViewUtils.setListViewHeightBasedOnChildren(lessonListView);
					progressBar.setVisibility(View.INVISIBLE);
					farther.setVisibility(View.VISIBLE);
				} else
					Show.toast(AcSearch.this, "抱歉，没有搜到相关课程");

				break;
			case INITE_CONTENT:

				GetSearchKeyword response2 = (GetSearchKeyword) msg.obj;

				if (response2 != null && response2.data != null) {
					typeList.addAll(response2.data);
					searchItemAdapter.notifyDataSetChanged();
				}

				break;
			}
			// progressDialog.dismiss();
		}
	};
	private Button btn_cancel;
	private boolean isGetData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_search);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		initeView();
		getData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stubs
		switch (v.getId()) {
		case R.id.btn_cancel:
			if (mode == 0) {
				// String searchStr=etSearch.getText().toString().trim();

				mode = 1;
				search(type, etSearch.getText().toString());
				imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
			} else {
				mode = 0;
				btn_cancel.setText("搜索");
				listView.setVisibility(View.VISIBLE);
				lessonListView.setVisibility(View.GONE);
				imm.showSoftInput(etSearch, 0);
			}

			break;

		default:
			break;
		}

	}

	void initeView() {

		lessonListView = (ListView) findViewById(R.id.lvLesson);
		lessonListView.setOnItemClickListener(this);
		lessonList = new ArrayList<LessonInfo>();
		mainImportantFreeAdapter = new MainImportantFreeAdapter(lessonList,
				this);
		lessonListView.setAdapter(mainImportantFreeAdapter);
		etSearch = (ClearEditText) findViewById(R.id.filter_edit);

		imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		// 显示键盘

		// 隐藏键盘

		listView = (GridView) findViewById(R.id.gvType);
		listView.setOnItemClickListener(this);
		typeList = new ArrayList<SearchKeywordInfo>();
		footer = LayoutInflater.from(this).inflate(R.layout.listview_footer,
				null);
		progressBar = (ProgressBar) footer.findViewById(R.id.main_pb);
		farther = (TextView) footer.findViewById(R.id.TextView_farther);
		farther.setText("更多");
		lessonListView.addFooterView(footer);
		searchItemAdapter = new KeyWordAdapter(this, typeList);
		listView.setAdapter(searchItemAdapter);
		listView.setOnScrollListener(new OnScrollListener() {
			private int lastItem;

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastItem = firstVisibleItem + visibleItemCount;

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (lastItem >= lessonList.size()
						&& progressBar.getVisibility() == View.INVISIBLE
						&& scrollState == SCROLL_STATE_IDLE) {
					// listView.addFooterView(footer);
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
						searchNextPage(pageNum, etSearch.getText().toString()
								.trim());

					}
				}
			}

		});
		etSearch.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				// if (hasFocus) {
				// lessonListView.setVisibility(View.GONE);
				// listView.setVisibility(View.VISIBLE);
				// } else {
				// lessonListView.setVisibility(View.VISIBLE);
				// listView.setVisibility(View.GONE);
				// }
			}
		});
		etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					Log.d("onEditorAction", "onEditorAction  搜索--->");
					search(type, v.getText().toString());
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.lvLesson) {
			LessonInfo info = lessonList.get(arg2);
			startActivity(new Intent(this, AcLessonDetail.class).putExtra(
					"info", info));
		} else {
			// for (int i = 0; i < typeList.size(); i++) {
			// if (i != arg2) {
			// typeList.get(i).isCheck = false;
			// }
			// }
			// boolean isChecked = typeList.get(arg2).isCheck;
			// typeList.get(arg2).isCheck = !isChecked;
			SearchKeywordInfo info = typeList.get(arg2);
			etSearch.setText("" + info.keyword);
			search(type, info.keyword);

		}
	}

	void search(final String type, final String keyword) {
		mode = 1;
		btn_cancel.setText("取消");
		listView.setVisibility(View.GONE);
		lessonListView.setVisibility(View.VISIBLE);
		lessonList.clear();
		new Thread() {
			@Override
			public void run() {
				GetCoursesInfo response = ApiUtils.getCoursesByCondition(1,
						type, keyword, hander);
				hander.sendMessage(Message.obtain(hander, SEARCH_CONTENT,
						response));
				super.run();
			}
		}.start();
	}

	void searchNextPage(final int page, final String keyword) {
		mode = 1;
		isGetData = true;
		btn_cancel.setText("取消");
		listView.setVisibility(View.GONE);
		lessonListView.setVisibility(View.VISIBLE);
		new Thread() {
			@Override
			public void run() {
				GetCoursesInfo response = ApiUtils.getCoursesByCondition(page,
						type, keyword, hander);
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
				GetSearchKeyword response = ApiUtils.getSearchKeywords(hander);
				hander.sendMessage(Message.obtain(hander, INITE_CONTENT,
						response));

				super.run();
			}
		}.start();
	}
}
