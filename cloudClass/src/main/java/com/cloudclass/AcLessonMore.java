package com.cloudclass;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudclass.adapter.MainImportantFreeAdapter;
import com.cloudclass.entity.GetCoursesInfo;
import com.cloudclass.entity.LessonInfo;
import com.cloudclass.utils.ApiUtils;

public class AcLessonMore extends Activity implements OnClickListener,
		OnItemClickListener {
	protected static final int INITE_CONTENT = 6;
	public static final int SAVE_OK = 2;
	public static final int SAVE_ERROE = 3;
	AppMain appMain;
	TextView titleName;
	boolean isModify;
	ListView listView;
	MainImportantFreeAdapter mainImportantFreeAdapter;
	private Activity con;
	// private progressDialog;
	private View footer;// listview的footer
	private TextView farther;// footer的文本
	private ProgressBar progressBar;// footer的progressBar
	// private View view;
	List<LessonInfo> list;
	int pageNum = 1;
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
					mainImportantFreeAdapter.notifyDataSetChanged();
				}

				break;
			}
			// progressDialog.dismiss();
		}
	};
	private int index;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.ac_lesson_more);
		con = this;
		appMain = (AppMain) con.getApplication();
		index = getIntent().getIntExtra("index", 0);
		initeView();
		getData(pageNum);
		super.onCreate(savedInstanceState);

	}

	private void initeView() {
		listView = (ListView) findViewById(R.id.lvLive);
		listView.setOnItemClickListener(this);
		findViewById(R.id.back_btn).setOnClickListener(this);
		TextView tvTitle = (TextView) findViewById(R.id.title_name);
		String[] tvStr = { getString(R.string.tv_favorite),
				getString(R.string.tv_recommend), getString(R.string.tv_week),
				getString(R.string.tv_vip) };
		tvTitle.setText(tvStr[index]);
		list = new ArrayList<LessonInfo>();
		mainImportantFreeAdapter = new MainImportantFreeAdapter(list, con);
		footer = LayoutInflater.from(con).inflate(R.layout.listview_footer,
				null);
		progressBar = (ProgressBar) footer.findViewById(R.id.main_pb);
		farther = (TextView) footer.findViewById(R.id.TextView_farther);
		farther.setText("更多");
		listView.addFooterView(footer);

		// listView.addHeaderView(TextView);

		listView.setAdapter(mainImportantFreeAdapter);

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
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				Log.d("onScrollStateChanged", "lastItem:" + lastItem
						+ ",scrollState:" + scrollState);
				if (lastItem >= list.size()
						&& progressBar.getVisibility() == View.INVISIBLE
						&& scrollState == SCROLL_STATE_IDLE) {
					// listView.addFooterView(footer);
					if (pageSize * pagecount >= list.size()) {
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
		// listView.setonRefreshListener(new OnRefreshListener() {
		//
		// @Override
		// public void onRefresh() {
		// new AsyncTask<Void, Void, Void>() {
		// protected Void doInBackground(Void... params) {
		// try {
		// Thread.sleep(1000);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// list.add(new LessonInfo());
		// return null;
		// }
		//
		// @Override
		// protected void onPostExecute(Void result) {
		// mainImportantFreeAdapter.notifyDataSetChanged();
		// listView.onRefreshComplete();
		// }
		// }.execute(null, null, null);
		// }
		// });
	}

	/**
	 * 获取数据
	 */
	void getData(final int pageNum) {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				GetCoursesInfo response = null;
				switch (index) {
				case 0:
					response = ApiUtils.getGuessFavoriteCourses(pageNum, 10, hander);
					break;
				case 1:
					response = ApiUtils.getRecommendedCourses(pageNum, 10, hander);
					break;
				case 2:
					response = ApiUtils.getWeekTitleCourses(pageNum, 10, hander);
					break;
				case 3:
					response = ApiUtils.getVIPCourses(pageNum, 10, hander);
					break;
				default:
					break;
				}
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
		case R.id.back_btn:
			finish();
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
