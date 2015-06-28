package com.bplow.look.bass.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheSample {

	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//CacheManager cacheManager = CacheManager.getInstance().create();
		CacheManager cacheManager = new CacheManager();
		//cacheManager.addCache("cache1");
		Cache cache = cacheManager.getCache("cache1");
		
		cache.getAverageGetTime();
		cache.getAverageSearchTime();
		
		cache.getKeys();//list
		cache.getMemoryStoreSize();//long
		
		
		
		String[] cname = cacheManager.getCacheNames();
		
		System.out.println("缓存的个数：--》"+cname.length);
		//cache./**/
		
		System.out.println("The Key In Cache?:"+cache.isKeyInCache("abc"));  
		System.out.println("Cache is :"+cache);
                                                
        Element result = cache.get("abc");
                                                                           
        if(null==result)                                                   
        {
            System.out.println("No Data In Ehcache");                      
            List list = new ArrayList();
                                                                           
            for(int i=20;i<21;i++)                                         
            {                                                              
                /*TbSysUser user = new TbSysUser();
                user.setUserId("wangxiaolei");
                list.add(user);    */                                     
            }                                                              
            cache.put(new Element("user1",list));
            //cache.flush();                                                 
            System.out.println(cache.getName());
            result = cache.get("user1");
            cache.put(new Element("user1","www"));
        }
        result = cache.get("user1");
        System.out.println(result.getValue());
//        List ehcacheList = (List)result.getValue();
                                                                           
//        Iterator<TbSysUser> iter =  ehcacheList.iterator();                           
//                                                                           
//        while (iter.hasNext()) {                                           
//        	TbSysUser element = iter.next();                       
//            System.out.println("Studeng name is:"+element.getUserId());
//        }


	}

}
