package com.bplow.base.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 公共Hibernate操作接口
 * @author 韩冬
 * 2011-12-2 上午11:36:46
 */
public interface IBaseHibernateDao {

	/**
	 * 获取hql语句条数
	 * 
	 * @param hql
	 * @return
	 */
	public int getRowNum(String hql);

	/**
	 * 获取hql语句条数
	 * 
	 * @param hql
	 * @param params
	 * @return
	 */
	public int getRowNum(String hql, Object... params);
	


	/**
	 * 查询hql
	 * 
	 * @param hql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List query(String hql);

	/**
	 * 查询hql
	 * 
	 * @param hql
	 * @param params
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List query(String hql, Object ... params);

	/**
	 * 分页查询hql
	 * 
	 * @param hql
	 * @param startPage
	 * @param endPage
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List query(String hql, int startPage, int endPage);

	/**
	 * 分页查询hql
	 * 
	 * @param hql
	 * @param startPage
	 * @param endPage
	 * @param params
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List query(String hql, int startPage, int endPage, Object... params);

	/**
	 * 根据ID查找指定对象
	 * 
	 * @param id
	 * @param obj
	 * @return
	 */
	public <T>Object findById(Serializable id, Class<T> type);

	/**
	 * 保存对象到数据库
	 * 
	 * @param obj
	 */
	public void saveObj(Object obj);

	/**
	 * 保存一组对象
	 * 
	 * @param listObj
	 */
	@SuppressWarnings("rawtypes")
	public void saveObj(List listObj);

	/**
	 * 删除一个对象
	 * 
	 * @param obj
	 */
	public void delObj(Object obj);

	/**
	 * 删除一组对象
	 * 
	 * @param listObj
	 */
	@SuppressWarnings("rawtypes")
	public void delObj(List listObj);

	/**
	 * 更新对象
	 * 
	 * @param obj
	 */
	public void updateObj(Object obj);

	/**
	 * 更新一组对象
	 * 
	 * @param listObj
	 */
	@SuppressWarnings("rawtypes")
	public void updateObj(List listObj);
	
	
	/**
	 * 保存或更新
	 * @param obj
	 */
	public void saveOrUpdate(Object obj);

}
