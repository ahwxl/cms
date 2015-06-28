package com.bplow.base.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.bplow.base.dao.IBaseHibernateDao;





@Repository("BaseHibernateDao")
public class BaseHibernateDaoImpl extends HibernateTemplate   implements IBaseHibernateDao  {

	
	 @Resource(name = "sessionFactory")
	    public void setSuperSessionFactory(SessionFactory sessionFactory) {
	        super.setSessionFactory(sessionFactory);
	        setCacheQueries(true); //开启二级缓存
	    }

	@SuppressWarnings("rawtypes")
	public int getRowNum(String hql)
	{
		int formIndex = hql.toLowerCase().indexOf("from");
		StringBuffer hqlPack = new StringBuffer();
		hqlPack.append("select max(rownum)   ");
		hqlPack.append(hql.substring(formIndex));
		hqlPack.append(" ");
		List l = find(hqlPack.toString());
		return Integer.parseInt(l.get(0)==null?"0":l.get(0).toString());
	}
	


	@SuppressWarnings("rawtypes")
	public int getRowNum(String hql, Object...params)
	{
		int formIndex = hql.toLowerCase().indexOf("from");
		StringBuffer hqlPack = new StringBuffer();
		hqlPack.append("select count(*)   ");
		hqlPack.append(hql.substring(formIndex));
		hqlPack.append(" ");
		List l = find(hqlPack.toString(),params);
		return Integer.parseInt(l.get(0)==null?"0":l.get(0).toString());
	}
	
	


	@SuppressWarnings("rawtypes")
	public List query(String hql)
	{    super.setCacheQueries(true);
		 return find(hql);
	}
	


	@SuppressWarnings("rawtypes")
	public List query(String hql, Object ... params)
	{
		 super.setCacheQueries(true);
		 return find(hql,params);
	}
	
	

    public <T>Object findById(Serializable id,Class<T> type)
    {
    	return get(type, id);
    }
    

    public void saveObj(Object obj)
    {
    	save(obj);
    }
    

 
	@SuppressWarnings("rawtypes")
	public void saveObj(List listObj)
    {
    	for(Object obj:listObj.toArray())
    	{
    		save(obj);
    	}
    }
    
    
    public void delObj(Object obj)
    {
    	delete(obj);
    }
    


	@SuppressWarnings("rawtypes")
	public void delObj(List listObj)
    {
    	for(Object obj:listObj.toArray())
    	{
    		delete(obj);
    	}
    }
    

    public void updateObj(Object obj)
    {
    	update(obj);
    }
    

 
	@SuppressWarnings("rawtypes")
	public void updateObj(List listObj)
    {
    	for(Object obj:listObj.toArray())
    	{
    		update(obj);
    	}
    }
    
    

	
	@SuppressWarnings("rawtypes")
	public List query(final String hql,final int startPage,final int maxResult)
	{
		return executeFind(new HibernateCallback() {
		     public Object doInHibernate(Session session)
		     throws HibernateException, SQLException {
		     Query query = session.createQuery(hql);
		     query.setFirstResult(startPage);
		     query.setMaxResults(maxResult);
		     return query.list();
		     }
		});
	}
	

	@SuppressWarnings("rawtypes")
	public List query(final String hql,final int startPage,final int maxResult, final Object...params)
	{
		return executeFind(new HibernateCallback() {
		     public Object doInHibernate(Session session)
		     throws HibernateException, SQLException {
		     Query query = session.createQuery(hql);	 
		     for(int i = 0; i < params.length; i++)
		     {
		    	 query.setParameter(i, params[i]);
		     }	    
		     query.setFirstResult(startPage);
		     query.setMaxResults(maxResult);
		     return query.list();
		     }
		});
	}

	

}
