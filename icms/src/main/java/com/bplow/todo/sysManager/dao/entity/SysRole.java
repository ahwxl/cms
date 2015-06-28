package com.bplow.todo.sysManager.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_ROLE")
public class SysRole {
	
	private String roleId;
	private String roleName;
	private String deptId;
	private String roleType;
	private String remark;
	private String locked;
	
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setLocked(String locked) {
		this.locked = locked;
	}
	@Id
	@Column(name="ROLEID")
	public String getRoleId() {
		return roleId;
	}
	@Column(name="ROLENAME")
	public String getRoleName() {
		return roleName;
	}
	@Column(name="DEPTID")
	public String getDeptId() {
		return deptId;
	}
	@Column(name="ROLETYPE")
	public String getRoleType() {
		return roleType;
	}
	@Column(name="REMARK")
	public String getRemark() {
		return remark;
	}
	@Column(name="LOCKED")
	public String getLocked() {
		return locked;
	}
	
	
	
	

}
