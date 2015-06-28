
package com.bplow.base.bs.impl;


import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bplow.base.bs.BaseBs;
import com.bplow.base.dao.IBaseHibernateDao;
//import com.bplow.base.dao.IBaseJdbcDao;






@Service("BaseBs")
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class BaseBsImpl implements BaseBs {
	
	@Resource 
	public IBaseHibernateDao baseHibernateDao;
//	@Resource 
//	public IBaseJdbcDao baseJdbcDao;
	
	public <T>Object findById(Serializable id,Class<T> type)
	{
		return baseHibernateDao.findById(id, type);
	}
	
	public void saveObj(Object obj)
	{
		baseHibernateDao.saveObj(obj);
	}


}
