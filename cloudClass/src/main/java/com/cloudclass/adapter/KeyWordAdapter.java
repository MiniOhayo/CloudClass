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
import com.cloudclass.entity.GetSearchKeyword.SearchKeywordInfo;

public class KeyWordAdapter extends BaseAdapter {
	private List<SearchKeywordInfo> list = null;
	private Context mContext;

	private boolean isAllLessonType = false;

	public KeyWordAdapter(Context mContext, List<SearchKeywordInfo> list) {
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
	public void updateListView(List<SearchKeywordInfo> list) {
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
		SearchKeywordInfo mContent = list.get(position);
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

		if (mContent.isCheck) {
			viewHolder.tvType.setBackgroundResource(R.drawable.main_theme_btn);
			viewHolder.tvType.setTextColor(mContext.getResources().getColor(
					R.color.front_white));
		} else {
			viewHolder.tvType.setBackgroundResource(R.drawable.main_btn_h);
			viewHolder.tvType.setTextColor(mContext.getResources().getColor(
					R.color.front_input));
		}

		viewHolder.tvType.setText(mContent.keyword);
		return view;

	}

	final static class ViewHolder {
		TextView tvType;

	}

}