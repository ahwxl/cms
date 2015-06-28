package com.bplow.todo.sysManager.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.bplow.look.bass.dao.SearchCriteria;

@Entity
@Table(name = "SYS_MENU")
public class SysMenu extends SearchCriteria{
	
	private String menuid;
	private String menuname;
	private String menuurl;
	private String moduleid;
	private String enabled;
	
	public SysMenu() {
		super();
	}

	public SysMenu(String menuid, String menuname, String menuurl,
			String moduleid, String enabled) {
		super();
		this.menuid = menuid;
		this.menuname = menuname;
		this.menuurl = menuurl;
		this.moduleid = moduleid;
		this.enabled = enabled;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	public void setMenuurl(String menuurl) {
		this.menuurl = menuurl;
	}

	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@Id
	@Column(name="MENUID")
	public String getMenuid() {
		return menuid;
	}
	
	@Column(name="MENUNAME")
	public String getMenuname() {
		return menuname;
	}

	@Column(name="MENUURL")
	public String getMenuurl() {
		return menuurl;
	}

	@Column(name="MODULEID")
	public String getModuleid() {
		return moduleid;
	}

	@Column(name="ENABLED")
	public String getEnabled() {
		return enabled;
	}

	
}
