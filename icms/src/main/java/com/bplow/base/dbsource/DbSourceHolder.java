package com.bplow.base.dbsource;

/**
 * 描述:   动态数据代理切换类
 * @author 韩冬
 *
 */
public class DbSourceHolder {
	  private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();  

	    public static void setDbSource(String  dbid) {  
	        contextHolder.set(dbid);  
	    }  
	      
	    public static String getDbSource() {  
	        return (String) contextHolder.get();  
	    }  
	      
	    public static void clearDbSource() {  
	        contextHolder.remove();  
	    }    
}
