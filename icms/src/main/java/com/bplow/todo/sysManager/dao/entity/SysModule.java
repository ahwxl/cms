package com.bplow.todo.sysManager.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.bplow.look.bass.dao.SearchCriteria;

@Entity
@Table(name = "SYS_MODULE")
public class SysModule extends SearchCriteria{
	
	private String moduleid;
	private String modulename;
	private String moduletype;
	private String pmoduleid;
	private int sortid;
	
	public SysModule() {
		super();
	}

	public SysModule(String moduleid, String modulename, String moduletype,
			String pmoduleid, int sortid) {
		super();
		this.moduleid = moduleid;
		this.modulename = modulename;
		this.moduletype = moduletype;
		this.pmoduleid = pmoduleid;
		this.sortid = sortid;
	}
	
	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}

	public void setModulename(String modulename) {
		this.modulename = modulename;
	}

	public void setModuletype(String moduletype) {
		this.moduletype = moduletype;
	}

	public void setPmoduleid(String pmoduleid) {
		this.pmoduleid = pmoduleid;
	}

	public void setSortid(int sortid) {
		this.sortid = sortid;
	}

	@Id
	@Column(name="MODULEID")
	public String getModuleid() {
		return moduleid;
	}

	@Column(name="MODULENAME")
	public String getModulename() {
		return modulename;
	}

	@Column(name="MODULETYPE")
	public String getModuletype() {
		return moduletype;
	}

	@Column(name="PMODULEID")
	public String getPmoduleid() {
		return pmoduleid;
	}

	@Column(name="SORTID")
	public int getSortid() {
		return sortid;
	}

}
