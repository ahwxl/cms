package com.bplow.todo.freemark_ex.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import com.bplow.todo.freemark_ex.dao.entity.FmCatalog;
import com.bplow.todo.freemark_ex.service.FreemarkService;

/**
 * 目录管理
 * 
 * @author wangxiaolei
 * @version $Id: CatalogController.java, v 0.1 2018年4月28日 下午3:07:11 wangxiaolei
 *          Exp $
 */

@Controller
public class CatalogController implements ServletContextAware {

	private ServletContext servletContext;

	@Autowired
	public FreemarkService freemarkService;

	/**
	 * 目录管理页面
	 */
	@RequestMapping(value = "/catalogPage")
	public String toShowRightMenuPage() {

		return "catalog";
	}

	/**
	 * 显示栏目树 page
	 */
	@RequestMapping(value = "/showCatalogTree", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String showCatalogTree(String node, HttpServletRequest request, HttpServletResponse response) {

		String tmp;

		tmp = freemarkService.getFmCatalogList(node);

		return tmp;
	}

	/**
	 * get catalog tree json grid data
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCatalogGridData", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getCatalogGridData(String node, HttpServletRequest request, HttpServletResponse response) {

		String tmp = null;

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
	@RequestMapping(value = "/doGetCatalog", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doGetCatalog(FmCatalog fmCatalog, HttpServletRequest request) {

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
	 * 产品1，文章2，列表3
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/doAddCatalog", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doAddCatalog(FmCatalog fmCatalog, HttpServletRequest request, @RequestParam("imagefile") MultipartFile file) throws Exception {

		saveImage(fmCatalog, file);

		freemarkService.saveFmCatalog(fmCatalog);

		return "{\"msg\":\"添加成功\"}";
	}

	/**
	 * 修改栏目
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@RequestMapping(value = "/doEditorCatalog", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doEditorCatalog(FmCatalog fmCatalog, HttpServletRequest request, @RequestParam("imagefile") MultipartFile file) throws FileNotFoundException,
			IOException {

		saveImage(fmCatalog, file);

		freemarkService.editorFmCatalog(fmCatalog);

		return "{\"msg\":\"修改成功\"}";
	}

	/**
	 * 进入修改栏目页面
	 */
	@RequestMapping(value = "/showEditorCatalogPage", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String goEditorCatalogPage(FmCatalog fmCatalog, HttpServletRequest request) {

		String fmCatalogJsom = null;
		try {
			fmCatalogJsom = freemarkService.getFmCatalogByIdToJson(fmCatalog);
		} catch (Exception e) {

			e.printStackTrace();
		}

		return fmCatalogJsom;
	}

	/**
	 * 删除栏目
	 */
	@RequestMapping(value = "/doDelCatalog", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doDelCatalog(FmCatalog fmCatalog, HttpServletRequest request) {

		freemarkService.delFmCatalog(fmCatalog);

		return "{success:true,info:'操作成功!'}";
	}

	/**
	 * 发布栏目
	 * 
	 * @return
	 */
	@RequestMapping(value = "/doPublicCatalog", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public FmCatalog doPublicCatalogAction(FmCatalog fmCatalog, HttpServletRequest request) {

		freemarkService.delFmCatalog(fmCatalog);

		return fmCatalog;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * @param fmCatalog
	 * @param file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void saveImage(FmCatalog fmCatalog, MultipartFile file) throws FileNotFoundException, IOException {
		String filename = file.getOriginalFilename();
		if (StringUtils.isNotBlank(filename)) {
			String filepath = servletContext.getRealPath("/");
			String uploadfilepath = filepath + "/userfiles/images/upload";
			File tmpimamge = new File(uploadfilepath);
			if (!tmpimamge.exists()) {
				tmpimamge.mkdir();
			}
			OutputStream out = new BufferedOutputStream(new FileOutputStream(uploadfilepath + "/" + filename));
			IOUtils.copy(file.getInputStream(), out);
			out.flush();
			out.close();
		}
		fmCatalog.setImageSrc("userfiles/images/upload/" + filename);
	}

}
