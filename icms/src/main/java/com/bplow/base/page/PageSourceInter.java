package com.bplow.base.page;

import java.util.List;

/**
 * 
 * <p>
 * Title:分页的接口
 * </p>
 */
public interface PageSourceInter {
	/**
	 * @return String 返回具体得URL
	 */
	public String getUrl();

	/**
	 * @return int 返回当前的页数
	 */
	public int getCurPage();

	/**
	 * getPageSize：返回分页大小
	 */
	public int getPageSize();

	/**
	 * getRowsCount：返回总记录行数
	 */
	public int getTotalRows();

	/**
	 * getPageCount：返回总页数
	 */
	public int getTotalPage();

	/**
	 * 第一页
	 * 
	 * @return int
	 */
	public int first();

	/**
	 * 最后一页
	 * 
	 * @return int
	 */
	public int last();

	/**
	 * 上一页
	 * 
	 * @return int
	 */
	public int previous();

	/**
	 * 下一页
	 * 
	 * @return int
	 */
	public int next();

	/**
	 * 第一页
	 * 
	 * @return boolean
	 */
	public boolean isFirst();

	/**
	 * 最后一页
	 * 
	 * 
	 * @return boolean
	 */
	public boolean isLast();

	/**
	 * 获取当前页数据
	 * 
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public List getData();

	public int getYouto();

	public void setCurPage(int curPage);

	@SuppressWarnings("rawtypes")
	public void setData(List data);

	public void setPageSize(int pageSize);

}
