package com.bplow.todo.sysManager.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.bplow.look.bass.dao.SearchCriteria;


/**
 * 系统组织机构表
 * @author www
 *
 */

@Entity
@Table(name = "sys_department")
public class SysDepartment extends SearchCriteria{
	
	private String departid;
	private String departname;
	private String departdesc;
	private String pdepartid;
	private int sortid;
	
	public SysDepartment(String departid, String departname, String departdesc,
			String pdepartid, int sortid) {
		super();
		this.departid = departid;
		this.departname = departname;
		this.departdesc = departdesc;
		this.pdepartid = pdepartid;
		this.sortid = sortid;
	}

	public SysDepartment() {
		super();
	}

	public void setDepartid(String departid) {
		this.departid = departid;
	}

	public void setDepartname(String departname) {
		this.departname = departname;
	}

	public void setDepartdesc(String departdesc) {
		this.departdesc = departdesc;
	}

	public void setPdepartid(String pdepartid) {
		this.pdepartid = pdepartid;
	}

	public void setSortid(int sortid) {
		this.sortid = sortid;
	}

	@Id
	@Column(name="departid")
	public String getDepartid() {
		return departid;
	}

	@Column(name="departname")
	public String getDepartname() {
		return departname;
	}

	@Column(name="departdesc")
	public String getDepartdesc() {
		return departdesc;
	}

	@Column(name="pdepartid")
	public String getPdepartid() {
		return pdepartid;
	}

	@Column(name="sortid")
	public int getSortid() {
		return sortid;
	}
	
	
	
	

}
