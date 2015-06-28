package com.bplow.base.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bplow.base.dao.IBaseJdbcDao;
import com.bplow.base.dbsource.DbSourceMap;
import com.bplow.base.util.JdbcHelper;


@Repository("BaseJdbcDao2")
public class BaseJdbcDaoImpl extends JdbcTemplate implements IBaseJdbcDao {


	
	@Resource(name = "multiJdbcDataSourceMap")
	public void setSuperSessionFactory(DbSourceMap dsMap) {
		super.setDataSource(dsMap);
	}

	/**
	 * 执行存储过程
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String execProc(final String spName, final List params,
			final List returnType) {
		Object obj = execute(new ConnectionCallback() {
			public Object doInConnection(Connection conn) throws SQLException,
					DataAccessException {
				conn.setAutoCommit(true);
				StringBuffer paramsStrBuf = new StringBuffer();
				String paramsStr = new String();
				for (@SuppressWarnings("unused")
				Object obj : params.toArray()) {
					paramsStrBuf.append("?").append(",");
				}
				for (@SuppressWarnings("unused")
				Object obj : returnType.toArray()) {
					paramsStrBuf.append("?").append(",");
				}

				if (paramsStrBuf.length() > 0) {
					paramsStr = paramsStrBuf.toString().substring(0,
							(paramsStrBuf.length() - 1));
				}

				CallableStatement cstmt = conn.prepareCall("{call " + spName
						+ " ( " + paramsStr + " ) }");

				for (int pi = 1; pi <= params.size(); pi++) {
					cstmt.setObject(pi, params.get((pi - 1)));
				}
				for (int ri = 1; ri <= returnType.size(); ri++) {
					cstmt.registerOutParameter(params.size() + ri,
							((Integer) returnType.get((ri - 1))).intValue());

				}
				cstmt.execute();
				StringBuffer out = new StringBuffer();
				if (params.size() > 0 && returnType.size() != 0) {
					for (int ri = 1; ri <= returnType.size(); ri++) {
						out = out.append(cstmt.getObject((params.size() + ri)))
								.append("#");
					}

				} else if (params.size() == 0 && returnType.size() != 0) {
					out = out.append(cstmt.getObject(1));
				}
				cstmt.close();
				conn.setAutoCommit(false);
				return out.toString();
			}
		});
		if (obj != null) {
			return String.valueOf(obj);
		}
		return null;

	}

	public void execSql(String sql) {
		execute(sql);

	}

	public void execSql(String sql, Object ... params) {
		super.update(sql, params);
	}

	@SuppressWarnings({ "rawtypes" })
	public List query(String sql) {
		return super.queryForList(sql);
	}





	@SuppressWarnings("rawtypes")
	public List query(String sql,Object... params) {
		return super.queryForList(sql, params);
	}

	@SuppressWarnings("rawtypes")
	public int queryNum(String sql) {
		StringBuffer newSql = new StringBuffer();
		newSql.append(" select count(1) as rowCount from (" + sql + ") ");
		@SuppressWarnings("unchecked")
		List l = super.query(newSql.toString(), new RowMapper() {
			public Object mapRow(ResultSet rs, int i) throws SQLException {
				return rs.getString(1);
			}
		});
		return Integer.parseInt(l.get(0).toString());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int queryNum(String sql, Object ... params) {
		StringBuffer newSql = new StringBuffer();
		newSql.append(" select count(1) as rowCount from (" + sql + ") ");
		List l = super.query(newSql.toString(), params, new RowMapper() {
			public Object mapRow(ResultSet rs, int i) throws SQLException {
				return rs.getString(1);
			}
		});
		return Integer.parseInt(l.get(0).toString());
	}

	public List<?> query(String sql, Class<?> type) {
		return super.query(sql,JdbcHelper.getRowMapperObject(type));
	}

	public  List<?> query(String sql, @SuppressWarnings("rawtypes") Class type, Object... params) {
		return super.query(sql,JdbcHelper.getRowMapperObject(type),params);
	}

	public List<?> queryMap(String sql) {
		return super.query(sql, JdbcHelper.getRowMapperMap());
	}

	public List<?> queryMap(String sql, Object... params) {
		return super.query(sql, JdbcHelper.getRowMapperMap(),params);
	}

	public String getPageSql(String sql, int startPage, int endPage) {
		StringBuilder sb = new StringBuilder();
		sb.append("select tt.* from (select t.*, rownum rw from ( ");
		sb.append(sql);
		sb.append(") t) tt where tt.rw>"+startPage+" and tt.rw<="+endPage+" ");
		return sb.toString();
		
	}
}
