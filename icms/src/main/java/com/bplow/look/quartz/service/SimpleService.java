package com.bplow.look.quartz.service;

import java.io.IOException;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

public class SimpleService implements Serializable{
	
	private static final long serialVersionUID = 122323233244334343L;
	private static final Logger logger = LoggerFactory.getLogger(SimpleService.class);
	
	public void testMethod(String triggerName){
		//这里执行定时调度业务
		
//		try {
//			Runtime.getRuntime().exec("cmd /k start javac");
//			
//			
//		} catch (IOException e) {			
//			e.printStackTrace();
//		}
		
		
		System.out.println("--------------------------------111122");
		logger.info(triggerName);
	}
	
	public void testMethod2(){
		
		try {
			Runtime.getRuntime().exec("cmd /k dir javac");
			
			
			
			
			
			
			
			
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		System.out.println("--------------------------------22222");
		logger.info("testMethod2");
	}
}
