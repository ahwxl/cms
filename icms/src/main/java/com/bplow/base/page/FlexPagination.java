package com.bplow.base.page;

import java.util.List;

/*
 * flex分页对象
 */
public class FlexPagination {

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	@SuppressWarnings("rawtypes")
	public List getDataList() {
		return dataList;
	}

	@SuppressWarnings("rawtypes")
	public void setDataList(List dataList) {
		this.dataList = dataList;
	}

	private int curPage = 0; // 当前页
	private int pageSize = 0; // 每页显示条数
	private int totalRows = 0; // 总的数据量
	private int totalPage = 0; // 总页数

	@SuppressWarnings("rawtypes")
	private List dataList; // 数据结果集

}
