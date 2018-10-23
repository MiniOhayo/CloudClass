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
import com.cloudclass.entity.CommentInfo;
import com.cloudclass.utils.NetUtils;

public class CommentsAdapter extends BaseAdapter {
	Context context;
	LayoutInflater layoutInflater;
	List<CommentInfo> comments;

	public CommentsAdapter(Context context, List<CommentInfo> comments) {
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		this.comments = comments;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return comments.size();
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
		CommentInfo commentInfo = comments.get(position);
		if (convertView == null) {
			vh = new ViewHold();
			convertView = layoutInflater.inflate(R.layout.item_commens, null);
			vh.img = (ImageView) convertView.findViewById(R.id.user_img);
			vh.name = (TextView) convertView.findViewById(R.id.user_name);
			vh.time = (TextView) convertView.findViewById(R.id.user_time);
			vh.comment = (TextView) convertView
					.findViewById(R.id.user_comments);
			convertView.setTag(vh);
		} else
			vh = (ViewHold) convertView.getTag();

		// vh.img = (ImageView) convertView.findViewById(R.id.user_img);
		if (!TextUtils.isEmpty(commentInfo.getName())) {
			vh.name.setText("" + commentInfo.getName());
		} else {
			vh.name.setText("");
		}

		vh.time.setText("" + commentInfo.getTimes());
		vh.comment.setText("" + commentInfo.getContent());
		NetUtils.loadImage(commentInfo.getHeadimage(), NetUtils.TiNE_IMAGE,
				vh.img);
		return convertView;
	}

	class ViewHold {
		ImageView img;
		TextView name;
		TextView comment;
		TextView time;
	}
}
