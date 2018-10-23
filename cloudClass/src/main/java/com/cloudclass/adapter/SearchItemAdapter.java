package com.cloudclass.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cloudclass.R;
import com.cloudclass.entity.LessonTypeInfo;

public class SearchItemAdapter extends BaseAdapter {
	private List<LessonTypeInfo> list = null;
	private Context mContext;

	private boolean isAllLessonType = false;

	public SearchItemAdapter(Context mContext, List<LessonTypeInfo> list) {
		this.mContext = mContext;
		this.list = list;
	}

	public boolean isAllLessonType() {
		return isAllLessonType;
	}

	public void setAllLessonType(boolean isAllLessonType) {
		this.isAllLessonType = isAllLessonType;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<LessonTypeInfo> list) {
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

	@SuppressLint("ResourceAsColor")
	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		LessonTypeInfo mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();

			view = LayoutInflater.from(mContext).inflate(R.layout.item_search,
					null);
			viewHolder.tvType = (TextView) view.findViewById(R.id.btn_item);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		//
		if (isAllLessonType) {
			if (mContent.isCheck) {
				viewHolder.tvType
						.setBackgroundResource(R.drawable.main_theme_btn);
				viewHolder.tvType.setTextColor(mContext.getResources()
						.getColor(R.color.front_white));
			} else {
				viewHolder.tvType.setBackgroundResource(R.drawable.main_btn_h);
				viewHolder.tvType.setTextColor(mContext.getResources()
						.getColor(R.color.front_input));
			}
		} else if (mContent.isCheck) {
			viewHolder.tvType.setBackgroundResource(R.drawable.search_item_h);
		} else
			viewHolder.tvType.setBackgroundResource(R.drawable.search_item_s);

		viewHolder.tvType.setText(mContent.name);
		return view;

	}

	final static class ViewHolder {
		TextView tvType;

	}

}