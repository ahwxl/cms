package com.bplow.base.page;

import java.io.Serializable;
import java.util.List;

//实现Serializable接口以便序列化 便于缓存到硬盘
public class PageVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<?> dataList;
	public List<?> getDataList() {
		return dataList;
	}
	public void setDataList(List<?> dataList) {
		this.dataList = dataList;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	private int totalCount;
	private int start;
	private int limit;

}
