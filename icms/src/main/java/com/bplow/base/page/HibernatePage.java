package com.bplow.base.page;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;


public class HibernatePage {
    String from;
    /**
     * 查询条件
     */
    String hql;
    /**
     * 操作对象
     */
    Object table = null;

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
     * 初始化分页模块
     * @param request
     * @param query   总条数
     */
    public void getPage(HttpServletRequest request, int query) {
        if (request.getParameter("totalRows") == null) {
            getRows(query);//获取总页数

        } else {
            totalRows = Integer.parseInt(request.getParameter("totalRows"));
        }
        this.curPage = request.getParameter("page") == null ? 1 : new Integer(
                request.getParameter("page")).intValue();
        this.pageSize = request.getParameter("everySize") == null ? 10
                : new Integer(request.getParameter("everySize")).intValue();
        this.totalPage = (int) Math.ceil((double) totalRows / pageSize);
       
        if (totalPage < 1){
            totalPage = 1;
        }
        
        if (curPage > totalPage){
            youto = curPage;
            curPage = totalPage;
        }
        if (curPage < 1)
            curPage = 1;
        this.url = request.getRequestURL().toString() + request.getParameter("actionMethod");
        this.from =request.getParameter("formName");
        // this.table = table;
    }
    
    
 
    
    /**
     * 初始化总页数

     * @param query
     * @throws HibernateException
     */
    public void getRows(int query) {
        this.totalRows = query;
    }

    /**
     * 计算开始页数

     * @param pageSize
     * @param curPage
     * @param totalRows
     * @return
     */
    public int getStart() {
        int start = (curPage - 1) * pageSize;     
        return start;
    }
    /**
     * 
     * 功能：计算结束页数

     * 
     * @return
     * @author egt-21
     */
    public int getEnd() {
        //Collection vehicleList = null;
        int start = (curPage - 1) * pageSize;
        int end = 0;
        if (start + pageSize > totalRows)
            end = totalRows;
        else
            end = pageSize;
        //vehicleList = dao.getPageData(start,end,hql);
        return end;
    }
    /**
     * 
     * 功能：计算结束页数

     * 
     * @return
     * @author egt-21
     */
    public int getEnd1() {
        //Collection vehicleList = null;
        int start = (curPage - 1) * pageSize;
        int end = 0;
        if (start + pageSize > totalRows)
            end = totalRows;
        else
            end = start+pageSize;
        //vehicleList = dao.getPageData(start,end,hql);
        return end;
    }
    /**
     * 用来构成页面要显示的HTML
     * 
     * @return 返回将要发送到页面标签的对象

     */
    public PageHtml getPageInfo() {
        PageHtml pageBar = new PageHtml(this);
        return pageBar;
    }

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

    public Object getTable() {
        return table;
    }

    public void setTable(Object table) {
        this.table = table;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getYouto() {
        return youto;
    }

    public void setYouto(int youto) {
        this.youto = youto;
    }
    
    public String getFrom() {
    
        return from;
    }
    
    public void setFrom(String from) {
    
        this.from = from;
    }
    
    
    /* Flex 分页 */
    /**
     * 初始化分页模块
     * @param request
     * @param query   总条数
     */
    public void getPage(FlexPagination pageVo, int query) {
        if (pageVo.getTotalRows() == 0) {
            getRows(query);//获取总页数

        } else {
            totalRows = pageVo.getTotalRows();
        }
        this.curPage = pageVo.getCurPage() == 0 ? 1 : pageVo.getCurPage();
        this.pageSize = pageVo.getPageSize() == 0 ? 10 : pageVo.getPageSize() ;
        this.totalPage = (int) Math.ceil((double) totalRows / pageSize);
       
        if (totalPage < 1){
            totalPage = 1;
        }
        
        if (curPage > totalPage){
            youto = curPage;
            curPage = totalPage;
        }
        if (curPage < 1)
            curPage = 1;
       // this.url = request.getRequestURL().toString() + request.getParameter("actionMethod");
       //this.from =request.getParameter("formName");
       // this.table = table;
    }

}
