package com.cloudclass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
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

import com.cloudclass.adapter.CommentsAdapter;
import com.cloudclass.adapter.FavoriteAdapter;
import com.cloudclass.entity.CommentInfo;
import com.cloudclass.entity.GetCommentInfo;
import com.cloudclass.entity.GetCoursesInfo;
import com.cloudclass.entity.LessonInfo;
import com.cloudclass.entity.ReturnInfo;
import com.cloudclass.entity.UpLoadInfo;
import com.cloudclass.entity.UpLoadRetInfo;
import com.cloudclass.utils.ApiUtils;

public class AcComments extends Activity implements OnItemClickListener,
		OnClickListener {

	private ListView listView;
	private ArrayList<CommentInfo> list;
	private CommentsAdapter commentsAdapter;

	// private TextView tvEdit;
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
				GetCommentInfo response = (GetCommentInfo) msg.obj;
				if (response != null) {

					progressBar.setVisibility(View.INVISIBLE);
					farther.setVisibility(View.INVISIBLE);
					if (response != null && response.data != null) {
						pageSize = response.pagesize;
						pagecount = response.pagecount;
						for (int i = 0; i < response.data.size(); i++) {
							CommentInfo commentInfo = response.data.get(i);
							long ti = commentInfo.getTime();
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							// String today="2015-05-17 16:08:54";
							commentInfo.setTimes(sdf.format(new Date(ti)));
						}
						list.addAll(response.data);
						commentsAdapter.notifyDataSetChanged();
					}
				}
				break;
			case DELETE_S:

				commentsAdapter.notifyDataSetChanged();
				if (list.isEmpty()) {
					isEdit = false;
					findViewById(R.id.ll_delete).setVisibility(View.GONE);
					// favoriteAdapter.setEdit(isEdit);
					// tvEdit.setVisibility(View.GONE);
					findViewById(R.id.ll_content).setVisibility(View.GONE);
					findViewById(R.id.ll_tips).setVisibility(View.VISIBLE);
					break;
				}
			}
		}
	};
	String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_comments);
		initeView();
		id = getIntent().getStringExtra("id");
		appMain = (AppMain) getApplication();
		findViewById(R.id.back_btn).setOnClickListener(this);
		getData(id, pageNum);
	}

	void initeView() {
		// tvEdit = (TextView) findViewById(R.id.title_edit);
		// tvEdit.setOnClickListener(this);
		// findViewById(R.id.btn_delete).setOnClickListener(this);
		listView = (ListView) findViewById(R.id.lvLive);
		listView.setOnItemClickListener(this);
		list = new ArrayList<CommentInfo>();
		commentsAdapter = new CommentsAdapter(this, list);
		listView.setAdapter(commentsAdapter);
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
						getData(id, pageNum);

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
			// edit();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
	}

	/**
	 * 获取数据
	 */
	void getData(final String id, final int pageNum) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				GetCommentInfo response = ApiUtils.getCommentsByCourseAndPage(
						id, pageNum, handler);
				handler.sendMessage(Message.obtain(handler, INITE_CONTENT,
						response));
				super.run();
			}
		}.start();
	}
}
// extends Activity {
// ListView listView;
// List<CommentInfo> list;
// CommentsAdapter commentsAdapter;
//
// @Override
// protected void onCreate(Bundle savedInstanceState) {
// // TODO Auto-generated method stub
// super.onCreate(savedInstanceState);
// setContentView(R.layout.ac_comments);
// listView = (ListView) findViewById(R.id.lvLive);
// list = new ArrayList<CommentInfo>();
// list.add(new CommentInfo());
// list.add(new CommentInfo());
// list.add(new CommentInfo());
// list.add(new CommentInfo());
// list.add(new CommentInfo());
// commentsAdapter = new CommentsAdapter(this, list);
// listView.setAdapter(commentsAdapter);
// }
//
// }
