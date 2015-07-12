package com.bplow.todo.freemark_ex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bplow.todo.freemark_ex.dao.FreeMarkHibernateDao;
import com.bplow.todo.freemark_ex.dao.FreeMarkJdbcDao;
import com.bplow.todo.freemark_ex.dao.entity.FmProduct;

@Service
@Transactional
public class ProductService {
	
	@Autowired
	public FreeMarkJdbcDao freeMarkJdbcDao;
	@Autowired
	public FreeMarkHibernateDao freeMarkHibernateDao;
	
	ThreadLocal<Integer> tl = new ThreadLocal();
	
	public void addProduct(FmProduct vo){
		freeMarkHibernateDao.saveFmProduct(vo);
	}
	
	public void updateProduct(FmProduct vo){
		freeMarkHibernateDao.updateFmProduct(vo);
	}
	
	public FmProduct queryProduct(FmProduct vo){
		return freeMarkHibernateDao.queryProductById(vo);
	}
	
	public List queryProductList(FmProduct vo){
		
		List list = freeMarkHibernateDao.getFmProductListByCatalogId(vo);
		
		return list;
	}

}
