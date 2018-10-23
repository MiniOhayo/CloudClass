package com.cloudclass.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudclass.R;
import com.cloudclass.entity.LessonInfo;
import com.cloudclass.utils.NetUtils;

public class FavoriteAdapter extends BaseAdapter {
	List<LessonInfo> list;
	Context con;
	private boolean isEdit;
	LayoutInflater layoutInflater;

	public FavoriteAdapter(Context con, List<LessonInfo> list) {
		this.list = list;
		this.con = con;
		layoutInflater = LayoutInflater.from(con);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
		notifyDataSetChanged();
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
		LessonInfo lessonInfo = list.get(position);
		if (convertView == null) {
			vh = new ViewHold();
			convertView = layoutInflater.inflate(
					R.layout.item_main_important_free, null);
			vh.img = (ImageView) convertView.findViewById(R.id.item_img);
			vh.tvInfo = (TextView) convertView.findViewById(R.id.show_content);
			vh.tvType = (TextView) convertView.findViewById(R.id.show_type);
			vh.tvName = (TextView) convertView.findViewById(R.id.show_name);
			vh.imgCheck = (ImageView) convertView.findViewById(R.id.img_check);
			convertView.setTag(vh);
		} else
			vh = (ViewHold) convertView.getTag();
		vh.tvName.setText(lessonInfo.name);
		vh.tvInfo.setText(lessonInfo.sintro);
		if (isEdit) {

			// viewHolder.imgCheck.setOnClickListener(new ImageCheckListener(
			// position));
			if (lessonInfo.isChecked) {
				vh.imgCheck.setVisibility(View.VISIBLE);
			} else
				vh.imgCheck.setVisibility(View.INVISIBLE);
		} else
			vh.imgCheck.setVisibility(View.GONE);
		if (lessonInfo.teacher != null
				&& !TextUtils.isEmpty(lessonInfo.teacher.name)) {
			vh.tvType.setText(lessonInfo.teacher.name);
		}
		if (!TextUtils.isEmpty(lessonInfo.image)) {
			NetUtils.loadImage(con,lessonInfo.image, NetUtils.MIDDLE_IMAGE, vh.img);
		}

		return convertView;
	}

	class ViewHold {
		ImageView img;
		TextView tvName;
		TextView tvInfo;
		TextView tvType;
		ImageView imgCheck;
	}

}
