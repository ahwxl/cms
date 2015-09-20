package com.bplow.todo.sysManager.service;

import javax.servlet.http.HttpServletRequest;

import com.bplow.todo.sysManager.dao.entity.User;


public interface UserService {
	
	boolean loginAction(User user,HttpServletRequest request);
	
	boolean loginOutAction(User user,HttpServletRequest request);

}
