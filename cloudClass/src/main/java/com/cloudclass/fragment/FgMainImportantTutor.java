package com.cloudclass.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudclass.AcTutorDetail;
import com.cloudclass.AppMain;
import com.cloudclass.R;
import com.cloudclass.entity.GetTeachersInfo;
import com.cloudclass.entity.TutorInfo;
import com.cloudclass.utils.ApiUtils;
import com.example.provinceselector.CharacterParser;
import com.example.provinceselector.PinyinComparator;
import com.example.provinceselector.SideBar;
import com.example.provinceselector.SideBar.OnTouchingLetterChangedListener;
import com.example.provinceselector.SortAdapter;
import com.example.provinceselector.SortModel;

/**
 * 主页面 导师列表
 * 
 * @author Administrator
 * 
 */
public class FgMainImportantTutor extends Fragment implements OnClickListener {
	protected static final int INITE_CONTENT = 6;
	public static final int SAVE_OK = 2;
	public static final int SAVE_ERROE = 3;
	AppMain appMain;
	TextView titleName;

	private FragmentActivity con;
	private ProgressDialog progressDialog;
	private View view;

	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	// private ClearEditText mClearEditText;

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> sourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	public Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case INITE_CONTENT:

				GetTeachersInfo response = (GetTeachersInfo) msg.obj;
				if (response != null && response.data != null) {
					sourceDateList = new ArrayList<SortModel>(
							response.data.size());
					for (int i = 0; i < response.data.size(); i++) {

						TutorInfo tutorInfo = response.data.get(i);
						sourceDateList.add(new SortModel(tutorInfo.id,
								tutorInfo.name, tutorInfo.firstletter,
								tutorInfo.job, tutorInfo.headimage));
						// 根据a-z进行排序源数据
						Collections.sort(sourceDateList, pinyinComparator);
						adapter = new SortAdapter(con, sourceDateList);
						sortListView.setAdapter(adapter);

					}
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
		view = inflater.inflate(R.layout.fg_main_important_tutor, container,
				false);

		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) view.findViewById(R.id.sidrbar);
		dialog = (TextView) view.findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});
		sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				// Toast.makeText(con,
				// ((SortModel) adapter.getItem(position)).getName(),
				// Toast.LENGTH_SHORT).show();
				startActivity(new Intent(con, AcTutorDetail.class).putExtra(
						"info", sourceDateList.get(position)));
			}
		});
		getData();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.menu_btn:

			break;

		}
	}

	/**
	 * 获取数据
	 */
	void getData() {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				GetTeachersInfo response = ApiUtils
						.getAllTeachersByPage(hander);

				hander.sendMessage(Message.obtain(hander, INITE_CONTENT,
						response));

				super.run();
			}
		}.start();
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String[] date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.length; i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}
}
