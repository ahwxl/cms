package com.bplow.look.bass.autoCreate;

/**
 * 字段属性描述
 * @author wangxiaolei
 * @Date Nov 5, 2012
 */
public class FieldAttribute {
	
	public String fieldNo;
	public String fieldName;//
	public String fieldType;
	public String isNeedVerify;//是否需要验证 0不需要 1需要
	public Integer fieldLength;//字段长度
	
	
	public FieldAttribute() {
		super();
	}


	public FieldAttribute(String fieldNo, String fieldName, String fieldType,
			String isNeedVerify, Integer fieldLength) {
		super();
		this.fieldNo = fieldNo;
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.isNeedVerify = isNeedVerify;
		this.fieldLength = fieldLength;
	}


	public String getFieldNo() {
		return fieldNo;
	}


	public void setFieldNo(String fieldNo) {
		this.fieldNo = fieldNo;
	}


	public String getFieldName() {
		return fieldName;
	}


	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}


	public String getFieldType() {
		return fieldType;
	}


	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}


	public String getIsNeedVerify() {
		return isNeedVerify;
	}


	public void setIsNeedVerify(String isNeedVerify) {
		this.isNeedVerify = isNeedVerify;
	}


	public Integer getFieldLength() {
		return fieldLength;
	}


	public void setFieldLength(Integer fieldLength) {
		this.fieldLength = fieldLength;
	}
	
	

}
