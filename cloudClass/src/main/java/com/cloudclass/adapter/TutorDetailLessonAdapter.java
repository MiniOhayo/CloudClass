package com.cloudclass.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudclass.R;
import com.cloudclass.entity.LessonInfo;
import com.cloudclass.utils.NetUtils;
import com.example.provinceselector.SortModel;

public class TutorDetailLessonAdapter extends BaseAdapter {
	List<LessonInfo> list;
	Context con;
	LayoutInflater layoutInflater;
	SortModel teacher;

	public TutorDetailLessonAdapter(List<LessonInfo> list, Context con) {
		this.list = list;
		this.con = con;
		layoutInflater = LayoutInflater.from(con);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public SortModel getTeacher() {
		return teacher;
	}

	public void setTeacher(SortModel teacher) {
		this.teacher = teacher;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHold vh;
		LessonInfo mContent = list.get(position);
		if (convertView == null) {
			vh = new ViewHold();
			convertView = layoutInflater.inflate(R.layout.item_tuotr_lesson,
					null);
			vh.img = (ImageView) convertView.findViewById(R.id.item_img);
			vh.tvInfo = (TextView) convertView
					.findViewById(R.id.show_content_tutor_name);
			vh.tvName = (TextView) convertView.findViewById(R.id.show_name);
			vh.tvTutorName = (TextView) convertView
					.findViewById(R.id.show_content_Title);
			convertView.setTag(vh);
		} else
			vh = (ViewHold) convertView.getTag();
		vh.tvName.setText("" + mContent.name);
		vh.tvTutorName.setText("" + teacher.getName());
		vh.tvInfo.setText(teacher.getInfo() + "");
		NetUtils.loadImage(con,mContent.image, NetUtils.TiNE_IMAGE, vh.img);
		return convertView;
	}

	class ViewHold {
		ImageView img;
		TextView tvName;
		TextView tvInfo;
		TextView tvTutorName;
	}

}
