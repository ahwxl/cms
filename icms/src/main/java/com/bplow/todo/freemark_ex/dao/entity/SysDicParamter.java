package com.bplow.todo.freemark_ex.dao.entity;
// default package
// Generated 2015-9-13 16:28:00 by Hibernate Tools 4.0.0

/**
 * SysDicParamter generated by hbm2java
 */
public class SysDicParamter implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4692312060355039720L;
	private Integer id;
	private String PName;
	private String PGroup;
	private String PDesc;
	private String PValue;
	private Integer POrder;
	
	private String idArray;

	public SysDicParamter() {
	}

	public SysDicParamter(String PName, String PGroup, String PDesc,
			String PValue, Integer POrder) {
		this.PName = PName;
		this.PGroup = PGroup;
		this.PDesc = PDesc;
		this.PValue = PValue;
		this.POrder = POrder;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPName() {
		return this.PName;
	}

	public void setPName(String PName) {
		this.PName = PName;
	}

	public String getPGroup() {
		return this.PGroup;
	}

	public void setPGroup(String PGroup) {
		this.PGroup = PGroup;
	}

	public String getPDesc() {
		return this.PDesc;
	}

	public void setPDesc(String PDesc) {
		this.PDesc = PDesc;
	}

	public String getPValue() {
		return this.PValue;
	}

	public void setPValue(String PValue) {
		this.PValue = PValue;
	}

	public Integer getPOrder() {
		return this.POrder;
	}

	public void setPOrder(Integer POrder) {
		this.POrder = POrder;
	}

	public String getIdArray() {
		return idArray;
	}

	public void setIdArray(String idArray) {
		this.idArray = idArray;
	}
	

}
