package com.bplow.todo.freemark_ex.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bplow.look.bass.utils.JsonStructureDataHelp;
import com.bplow.todo.freemark_ex.dao.FreeMarkHibernateDao;
import com.bplow.todo.freemark_ex.dao.FreeMarkJdbcDao;
import com.bplow.todo.freemark_ex.dao.entity.FmProduct;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
@Transactional
public class ProductService {

	@Autowired
	public FreeMarkJdbcDao freeMarkJdbcDao;
	@Autowired
	public FreeMarkHibernateDao freeMarkHibernateDao;

	// C:/Users/qian/git/cmsfront/cmsfront/src/main/webapp
	private String cmsfrontpath = "/home/wxl/tomcat8080/webapps/ROOT";

	public void addProduct(FmProduct vo) {
		freeMarkHibernateDao.saveFmProduct(vo);
	}
	public void delProduct(FmProduct vo){
		freeMarkHibernateDao.delFmProduct(vo);
	}

	public void updateProduct(FmProduct product, MultipartFile file,
			HttpServletRequest request) throws IOException {
		FmProduct oldobj = freeMarkHibernateDao.queryProductById(product);
		String filename = file.getOriginalFilename();
		if (StringUtils.isNotBlank(filename)) {
			String filepath = request.getSession().getServletContext().getRealPath("/");
			String uploadfilepath = filepath + "/userfiles/images/upload";
			File tmpimamge = new File(uploadfilepath);
			if (!tmpimamge.exists()) {
				tmpimamge.mkdir();
			}
			OutputStream out = new BufferedOutputStream(new FileOutputStream(
					uploadfilepath + "/" + filename));
			IOUtils.copy(file.getInputStream(), out);

			OutputStream outto = new BufferedOutputStream(new FileOutputStream(
					cmsfrontpath + "/userfiles/images/upload/" + filename));
			IOUtils.copy(file.getInputStream(), outto);
			out.flush();
			out.close();
		}
		if(null != filename){
			oldobj.setProductImageUrl("/newcms/userfiles/images/upload/" + filename);
		}
		oldobj.setContent(product.getContent());
		oldobj.setProductName(product.getProductName());
		oldobj.setProductDesc(product.getProductDesc());
		oldobj.setGmtModify(new Date());

		freeMarkHibernateDao.updateFmProduct(oldobj);
	}

	public FmProduct queryProduct(FmProduct vo) {
		return freeMarkHibernateDao.queryProductById(vo);
	}
	
	public String queryProductByIdJson(FmProduct vo) throws JsonGenerationException, JsonMappingException, IOException{
		FmProduct tmp = freeMarkHibernateDao.queryProductById(vo);
		tmp.setContentStr(IOUtils.toString(tmp.getContent(), "GBK"));
		String str = new JsonStructureDataHelp(tmp).getObjectToJsonString();
		return str;
	}

	public List queryProductList(FmProduct vo) {

		List list = freeMarkHibernateDao.getFmProductListByCatalogId(vo);

		return list;
	}

	public String queryProductListForJson(FmProduct vo)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<FmProduct> list = freeMarkHibernateDao
				.getFmProductListByCatalogId(vo);
		String jsonTreeData = new JsonStructureDataHelp(list).getJsonByList();
		return jsonTreeData;
	}

}
