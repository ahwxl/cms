package com.bplow.look.bass.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * jms 帮助类
 * 观察队列中 消费者的个数
 * @author new
 *
 */

public class JmsHelp {
	
	protected static final Log logger = LogFactory.getLog(JmsHelp.class);
	
	public static int run(){
		/*DefaultMessageListenerContainer	queueContainer = (DefaultMessageListenerContainer)SpringUtils.getBean("queueContainer");
		
		//int activeQueuenum = queueContainer.getActiveConsumerCount();
		int activeQueuenum = queueContainer.getConcurrentConsumers();
		logger.info("ActiveConsumerCount is "+activeQueuenum);
		int scheduleQueuenum = queueContainer.getScheduledConsumerCount();
		logger.info("ScheduledConsumerCount is "+scheduleQueuenum);
		
		int pauseQueue = queueContainer.getPausedTaskCount();
		logger.info("ConcurrentConsumers is "+pauseQueue);*/
		
		//queueContainer.get;
		
		
		
		return 0;
	}
	
	
	

}
