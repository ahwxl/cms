package com.bplow.todo.sysManager.service.impl;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.bplow.todo.sysManager.dao.entity.User;
import com.bplow.todo.sysManager.service.UserService;



@Service("sysUserServiceImpl")
public class UserServiceImpl implements UserService{

//	@Autowired
//	@Qualifier("sysUserDaoImpl")
//	private UserDao userDao;
	
	private String sid ="lgu";
	
	public boolean loginAction(User user,HttpServletRequest request) {
		Boolean loginResult = false;
		try {
			//User returnuser =userDao.queryUserByIdAddPwd(user);
			User returnuser = null;
			if("admin168".equals(user.getUserPwd())){
				returnuser = new User();
				returnuser.setUserName("admin");
			}
			
			if(null != returnuser){//登陆成功
				request.getSession().setAttribute(sid, returnuser);
				loginResult =true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return loginResult;
	}

	public boolean loginOutAction(User user, HttpServletRequest request) {
		request.getSession().removeAttribute(sid);
		return true;
	}

}
