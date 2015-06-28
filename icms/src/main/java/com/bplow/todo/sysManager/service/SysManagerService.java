package com.bplow.todo.sysManager.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.List;
import java.util.UUID;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.UUID;




import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.transaction.annotation.Transactional;

import com.bplow.look.bass.IPagination;
import com.bplow.look.bass.utils.JsonStructureDataHelp;
import com.bplow.todo.sysManager.Util.MD5Util;
import com.bplow.todo.sysManager.dao.SysManagerHibernateDao;
import com.bplow.look.bass.utils.JsonStructureDataHelp;
import com.bplow.look.bass.utils.JsonStructureDataHelp;
import com.bplow.todo.freemark_ex.dao.entity.FmCatalog;
import com.bplow.todo.sysManager.dao.SysManagerJdbcDao;
import com.bplow.todo.sysManager.dao.entity.SysDepartment;
import com.bplow.todo.sysManager.dao.entity.SysUser;

import org.springframework.transaction.annotation.Transactional;

import com.bplow.look.bass.IPagination;
import com.bplow.todo.sysManager.dao.SysManagerJdbcDao;
import com.bplow.todo.sysManager.dao.SysManagerHibernateDao;
import com.bplow.todo.sysManager.dao.entity.SysDepartment;
import com.bplow.todo.sysManager.dao.entity.SysMenu;
import com.bplow.todo.sysManager.dao.entity.SysRole;
import com.bplow.todo.sysManager.dao.entity.SysModule;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;


@Transactional
public class SysManagerService {
	@Autowired
	SysManagerHibernateDao sysManagerHibernateDao;
	@Autowired
	SysManagerJdbcDao sysManagerJdbcDao;
    //---------------------------------组织机构管理-------------------------------------------------
    public void saveDepartment(SysDepartment vo){
    	vo.setDepartid(UUID.randomUUID().toString().replace("-", ""));
    	sysManagerHibernateDao.saveDepartment(vo);
    }
	/**
	 * 删除组织结构
	 * 
	 */
    public void delDepartment(SysDepartment vo){
    	sysManagerHibernateDao.delDepartment(vo);
    }
	/**
	 * 修改组织结构
	 * 
	 */
    public void updateDepartment(SysDepartment vo){
    	sysManagerHibernateDao.updateDepartment(vo);
    }
    
    /**
     * 组织结构
     * 返回 tree
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    public String getDepartmentListTreeJson(SysDepartment vo) throws Exception{
    	List list = sysManagerHibernateDao.getDepartmentListByPid(vo);
    	String jsonTreeData = new JsonStructureDataHelp(list).getJsonTreeByList().replace("departid", "id").replace("departname", "name")
    			.replace("sortid", "isParent");
    	
    	return jsonTreeData;
    }
    
    /**
     * 组织结构
     * 返回 grid
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    public String getDepartmentListGridJson(SysDepartment vo) throws Exception{
    	List list = sysManagerHibernateDao.getDepartmentListByPid(vo);
    	String jsonTreeData = new JsonStructureDataHelp(list).getJsonByList();
    	
    	return jsonTreeData;
    }
    
	/**
	 * 查询组织机构列表   分页
	 * @param po
	 * @return
	 */
    public String getDepartmentListPage(SysDepartment vo)throws Exception{
    	
    	IPagination ipagination = sysManagerHibernateDao.getDepartmentListPage(vo);
    	
    	return ipagination.getJsonByList();
    }

	public String getAllSysMenuJson(SysMenu vo)throws Exception{
	    	
	    	IPagination ipagination = sysManagerHibernateDao.getAllSysMenu(vo,vo.getStart(),vo.getLimit());
	   	 
	   	 
	   	    String jsonGridData = ipagination.getJsonByList();
	    	
	    	return jsonGridData;

    }
	/**
	 * 批量删除组织机构数据
	 * @param vo
	 */
	public void delDepartmentList(SysDepartment po ){
		sysManagerHibernateDao.delDepartmentList(po);
	}
	/**
	 * obtain all users
	 */
	public List getAllUsers(SysUser vo){
		
		return sysManagerHibernateDao.getAllUsers(vo);
			
		
	}
	public String getAllUsersPage(SysUser vo) throws JsonGenerationException, Exception, IOException{


		IPagination ipagination = sysManagerHibernateDao.getAllUsers(vo, vo.getStart(), vo.getLimit());
		String jsonGridData = ipagination.getJsonByList();
		return jsonGridData;
	}
	/**
	 * 分页
	 * 用户列表
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public String queryForUsersPagination(SysUser po) throws JsonGenerationException, JsonMappingException, IOException{
		IPagination ipagination = sysManagerJdbcDao.queryForUsersPagination(po, po.getStart(), po.getLimit());
		String jsonGridData = ipagination.getJsonByList();
		return jsonGridData;
		
	}
	/**
	 * 添加用户
	 * 
	 */
	public void saveSysUser(SysUser po){
		po.setUserId(UUID.randomUUID().toString().replace("-", ""));
		po.setEnabled("1");
		sysManagerHibernateDao.saveSysUser(po);
	}
	/**
	 * 删除用户
	 * 
	 */
	public void delSysUser(SysUser po){
		sysManagerHibernateDao.delSysUser(po);
	}
	/**
	 * 修改用户
	 * 
	 */
	public void updateSysUser(SysUser po){
		sysManagerHibernateDao.updateSysUser(po);
	}
	/**
	 * 批量修改用户状态
	 * @param vo
	 */
	public void updateSysUserEnabled(SysUser po ){
	    if(StringUtils.isNotEmpty(po.getUserId())){
	    	po.setEnabled("2");//删除用户（用户失效）
	    	sysManagerHibernateDao.updateSysUserEnabled(po);
	    }
	}
		/**
		 * 重置用户密码
		 * @param vo
		 */
		public void setPwdSysUser(SysUser po ){
		    if(StringUtils.isNotEmpty(po.getUserId())){
		    	po.setUserPwd("e10adc3949ba59abbe56e057f20f883e");
		    	sysManagerHibernateDao.updateSysUserPwd(po);
		    }
	}

