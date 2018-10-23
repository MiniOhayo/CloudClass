package com.cloudclass;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudclass.adapter.TutorDetailLessonAdapter;
import com.cloudclass.entity.GetTeacherDetailInfo;
import com.cloudclass.entity.LessonInfo;
import com.cloudclass.utils.ApiUtils;
import com.cloudclass.utils.NetUtils;
import com.cloudclass.utils.ViewUtils;
import com.example.provinceselector.SortModel;

public class AcTutorDetail extends Activity implements OnClickListener,
		OnItemClickListener {
	ListView listView;
	List<LessonInfo> list;
	TutorDetailLessonAdapter tutorDetailLessonAdapter;
	protected static final int INITE_CONTENT = 6;
	TextView tvDetail, tvHide, tvShow;
	SortModel sortModel;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case INITE_CONTENT:
				GetTeacherDetailInfo response = (GetTeacherDetailInfo) msg.obj;
				if (response != null) {
					if (response.courses != null) {
						list.addAll(response.courses);
						tutorDetailLessonAdapter.notifyDataSetChanged();
						tvDetail.setText("   " + response.detail);
						tvlessonNum.setText("课程数:" + response.coursestotal);
						ViewUtils.setListViewHeightBasedOnChildren(listView);
					}
				}
				break;
			}
		}
	};
	private TextView tvsShowTutor;
	private TextView tvTutorJob;
	private ImageView imgFace;
	private TextView tvlessonNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_tutor_detail);
		tvlessonNum = (TextView) findViewById(R.id.lessonNum);
		tvsShowTutor = (TextView) findViewById(R.id.show_tutor_title);
		tvTutorJob = (TextView) findViewById(R.id.show_tutor_job);
		sortModel = (SortModel) getIntent().getSerializableExtra("info");
		tvsShowTutor.setText(sortModel.getName());
		tvTutorJob.setText(sortModel.getInfo());
		tvDetail = (TextView) findViewById(R.id.detail_info);
		tvHide = (TextView) findViewById(R.id.detail_hide);
		tvShow = (TextView) findViewById(R.id.detail_info_show);
		listView = (ListView) findViewById(R.id.lvLive);
		list = new ArrayList<LessonInfo>();
		tutorDetailLessonAdapter = new TutorDetailLessonAdapter(list, this);
		tutorDetailLessonAdapter.setTeacher(sortModel);
		listView.setAdapter(tutorDetailLessonAdapter);
		listView.setOnItemClickListener(this);
		findViewById(R.id.back_btn).setOnClickListener(this);
		tvHide.setOnClickListener(this);
		tvShow.setOnClickListener(this);

		getData(sortModel.getId(), 1);
		imgFace = (ImageView) findViewById(R.id.img_face);
		imgFace.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				NetUtils.loadImage(sortModel.getImage(), NetUtils.TiNE_IMAGE,
						imgFace);
			}
		}, 50);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_btn:
			finish();
			break;
		case R.id.detail_hide:
			tvHide.setVisibility(View.GONE);
			tvDetail.setVisibility(View.GONE);
			tvShow.setVisibility(View.VISIBLE);
			break;
		case R.id.detail_info_show:
			tvShow.setVisibility(View.GONE);
			tvHide.setVisibility(View.VISIBLE);
			tvDetail.setVisibility(View.VISIBLE);
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
				GetTeacherDetailInfo response = ApiUtils.getDetailByTeacher(id,
						pageNum, handler);
				handler.sendMessage(Message.obtain(handler, INITE_CONTENT,
						response));
				super.run();
			}
		}.start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, AcLessonDetail.class).putExtra("info",
				list.get(position)));
	}

}
