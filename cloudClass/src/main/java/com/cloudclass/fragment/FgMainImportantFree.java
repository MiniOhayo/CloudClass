package com.cloudclass.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudclass.AcLessonDetail;
import com.cloudclass.AppMain;
import com.cloudclass.R;
import com.cloudclass.adapter.MainImportantFreeAdapter;
import com.cloudclass.entity.GetCoursesInfo;
import com.cloudclass.entity.LessonInfo;
import com.cloudclass.utils.ApiUtils;
import com.cloudclass.utils.ViewUtils;
import com.cloudclass.view.PullListView;
import com.cloudclass.view.Show;
import com.cloudclass.view.PullListView.OnRefreshListener;

public class FgMainImportantFree extends Fragment implements OnClickListener,
		OnItemClickListener {
	protected static final int INITE_CONTENT = 6;
	public static final int SAVE_OK = 2;
	public static final int SAVE_ERROE = 3;
	AppMain appMain;
	TextView titleName;
	boolean isModify;

	ListView listView;
	MainImportantFreeAdapter mainImportantFreeAdapter;
	private FragmentActivity con;
	// private ProgressDialog progressDialog;
	private View footer;// listview的footer
	private TextView farther;// footer的文本
	private ProgressBar progressBar;// footer的progressBar
	private View view;
	List<LessonInfo> list;
	int pageSize;
	int pagecount;
	// public ArrayList<Fragment> fragments = new ArrayList<Fragment>();;
	public Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case INITE_CONTENT:

				GetCoursesInfo response = (GetCoursesInfo) msg.obj;
				progressBar.setVisibility(View.INVISIBLE);
				farther.setVisibility(View.INVISIBLE);
				if (response != null && response.data != null) {
					pageSize = response.pagesize;
					pagecount = response.pagecount;
					list.addAll(response.data);
					if (list.isEmpty()) {
						Show.toast(con, "抱歉，没有找到相关课程");
					}
					mainImportantFreeAdapter.notifyDataSetChanged();
				}

				break;

			}
			// progressDialog.dismiss();
		}
	};
	private int pageNum = 1;

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
		view = inflater.inflate(R.layout.fg_main_important_free, container,
				false);
		listView = (ListView) view.findViewById(R.id.lvLive);
		listView.setOnItemClickListener(this);
		list = new ArrayList<LessonInfo>();
		mainImportantFreeAdapter = new MainImportantFreeAdapter(list, con);
		footer = inflater.inflate(R.layout.listview_footer, null);
		progressBar = (ProgressBar) footer.findViewById(R.id.main_pb);
		farther = (TextView) footer.findViewById(R.id.TextView_farther);
		farther.setText("更多");
		listView.addFooterView(footer);
		TextView TextView = new TextView(con);
		TextView.setText("list  头结点");
		// listView.addHeaderView(TextView);

		listView.setAdapter(mainImportantFreeAdapter);

		getData(pageNum);

		inflater = LayoutInflater.from(con);

		footer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (progressBar.getVisibility() == View.INVISIBLE) {
					// nextPage();
				}
			}
		});

		listView.setOnScrollListener(new OnScrollListener() {
			private int lastItem;

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastItem = firstVisibleItem + visibleItemCount;

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
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
						getData(pageNum);

					}
				}
			}

		});

	}

	/**
	 * 获取数据
	 */
	void getData(final int pageNum) {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				GetCoursesInfo response = ApiUtils.getCoursesByCondition(
						pageNum, 0, 0, hander);

				hander.sendMessage(Message.obtain(hander, INITE_CONTENT,
						response));

				super.run();
			}
		}.start();
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		startActivity(new Intent(con, AcLessonDetail.class).putExtra("info",
				list.get(position)));
	}

}
