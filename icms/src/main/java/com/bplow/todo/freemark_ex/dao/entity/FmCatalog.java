package com.bplow.todo.freemark_ex.dao.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FM_CATALOG")
public class FmCatalog {
	
	private String catalogId;
	private String catalogName;
	private String catalogDesc;
	private String secondName;
	private String imageSrc;
	private int orderId;
	private Date operateDate;
	private String isDeleteFlag;
	private String catalogType;
	private String pCatalogId;
	
	
	public FmCatalog() {
		super();
	}
	
	


	public FmCatalog(String catalogId) {
		super();
		this.pCatalogId = catalogId;
	}




	@Id
	public String getCatalogId() {
		return catalogId;
	}


	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

    @Column(name="catalog_name")
	public String getCatalogName() {
		return catalogName;
	}


	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	@Column(name="catalog_desc")
	public String getCatalogDesc() {
		return catalogDesc;
	}


	public void setCatalogDesc(String catalogDesc) {
		this.catalogDesc = catalogDesc;
	}

	@Column(name="second_name")
	public String getSecondName() {
		return secondName;
	}


	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	@Column(name="image_src")
	public String getImageSrc() {
		return imageSrc;
	}


	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}

	@Column(name="order_id")
	public int getOrderId() {
		return orderId;
	}


	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	@Column(name="operate_date")
	public Date getOperateDate() {
		return operateDate;
	}


	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	@Column(name="is_delete_flag")
	public String getIsDeleteFlag() {
		return isDeleteFlag;
	}


	public void setIsDeleteFlag(String isDeleteFlag) {
		this.isDeleteFlag = isDeleteFlag;
	}

	@Column(name="catalog_type")
	public String getCatalogType() {
		return catalogType;
	}


	public void setCatalogType(String catalogType) {
		this.catalogType = catalogType;
	}

	@Column(name="parent_catalog_id")
	public String getpCatalogId() {
		return pCatalogId;
	}


	public void setpCatalogId(String pCatalogId) {
		this.pCatalogId = pCatalogId;
	}
	
	
	
	

}
