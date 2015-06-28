package com.bplow.look.bass.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
//import org.springframework.transaction.config.internalTransactionAdvisor;


public class InitContextListener implements ServletContextListener{

	public void contextDestroyed(ServletContextEvent sce) {
				
	}

	public void contextInitialized(ServletContextEvent sce) {
	}

}
