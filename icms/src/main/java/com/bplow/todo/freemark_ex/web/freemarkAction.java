package com.bplow.todo.freemark_ex.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.ResponseWrapper;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import com.bplow.todo.freemark_ex.dao.entity.FmCatalog;
import com.bplow.todo.freemark_ex.dao.entity.FmContent;
import com.bplow.todo.freemark_ex.dao.entity.FmProduct;
import com.bplow.todo.freemark_ex.dao.entity.TbFreemarkInfo;
import com.bplow.todo.freemark_ex.service.FreemarkService;
import com.bplow.todo.freemark_ex.service.ProductService;
import com.bplow.todo.sysManager.dao.entity.SysDepartment;
import com.bplow.todo.sysManager.dao.entity.SysRole;
import com.bplow.todo.sysManager.dao.entity.SysUser;

import freemarker.template.TemplateException;

@Controller
public class freemarkAction implements ServletContextAware{
	
	@Autowired
	public FreemarkService freemarkService;
	
	public ServletContext servletContext;
	@Autowired
	public ProductService productService;
	
	
	@RequestMapping(value = "/freemark", method = RequestMethod.GET) 
	@ResponseBody
	public String list(Model model) {
		
		String a = "大家好！";
		TbFreemarkInfo vo2 = new TbFreemarkInfo();
		vo2.setId("233");
		vo2.setTmpl_name("首页模板2");
		
		
		freemarkService.saveFreemarkTmp(vo2);
		
		return a;
	}
	
	@RequestMapping(value = "/freemark2", method = RequestMethod.POST)
	@ResponseBody
	public String list2(Model model) {
		
		String a = "test";
				
		
		return a;
	}
	
