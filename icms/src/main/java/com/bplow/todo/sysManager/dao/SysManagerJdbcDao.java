package com.bplow.todo.sysManager.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.lob.LobCreator;

import java.sql.PreparedStatement;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.orm.ObjectRetrievalFailureException;

import com.bplow.look.bass.BaseJdbcDaoSupport;
import com.bplow.look.bass.IPagination;
import com.bplow.look.bass.dao.usertype.SQLEntity;
import com.bplow.todo.freemark_ex.dao.entity.TbFreemarkInfo;
import com.bplow.todo.sysManager.dao.entity.SysUser;

public class SysManagerJdbcDao extends BaseJdbcDaoSupport{
	@Autowired
	private LobHandler defaultLobHandler;
	
	/**
	 * 分页
	 * 用户列表
	 */
	public IPagination queryForUsersPagination(SysUser po, int firstResult, int maxResults){

		SQLEntity sqlEntity = new SQLEntity();
		sqlEntity.append(" select b.loginname,b.userid,b.username,b.locked,b.enabled,b.sex,b.remark,b.password,a. departid,a.departname from newcms.sys_department as a inner  join " +
				"newcms.users as b on a.departid=b.deptid and b.enabled=1 ");
	    if(StringUtils.isNotEmpty(po.getLoginName())){
	    	sqlEntity.append(" and b.loginname like ?",new String("%"+po.getLoginName()+"%"));
	    }
	    if(StringUtils.isNotEmpty(po.getDeptId())){
	    	sqlEntity.append(" and a.departid = ?",new String(po.getDeptId()));
	    }
		System.out.println("模板列表查看sql:"+sqlEntity.toString());
		return this.queryForPaginationByMySql(sqlEntity.toString(), null, new RowMapper() {
		    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	SysUser vo = new SysUser();
		    	vo.setLoginName(rs.getString("loginname"));
		    	vo.setUserId(rs.getString("userid"));
		    	vo.setUserName(rs.getString("username"));
		    	vo.setLocked(rs.getString("locked"));
		    	vo.setEnabled(rs.getString("enabled"));
		    	vo.setRemark(rs.getString("remark"));
		    	vo.setSex(rs.getString("sex"));
		    	vo.setDeptId(rs.getString("departid"));
		    	vo.setDeptName(rs.getString("departname"));
		    	vo.setUserPwd(rs.getString("password"));
			    return vo;
		    }
		}, firstResult, maxResults);
	}

	/**
	 * 保存用户帐号到authorities表中
	 * @param obj
	 * @throws SQLException 
	 * @throws CannotGetJdbcConnectionException 
	 */
	public void saveUserAuthorities(final String username, final String authority) throws CannotGetJdbcConnectionException, SQLException{
		String sql = "insert into newcms.authorities (username,authority) values(?,?)";
		getJdbcTemplate().execute(sql, new AbstractLobCreatingPreparedStatementCallback(this.lobHandler) {
			protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException {
				ps.setString(1, username);
				ps.setString(2, authority);
			
			}
		});
	}
	/**
	 * 获取一个用户
	 * @param obj
	 */
	public SysUser getSysUser(SysUser sysUser){
		SysUser sysUsernew = null;
		String sql = " select a.username,a.password,a.enabled,a.userid," +
				"a.sex,a.deptid,a.locked,a.remark,a.usertype,a.loginname from newcms.users a  where a.username='"+sysUser.getUserName()+"'";
    	System.out.println(sql);
		try {
			sysUsernew =  (SysUser)this.getJdbcTemplate().queryForObject(sql, new RowMapper() {
		    public SysUser mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	SysUser vo = new SysUser();
		    	vo.setDeptId(rs.getString("deptid"));
		    	vo.setEnabled(rs.getString("enabled"));
		    	vo.setLocked(rs.getString("locked"));
		    	vo.setLoginName(rs.getString("loginname"));
		    	vo.setRemark(rs.getString("remark"));
		    	vo.setSex(rs.getString("sex"));
		    	vo.setUserId(rs.getString("userid"));
		    	vo.setUserName(rs.getString("username"));
		    	vo.setUserPwd(rs.getString("password"));
		    	vo.setUserType(rs.getString("usertype"));
			    return vo;
		    }});
		} catch (EmptyResultDataAccessException e) {
			throw new ObjectRetrievalFailureException(SysUser.class,null);
		}
		return sysUsernew;
	}
	

}
