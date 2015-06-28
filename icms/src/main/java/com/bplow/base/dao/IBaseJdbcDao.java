
package com.bplow.base.dao;

import java.util.List;

/**
 * 公共JDBC操作接口
 * @author 韩冬
 * 2011-12-2 上午11:37:34
 */
public interface IBaseJdbcDao {

	/**

	 * 执行Sql获取返回行数
	 * 
	 * @param sql
	 * @return
	 */
	public int queryNum(String sql);

	/**
	 * 执行Sql获取返回行数
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public int queryNum(String sql, Object ... params);

	/**
	 * 执行Sql查询语句
	 * 
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List query(String sql);

	/**
	 * 执行Sql查询语句返回obj对象集合
	 * 
	 * @param sql
	 * @param obj
	 * @return

	 */
	public List<?> query(String sql,Class<?> type);

	/**
	 * 执行Sql查询语句返回obj对象集合
	 * 
	 * @param sql
	 * @param obj
	 * @param params
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List query(String sql, Class type, Object ... params);

	/**
	 * 执行Sql查询语句返回集合
	 * 
	 * @param sql
	 * @param obj
	 * @param params
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List query(String sql, Object ... params);

	/**
	 * 执行Sql
	 * 
	 * @param sql
	 */
	public void execSql(String sql);

	/**
	 * 执行Sql
	 * 
	 * @param sql
	 * @param params
	 */
	public void execSql(String sql, Object ... params);
	


	/**
	 * 执行存储过程
	 * 
	 * @param spName
	 * @param retunType
	 * @param params
	 * @return
	 */
	public String execProc(final String spName,final List<?> params,final List<?> returnType);
	
	
	/**
	 * 执行Sql查询语句返回Map对象集合
	 * 
	 * @param sql
	 * @param obj
	 * @return
	 */
	public List<?> queryMap(String sql);

	/**
	 * 执行Sql查询语句返回Map对象集合
	 * 
	 * @param sql
	 * @param obj
	 * @param params
	 * @return
	 */
	public List<?> queryMap(String sql, Object ... params);
	
	
	/**
	 * 功能描述: 获取分页Sql语句
	 * 时          间: 2012-2-6
	 * @author  韩冬 
	 * @param sql
	 * @param startPage
	 * @param endPage
	 * @return
	 */
	public String getPageSql(String sql,int startPage, int endPage);


	
}
