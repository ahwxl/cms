package com.bplow.todo.freemark_ex.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import com.bplow.todo.freemark_ex.dao.entity.FmContent;
import com.bplow.todo.freemark_ex.dao.entity.SysDicParamter;
import com.bplow.todo.freemark_ex.dao.entity.TbFreemarkInfo;
import com.bplow.todo.freemark_ex.service.FreemarkService;
import com.bplow.todo.sysManager.dao.entity.SysDepartment;
import com.bplow.todo.sysManager.dao.entity.SysRole;
import com.bplow.todo.sysManager.dao.entity.SysUser;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import freemarker.template.TemplateException;

@Controller
public class freemarkAction implements ServletContextAware {

	@Autowired
	public FreemarkService freemarkService;

	public ServletContext servletContext;
	
	// -----------------------------------------------模板管理----------------------------------------------
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

	@RequestMapping(value = "/showfreemarklist", method = RequestMethod.GET)
	@ResponseBody
	public String showFreemarkTmpItem(TbFreemarkInfo vo, Model model) {

		freemarkService.getFreemarkTmp(vo);
		String rtcnt = null;
		try {

			rtcnt = freemarkService.executeFtl();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}

		return rtcnt;
	}

	// ----------------------------------------------------产品管理---------------------------------------

	@RequestMapping(value = "/ckfinder/_samples/standalone.html", method = RequestMethod.GET)
	public String toImageMngPage(Model model) {

		return "showImageMngPage";
	}

	@RequestMapping(value = "/ckfinderPop", method = RequestMethod.GET)
	public String toImageMngPage_(Model model) {

		return "BrowersImagePop";
	}

	// ------------------------------------------------------产品管理----------------------------------------------------------------------------------
	/**
	 * ajax 返回文章列表
	 */
	@RequestMapping(value = "/getCntList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getCntList(FmContent vo, int start, int limit) {

		String rtjon = freemarkService.getCntList(vo, start, limit);
		return rtjon;
	}

	@RequestMapping(value = "/goTmplListPage", method = RequestMethod.GET)
	public String goTmplListPage(Model model) {
		return "goTmplListPage";
	}

