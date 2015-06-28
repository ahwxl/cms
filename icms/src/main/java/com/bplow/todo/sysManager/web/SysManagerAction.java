package com.bplow.todo.sysManager.web;

import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.bplow.todo.sysManager.dao.entity.SysDepartment;
import com.bplow.todo.sysManager.dao.entity.SysUser;
import com.bplow.todo.sysManager.service.SysManagerService;
import com.bplow.todo.sysManager.Util.MD5Util;
import com.bplow.todo.sysManager.dao.entity.SysMenu;
import com.bplow.todo.sysManager.dao.entity.SysRole;
import com.bplow.todo.sysManager.dao.entity.SysModule;



@Controller
public class SysManagerAction implements ServletContextAware{
	
	@Autowired
	SysManagerService sysManagerService;

	public ServletContext servletContext;
	
	
	//--------------------------------------begin菜单管理begin-------------------------------------------
	/**
	 * 获取菜单列表
	 */
	@RequestMapping(value = "/getSysMenuDataJsonNew", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getSysMenuDataJsonNew(SysMenu sysMenu,HttpServletRequest request,HttpServletResponse response){
		
		String tmp =null ;
		
		try {
			tmp = sysManagerService.getAllSysMenuJson(sysMenu);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("<<------>>"+tmp);
	
		
		return tmp;
	}
	
	/**
	 * 添加一个菜单项
	 */
	@RequestMapping(value = "/addSysMenu", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String addSysMenu(SysMenu sysMenu,HttpServletRequest request,HttpServletResponse response){
		
		
		sysManagerService.saveSysMenu(sysMenu);
		
		return "{success:true,info:'操作成功!'}";
	}
	
	/**
	 * 删除一个菜单项
	 */
	@RequestMapping(value = "/doDelSysMenu", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String doDelSysMenu(SysMenu sysMenu ,HttpServletRequest request){
		
		sysManagerService.delSysMenu(sysMenu);
		
		return "{success:true,info:'操作成功!'}";
	}
	
	/**
	 * 进入修改菜单项页面
	 */
	@RequestMapping(value = "/showEditordoEditorSysMenuPage", method = RequestMethod.GET)
	public String showEditordoEditorSysMenuPage(Model model,String menuid){
		SysMenu sysMenu = new SysMenu();
        try {
        	sysMenu.setMenuid(menuid);
        	sysMenu =  sysManagerService.getSysMenuByIdToJson(sysMenu);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("sysMenu", sysMenu);
		return "editSysMenuPage";
	}
	
	/**
	 * 修改一个菜单项
	 */
	@RequestMapping(value = "/doEditorSysMenu", method = RequestMethod.POST)
	@ResponseBody
	public String doEditorSysMenu(SysMenu sysMenu ,HttpServletRequest request){
		sysManagerService.editorSysMenu(sysMenu);
		return "ok";
	}
	
	//--------------------------------------end菜单管理end-------------------------------------------
	
	//--------------------------------------begin模块管理begin-------------------------------------------
	

	//--------------------------------------begin角色管理begin-----------------------------------
	/*获取角色信息*/
	@RequestMapping(value = "/getRoleGridDataJson", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getRoleGridData(String node,HttpServletRequest request,HttpServletResponse response) {
		
		String tmp =null ;
		SysRole sysRole = new SysRole();
		
		try {
			tmp = sysManagerService.getAllRoleJson(sysRole);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(tmp);
		return tmp;
	}
	/*添加角色信息*/
	@RequestMapping(value = "/addRole", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String addRole(SysRole vo, HttpServletRequest request,HttpServletResponse response){
		sysManagerService.saveRole(vo);
		return "{success:true,info:'操作成功!'}";
	}
	/*删除角色*/
	@RequestMapping(value = "/delRole", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String delRole(SysRole vo, HttpServletRequest request,HttpServletResponse response){
		sysManagerService.delRole(vo);
		return "{success:true,info:'操作成功!'}";
	}
	/*更新角色*/
	@RequestMapping(value = "/updateRole", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String updateRole(SysRole vo, HttpServletRequest request,HttpServletResponse response){
		sysManagerService.updateRole(vo);
		return "{success:true,info:'操作成功!'}";
	}
	//----------------end角色管理----------------

	@RequestMapping(value = "/getModuleTreeDataJson", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getModuleTreeDataJson(String node,SysModule sysModule,HttpServletRequest request,HttpServletResponse response){
		
		String tmp =null ;
		
		try {
			tmp = sysManagerService.getModuleListTreeJson(sysModule);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("<<------>>"+tmp);
		
		
		return tmp;
	}

	
	@RequestMapping(value = "/getModuleGridDataJson", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getModuleGridDataJson(SysModule sysModule,HttpServletRequest request,HttpServletResponse response){
		
		String tmp =null ;
		//SysUser sysUser = new SysUser();
		
		try {
			tmp = sysManagerService.getModuleListPage(sysModule);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("<<------>>"+tmp);
		
		
		return tmp;
	}
	
	/**
	 * 删除一个模块
	 */
	@RequestMapping(value = "/doDelSysModule", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String doDelSysModule(SysModule sysModule ,HttpServletRequest request){
		
		sysManagerService.delSysModule(sysModule);
		
		return "{success:true,info:'操作成功!'}";
	}
	
	/**
	 * 进入修改模块页面
	 */
	@RequestMapping(value = "/toEditSysModulePage", method = RequestMethod.GET)
	public String toEditSysModulePage(Model model,String moduleid){
		SysModule sysModule = new SysModule();
        try {
        	sysModule.setModuleid(moduleid);
        	sysModule =  sysManagerService.getSysModuleById(sysModule);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("sysModule", sysModule);
		return "editSysModulePage";
	}
	
	/**
	 * 修改一个模块
	 */
	@RequestMapping(value = "/doEditorSysModule", method = RequestMethod.POST)
	@ResponseBody
	public String doEditorSysModule(SysModule sysModule ,HttpServletRequest request){
		sysManagerService.editorSysModule(sysModule);
		return "ok";
	}
	
	/**
	 * 添加一个模块
	 */
	@RequestMapping(value = "/addSysModule", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String addSysModule(SysModule sysModule,HttpServletRequest request,HttpServletResponse response){
		
		
		sysManagerService.saveSysModule(sysModule);
		
		return "{success:true,info:'操作成功!'}";
	}
	
	//--------------------------------------end模块管理end-------------------------------------------
	
	
	
	public void setServletContext(ServletContext _servletContext) {
		servletContext = _servletContext;
	}
	//----------------组织管理----------------------------------------------------
	@RequestMapping(value = "/getDepartmentGridDataJsonNew", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getDepartmentGridJson(SysDepartment sysDepartment,HttpServletRequest request,HttpServletResponse response){
		
		String tmp =null ;
		String queryParam = request.getParameter("queryParam");
		sysDepartment.setDepartname(queryParam);
		try {
			tmp = sysManagerService.getDepartmentListPage(sysDepartment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp;
	}
	
	@RequestMapping(value = "/getDepartmentTreeDataJsonNew", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getDepartmentTreeJson(String node,SysDepartment sysDepartment,HttpServletRequest request,HttpServletResponse response){
		
		String tmp =null ;
		try {
			tmp = sysManagerService.getDepartmentListTreeJson(sysDepartment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp;
	}
	
	@RequestMapping(value = "/addDepartmentNew", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String addDepartment(SysDepartment sysDepartment,HttpServletRequest request,HttpServletResponse response){
		
		
		sysManagerService.saveDepartment(sysDepartment);
		
		return "{success:true,info:'操作成功!'}";
	}
	
	@RequestMapping(value = "/delDepartmentNew", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String delDepartment(SysDepartment sysDepartment,HttpServletRequest request,HttpServletResponse response){
	
		sysManagerService.delDepartmentList(sysDepartment);
		
		return "{success:true,info:'操作成功!'}";
	}
	
	@RequestMapping(value = "/getDepartmentInfoNew", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getDepartmentInfo(SysDepartment sysDepartment,HttpServletRequest request,HttpServletResponse response){
		
		
		sysManagerService.delDepartment(sysDepartment);
		
		return "{success:true,info:'操作成功!'}";
	}
	
	@RequestMapping(value = "/updateDepartmentInfoNew", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String updateDepartmentInfo(SysDepartment sysDepartment,HttpServletRequest request,HttpServletResponse response){
		
		
		sysManagerService.updateDepartment(sysDepartment);
		
		return "{success:true,info:'操作成功!'}";
	}
	//----------------用户管理----------------------------------------------------
	@RequestMapping(value = "/getSysUserGridJson", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getSysUserGridJson(SysUser sysUser,HttpServletRequest request,HttpServletResponse response){
		
		String tmp =null ;
		String queryParam = request.getParameter("queryParam");
		sysUser.setLoginName(queryParam);
		try {
			tmp = null;
			System.out.println(tmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp;
	}
	
	
	@RequestMapping(value = "/addSysUser", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String addSysUser(SysUser sysUser,HttpServletRequest request,HttpServletResponse response) throws Exception, SQLException{
		
		sysUser.setUserPwd(MD5Util.createMD5(sysUser.getUserPwd()));
		sysManagerService.saveSysUser(sysUser);
		sysManagerService.saveUserAuthorities(sysUser.getUserName(), "ROLE_USER");
		return "{success:true,info:'操作成功!'}";
	}
	
	@RequestMapping(value = "/delSysUser", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String delSysUser(SysUser sysUser,HttpServletRequest request,HttpServletResponse response){
	
		sysManagerService.delSysUser(sysUser);
		
		return "{success:true,info:'操作成功!'}";
	}
	
	
	@RequestMapping(value = "/updateSysUserList", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String updateSysUserList(SysUser sysUser,HttpServletRequest request,HttpServletResponse response){
		
		sysUser.setUserPwd(MD5Util.createMD5(sysUser.getUserPwd()));
		sysManagerService.updateSysUser(sysUser);
		
		return "{success:true,info:'操作成功!'}";
	}
	@RequestMapping(value = "/updateSysUser", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String updateSysUser(SysUser sysUser,HttpServletRequest request,HttpServletResponse response){
		
		
		sysManagerService.updateSysUserEnabled(sysUser);
		
		return "{success:true,info:'操作成功!'}";
	}
	@RequestMapping(value = "/setPwdSysUser", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String setPwdSysUser(SysUser sysUser,HttpServletRequest request,HttpServletResponse response){
		
		
		sysManagerService.setPwdSysUser(sysUser);
		
		return "{success:true,info:'操作成功!'}";
	}
	@RequestMapping(value = "/updateSysUserPwd", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String updateSysUserPwd(SysUser sysUser,HttpServletRequest request,HttpServletResponse response){
		
		
		SysUser SysUserNew = sysManagerService.getSysUser(sysUser);
		SysUserNew.setUserPwd(MD5Util.createMD5(sysUser.getNewPwd()));
		sysManagerService.updateSysUser(SysUserNew);
		
		return "{success:true,info:'操作成功!'}";
	}
	@RequestMapping(value = "/verifyPassword", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String verifyPassword(SysUser sysUser,HttpServletRequest request,HttpServletResponse response){		
        String  userName = request.getParameter("userName");
		String  userPwd = request.getParameter("userPwd");
		sysUser.setUserName(userName);
		SysUser SysUserNew = sysManagerService.getSysUser(sysUser);
	    if(SysUserNew.getUserPwd().equals(MD5Util.createMD5(userPwd)))		
		return "{success:true,info:'sucess'}";
	    else return "{success:true,info:'当前密码不正确，请核对！'}";
	}
	
	
	
}
