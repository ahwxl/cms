package com.bplow.base.bs;


import java.io.Serializable;

/**
 * 公共业务层接口(继承用)
 * @author 韩冬
 * 2011-12-2 上午11:33:47
 */
public interface BaseBs 
{
	/**
	 * Hibernate 根据id查找类
	 * @param <T>
	 * @param id
	 * @param type
	 * @return
	 */
	public <T>Object findById(Serializable id,Class<T> type);
	
	/**
	 * Hibernate保存对象
	 * @param obj
	 * @return
	 */
	public void saveObj(Object obj);
}
