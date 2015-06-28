package com.bplow.look.bass.utils;

import org.springframework.web.context.ContextLoader;

public class SpringUtils {
	
	public static Object getBean(String beanName){
		return ContextLoader.getCurrentWebApplicationContext().getBean(beanName);
	}

}
