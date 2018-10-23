package com.cloudclass.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.cloudclass.R;
import com.cloudclass.entity.LessonInfo;

public class HistoryAdapter extends BaseAdapter implements SectionIndexer {
	private List<LessonInfo> list = null;
	private Context mContext;

	public HistoryAdapter(Context mContext, List<LessonInfo> list) {
		this.mContext = mContext;
		this.list = list;
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
		final LessonInfo mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_history,
					null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.tvInfo = (TextView) view.findViewById(R.id.info);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		// 根据position获取分类的首字母的Char ascii值

		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现

		String section = getTimeForPosition(position);
		if (position == getPositionForTimes(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText("" + mContent.timeTag);

		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}

		 viewHolder.tvInfo.setText(mContent.name+"观看到"+mContent.learnprogress);
		 viewHolder.tvTitle.setText(""+mContent.name);

		return view;

	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		TextView tvInfo;
	}

	@Override
	public int getPositionForSection(int section) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getTimeForPosition(int position) {
		// TODO Auto-generated method stub
		return list.get(position).timeTag;
	}

	public int getPositionForTimes(String section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).timeTag;
			if (sortStr.equals(section)) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}
}