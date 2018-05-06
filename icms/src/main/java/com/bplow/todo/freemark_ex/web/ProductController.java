/**
 * www.bplow.com
 */
package com.bplow.todo.freemark_ex.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.bplow.todo.freemark_ex.dao.entity.FmProduct;
import com.bplow.todo.freemark_ex.service.ProductService;

/**
 * @desc 产品管理
 * @author wangxiaolei
 * @date 2018年5月1日 下午5:31:36
 */
@Controller
public class ProductController implements ServletContextAware {

	private ServletContext servletContext;

	@Autowired
	public ProductService productService;

	// C:/Users/qian/git/cmsfront/cmsfront/src/main/webapp
	private String cmsfrontpath = "/home/wxl/tomcat8080/webapps/ROOT";

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * 添加产品页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/showAddProductPage")
	public String toAddProductPage(Model model) {

		return "addProduct";
	}

	/**
	 * 显示产品列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/showProduct")
	public String toProductMngPage(Model model) {

		return "showProduct";
	}

	/**
	 * 保存产品
	 * 
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
	public String saveProduct(FmProduct product,
			@RequestParam("file") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception {

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

}
