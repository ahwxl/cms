package com.bplow.todo.sysManager.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bplow.look.bass.IPagination;
import com.bplow.look.bass.dao.HibernateDao;
import com.bplow.look.bass.dao.usertype.SQLEntity;
import com.bplow.todo.freemark_ex.dao.entity.FmCatalog;
import com.bplow.todo.sysManager.dao.entity.SysDepartment;
import com.bplow.todo.sysManager.dao.entity.SysMenu;
import com.bplow.todo.sysManager.dao.entity.SysRole;
import com.bplow.todo.sysManager.dao.entity.SysModule;
import com.bplow.todo.sysManager.dao.entity.SysUser;

public class SysManagerHibernateDao extends HibernateDao{
	
	
	public void delSysMenu(SysMenu vo){
		this.getSession().delete(vo);
	}
	
	public SysMenu getSysMenu(SysMenu vo){
		return (SysMenu)this.getSession().get(SysMenu.class, vo.getMenuid());
	}
	
	public void editorSysMenu(SysMenu vo){
		this.getSession().update(vo);
	}
	
	public IPagination getModuleListPage(SysModule mo){
		String hql = "select new SysModule( a.moduleid,a.modulename,a.moduletype,a.pmoduleid,a.sortid) from SysModule a where a.pmoduleid ='"+mo.getPmoduleid()+"'";
		IPagination ipagination = this.queryForPagination(hql, null, mo.getStart(), mo.getLimit());		
		return ipagination;
	}

	public void delSysModule(SysModule vo){
		this.getSession().delete(vo);
	}
	
	public SysModule getSysModule(SysModule vo){
		return (SysModule)this.getSession().get(SysModule.class, vo.getModuleid());
	}
	
	public void editorSysModule(SysModule vo){
		this.getSession().update(vo);
	}
	
	public void saveSysModule1(SysModule vo){
		this.getSession().save(vo);
	}
	/**
	 * 添加组织结构
	 * 
	 */
	public void saveDepartment(SysDepartment po){
		this.getSession().save(po);
	}
	/**
	 * 删除组织结构
	 * 
	 */
	public void delDepartment(SysDepartment po){
		this.getSession().delete(po);
	}
	/**
	 * 修改组织结构
	 * 
	 */
	public void updateDepartment(SysDepartment po){
		this.getSession().update(po);
	}
	/**
	 * 获取组织结构
	 * 
	 */
	public SysDepartment getDepartmentById(SysDepartment po){
		
	    return (SysDepartment)this.getSession().get(SysDepartment.class, po.getDepartid());
	}
	/**
	 * 查询组织机构列表   父编码
	 * @param po
	 * @return
	 */
	public List getDepartmentListByPid(SysDepartment po){
		return this.getSession().createQuery("from SysDepartment a where a.pdepartid =:pdepartid").
				setString("pdepartid", po.getPdepartid()).list();
	}
	/**
	 * 查询组织机构列表   分页
	 * @param po
	 * @return
	 */
	public IPagination getDepartmentListPage(SysDepartment po){
	    SQLEntity sqlEntity = new SQLEntity();
	    sqlEntity.append("select new SysDepartment( a.departid,a.departname,a.departdesc,a.pdepartid,a.sortid) from SysDepartment a where 1=1 ");
	    if(StringUtils.isNotEmpty(po.getPdepartid())){
	    	sqlEntity.append(" and a.pdepartid = ?",new String(po.getPdepartid()));
	    }
	    if(StringUtils.isNotEmpty(po.getDepartname())){
	    	sqlEntity.append(" and a.departname like ?",new String("%"+po.getDepartname()+"%"));
	    }
	    System.out.print(sqlEntity.toString());
		IPagination ipagination = this.queryForPagination(sqlEntity.toString(), null, po.getStart(), po.getLimit());		
		return ipagination;
	}
//--------------------菜单管理------------------------------
	
	public void saveSysMenu(SysMenu vo){
		this.getSession().save(vo);
	}
	
	
	public IPagination getAllSysMenu(SysMenu vo,int start,int limit){
		String hql = "select new SysMenu( a.menuid,a.menuname,a.menuurl,a.moduleid,a.enabled) from SysMenu a";
		IPagination ipagination = this.queryForPagination(hql, null, vo.getStart(), vo.getLimit());		
		return ipagination;
	}
	/**
	 * 批量删除组织机构数据
	 * @param vo
	 */
	public void delDepartmentList(SysDepartment po ){
	    if(StringUtils.isNotEmpty(po.getDepartid())){
	    	this.getSession().createQuery("delete SysDepartment a where a.departid in ("+po.getDepartid()+") ").executeUpdate();
	    }
		
	}
	
	//--------------------------------角色管理-------------------------------------------
	public void saveRole(SysRole vo){
		this.getSession().save(vo);
	}
	
	public void delRole(SysRole vo){
		this.getSession().delete(vo);
	}
	
	public SysRole updateRole(SysRole vo){
		this.getSession().update(vo);
		return vo;
	}
	
	/**
	 * obtain all roles
	 */
	public List getAllRoles(SysRole vo){
		List list = this.getSession().createQuery("from SysRole ").list();
		
		return list;
	}
	
	//get a role by id
	public SysRole getRoleById(SysRole vo){
		
		SysRole sysRole = (SysRole)this.getSession().createQuery("from SysRole a where a.roleId = :roleId")
				.setString("roleId", vo.getRoleId()).uniqueResult();
		
		return sysRole;
	}
	
	//--------------------------------用户管理-------------------------------------------
	/**
	 * obtain all users
	 */
	public List getAllUsers(SysUser vo){
		
        List list = this.getSession().createQuery(" from SysUser a where a.locked='1' and a.enabled='1' ").list();
		
		return list;	
	}
		
//----------------模块管理------------------------------------
	public List getModuleListByPid(SysModule mo){
		return this.getSession().createQuery("from SysModule a where a.pmoduleid =:pmoduleid").
				setString("pmoduleid", mo.getPmoduleid()).list();
	}
	public IPagination getAllUsers(SysUser vo,int start,int limit){
		String hql = "from SysUser a where a.locked='1' and a.enabled='1'";
		IPagination ipagination = this.queryForPagination(hql, null, vo.getStart(), vo.getLimit());		
		return ipagination;
	}
	/**
	 * 添加用户
	 * 
	 */
	public void saveSysUser(SysUser po){
		this.getSession().save(po);
	}
	/**
	 * 删除用户
	 * 
	 */
	public void delSysUser(SysUser po){
		this.getSession().delete(po);
	}
	/**
	 * 修改用户
	 * 
	 */
	public void updateSysUser(SysUser po){
		this.getSession().update(po);
	}
	/**
	 * 批量修改用户状态
	 * @param vo
	 */
	public void updateSysUserPwd(SysUser po ){
	    if(StringUtils.isNotEmpty(po.getUserId())){
	    	this.getSession().createQuery("update SysUser a set a.userPwd='"+po.getUserPwd()+"' where a.userId in ("+po.getUserId()+") ").executeUpdate();
	    }
		
	}
	/**
	 * 批量修改用户状态
	 * @param vo
	 */
	public void updateSysUserEnabled(SysUser po ){
	    if(StringUtils.isNotEmpty(po.getUserId())){
	    	this.getSession().createQuery("update SysUser a set a.enabled='2' where a.userId in ("+po.getUserId()+") ").executeUpdate();
	    }
		
	}
	/**
	 * 获取用户对象
	 * 
	 */
	public SysUser getSysUser(SysUser po){
		
	    return (SysUser)this.getSession().get(SysUser.class, po.getUserName());
	}
	
}
