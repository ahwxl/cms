package com.bplow.base.page;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;



/**
 * 分页--初始化参数
 * 
 * 
 */
public class PageSource implements PageSourceInter {
	/**
	 * 分页数据
	 */
	
	@SuppressWarnings("rawtypes")
	List data = null;

	/**
	 * 当前页

	 */
	int curPage;

	/**
	 * 每页显示的记录数
	 */
	int pageSize;

	/**
	 * 记录行数
	 */
	int totalRows;

	/**
	 * 页数
	 */
	int totalPage;

	/**
	 * 针对不同模块所使用得参数

	 */
	String url;

	/**
	 * 判断将要显示得页是否有数据
	 */
	int youto = 0;

	/**
	 * @param data
	 */
	@SuppressWarnings("rawtypes")
	public PageSource(List data) {
		this.data = data;
		this.curPage = 1;
		this.totalRows = data.size();
		this.totalPage = (int) Math.ceil((double) totalRows / pageSize);
	}

	/**
	 * @param data
	 * @param curPage
	 */
	@SuppressWarnings("rawtypes")
	public PageSource(List data, int curPage) {
		this.data = data;
		this.curPage = curPage;
		this.totalRows = data.size();
		this.totalPage = (int) Math.ceil((double) totalRows / pageSize);
	}

	/**
	 * @param data
	 * @param curPage
	 * @param pageSize
	 */
	@SuppressWarnings("rawtypes")
	public PageSource(List data, int curPage, int pageSize) {
		this.data = data;
		this.curPage = curPage;
		this.pageSize = pageSize;
		this.totalRows = data.size();
		this.totalPage = (int) Math.ceil((double) totalRows / pageSize);
	}

	/**
	 * 初始化分页使用到的参数
	 * 
	 * @param HttpServletRequest
	 *            request
	 * @param List
	 *            data 将此替换为你查询到得数据集合
	 * @param String
	 *            doWhat 此servlet可能有很多动作，比如:/servlet?do=action1,
	 *            /servlet?do=action2。替换为你想要的，格式为： ?do=action
	 */
	@SuppressWarnings("rawtypes")
	public PageSource(HttpServletRequest request, List data, String doWhat) {
		this.url = request.getRequestURL().toString() + doWhat;
		if (data != null) {
			this.data = data;
			this.curPage = request.getParameter("page") == null ? 1
					: new Integer(request.getParameter("page")).intValue();
			this.pageSize = request.getParameter("everySize") == null ? 10
					: new Integer(request.getParameter("everySize")).intValue();
			this.totalRows = data.size();
			this.totalPage = (int) Math.ceil((double) totalRows / pageSize);
			if (curPage > totalPage) {
				youto = curPage;
				curPage = totalPage;
			}
			if (curPage < 1)
				curPage = 1;
		}
	}


	/**
	 * @param request
	 * @param data
	 * @param pageSize
	 */
	@SuppressWarnings("rawtypes")
	public PageSource(HttpServletRequest request, List data, int pageSize) {
		if (data != null) {
			this.data = data;
			this.curPage = request.getParameter("page") == null ? 1
					: new Integer(request.getParameter("page")).intValue();
			this.pageSize = pageSize;
			this.totalRows = data.size();
			this.totalPage = (int) Math.ceil((double) totalRows / pageSize);
		}
	}

	/**
	 * getCurPage:
	 * 
	 * @return int 返回当前的页数
	 */
	public int getCurPage() {
		return curPage;
	}

	/**
	 * getPageSize：返回分页大小
	 * 
	 * @return int
	 */

	public int getPageSize() {
		return pageSize;
	}

	/**
	 * gettotalRows：返回总记录行数
	 * 
	 * @return int
	 */
	public int getTotalRows() {
		return totalRows;
	}

	/**
	 * gettotalPage：返回总页数
	 * 
	 * @return int
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * 第一页
	 * 
	 * @return int
	 */
	public int first() {
		return 1;
	}

	/**
	 * 最后一页
	 * 
	 * @return int
	 */
	public int last() {
		return totalPage;
	}

	/**
	 * 上一页
	 * 
	 * @return int
	 */
	public int previous() {
		return (curPage - 1 < 1) ? 1 : curPage - 1;
	}

	/**
	 * 下一页
	 *
	 * @return int
	 */
	public int next() {
		return (curPage + 1 > totalPage) ? totalPage : curPage + 1;
	}

	/**
	 * 第一页
	 * 
	 * @return boolean
	 */

	public boolean isFirst() {
		return (curPage == 1) ? true : false;
	}

	/**
	 * 最后一页
	 * 
	 * @return boolean
	 */
	public boolean isLast() {
		return (curPage == totalPage) ? true : false;

	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getData() {
		List curData = null;
		if (data != null) {
			curData = new LinkedList();
			int start = (curPage - 1) * pageSize;
			int end = 0;
			if (start + pageSize > totalRows)
				end = totalRows;
			else
				end = start + pageSize;
			for (int i = start; i < end; i++) {
				curData.add(this.data.get(i));
			}
		}
		return curData;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	@SuppressWarnings("rawtypes")
	public void setData(List data) {
		this.data = data;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		this.totalPage = (int) Math.ceil((double) totalRows / pageSize);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getYouto() {
		return youto;
	}

	public void setYouto(int youto) {
		this.youto = youto;
	}

}