		/**
		 * 获取用户对象
		 * 
		 */
		public SysUser getSysUser(SysUser po){
			
		    return (SysUser)sysManagerJdbcDao.getSysUser(po);
		}
		/**
		 * 保存用户帐号到authorities表中
		 * @param obj
		 * @throws SQLException 
		 * @throws CannotGetJdbcConnectionException 
		 */
		public void saveUserAuthorities(final String username, final String authority) throws CannotGetJdbcConnectionException, SQLException{
			sysManagerJdbcDao.saveUserAuthorities(username, authority);
		}
	/**
     * 删除对象
     */
     public void delSysMenu(SysMenu vo){
    	 sysManagerHibernateDao.delSysMenu(vo);
	}
     
     public void editorSysMenu(SysMenu vo){
 		
    	 SysMenu tmpvo = sysManagerHibernateDao.getSysMenu(vo);
 		
 		tmpvo.setMenuname(vo.getMenuname());
 		tmpvo.setMenuurl(vo.getMenuurl());
 		tmpvo.setModuleid(vo.getModuleid());
 		tmpvo.setEnabled(vo.getEnabled());
 		sysManagerHibernateDao.editorSysMenu(tmpvo);
 		
 	}
     
     public SysMenu getSysMenuByIdToJson(SysMenu vo) throws Exception{
 		
    	 SysMenu tmpvo = sysManagerHibernateDao.getSysMenu(vo);
 		
 		return tmpvo;
 	}
     
     /**
      * 模块管理tree
      * 返回 tree
      */
     public String getModuleListTreeJson(SysModule vo) throws Exception{
     	List list = sysManagerHibernateDao.getModuleListByPid(vo);
     	String jsonTreeData = new JsonStructureDataHelp(list).getJsonTreeByList().replace("moduleid", "id").replace("modulename", "text")
     			.replace("sortid", "cls");
     	
     	return jsonTreeData;
     }
     
     /**
      * page
      */
     public String getModuleListPage(SysModule vo)throws Exception{
     	
     	IPagination ipagination = sysManagerHibernateDao.getModuleListPage(vo);
     	
     	return ipagination.getJsonByList();
     }
     
     /**
      * 删除对象
      */
      public void delSysModule(SysModule vo){
     	 sysManagerHibernateDao.delSysModule(vo);
 	}
      
      
      public SysModule getSysModuleById(SysModule vo) throws Exception{
  		
    	  SysModule tmpvo = sysManagerHibernateDao.getSysModule(vo);
  		
  		return tmpvo;
  	}
      
      public void editorSysModule(SysModule vo){
   		
    	SysModule tmpvo = sysManagerHibernateDao.getSysModule(vo);
  		
  		tmpvo.setModulename(vo.getModulename());
  		tmpvo.setModuletype(vo.getModuletype());
  		tmpvo.setPmoduleid(vo.getPmoduleid());
  		tmpvo.setSortid(vo.getSortid());
  		sysManagerHibernateDao.editorSysModule(tmpvo);
  		
  	}
      
      public void saveSysModule(SysModule vo){
  		vo.setModuleid(UUID.randomUUID().toString().replace("-", ""));
  		sysManagerHibernateDao.saveSysModule1(vo);
  	}
    
  	
  	public void saveSysMenu(SysMenu vo){
  		vo.setMenuid(UUID.randomUUID().toString().replace("-", ""));
  		sysManagerHibernateDao.saveSysMenu(vo);
  	}
  	
  //------------------------------角色管理------------------------------------------------

//obtain all roles
 public String getAllRoleJson(SysRole vo) throws Exception{
	 
	 List list = sysManagerHibernateDao.getAllRoles(vo);
	 
	 
	 String jsonGridData = new JsonStructureDataHelp(list).getJsonByList();
	 
	 return jsonGridData;
 }
//添加角色
public void saveRole(SysRole vo){
	 vo.setRoleId(UUID.randomUUID().toString().replace("-", ""));
	 sysManagerHibernateDao.saveRole(vo);

}
//删除角色
public void delRole(SysRole vo){
	sysManagerHibernateDao.delRole(vo);
}
//更新角色
public void updateRole(SysRole vo){
	sysManagerHibernateDao.updateRole(vo);
}

}