	@RequestMapping(value = "/freemark2", method = RequestMethod.GET)
	public String list3(String id, Model model) {
		
		String a = "test";
				
		
		return a;
	}

	
	@RequestMapping(value = "/showfreemarklist", method = RequestMethod.GET)
	@ResponseBody
	public String showFreemarkTmpItem(TbFreemarkInfo vo,Model model){
		
		freemarkService.getFreemarkTmp(vo);
		String rtcnt = null;
		try {
			
			rtcnt =	freemarkService.executeFtl();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		
		return rtcnt;
	}
	
	
	@RequestMapping(value = "/showAddCntPage", method = RequestMethod.GET)
	public String toAddCntPage(Model model){
		
		
		
		
		return "addArticle";
	}
	/**
	 * 添加产品页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/showAddProductPage", method = RequestMethod.GET)
	public String toAddProductPage(Model model){
		
		
		
		
		return "addProduct";
	}
	
	@RequestMapping(value = "/ckfinder/_samples/standalone.html", method = RequestMethod.GET)
	public String toImageMngPage(Model model){
		
		
		
		
		return "showImageMngPage";
	}
	
	@RequestMapping(value = "/ckfinderPop", method = RequestMethod.GET)
	public String toImageMngPage_(Model model){
		
		
		
		
		return "BrowersImagePop";
	}
	
	
	
	/**
	 * 显示文章列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/showArticle", method = RequestMethod.GET)
	public String toImageMngPage2(Model model){
		
		
		
		
		return "showArticle";
	}
	
	/**
	 * 显示产品列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/showProduct", method = RequestMethod.GET)
	public String toProductMngPage(Model model){
		
		
		return "showProduct";
	}
	
	
	@RequestMapping(value = "/saveProduct", method = RequestMethod.POST)
	@ResponseBody
	public String saveProduct(FmProduct product, HttpServletRequest request,
			HttpServletResponse response, Model model) throws Exception {

		productService.addProduct(product);

		return "{success:true,info:'操作成功!'}";
	}
	
	
	@RequestMapping(value = "/showPortal", method = RequestMethod.GET)
	public String toImageMngPage3(Model model){
		
		
		
		
		return "showPortal";
	}
	
	
	@RequestMapping(value = "/saveContent", method = RequestMethod.POST)
	@ResponseBody
	public String saveCnt(FmContent fmContent,HttpServletRequest request, HttpServletResponse response,Model model) 
			throws Exception{
		
		
		String cnt = request.getRequestURI();
		System.out.println(request.getSession(true).getId());
		
		
		freemarkService.saveCnt(fmContent);
		
		
		return "{success:true,info:'操作成功!'}";
	}
	
	/**
	 * ajax 返回文章列表
	 */
	@RequestMapping(value = "/getCntList", method = RequestMethod.POST ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getCntList(FmContent vo,int start,int limit){
		
		String rtjon = freemarkService.getCntList(vo, start, limit);
		//System.out.println(httpHeader.getContentType());
		return rtjon;
	}
	
	@RequestMapping(value = "/goTmplListPage", method = RequestMethod.GET)
	public String goTmplListPage(Model model){		
		return "goTmplListPage";
	}
	
	/**
	 * ajax template json
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getFreemarkTempList", method = RequestMethod.POST ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getTemplateListJson(FmContent vo,int start,int limit){
		
		String rtjon = freemarkService.getCntList(vo, start, limit);
		
		return rtjon;
	}
	
	//显示添加模板页面
	@RequestMapping(value = "/goAddTmplPage", method = RequestMethod.GET)
	public String goAddTmplPage(Model model){
		model.addAttribute("www", "网络");		
		return "addTmplFilePage";
	}
	
	//保存文件模板
	@RequestMapping(value = "/addTmplPage", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String addTmplPage(@RequestParam("file") MultipartFile file,HttpServletRequest request) {
		
		String fileaddr = request.getRealPath("WEB-INF/ftl/"+file.getOriginalFilename());
		File tmpfile = new File(fileaddr);
		if(!file.isEmpty()){
			byte[] in;
			try {
				in = file.getBytes();
				FileUtils.writeByteArrayToFile(tmpfile, in);
				
			} catch (IOException e) {				
				e.printStackTrace();
			}
			
			
			
		}
		
		
		return "ok";
	}
	/**
	 * 
	 */
	@RequestMapping(value = "/showrightMenu", method = RequestMethod.GET)
	public String toShowRightMenuPage(){
		
		//System.out.println(servletContext.getContextPath());
		
		return "showrightMenu";
	}
	
	/**
	 * 发布内容
	 * @return
	 */
	@RequestMapping(value = "/doPublicCnt", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String doPublicCnt(String id ,HttpServletRequest request){
		
		try {
			String rtvalue = freemarkService.doPublicCnt(id, request);
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (TemplateException e) {
			
			e.printStackTrace();
		}
		
		return "操作成功！";
	}
	
	/**
	 * 删除文章
	 */
	@RequestMapping(value = "/doDelCnt", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String doDelCnt(String id ,HttpServletRequest request){
		
		try {
			String rtvalue = freemarkService.doPublicCnt(id, request);
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		
		return "操作成功！";
	}
	
	
	
//----------------------------------------------------------------------------------------------------------	
	/**
	 * 栏目管理
	 */
	
	/**
	 * 显示栏目树
	 */
	@RequestMapping(value = "/showCatalogTree", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String showCatalogTree(String node,HttpServletRequest request,HttpServletResponse response){
		
		String tmp ;
		
		tmp = freemarkService.getFmCatalogList(node);
		
		return tmp;
	}
	
	/**
	 * get catalog tree json grid data
	 * @throws Exception 
	 */
	@RequestMapping(value = "/getCatalogGridData", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getCatalogGridData(String node,HttpServletRequest request,HttpServletResponse response) {
		
		String tmp =null ;
		
		try {
			tmp = freemarkService.getFmCatalogListJsonGridData(node);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(tmp);
		return tmp;
	}
	
	
	
	/**
	 * 查看栏目信息
	 */
	@RequestMapping(value = "/doGetCatalog", method = RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String doGetCatalog(FmCatalog fmCatalog,HttpServletRequest request){
		
		String fmCatalogJson = null;
		try {
			fmCatalogJson = freemarkService.getFmCatalogByIdToJson(fmCatalog);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return fmCatalogJson;
	}
	
	
	
	
	/**
	 * 添加栏目
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/doAddCatalog", method = RequestMethod.POST)
	@ResponseBody
	public String doAddCatalog(FmCatalog fmCatalog ,HttpServletRequest request){
		
		freemarkService.saveFmCatalog(fmCatalog);
		
		return "ok";
	}
	
	
	/**
	 * 修改栏目
	 */
	@RequestMapping(value = "/doEditorCatalog", method = RequestMethod.POST)
	@ResponseBody
	public String doEditorCatalog(FmCatalog fmCatalog ,HttpServletRequest request){
		
		freemarkService.editorFmCatalog(fmCatalog);
		
		return "ok";
	}
	/**
	 * 进入修改栏目页面
	 */
	@RequestMapping(value = "/showEditorCatalogPage", method = RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String goEditorCatalogPage(FmCatalog fmCatalog,HttpServletRequest request){
		
		String fmCatalogJsom = null;
        try {
			fmCatalogJsom =  freemarkService.getFmCatalogByIdToJson(fmCatalog);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
        System.out.println(fmCatalogJsom);
		
		return fmCatalogJsom;
	}
	
	
	
	/**
	 * 删除栏目
	 */
	@RequestMapping(value = "/doDelCatalog", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
	@ResponseBody
	public FmCatalog doDelCatalog(FmCatalog fmCatalog ,HttpServletRequest request){
		
		freemarkService.delFmCatalog(fmCatalog);
		
		return fmCatalog;
	}
	/**
	 * 发布栏目
	 * @return
	 */
	@RequestMapping(value = "/doPublicCatalog", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
	@ResponseBody
	public FmCatalog doPublicCatalogAction(FmCatalog fmCatalog ,HttpServletRequest request){
		
		freemarkService.delFmCatalog(fmCatalog);
		
		return fmCatalog;
	}
	
//----------------------------------------------------------------------------------------------------------	
	
//---------------------------------------系统管理-----------------------------------------------------------------	
	@RequestMapping(value = "/showOrganizationMngPage", method = RequestMethod.GET)
	public String viewOrganizationMngPage(){
		
		
		return "showSystemMng";
	}
	
	
	
	
	
	
	
	
	//----------------角色管理----------------/getRoleGridDataJson
	@RequestMapping(value = "/getRoleGridDataJson_", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getRoleGridData(String node,HttpServletRequest request,HttpServletResponse response) {
		
		String tmp =null ;
		SysRole sysRole = new SysRole();
		String roleName = request.getParameter("roleName");
		if(roleName!=null || roleName != ""){
			sysRole.setRoleName(roleName);
		}
		
		try {
			tmp = freemarkService.getAllRoleJson(sysRole);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(tmp);
		return tmp;
	}
	
	
	//----------------用户管理----------------
	@RequestMapping(value = "/showUserMngPage", method = RequestMethod.GET)
	public String viewUserMngPage(){
		
		
		return "userMng";
	}
	@RequestMapping(value = "/getUserGridDataJson", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getUserGridJson(SysUser sysUser,HttpServletRequest request,HttpServletResponse response){
		
		String tmp =null ;
		//SysUser sysUser = new SysUser();
		String userName = request.getParameter("userName");
		if(userName != null || userName !=""){
			sysUser.setUserName(userName);
		}
		try {
			tmp = freemarkService.getAllUserJson(sysUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("<<------>>"+tmp);
	
		
		return tmp;
	}
	
	//add user
	public String addUserAction(){
		
		
		return "ok";
	}
	
	//update user
	public String editorUserAction(){
		
		
		return "ok";
	}
	
	//delete user
	public String delUserAction(){
		
		
		return "";
	}
	
	
	//----------------组织管理----------------------------------------------------
	@RequestMapping(value = "/getDepartmentGridDataJson", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getDepartmentGridJson(SysDepartment sysDepartment,HttpServletRequest request,HttpServletResponse response){
		
		String tmp =null ;
		//SysUser sysUser = new SysUser();
		
		try {
			tmp = freemarkService.getDepartmentListPage(sysDepartment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("<<------>>"+tmp);
		
		
		return tmp;
	}
	
	@RequestMapping(value = "/getDepartmentTreeDataJson", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getDepartmentTreeJson(String node,SysDepartment sysDepartment,HttpServletRequest request,HttpServletResponse response){
		
		String tmp =null ;
		//SysUser sysUser = new SysUser();
		
		try {
			tmp = freemarkService.getDepartmentListTreeJson(sysDepartment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("<<------>>"+tmp);
		
		
		return tmp;
	}
	
	@RequestMapping(value = "/addDepartment", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String addDepartment(SysDepartment sysDepartment,HttpServletRequest request,HttpServletResponse response){
		
		
		freemarkService.saveDepartment(sysDepartment);
		
		return "{success:true,info:'操作成功!'}";
	}
	
	@RequestMapping(value = "/delDepartment", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String delDepartment(SysDepartment sysDepartment,HttpServletRequest request,HttpServletResponse response){
		
		
		freemarkService.delDepartment(sysDepartment);
		
		return "{success:true,info:'操作成功!'}";
	}
	
	@RequestMapping(value = "/getDepartmentInfo", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getDepartmentInfo(SysDepartment sysDepartment,HttpServletRequest request,HttpServletResponse response){
		
		
		freemarkService.delDepartment(sysDepartment);
		
		return "{success:true,info:'操作成功!'}";
	}
	
	@RequestMapping(value = "/updateDepartmentInfo", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String updateDepartmentInfo(SysDepartment sysDepartment,HttpServletRequest request,HttpServletResponse response){
		
		
		freemarkService.updateDepartment(sysDepartment);
		
		return "{success:true,info:'操作成功!'}";
	}
	
	
	//----------------菜单管理
	
	
	
	
	
	//-----------------模块管理
	
	
	
	
	//------------------授权管理
	
	
	
//---------------------------------------系统管理end--------------------------------------------------------------	
	
	
	
	
	
	
	
	
	
	
	
	

	public void setServletContext(ServletContext servletContext) {
		servletContext = servletContext;
	}

}
