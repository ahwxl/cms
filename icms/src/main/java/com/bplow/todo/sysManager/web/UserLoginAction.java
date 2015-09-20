package com.bplow.todo.sysManager.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bplow.todo.sysManager.dao.entity.User;
import com.bplow.todo.sysManager.service.UserService;


@Controller
public class UserLoginAction {
	
	@Autowired
	@Qualifier("sysUserServiceImpl")
	private UserService userService;
	/*系统首页*/
	@RequestMapping(value="/mainPage")
	public String mainPage(Map<String, Object> model,HttpServletRequest request){
		
		
		return "mainPage";
	}
	/*登录页面*/
	@RequestMapping(value="/login")
	public String doLogin(Map<String, Object> model,HttpServletRequest request){
		
		
		return "login";
	}
	
	
	@RequestMapping(value="/loginAction",method = RequestMethod.POST)
	@ResponseBody
	public String doWelcome(Map<String, Object> model,User user,HttpServletRequest request){
		String returnjson = "{success:true,info:'密码错误'}";
		if(userService.loginAction(user, request)){
			returnjson = "{success:true,info:'ok'}";
		}
		
		return returnjson;
	}
	
	@RequestMapping(value="/loginOut")
	public String doLoginOut(Map<String, Object> model,User user,HttpServletRequest request){
		String returnjson = "{success:true,info:'密码错误'}";
		if(userService.loginOutAction(user, request)){
			returnjson = "{success:true,info:'ok'}";
		}
		
		return "redirect:login";
	}

}
