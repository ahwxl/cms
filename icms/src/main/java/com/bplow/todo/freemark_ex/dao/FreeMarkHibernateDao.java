package com.bplow.todo.freemark_ex.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import sun.misc.UUDecoder;

import com.bplow.look.bass.IPagination;
import com.bplow.look.bass.dao.HibernateDao;
import com.bplow.todo.freemark_ex.dao.entity.FmCatalog;
import com.bplow.todo.freemark_ex.dao.entity.FmProduct;
import com.bplow.todo.freemark_ex.dao.entity.SysDicParamter;
import com.bplow.todo.freemark_ex.dao.entity.TbFreemarkInfo;
import com.bplow.todo.sysManager.dao.entity.SysDepartment;
import com.bplow.todo.sysManager.dao.entity.SysRole;
import com.bplow.todo.sysManager.dao.entity.SysUser;

public class FreeMarkHibernateDao extends HibernateDao{
	
	/**
	 * 保存模板信息
	 * @param vo
	 */
	public void SaveFreemarkTmp(TbFreemarkInfo vo){
		this.getSession().save(vo);
	}
	
	public void saveFmCatalog(FmCatalog vo){
		this.getSession().save(vo);
	}
	
	public void editorFmCatalog(FmCatalog vo){
		this.getSession().update(vo);
	}
	
	public void delFmCatalog(FmCatalog vo){
		this.getSession().delete(vo);
	}
	
	public FmCatalog getFmCatalog(FmCatalog vo){
		return (FmCatalog)this.getSession().get(FmCatalog.class, vo.getCatalogId());
	}
	
	/**
	 * 获取父目录下得所有子目录
	 */
	public List getFmCatalogList(FmCatalog vo){
		
		List list = this.getSession().createQuery(" from FmCatalog a where a.pCatalogId = :parentCatalogID order by a.orderId ").
				setString("parentCatalogID", vo.getpCatalogId()).list();
		
		return list;
	}
    /**
     * 获取某目录的父目录的同级目录
     */
	public List getParentFmCatalogList(FmCatalog vo){
		
		List list = this.getSession().createQuery(" from FmCatalog a where a.pCatalogId = (select a.pCatalogId from FmCatalog a where a.catalogId = :catalogId ) ").
				setString("catalogId", vo.getpCatalogId()).list();
		
		return list;
	}
	
	//--------------------------------产品管理-------------------------------------------
	public void saveFmProduct(FmProduct vo){
		vo.setGmtCreate(new Date());
		vo.setGmtModify(new Date());
		vo.setIsShow('1');
		this.getSession().save(vo);
	}
	public void updateFmProduct(FmProduct vo){
		this.getSession().update(vo);
	}
	public void delFmProduct(FmProduct vo){
		this.getSession().delete(vo);
	}
	public FmProduct queryProductById(FmProduct vo){
		return (FmProduct)this.findUnique(" From FmProduct a where a.productId = ? ", vo.getProductId());
	}
	/**
	 * 查询目录下所有产品
	 * @param vo
	 * @return
	 */
	public List getFmProductListByCatalogId(FmProduct vo){
		List list = this.getSession().createQuery("from FmProduct a where a.catalogId = :catalogId").setString("catalogId", vo.getCatalogId()).list();
		
		return list;
	}
	
	
	//--------------------------------角色管理-------------------------------------------
	public void saveRole(SysRole vo){
		this.getSession().save(vo);
	}
	
	public void delRole(SysRole vo){
		this.getSession().delete(vo);
	}
	
	public void updateRole(SysRole vo){
		this.getSession().update(vo);
	}
	
	/**
	 * obtain all roles
	 */
	public List getAllRoles(SysRole vo){
		List list = null;
		if(StringUtils.isNotEmpty(vo.getRoleName())){
			list = this.getSession().createQuery("from SysRole a where a.roleName like :roleName").setString("roleName", "%"+vo.getRoleName()+"%").list();
		}else{
			list = this.getSession().createQuery("from SysRole ").list();
		}
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
		
        List list = this.getSession().createQuery("select new SysUser( a.userName,a.enabled) from SysUser a").list();
		
		return list;		
		
	}
	
	public IPagination getAllUsers(SysUser vo,int start,int limit){
		String hql = "select new SysUser( a.userName,a.enabled) from SysUser a";
		if(StringUtils.isNotEmpty(vo.getUserName())){
			hql += " where a.userName like '%" + vo.getUserName() + "%'";
		}
		IPagination ipagination = this.queryForPagination(hql, null, vo.getStart(), vo.getLimit());		
		return ipagination;
	}
	
	//--------------------------------组织机构管理-------------------------------------------
	/**
	 * 添加组织
	 * 
	 */
	public void saveDepartment(SysDepartment po){
		this.getSession().save(po);
	}
	public void delDepartment(SysDepartment po){
		this.getSession().delete(po);
	}
	public void updateDepartment(SysDepartment po){
		this.getSession().update(po);
	}
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
	
	public IPagination getDepartmentListPage(SysDepartment po){
		String hql = "select new SysDepartment( a.departid,a.departname,a.departdesc,a.pdepartid,a.sortid) from SysDepartment a where a.pdepartid ='"+po.getPdepartid()+"'";
		IPagination ipagination = this.queryForPagination(hql, null, po.getStart(), po.getLimit());		
		return ipagination;
	}
	
	//----------------------------------系统参数字典------------------------------------
	public void saveSysDicPara(SysDicParamter vo){
		this.getSession().save(vo);
	}
	
	public void delSysDicPara(SysDicParamter vo){
		this.getSession().delete(vo);
	}
	
	public void editorSysDicPara(SysDicParamter vo){
		this.getSession().update(vo);
	}
	
	public SysDicParamter queryDicById(SysDicParamter vo){
		return (SysDicParamter)this.getSession().get(vo.getClass(), vo.getId());
	}


	/**
	 * 根据分组查询 参数列表
	 * @param vo
	 * @return
	 */
	public List queryDicParaList(SysDicParamter vo){
		return this.getSession().createQuery("from SysDicParamter a where a.PGroup =:PGroup").
				setString("PGroup", vo.getPGroup() ).list();
	}
	

}
