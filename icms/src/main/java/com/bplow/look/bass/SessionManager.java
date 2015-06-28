package com.bplow.look.bass;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bplow.look.bass.dao.TbSysUser;
import com.bplow.look.bass.web.WebContext;


/**
 * @author new
 *
 */

public class SessionManager {
	
	/**
     * 该常量用来在session中保存用户信息的关键字
     */
    public final static String KEY = "lgu";
    protected static final Log logger = LogFactory.getLog(SessionManager.class);
    //得到在线用户数，多个用户读写sessions，需要同步
    //把 sessions 的属性从 public -> private ，不允许外部类直接读写，避免发生快速存储异常
    private static Map<String,HttpSession> sessions = Collections.synchronizedMap(new HashMap<String,HttpSession>());
    
    
    /**
     * 根据请求会话判断用户是否已经登录判断
     * @param session
     * @return
     */
    public static boolean isLogin(HttpSession ssn)
    {
        return getLoginUser(ssn) != null;
    }

    /**
     * 根据请求会话判断用户是否已经登录判断
     * @param session
     * @return
     */
    public static boolean isLogin(ServletRequest request)
    {
        return isLogin(((HttpServletRequest) request).getSession());
    }
    
    /**
     * 用于获取登录用户的资料信息
     * @param ssn
     * @return
     */
    public static TbSysUser getLoginUser(HttpSession ssn)
    {
        return (TbSysUser) ssn.getAttribute(KEY);
    }
    
    /**
     * 用户成功登录后将用户信息保存到会话中
     * @param ssn
     * @param user
     */
    public static void loginUser(HttpSession ssn, TbSysUser user)
    {
        if (ssn != null && user != null)
        {
            ssn.setAttribute(KEY, user);
            //在全局静态变量中保存sessionid
            sessions.put(ssn.getId(),ssn);
        }
    }
    
    public static void logoutUser()
    {
        HttpSession ssn = getSession();
        logoutUser(ssn);
    }

    public static void removeSessionId(String sessionid){
    	sessions.remove(sessionid);
    }
    
    public static void logoutUser(HttpSession ssn)
    {
    	String sid = ssn.getId();
    	ssn.removeAttribute(KEY);
    	logger.info("System destory sesion id is "+sid);
        ssn.invalidate();
        removeSessionId(sid);
    }
    
    /**
     * 打印出所有用户的session id
     */
    public static void printAllLoginUser(){
    	
    	Object sidArray[] = sessions.keySet().toArray();
    	for(Object obj : sidArray){
    		HttpSession sessionObj = sessions.get(obj);
    		//System.out.println(sessionObj.getId()+"------------"+((TbSysUser)(sessionObj.getAttribute(KEY))).getUserId());    		
    	}
   	
    }
    /**
     * 当前在线人数
     * @return
     */
    public static int getLoginUserNum(){
    	return sessions.size();
    }
    
    
    private static HttpServletRequest getRequest()
    {
        return WebContext.getWebContext().getRequest();
    }
    
    private static HttpSession getSession()
    {
        HttpSession session = null;
        HttpServletRequest request = getRequest();
        if(request != null)
            session = request.getSession();
        return session;
    }
}
