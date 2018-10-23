package com.cloudclass.entity;

import java.util.ArrayList;
import java.util.List;

public class GetSearchKeyword {

	public List<SearchKeywordInfo> data = new ArrayList<SearchKeywordInfo>();

	public GetSearchKeyword() {
		super();
	}

	public class SearchKeywordInfo {
		public String keyword;
		public int times;
		public boolean isCheck;

		public SearchKeywordInfo() {

		}

	}
}
