package com.cloudclass;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cloudclass.adapter.HistoryAdapter;
import com.cloudclass.entity.DayType;
import com.cloudclass.entity.GetCoursesInfo;
import com.cloudclass.entity.LessonInfo;
import com.cloudclass.utils.ApiUtils;
import com.cloudclass.utils.Utils;

public class AcHistory extends Activity implements OnItemClickListener,
		OnClickListener {

	private ListView listView;
	private ArrayList<LessonInfo> list;
	private HistoryAdapter historyAdapter;

	private TextView tvEdit;
	boolean isEdit = false;
	AppMain appMain;
	private View footer;// listview的footer
	private TextView farther;// footer的文本
	private ProgressBar progressBar;// footer的progressBar
	int pageSize;
	int pagecount;
	private int pageNum = 1;
	protected static final int INITE_CONTENT = 6;
	protected static final int DELETE_S = 3;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {

			case INITE_CONTENT:
				GetCoursesInfo response = (GetCoursesInfo) msg.obj;
				if (response != null) {
					progressBar.setVisibility(View.INVISIBLE);
					farther.setVisibility(View.INVISIBLE);
					if (response != null && response.data != null) {
						pageSize = response.pagesize;
						pagecount = response.pagecount;
						for (int i = 0; i < response.data.size(); i++) {
							LessonInfo info = response.data.get(i);
							// info.timeTag
							DayType tempDayType = Utils
									.getDayType(info.updatedate);
							if (tempDayType == DayType.today) {
								info.timeTag = "最近一天";
							} else if (tempDayType == DayType.week) {
								info.timeTag = "最近一周";
							} else if (tempDayType == DayType.month) {
								info.timeTag = "最近一月";
							} else if (tempDayType == DayType.moreLong) {
								info.timeTag = "更久";
							}
						}
						list.addAll(response.data);
						historyAdapter.notifyDataSetChanged();
					}
				}
				break;
			case DELETE_S:

				historyAdapter.notifyDataSetChanged();
				if (list.isEmpty()) {
					isEdit = false;
					findViewById(R.id.ll_delete).setVisibility(View.GONE);
					// historyAdapter.setEdit(isEdit);
					tvEdit.setVisibility(View.GONE);
					findViewById(R.id.ll_content).setVisibility(View.GONE);
					findViewById(R.id.ll_tips).setVisibility(View.VISIBLE);
					break;
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_history);
		initeView();
		appMain = (AppMain) getApplication();
		findViewById(R.id.back_btn).setOnClickListener(this);
		if (appMain.loginInfo != null) {
			getData(appMain.loginInfo.id, pageNum);
		}

	}

	void initeView() {
		listView = (ListView) findViewById(R.id.lvLive);


		list = new ArrayList<LessonInfo>();
		historyAdapter = new HistoryAdapter(this, list);
		listView.setAdapter(historyAdapter);
		footer = LayoutInflater.from(this).inflate(R.layout.listview_footer,
				null);
		progressBar = (ProgressBar) footer.findViewById(R.id.main_pb);
		farther = (TextView) footer.findViewById(R.id.TextView_farther);
		farther.setText("更多");
		listView.addFooterView(footer, null, false);
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(new OnScrollListener() {
			private int lastItem;

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastItem = firstVisibleItem + visibleItemCount;

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				Log.d("onScrollStateChanged", "lastItem:" + lastItem
						+ ",scrollState:" + scrollState);
				if (lastItem >= list.size()
						&& progressBar.getVisibility() == View.INVISIBLE
						&& scrollState == SCROLL_STATE_IDLE) {
					// listView.addFooterView(footer);
					if (pageSize * pagecount <= list.size()) {
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
						getData(appMain.loginInfo.id, pageNum);

					}
				}
			}

		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_btn:
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		LessonInfo info = list.get(arg2);
		startActivity(new Intent(this, AcLessonDetail.class).putExtra("info",
				info));
	}

	/**
	 * 获取数据
	 */
	void getData(final String id, final int pageNum) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				GetCoursesInfo response = ApiUtils.getPlayRecordsByUser(id,
						pageNum, handler);
				handler.sendMessage(Message.obtain(handler, INITE_CONTENT,
						response));
				super.run();
			}
		}.start();
	}
}
