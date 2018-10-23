package com.cloudclass.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudclass.AcLessonDetail;
import com.cloudclass.AppMain;
import com.cloudclass.R;
import com.cloudclass.entity.DayType;
import com.cloudclass.entity.GetCoursesInfo;
import com.cloudclass.entity.LessonInfo;
import com.cloudclass.utils.ApiUtils;
import com.cloudclass.utils.NetUtils;
import com.cloudclass.utils.Utils;

public class FgMainImportantNew extends Fragment implements OnClickListener,
		OnItemClickListener {

	protected static final int INITE_CONTENT = 6;
	public static final int SAVE_OK = 2;
	public static final int SAVE_ERROE = 3;
	AppMain appMain;
	TextView titleName;
	boolean isModify;

	private FragmentActivity con;
	private ProgressDialog progressDialog;
	private View view;
	private View footer;// listview的footer
	private TextView farther;// footer的文本
	private ProgressBar progressBar;// footer的progressBar
	private GroupListAdapter adapter = null;
	private ListView listView = null;
	private List<LessonInfo> list = new ArrayList<LessonInfo>();
	private List<String> listTag = new ArrayList<String>();
	DayType dayType;
	private int pageNum = 1;
	private int pageSize;
	private int pagecount;
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
					for (int i = 0; i < response.data.size(); i++) {
						LessonInfo lessonInfo = response.data.get(i);
						String time = lessonInfo.updatedate;

						DayType tempDayType = Utils.getDayType(time);
						if (tempDayType != dayType) {
							if (tempDayType == DayType.today) {
								list.add(new LessonInfo("今日更新"));
								listTag.add("今日更新");
								dayType = tempDayType;
							} else if (tempDayType == DayType.week) {
								list.add(new LessonInfo("本周更新"));
								listTag.add("本周更新");
								dayType = tempDayType;
							} else if (tempDayType == DayType.month) {
								list.add(new LessonInfo("本月更新"));
								listTag.add("本月更新");
								dayType = tempDayType;
							} else if (tempDayType == DayType.moreLong) {
								list.add(new LessonInfo("更久"));
								listTag.add("更久");
								dayType = tempDayType;
							}
						}
						list.add(lessonInfo);

					}
					adapter.notifyDataSetChanged();
				}

				break;

			}
			// progressDialog.dismiss();
		}
	};

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
		view = inflater.inflate(R.layout.fg_main_important_new, container,
				false);
		// setData();
		adapter = new GroupListAdapter(con, list, listTag);
		listView = (ListView) view.findViewById(R.id.group_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		setListFooter(inflater);

	}

	void setListFooter(LayoutInflater inflater) {
		footer = inflater.inflate(R.layout.listview_footer, null);
		progressBar = (ProgressBar) footer.findViewById(R.id.main_pb);
		farther = (TextView) footer.findViewById(R.id.TextView_farther);
		farther.setText("更多");
		listView.addFooterView(footer);
		TextView TextView = new TextView(con);
		TextView.setText("list  头结点");
		// listView.addHeaderView(TextView);
		getData(pageNum);
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

	// public void setData() {
	// list.add("今日更新");
	// listTag.add("今日更新");
	// for (int i = 0; i < 2; i++) {
	// list.add("阿凡达" + i);
	// }
	// list.add("本周更新");
	// listTag.add("本周更新");
	// for (int i = 0; i < 3; i++) {
	// list.add("比特风暴" + i);
	// }
	// list.add("更早");
	// listTag.add("更早");
	// for (int i = 0; i < 30; i++) {
	// list.add("查理风云" + i);
	// }
	// }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.menu_btn:

			break;

		}
	}

	private static class GroupListAdapter extends BaseAdapter {

		private List<String> listTag = null;
		private List<LessonInfo> list;
		Context context;

		public GroupListAdapter(Context context, List<LessonInfo> objects,
				List<String> tags) {
			this.context = context;
			this.listTag = tags;
			this.list = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			LessonInfo lessonInfo = list.get(position);
			if (listTag.contains(getItem(position))) {
				view = LayoutInflater.from(context).inflate(
						R.layout.group_list_item_tag, null);
				TextView textView = (TextView) view
						.findViewById(R.id.group_list_item_text);
				textView.setText(getItem(position));
			} else {
				view = LayoutInflater.from(context).inflate(
						R.layout.group_list_item, null);
				TextView textView = (TextView) view
						.findViewById(R.id.group_list_item_text);
				textView.setText(list.get(position).name);
				TextView textContent = (TextView) view
						.findViewById(R.id.show_content);
				textContent.setText(list.get(position).teacher.name + "   "
						+ list.get(position).teacher.job);
				ImageView img = (ImageView) view.findViewById(R.id.item_img);
				if (!TextUtils.isEmpty(lessonInfo.image)) {
					NetUtils.loadImage(context,lessonInfo.image, NetUtils.MIDDLE_IMAGE,
							img);
				}
			}

			return view;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public String getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position).timeTag;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		LessonInfo lessonInfo = list.get(position);
		if (lessonInfo.id != 0) {
			startActivity(new Intent(con, AcLessonDetail.class).putExtra(
					"info", lessonInfo));
		}

	}

	/**
	 * 获取数据
	 */
	void getData(final int page) {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				GetCoursesInfo response = ApiUtils.getCoursesByCondition(page,
						hander);

				hander.sendMessage(Message.obtain(hander, INITE_CONTENT,
						response));

				super.run();
			}
		}.start();
	}
}
