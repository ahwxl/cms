/*
 * JDBC帮助类
 * 韩冬
 */
package com.bplow.base.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

//import oracle.sql.TIMESTAMP;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.RowMapper;

public class JdbcUtil 
{
	 protected static final Log logger = LogFactory.getLog(JdbcUtil.class);

	
    /**
     * 结果集映射到vo
     * @param vo
     * @param rs
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws SQLException
     */
	public static void padVo(Object vo,ResultSet rs) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SQLException 
	{
		 Class<? extends Object> actualEditable = vo.getClass();  
	     PropertyDescriptor targetPds[] = BeanUtils.getPropertyDescriptors(actualEditable); 
	     for (PropertyDescriptor pdObj : targetPds)
	     {
	    	 if(pdObj.getName()!=null)
	    	 {
		    	 Method writeMethod = pdObj.getWriteMethod();
		         writeMethod.setAccessible(true); //设置到Vo
		         writeMethod.invoke(vo, new Object[] { rs.getObject(pdObj.getName())});
	    	 }
	     }
	     
	}
	
	/**
	 * 结果集映射到vo转换成String类型
	 * @param vo
	 * @param rs
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws SQLException
	 */
	public static void padVoString(Object vo,ResultSet rs) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SQLException
	{
		 Class<? extends Object> actualEditable = vo.getClass();  
	     PropertyDescriptor targetPds[] = BeanUtils.getPropertyDescriptors(actualEditable); 
	     for (PropertyDescriptor pdObj : targetPds)
	     {
	    	 if(pdObj.getName()!=null)
	    	 {
		    	 Method writeMethod = pdObj.getWriteMethod();
		         writeMethod.setAccessible(true); //设置到Vo
		         writeMethod.invoke(vo, new Object[] { String.valueOf(rs.getObject(pdObj.getName()))});
	    	 }
	     }
	}
	
	
	

	
	
	/**
	 * 结果集映射到vo 可以通用Hiernate生成Vo
	 * @param vo
	 * @param rs
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws SQLException
	 */
	public static void padVoLikeHibernate(Object vo,ResultSet rs) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SQLException
	{
		 Class<? extends Object> actualEditable = vo.getClass();  
	     PropertyDescriptor targetPds[] = BeanUtils.getPropertyDescriptors(actualEditable); 
	     for (PropertyDescriptor pdObj : targetPds)
	     {
	    	 if(pdObj.getName()!=null)
	    	 {
	    		 Method writeMethod = pdObj.getWriteMethod();
		         writeMethod.setAccessible(true);
	    		 String className = rs.getObject(pdObj.getName()).getClass().toString();
	    		 if(className.indexOf("BigDecimal")>0)
	    		 {
	    			 writeMethod.invoke(vo, new Object[] {((BigDecimal)rs.getObject(pdObj.getName())).longValue()});	 
	    		 }
	    		 /*else if(className.indexOf("TIMESTAMP")>0)         //时间戳
		    	 {
		    		 writeMethod.invoke(vo, new Object[] {((TIMESTAMP)rs.getObject(pdObj.getName())).dateValue()});
		    	 }*/
		    	 else
		    	 {
		    	     writeMethod.invoke(vo, new Object[] {rs.getObject(pdObj.getName())});
		    	 }
	    	 }
	     }
	}
	
	
	/**
	 * 结果集添加到集合以数组返回
	 * @param returnList
	 * @param rs
	 * @throws SQLException
	 */
	public static void padVoList(List<Object> returnList,ResultSet rs) throws SQLException
	 {
    	 int colNum = rs.getMetaData().getColumnCount();
    	 for ( int i = 1 ; i <= colNum ; i++)
    	 {
    		 String colName = rs.getMetaData().getColumnName(i);
    		 returnList.add(rs.getObject(colName));
    	 }	  
	 }
	
    
	 /**
	  * 获取数组映射
	  * @return
	  */
	 public static RowMapper<Object> getRowMapperArg()
	 {
		 return new RowMapper<Object>() {
	          public Object[] mapRow(ResultSet rs, int i) throws SQLException 
	          { 
	        	    List<Object> returnList = new LinkedList<Object>();
	        	    padVoList(returnList, rs);
	 			    return returnList.toArray();
	          }
	      };
	 }
	 
	 
	 /**
	  * 获取字符串映射
	  * @param obj
	  * @return
	  */
	 public static RowMapper<Object> getRowMapperString(final Object obj)
	  {
	 	return new RowMapper<Object>() {
	          public Object mapRow(ResultSet rs, int i) throws SQLException 
	          { 
	        	    Object returnObj = BeanUtils.instantiateClass(obj.getClass()); //每次new一个新对象
	        	    try {
						padVoString(returnObj, rs);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
	 			   return returnObj;
	          }
	      };
	  }
	 
	 
	 /**
	  * 获取原对象映射
	  * @param obj
	  * @return
	  */
	 public static RowMapper<Object> getRowMapperObject(final Object obj)
	  {
	 	return new RowMapper<Object>() {
	          public Object mapRow(ResultSet rs, int i) throws SQLException 
	          { 
	        	   Object returnObj = BeanUtils.instantiateClass(obj.getClass()); //每次new一个新对象	        	    
	        	    	try {
							padVo(returnObj, rs);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
	 			  return returnObj;
	          }
	      };
	  }
	
	 /**
	  * 获取原对象映射
	  * @param obj

	  * @return
	  */
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T>RowMapper<T> getRowMapperObject(final Class<T> type)
	  {
	 	return new RowMapper() {
	          public Object mapRow(ResultSet rs, int i) throws SQLException 
	          { 
	 			  return fillVo(type,rs);
	          }
	      };
	  }
	 
	 
	  /**
	     * 结果集映射到vo
	     * @param vo
	     * @param rs
	     * @throws IllegalArgumentException
	     * @throws IllegalAccessException
	     * @throws InvocationTargetException
	     * @throws SQLException
	     */
		@SuppressWarnings("unchecked")
		public static <T> T fillVo(Class<T> type,ResultSet rs)
		{
			 Object obj = BeanUtils.instantiateClass(type); //每次new一个新对象	     
		     PropertyDescriptor targetPds[] = BeanUtils.getPropertyDescriptors(type); 
		     for (PropertyDescriptor pdObj : targetPds)
		     {
		    	 if(pdObj.getName()!=null)
		    	 {
			    	 Method writeMethod = pdObj.getWriteMethod();
			         writeMethod.setAccessible(true); //设置到Vo
			         try {
						writeMethod.invoke(obj, new Object[] { rs.getObject(pdObj.getName())});
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
		    	 }
		     }
		     
		     return (T)obj;
		     
		}
		
		/**
		 * 获取sqlLike
		 * @param str
		 * @return
		 */
		public static String getLikeSql(String str)
		{
			str = null==str?"":str;
			StringBuilder sb = new StringBuilder();
			sb.append("%").append(str).append("%");
			return sb.toString();
		}

}
