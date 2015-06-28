package com.bplow.look.bass.listener;

import java.util.HashSet;     
import javax.servlet.ServletContext;     
import javax.servlet.http.HttpSession;     
import javax.servlet.http.HttpSessionEvent;     
import javax.servlet.http.HttpSessionListener;     

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bplow.look.bass.SessionManager;
    
/**   
 * 该HttpSessionListener统计该用用同时在线人数
 *    
 */    
public class MySessionListener implements HttpSessionListener {
	
	protected static final Log logger = LogFactory.getLog(MySessionListener.class);
    
    /**   
     *  当用户访问该应用时，自动被监听，创建会话   
     */    
    @SuppressWarnings("unchecked")     
    public void sessionCreated(HttpSessionEvent event) {
    	
        HttpSession session = event.getSession();
        logger.info("session create is "+session.getId()+"   time is"+session.getMaxInactiveInterval());
//        ServletContext context = session.getServletContext();     
//        HashSet sessions = (HashSet) context.getAttribute("sessions");     
//        if (sessions == null) {     
//            sessions = new HashSet();     
//            context.setAttribute("sessions", sessions);     
//        }
        // 新建的session添加到sessions中
//        sessions.add(session);     
        // 可以在别处从context范围中取出sessions集合     
        // 然后使用sessions.size()获取当前活动的session数量,即为"在线人数"     
    
    }     
    
    /**   
     * 当用户调用session.invalidate()方法，或会话超时时会执行该方法   
     */    
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        logger.info("session destroy is "+session.getId());
        SessionManager.removeSessionId(session.getId());
//        ServletContext context = session.getServletContext();     
//        HashSet sessions = (HashSet) context.getAttribute("sessions");
        //销毁session, 从sessions中移除     
//        sessions.remove(session);
//        logger.info("current session number  is "+sessions.size());
    }     
    
}
