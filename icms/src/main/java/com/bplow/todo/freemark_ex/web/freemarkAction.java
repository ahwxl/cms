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
import com.bplow.todo.freemark_ex.dao.entity.FmProduct;
import com.bplow.todo.freemark_ex.dao.entity.SysDicParamter;
import com.bplow.todo.freemark_ex.dao.entity.TbFreemarkInfo;
import com.bplow.todo.freemark_ex.service.FreemarkService;
import com.bplow.todo.freemark_ex.service.ProductService;
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

    public ServletContext  servletContext;
    @Autowired
    public ProductService  productService;
    //C:/Users/qian/git/cmsfront/cmsfront/src/main/webapp
    private String         cmsfrontpath = "/home/wxl/tomcat8080/webapps/ROOT"; ///

    //-----------------------------------------------模板管理----------------------------------------------
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

    //----------------------------------------------------文章管理---------------------------------------
    /**
     * 显示文章列表
     * @param model
     * @return
     */
    @RequestMapping(value = "/showArticle", method = RequestMethod.GET)
    public String toImageMngPage2(Model model) {

        return "showArticle";
    }

    /**
     * 添加文章页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/showAddCntPage", method = RequestMethod.GET)
    public String toAddCntPage(Model model) {

        return "addArticle";
    }

    /**
     * 添加文章页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/showEditorCntPage", method = RequestMethod.GET)
    public String toEditorCntPage(Model model, FmContent fmContent) {

        model.addAttribute("fmContent", fmContent);

        return "editorArticle";
    }

    /**
     * 添加文章 操作
     * @param fmContent
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveContent", method = RequestMethod.POST)
    @ResponseBody
    public String saveCnt(FmContent fmContent, HttpServletRequest request,
                          HttpServletResponse response, Model model) throws Exception {
        String cnt = request.getRequestURI();
        //System.out.println(request.getSession(true).getId());
        freemarkService.saveCnt(fmContent);
        return "{success:true,info:'操作成功!'}";
    }

    /**
     * 修改文章
     * @param fmContent
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/editorContent", method = RequestMethod.POST)
    @ResponseBody
    public String editorCnt(FmContent fmContent, HttpServletRequest request,
                            HttpServletResponse response, Model model) throws Exception {
        freemarkService.editorCnt(fmContent);
        return "{success:true,info:'操作成功!'}";
    }

    @RequestMapping(value = "/queryCntById", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String queryCntById(FmContent fmContent, HttpServletRequest request,
                               HttpServletResponse response, Model model)
                                                                         throws JsonGenerationException,
                                                                         JsonMappingException,
                                                                         IOException {
        String str = freemarkService.getCntByIdToJson(fmContent.getId());
        return str;
    }

    /**
     * 添加产品页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/showAddProductPage", method = RequestMethod.GET)
    public String toAddProductPage(Model model) {

        return "addProduct";
    }

    @RequestMapping(value = "/ckfinder/_samples/standalone.html", method = RequestMethod.GET)
    public String toImageMngPage(Model model) {

        return "showImageMngPage";
    }

    @RequestMapping(value = "/ckfinderPop", method = RequestMethod.GET)
    public String toImageMngPage_(Model model) {

        return "BrowersImagePop";
    }

    //------------------------------------------------------产品管理----------------------------------------------------------------------------------	
    /**
     * 显示产品列表
     * @param model
     * @return
     */
    @RequestMapping(value = "/showProduct", method = RequestMethod.GET)
    public String toProductMngPage(Model model) {

        return "showProduct";
    }

    /**
     * 保存产品
     * @param product
     * @param file
     * @param name
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveProduct", method = RequestMethod.POST)
    @ResponseBody
    public String saveProduct(FmProduct product, @RequestParam("file") MultipartFile file,
                              HttpServletRequest request, HttpServletResponse response, Model model)
                                                                                                    throws Exception {

        String filename = file.getOriginalFilename();
        if (StringUtils.isNotBlank(filename)) {
            String filepath = request.getSession().getServletContext().getRealPath("/");
            String uploadfilepath = filepath + "/userfiles/images/upload";
            File tmpimamge = new File(uploadfilepath);
            if (!tmpimamge.exists()) {
                tmpimamge.mkdir();
            }
            OutputStream out = new BufferedOutputStream(new FileOutputStream(uploadfilepath + "/"
                                                                             + filename));
            IOUtils.copy(file.getInputStream(), out);
            out.flush();
            out.close();
            OutputStream outto = new BufferedOutputStream(new FileOutputStream(
                cmsfrontpath + "/userfiles/images/upload/" + filename));
            IOUtils.copy(file.getInputStream(), outto);
            outto.flush();
            outto.close();
        }
        product.setProductImageUrl("userfiles/images/upload/" + filename);
        productService.addProduct(product);

        return "{success:true,info:'操作成功!'}";
    }

    @RequestMapping(value = "/showPortal", method = RequestMethod.GET)
    public String toImageMngPage3(Model model) {

        return "showPortal";
    }

    /**
     * ajax 返回文章列表
     */
    @RequestMapping(value = "/getCntList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getCntList(FmContent vo, int start, int limit) {

        String rtjon = freemarkService.getCntList(vo, start, limit);
        //System.out.println(httpHeader.getContentType());
        return rtjon;
    }

    @RequestMapping(value = "/goTmplListPage", method = RequestMethod.GET)
    public String goTmplListPage(Model model) {
        return "goTmplListPage";
    }

    /**
     * ajax template json
     * @param model
     * @return
     */
    @RequestMapping(value = "/getFreemarkTempList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getTemplateListJson(FmContent vo, int start, int limit) {

        String rtjon = freemarkService.getCntList(vo, start, limit);

        return rtjon;
    }

    //显示添加模板页面
    @RequestMapping(value = "/goAddTmplPage", method = RequestMethod.GET)
    public String goAddTmplPage(Model model) {
        model.addAttribute("www", "网络");
        return "addTmplFilePage";
    }

    //保存文件模板
    @RequestMapping(value = "/addTmplPage", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String addTmplPage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

        String fileaddr = request.getRealPath("WEB-INF/ftl/" + file.getOriginalFilename());
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

    //-----------------------------------------------目录管理-------------------------------------------
    /**
     * 目录管理页面
     */
    @RequestMapping(value = "/showrightMenu", method = RequestMethod.GET)
    public String toShowRightMenuPage() {

        //System.out.println(servletContext.getContextPath());

        return "showrightMenu";
    }

    /**
     * 发布内容
     * @return
     */
    @RequestMapping(value = "/doPublicCnt", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String doPublicCnt(String id, HttpServletRequest request) {

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
    @RequestMapping(value = "/doDelCnt", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String doDelCnt(String id, HttpServletRequest request) {

        boolean rtvalue = freemarkService.delCntById(id);

        return "{success:true,info:'操作成功!'}";
    }

    //--------------------------------------------------目录管理----------------------------------------------------	

    

    //----------------------------------------------------------------------------------------------------------	

    //---------------------------------------系统管理-----------------------------------------------------------------	
    @RequestMapping(value = "/showOrganizationMngPage", method = RequestMethod.GET)
    public String viewOrganizationMngPage() {

        return "showSystemMng";
    }

    //----------------角色管理----------------/getRoleGridDataJson
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

    //----------------用户管理----------------
    @RequestMapping(value = "/showUserMngPage", method = RequestMethod.GET)
    public String viewUserMngPage() {

        return "userMng";
    }

    @RequestMapping(value = "/getUserGridDataJson", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getUserGridJson(SysUser sysUser, HttpServletRequest request,
                                  HttpServletResponse response) {

        String tmp = null;
        //SysUser sysUser = new SysUser();
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

    //add user
    public String addUserAction() {

        return "ok";
    }

    //update user
    public String editorUserAction() {

        return "ok";
    }

    //delete user
    public String delUserAction() {

        return "";
    }

    //----------------组织管理----------------------------------------------------
    @RequestMapping(value = "/getDepartmentGridDataJson", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getDepartmentGridJson(SysDepartment sysDepartment, HttpServletRequest request,
                                        HttpServletResponse response) {

        String tmp = null;
        //SysUser sysUser = new SysUser();

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
    public String getDepartmentTreeJson(String node, SysDepartment sysDepartment,
                                        HttpServletRequest request, HttpServletResponse response) {

        String tmp = null;
        //SysUser sysUser = new SysUser();

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
    public String addDepartment(SysDepartment sysDepartment, HttpServletRequest request,
                                HttpServletResponse response) {

        freemarkService.saveDepartment(sysDepartment);

        return "{success:true,info:'操作成功!'}";
    }

    @RequestMapping(value = "/delDepartment", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String delDepartment(SysDepartment sysDepartment, HttpServletRequest request,
                                HttpServletResponse response) {

        freemarkService.delDepartment(sysDepartment);

        return "{success:true,info:'操作成功!'}";
    }

    @RequestMapping(value = "/getDepartmentInfo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getDepartmentInfo(SysDepartment sysDepartment, HttpServletRequest request,
                                    HttpServletResponse response) {

        freemarkService.delDepartment(sysDepartment);

        return "{success:true,info:'操作成功!'}";
    }

    @RequestMapping(value = "/updateDepartmentInfo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String updateDepartmentInfo(SysDepartment sysDepartment, HttpServletRequest request,
                                       HttpServletResponse response) {

        freemarkService.updateDepartment(sysDepartment);

        return "{success:true,info:'操作成功!'}";
    }

    //----------------菜单管理

    //-----------------模块管理

    //------------------授权管理

    //---------------------------------------系统管理end--------------------------------------------------------------	

    //--------------------------------参数管理----------------------------------------------

    @RequestMapping(value = "/sysDicParaMng", produces = "text/html;charset=UTF-8")
    public String sysDicParaListPage(SysDicParamter sys, HttpServletRequest request,
                                     HttpServletResponse response) {

        return "sysDicPara";
    }

    @RequestMapping(value = "/sysDicParaList", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String sysDicParaList(SysDicParamter sys, HttpServletRequest request,
                                 HttpServletResponse response) throws JsonGenerationException,
                                                              JsonMappingException, IOException {

        sys.setPGroup("1");
        String str = freemarkService.getDicParaListByGroup(sys);

        return str;
    }

    @RequestMapping(value = "/addSysDicPara", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String addSysDicPara(SysDicParamter dicPara, @RequestParam("file") MultipartFile file,
                                HttpServletRequest request, HttpServletResponse response)
                                                                                         throws Exception {

        String filename = file.getOriginalFilename();
        if (StringUtils.isNotBlank(filename)) {
            String filepath = request.getSession().getServletContext().getRealPath("/");
            String uploadfilepath = filepath + "/userfiles/images/upload";
            File tmpimamge = new File(uploadfilepath);
            if (!tmpimamge.exists()) {
                tmpimamge.mkdir();
            }
            OutputStream out = new BufferedOutputStream(new FileOutputStream(uploadfilepath + "/"
                                                                             + filename));
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
    public String delSysDicPara(SysDicParamter dicPara, HttpServletRequest request,
                                HttpServletResponse response) {

        freemarkService.delDicPara(dicPara);

        return "{success:true,info:'操作成功!'}";
    }

    @RequestMapping(value = "/updateSysDicPara", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String updateSysDicPara(SysDicParamter dicPara, HttpServletRequest request,
                                   HttpServletResponse response) {

        freemarkService.editorDicPara(dicPara);

        return "{success:true,info:'操作成功!'}";
    }

    @RequestMapping(value = "/querySysDicParaById", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String querySysDicPara(SysDicParamter dicPara, HttpServletRequest request,
                                  HttpServletResponse response) throws JsonGenerationException,
                                                               JsonMappingException, IOException {

        String str = freemarkService.queryDicById(dicPara);

        return str;
    }

    //--------------------------------参数管理end----------------------------------------------

    public void setServletContext(ServletContext servletContext) {
        servletContext = servletContext;
    }

}
