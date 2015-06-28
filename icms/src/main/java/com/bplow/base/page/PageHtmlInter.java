package com.bplow.base.page;;

public   interface   PageHtmlInter   {   
    
     /**
      * 
      * 功能：获取当前页数
      * 
      * @return
     
      */ 
	public int getCurrentPage();
/**
 * 
 * 功能：生成HTML
 * 
 * @return

 */
    public String getHTML();
/**
 * 
 * 功能：获取总页数

 * 
 * @return

 */
    public int getTotalPages();
/**
 * 
 * 功能：获取总记录数
 * 
 * @return

 */
    public int getTotalRows();
/**
 * 
 * 功能：开始创建HTML代码
 * 
 * @return

 */
    public String getStartHTML();   
	    
	  }   

