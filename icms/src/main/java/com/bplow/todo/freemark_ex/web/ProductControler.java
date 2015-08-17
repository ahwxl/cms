package com.bplow.todo.freemark_ex.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
import org.springframework.web.multipart.MultipartFile;

import com.bplow.todo.freemark_ex.dao.entity.FmProduct;
import com.bplow.todo.freemark_ex.service.ProductService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;


@Controller
public class ProductControler {
	
	@Autowired
	public ProductService productService;
	
	@RequestMapping(value = "/queryProductListById", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String queryProductListById(FmProduct fmContent, HttpServletRequest request,
			HttpServletResponse response, Model model) throws JsonGenerationException, JsonMappingException, IOException {
		String str = productService.queryProductListForJson(fmContent);
		
		return str;
	}
	
	@RequestMapping(value = "/queryProductById", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String queryProductById(FmProduct fmContent, HttpServletRequest request,
			HttpServletResponse response, Model model) throws JsonGenerationException, JsonMappingException, IOException {
		String str = productService.queryProductByIdJson(fmContent);
		
		return str;
	}
	/**
	 * 
	 * 显示修改产品
	 * @param fmContent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value = "/showEditorProductPage", produces="text/html;charset=UTF-8")
	public String editorProductPage(FmProduct fmProduct, HttpServletRequest request,
			HttpServletResponse response, Model model) throws JsonGenerationException, JsonMappingException, IOException {
		
		model.addAttribute("fmProduct", fmProduct);
		
		return "editorProduct";
	}
	/**
	 * 修改产品
	 * @param fmContent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value = "/editorProductAction", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String editorProductAction(FmProduct product,@RequestParam("file")MultipartFile file, HttpServletRequest request,
			HttpServletResponse response, Model model) throws JsonGenerationException, JsonMappingException, IOException {
		
		
		productService.updateProduct(product,file,request);
		
		return "{success:true,info:'操作成功!'}";
	}
	
	/**
	 * 删除action
	 * @param fmContent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value = "/delProductAction", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String delProductAction(FmProduct product, HttpServletRequest request,
			HttpServletResponse response, Model model) throws JsonGenerationException, JsonMappingException, IOException {
		
		productService.delProduct(product);
		return "{success:true,info:'操作成功!'}";
	}
	

}
