package com.cloudclass.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudclass.R;
import com.cloudclass.entity.LessonInfo;

public class DownLoadAdapter extends BaseAdapter {
	private List<LessonInfo> list = null;
	private Context mContext;
	private boolean isEdit;
	String pause = "暂停";
	String down = "下载";
	String play = "播放";

	public DownLoadAdapter(Context mContext, List<LessonInfo> list) {
		this.mContext = mContext;
		this.list = list;
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
		notifyDataSetChanged();
	}

	public void setItemChecked(int position) {

	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<LessonInfo> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		LessonInfo mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.item_download, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_name);
			viewHolder.imgCheck = (ImageView) view.findViewById(R.id.img_check);
			viewHolder.pbTime = (ProgressBar) view.findViewById(R.id.pb_cache);
			viewHolder.tv_tutor = (TextView) view.findViewById(R.id.tv_tutor);
			viewHolder.tvState = (TextView) view.findViewById(R.id.tv_state);
			viewHolder.tvSize = (TextView) view.findViewById(R.id.tv_size);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		if (isEdit) {

			// viewHolder.imgCheck.setOnClickListener(new ImageCheckListener(
			// position));
			if (mContent.isChecked) {
				viewHolder.imgCheck.setVisibility(View.VISIBLE);
			} else
				viewHolder.imgCheck.setVisibility(View.INVISIBLE);
		} else
			viewHolder.imgCheck.setVisibility(View.GONE);
		viewHolder.pbTime.setProgress(mContent.downLodProgress);
		viewHolder.tv_tutor.setText("" + mContent.teacher.name);
		viewHolder.tvTitle.setText("" + mContent.name);
		viewHolder.tvState.setText(mContent.downLoadState + "");
		viewHolder.tvSize.setText(mContent.downLodProgress + "%");

		return view;

	}

	final static class ViewHolder {
		ProgressBar pbTime;
		TextView tvTitle;
		TextView tv_tutor;
		TextView tvSize;
		TextView tvState;
		ImageView imgCheck;
	}

	class ImageCheckListener implements android.view.View.OnClickListener {
		int position;

		public ImageCheckListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}

	}
}
