package com.cloudclass;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Toast;

import com.cloudclass.adapter.DownLoadAdapter;
import com.cloudclass.entity.GetCoursesInfo;
import com.cloudclass.entity.LessonInfo;
import com.cloudclass.entity.ReturnInfo;
import com.cloudclass.entity.UpLoadInfo;
import com.cloudclass.entity.UpLoadRetInfo;
import com.cloudclass.utils.ApiUtils;
import com.cloudclass.utils.DownLoadFile;
import com.cloudclass.utils.SdcardTools;
import com.cloudclass.view.Show;

public class AcDownLoad extends Activity implements OnItemClickListener,
		OnClickListener {

	private ListView listView;
	private ArrayList<LessonInfo> list;
	private DownLoadAdapter downLoadAdapter;
	private TextView tvEdit;
	boolean isEdit = false;
	AppMain appMain;
	private UpdateReceiver upReceiver;
	private View footer;// listview的footer
	private TextView farther;// footer的文本
	private ProgressBar progressBar;// footer的progressBar
	int pageSize;
	int pagecount;
	private int pageNum = 1;

	TextView stdory_num, cache_num;
	protected static final int INITE_CONTENT = 6;
	protected static final int DELETE_S = 1;
	protected static final int DELETE_F = 2;
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
						downLoadAdapter.notifyDataSetChanged();
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

				downLoadAdapter.notifyDataSetChanged();
				if (list.isEmpty()) {
					isEdit = false;
					findViewById(R.id.ll_delete).setVisibility(View.GONE);
					downLoadAdapter.setEdit(isEdit);
					tvEdit.setVisibility(View.GONE);
					findViewById(R.id.ll_content).setVisibility(View.GONE);
					findViewById(R.id.ll_tips).setVisibility(View.VISIBLE);
				}
				break;
			case DELETE_F:
				String message = msg.obj.toString();
				Show.toast(AcDownLoad.this, "" + message);

				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_down_load);
		initeView();

		upReceiver = new UpdateReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.MY_RECEIVER");
		registerReceiver(upReceiver, filter);
		appMain = (AppMain) getApplication();
		getData(appMain.loginInfo.id, pageNum);
	}

	void initeView() {
		listView = (ListView) findViewById(R.id.lvLive);
		stdory_num = (TextView) findViewById(R.id.stdory_num);
		cache_num = (TextView) findViewById(R.id.cache_num);
		stdory_num.append(":" + SdcardTools.getSDAvailableSize(this));

		try {
			cache_num.append(":"
					+ SdcardTools.getFormateSize(this, DownLoadFile.MP4_DIR));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listView.setOnItemClickListener(this);
		findViewById(R.id.back_btn).setOnClickListener(this);
		tvEdit = (TextView) findViewById(R.id.title_edit);
		findViewById(R.id.btn_delete).setOnClickListener(this);
		tvEdit.setOnClickListener(this);
		list = new ArrayList<LessonInfo>();
		downLoadAdapter = new DownLoadAdapter(this, list);
		listView.setAdapter(downLoadAdapter);
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		LessonInfo info = list.get(arg2);
		if (isEdit) {
			boolean isChecked = info.isChecked;

			list.get(arg2).isChecked = !isChecked;
			downLoadAdapter.notifyDataSetChanged();
		} else if (info.downLodProgress == 100) {
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
		downLoadAdapter.setEdit(isEdit);
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

						ReturnInfo ret = ApiUtils.uploadDownloadRecordsByUser(
								upLoadRetInfo, handler);
						if (ret != null) {
							if (ret.issuccess) {
								list.removeAll(deleteList);
								handler.sendEmptyMessage(DELETE_S);

							} else
								handler.sendMessage(Message.obtain(handler,
										DELETE_F, ret.errormsg));
						}

						super.run();
					}
				}.start();

			}

			break;
		default:
			break;
		}

	}

	/**
	 * 获取数据
	 */
	void getData(final String id, final int pageNum) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				GetCoursesInfo response = ApiUtils.getDownloadRecordsByUser(id,
						pageNum, handler);
				handler.sendMessage(Message.obtain(handler, INITE_CONTENT,
						response));
				super.run();
			}
		}.start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(upReceiver);
		super.onDestroy();
	}

	/**
	 * 列表更新广播接收器
	 * 
	 * @author Administrator
	 * 
	 */
	public class UpdateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			int id = intent.getIntExtra("id", 0);
			int progress = intent.getIntExtra("progress", 0);
			for (int i = 0; i < list.size(); i++) {
				LessonInfo info = list.get(i);
				if (info.id == id) {
					info.downLodProgress = progress;
					if (info.downLodProgress == 100) {
						info.downLoadState = "播放";
					} else
						info.downLoadState = "下载中";
					downLoadAdapter.notifyDataSetChanged();
					break;
				}
			}
			Log.w("AcDownLoad", "列表刷新~~~~~~~~~~~~・~~~~~id:" + id + ",progress"
					+ progress);
		}
	}
}
