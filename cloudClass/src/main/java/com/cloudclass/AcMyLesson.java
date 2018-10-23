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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.cloudclass.adapter.FavoriteAdapter;
import com.cloudclass.entity.GetCoursesInfo;
import com.cloudclass.entity.LessonInfo;
import com.cloudclass.entity.ReturnInfo;
import com.cloudclass.entity.UpLoadInfo;
import com.cloudclass.entity.UpLoadRetInfo;
import com.cloudclass.utils.ApiUtils;

public class AcMyLesson extends Activity implements OnItemClickListener,
		OnClickListener {

	private ListView listView;
	private ArrayList<LessonInfo> list;
	private FavoriteAdapter favoriteAdapter;

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
	protected static final int DELETE_S = 1;
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
						list.addAll(response.data);
						favoriteAdapter.notifyDataSetChanged();
						if (list.isEmpty()) {

							findViewById(R.id.ll_content).setVisibility(
									View.GONE);
							findViewById(R.id.ll_tips).setVisibility(
									View.VISIBLE);
						}
					}
				}
				break;
			case DELETE_S:

				favoriteAdapter.notifyDataSetChanged();
				if (list.isEmpty()) {
					isEdit = false;
					findViewById(R.id.ll_delete).setVisibility(View.GONE);
					// favoriteAdapter.setEdit(isEdit);
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
		setContentView(R.layout.ac_my_lesson);
		initeView();
		appMain = (AppMain) getApplication();
		findViewById(R.id.back_btn).setOnClickListener(this);
		getData(appMain.loginInfo.id, pageNum);
	}

	void initeView() {
		tvEdit = (TextView) findViewById(R.id.title_edit);
		tvEdit.setOnClickListener(this);
		findViewById(R.id.btn_delete).setOnClickListener(this);
		listView = (ListView) findViewById(R.id.lvLive);
		listView.setOnItemClickListener(this);
		list = new ArrayList<LessonInfo>();
		favoriteAdapter = new FavoriteAdapter(this, list);
		listView.setAdapter(favoriteAdapter);
		footer = LayoutInflater.from(this).inflate(R.layout.listview_footer,
				null);
		progressBar = (ProgressBar) footer.findViewById(R.id.main_pb);
		farther = (TextView) footer.findViewById(R.id.TextView_farther);
		farther.setText("更多");
		listView.addFooterView(footer);
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
		case R.id.title_edit:
			edit();
			break;
		case R.id.btn_delete:
			final ArrayList<LessonInfo> deleteList = new ArrayList<LessonInfo>();
			final UpLoadRetInfo upLoadRetInfo = new UpLoadRetInfo();

			boolean isSelected = false;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).isChecked) {
					deleteList.add(list.get(i));
					upLoadRetInfo.data.add(new UpLoadInfo(appMain.loginInfo.id,
							String.valueOf(list.get(i).id), true));
					isSelected = true;
				}
			}
			if (!isSelected) {
				Toast.makeText(this, "您还没有选择删除项", Toast.LENGTH_SHORT).show();
			} else {
				new Thread() {

					@Override
					public void run() {

						ReturnInfo ret = ApiUtils.uploadMyCoursesByUser(
								upLoadRetInfo, handler);
						if (ret.issuccess) {
							list.removeAll(deleteList);
							handler.sendEmptyMessage(DELETE_S);

						}
						super.run();
					}
				}.start();

			}

			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (isEdit) {
			boolean isChecked = list.get(arg2).isChecked;

			list.get(arg2).isChecked = !isChecked;
			favoriteAdapter.notifyDataSetChanged();
		} else {
			LessonInfo info = list.get(arg2);
			startActivity(new Intent(this, AcLessonDetail.class).putExtra(
					"info", info));
		}
	}

	public void edit() {
		if (isEdit) {
			isEdit = false;
			tvEdit.setText("编辑");
			findViewById(R.id.ll_delete).setVisibility(View.GONE);
		} else {
			isEdit = true;
			tvEdit.setText("完成");
			findViewById(R.id.ll_delete).setVisibility(View.VISIBLE);

		}
		favoriteAdapter.setEdit(isEdit);
	}

	/**
	 * 获取数据
	 */
	void getData(final String id, final int pageNum) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				GetCoursesInfo response = ApiUtils.getMyCoursesByUser(id,
						pageNum, handler);
				handler.sendMessage(Message.obtain(handler, INITE_CONTENT,
						response));
				super.run();
			}
		}.start();
	}
}