	/**
	 * ajax template json
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getFreemarkTempList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getTemplateListJson(FmContent vo, int start, int limit) {

		String rtjon = freemarkService.getCntList(vo, start, limit);

		return rtjon;
	}

	// 显示添加模板页面
	@RequestMapping(value = "/goAddTmplPage", method = RequestMethod.GET)
	public String goAddTmplPage(Model model) {
		model.addAttribute("www", "网络");
		return "addTmplFilePage";
	}

	// 保存文件模板
	@RequestMapping(value = "/addTmplPage", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String addTmplPage(@RequestParam("file") MultipartFile file,
			HttpServletRequest request) {

		String fileaddr = request.getRealPath("WEB-INF/ftl/"
				+ file.getOriginalFilename());
		File tmpfile = new File(fileaddr);
		if (!file.isEmpty()) {
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

	// ---------------------------------------系统管理-----------------------------------------------------------------
	@RequestMapping(value = "/showOrganizationMngPage", method = RequestMethod.GET)
	public String viewOrganizationMngPage() {

		return "showSystemMng";
	}

	// ----------------角色管理----------------/getRoleGridDataJson
	@RequestMapping(value = "/getRoleGridDataJson_", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getRoleGridData(String node, HttpServletRequest request,
			HttpServletResponse response) {

		String tmp = null;
		SysRole sysRole = new SysRole();
		String roleName = request.getParameter("roleName");
		if (roleName != null || roleName != "") {
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

	// ----------------用户管理----------------
	@RequestMapping(value = "/showUserMngPage", method = RequestMethod.GET)
	public String viewUserMngPage() {

		return "userMng";
	}

	@RequestMapping(value = "/getUserGridDataJson", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getUserGridJson(SysUser sysUser, HttpServletRequest request,
			HttpServletResponse response) {

		String tmp = null;
		// SysUser sysUser = new SysUser();
		String userName = request.getParameter("userName");
		if (userName != null || userName != "") {
			sysUser.setUserName(userName);
		}
		try {
			tmp = freemarkService.getAllUserJson(sysUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("<<------>>" + tmp);

		return tmp;
	}

	// add user
	public String addUserAction() {

		return "ok";
	}

	// update user
	public String editorUserAction() {

		return "ok";
	}

	// delete user
	public String delUserAction() {

		return "";
	}

	// ----------------组织管理----------------------------------------------------
	@RequestMapping(value = "/getDepartmentGridDataJson", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getDepartmentGridJson(SysDepartment sysDepartment,
			HttpServletRequest request, HttpServletResponse response) {

		String tmp = null;
		// SysUser sysUser = new SysUser();

		try {
			tmp = freemarkService.getDepartmentListPage(sysDepartment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("<<------>>" + tmp);

		return tmp;
	}

	@RequestMapping(value = "/getDepartmentTreeDataJson", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getDepartmentTreeJson(String node,
			SysDepartment sysDepartment, HttpServletRequest request,
			HttpServletResponse response) {

		String tmp = null;
		// SysUser sysUser = new SysUser();

		try {
			tmp = freemarkService.getDepartmentListTreeJson(sysDepartment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("<<------>>" + tmp);

		return tmp;
	}

	@RequestMapping(value = "/addDepartment", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String addDepartment(SysDepartment sysDepartment,
			HttpServletRequest request, HttpServletResponse response) {

		freemarkService.saveDepartment(sysDepartment);

		return "{success:true,info:'操作成功!'}";
	}

	@RequestMapping(value = "/delDepartment", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delDepartment(SysDepartment sysDepartment,
			HttpServletRequest request, HttpServletResponse response) {

		freemarkService.delDepartment(sysDepartment);

		return "{success:true,info:'操作成功!'}";
	}

	@RequestMapping(value = "/getDepartmentInfo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getDepartmentInfo(SysDepartment sysDepartment,
			HttpServletRequest request, HttpServletResponse response) {

		freemarkService.delDepartment(sysDepartment);

		return "{success:true,info:'操作成功!'}";
	}

	@RequestMapping(value = "/updateDepartmentInfo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateDepartmentInfo(SysDepartment sysDepartment,
			HttpServletRequest request, HttpServletResponse response) {

		freemarkService.updateDepartment(sysDepartment);

		return "{success:true,info:'操作成功!'}";
	}

	// ----------------菜单管理

	// -----------------模块管理

	// ------------------授权管理

	// ---------------------------------------系统管理end--------------------------------------------------------------

	// --------------------------------参数管理----------------------------------------------

	@RequestMapping(value = "/sysDicParaMng", produces = "text/html;charset=UTF-8")
	public String sysDicParaListPage(SysDicParamter sys,
			HttpServletRequest request, HttpServletResponse response) {

		return "sysDicPara";
	}

	@RequestMapping(value = "/sysDicParaList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String sysDicParaList(SysDicParamter sys,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {

		sys.setPGroup("1");
		String str = freemarkService.getDicParaListByGroup(sys);

		return str;
	}

	@RequestMapping(value = "/addSysDicPara", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String addSysDicPara(SysDicParamter dicPara,
			@RequestParam("file") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String filename = file.getOriginalFilename();
		if (StringUtils.isNotBlank(filename)) {
			String filepath = request.getSession().getServletContext()
					.getRealPath("/");
			String uploadfilepath = filepath + "/userfiles/images/upload";
			File tmpimamge = new File(uploadfilepath);
			if (!tmpimamge.exists()) {
				tmpimamge.mkdir();
			}
			OutputStream out = new BufferedOutputStream(new FileOutputStream(
					uploadfilepath + "/" + filename));
			IOUtils.copy(file.getInputStream(), out);
			out.flush();
			out.close();
			dicPara.setPValue("newcms/userfiles/images/upload/" + filename);
		}
		dicPara.setPGroup("1");
		freemarkService.addDicPara(dicPara);

		return "{success:true,info:'操作成功!'}";
	}

	@RequestMapping(value = "/delSysDicPara", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delSysDicPara(SysDicParamter dicPara,
			HttpServletRequest request, HttpServletResponse response) {

		freemarkService.delDicPara(dicPara);

		return "{success:true,info:'操作成功!'}";
	}

	@RequestMapping(value = "/updateSysDicPara", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateSysDicPara(SysDicParamter dicPara,
			HttpServletRequest request, HttpServletResponse response) {

		freemarkService.editorDicPara(dicPara);

		return "{success:true,info:'操作成功!'}";
	}

	@RequestMapping(value = "/querySysDicParaById", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String querySysDicPara(SysDicParamter dicPara,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {

		String str = freemarkService.queryDicById(dicPara);

		return str;
	}

	// --------------------------------参数管理end----------------------------------------------

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
