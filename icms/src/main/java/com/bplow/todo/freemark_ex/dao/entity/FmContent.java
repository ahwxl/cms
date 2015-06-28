package com.bplow.todo.freemark_ex.dao.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class FmContent {
	private String id;
	private String cnt_caption;
	private String second_caption;
	private String content;
	private String operate_user_id;
	
	@DateTimeFormat(pattern="MM-dd-yyyy")
	private Date operate_date;
	
	private String is_delete_flag;
	private int  order_id;
	private int  click_num;
	private String catalog_id;
	
	
	public FmContent() {
		super();
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getCnt_caption() {
		return cnt_caption;
	}


	public void setCnt_caption(String cnt_caption) {
		this.cnt_caption = cnt_caption;
	}


	public String getSecond_caption() {
		return second_caption;
	}


	public void setSecond_caption(String second_caption) {
		this.second_caption = second_caption;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getOperate_user_id() {
		return operate_user_id;
	}


	public void setOperate_user_id(String operate_user_id) {
		this.operate_user_id = operate_user_id;
	}


	public Date getOperate_date() {
		return operate_date;
	}


	public void setOperate_date(Date operate_date) {
		this.operate_date = operate_date;
	}


	public String getIs_delete_flag() {
		return is_delete_flag;
	}


	public void setIs_delete_flag(String is_delete_flag) {
		this.is_delete_flag = is_delete_flag;
	}


	public int getOrder_id() {
		return order_id;
	}


	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}


	public int getClick_num() {
		return click_num;
	}


	public void setClick_num(int click_num) {
		this.click_num = click_num;
	}


	public String getCatalog_id() {
		return catalog_id;
	}


	public void setCatalog_id(String catalog_id) {
		this.catalog_id = catalog_id;
	}
	
	
	
	
	

}
