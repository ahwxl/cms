package com.bplow.base.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.jdbc.core.RowMapper;



public class JdbcHelper {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object propertyFill(Object obj,ResultSet rs) throws SQLException, IllegalAccessException, InvocationTargetException
	{
		//Map propertyMap = new HashMap();
		List colNameList = new LinkedList();  //数据列
		
		int colNum = rs.getMetaData().getColumnCount();
		for(int i = 0; i < colNum; i++)
        {
            String colName = rs.getMetaData().getColumnName(i + 1);
            if(colName != null)
                colNameList.add(colName.toUpperCase());
        }
		
		//获取类属性
		 BeanWrapper bw = new BeanWrapperImpl(obj);
		 PropertyDescriptor pd[] = bw.getPropertyDescriptors();
		 
		 for (PropertyDescriptor targetProperty : pd )
		 {
			 String targetName = targetProperty.getName(); //获取属性对应名称
			 if (colNameList.contains(targetName.toUpperCase()))
			 {
				 Object  res = rs.getObject(targetName);
			     BeanUtils.setProperty(obj, targetName, res);
			 }
		        
		 }
		 
		 return obj;
		
	}
	
	public static Map<String, Object> propertyFill(ResultSet rs) throws SQLException, IllegalAccessException, InvocationTargetException
	{
		Map<String, Object> propertyMap = new HashMap<String, Object>();
		int colNum = rs.getMetaData().getColumnCount();
		for(int i = 0; i < colNum; i++)
        {
            String colName = rs.getMetaData().getColumnName(i + 1);
            propertyMap.put(colName.toLowerCase(), rs.getObject(i + 1));
        }
		return propertyMap;
	}
	
	
	/**
	 * 功能描述: 获取Object类型映射
	 * 时          间: 2011-12-9
	 * @author  韩冬 
	 * @param type
	 * @return
	 */
	public static RowMapper<?> getRowMapperObject(final Class<?> type)
	  {
	 	return new RowMapper<Object>() {
	          public Object mapRow(ResultSet rs, int i) 
	          { 
	        	  
						try {
							return propertyFill(type.newInstance(),rs);
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							e.printStackTrace();
						}
					  return null;
				
	          }
	      };
	  }
	
	/**
	 * 功能描述: 获取Map数据映射
	 * 时          间: 2011-12-9
	 * @author  韩冬 
	 * @return
	 */
	public static RowMapper<?> getRowMapperMap()
	{
		return new RowMapper<Object>() {
	          public Map<?, ?> mapRow(ResultSet rs, int i) 
	          { 
	        	  
						try {
							return propertyFill(rs);
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} 
					  return null;
				
	          }
	      };
	}
	 

}
