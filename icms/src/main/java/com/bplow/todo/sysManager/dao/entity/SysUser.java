package com.bplow.todo.sysManager.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.bplow.look.bass.dao.SearchCriteria;

@Entity
@Table(name = "USERS")
public class SysUser extends SearchCriteria{
	
	private String userId;   //用户编号
	private String userName; //登陆账号
	private String userPwd;  //登陆密码
	private String sex;      //性别
	private String deptId;   //所属部门
	private String locked;   //是否锁定
	private String remark;   //备注
	private String enabled;  //是否生效
	private String userType; //人员类型
	private String loginName; //登陆名字
	
	
	private String deptName; //所属部门
	private String newPwd; //新密码

	public SysUser(String userName, String enabled) {
		super();
		this.userName = userName;
		this.enabled = enabled;
	}


	public SysUser(String userId, String userName, String userPwd, String sex,
			String deptId, String locked, String remark, String enabled,
			String userType, String loginName,String deptName) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userPwd = userPwd;
		this.sex = sex;
		this.deptId = deptId;
		this.locked = locked;
		this.remark = remark;
		this.enabled = enabled;
		this.userType = userType;
		this.loginName = loginName;
		this.deptName = deptName;
	}

	public SysUser() {
		super();
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public void setLocked(String locked) {
		this.locked = locked;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	@Column(name="USERID")
	public String getUserId() {
		return userId;
	}

	@Id
	@Column(name="USERNAME")
	public String getUserName() {
		return userName;
	}

	@Column(name="PASSWORD")
	public String getUserPwd() {
		return userPwd;
	}
	@Column(name="sex")
	public String getSex() {
		return sex;
	}
	@Column(name="deptid")
	public String getDeptId() {
		return deptId;
	}
	@Column(name="locked")
	public String getLocked() {
		return locked;
	}
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}

	@Column(name="ENABLED")
	public String getEnabled() {
		return enabled;
	}
	@Column(name="usertype")
	public String getUserType() {
		return userType;
	}
	@Column(name="loginname")
	public String getLoginName() {
		return loginName;
	}
	@Column(insertable=false, updatable=false)
	public String getDeptName() {
	return deptName;
	}




	@Column(insertable=false, updatable=false)
	public String getNewPwd() {
		return newPwd;
	}


	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}


	
	

}
