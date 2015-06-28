package com.bplow.look.bass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.support.lob.LobHandler;

import com.bplow.look.bass.dao.usertype.HQLEntity;
import com.bplow.look.bass.dao.usertype.SQLEntity;

public class BaseJdbcDaoSupport extends SimpleJdbcDaoSupport{

	public LobHandler lobHandler;
	public SimpleJdbcCall procReadActor;
	public Log log = LogFactory.getLog(BaseJdbcDaoSupport.class);

	public LobHandler getLobHandler() {
		return lobHandler;
	}

	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}
	
	public IPagination queryForPagination(String sqlEntity, String sqlCountEntity, RowMapper rowMapper, int firstResult, 
			int maxResults)throws DataAccessException
	{
	    IPagination pagination = new SimplePagination(firstResult, maxResults);
	    
	    /**
	     * mysql 分页sql
	     */
	    //sqlEntity = sqlEntity + " limit "+firstResult +","+maxResults;
	    
	    /**
	     * ORACLE 分页
	     */
	    String sql_pre ="";
//	    if(sqlEntity.toUpperCase().indexOf("ORDER") < 0){
//	       sql_pre = " select * from ("+ StringUtils.substringBefore(sqlEntity.toUpperCase(), "FROM")+",rownum rownum_ from " +
//	       StringUtils.substringAfterLast( sqlEntity.toUpperCase(), "FROM") + " and rownum <= "+(firstResult+ maxResults)+") where rownum_ > "+firstResult;
//	    }else{
//	       sql_pre = " select * from ("+ StringUtils.substringBefore(sqlEntity.toUpperCase(), "FROM")+",rownum rownum_ from " +
//		   StringUtils.substringBefore(StringUtils.substringAfterLast( sqlEntity.toUpperCase(), "FROM"),"ORDER") + " and rownum <= "+(firstResult+ maxResults)+"   " +
//		   		"order "+StringUtils.substringAfterLast( sqlEntity.toUpperCase(), "ORDER")+") where rownum_ > "+firstResult;
//
//	    }
	    sql_pre =" select * from (select row_.*, rownum rownum_ from ("+sqlEntity+") row_  where rownum <= "+(firstResult+ maxResults)+")   where rownum_ > "+firstResult;
	    
	    
	    sqlCountEntity = " select count(1) from ("+ sqlEntity +") a";
	    
	    System.out.println("分页查询:"+sql_pre);
	    //System.out.println("分页查询:"+sqlCountEntity.toString());
	    pagination.setQuerySQL(sql_pre);
	    pagination.setQueryCountSQL(sqlCountEntity);
	    return fillPagination(pagination, rowMapper);
	}
	
	public IPagination queryForPaginationByMySql(String sqlEntity, String sqlCountEntity, RowMapper rowMapper, int firstResult, 
			int maxResults)throws DataAccessException
	{
	    IPagination pagination = new SimplePagination(firstResult, maxResults);
	    
	    
	    String sql_pre =sqlEntity +" limit "+firstResult+", "+ maxResults;
	    sqlCountEntity = " select count(1) from ("+ sqlEntity +") a";
	    
	    pagination.setQuerySQL(sql_pre);
	    pagination.setQueryCountSQL(sqlCountEntity);
	    return fillPagination(pagination, rowMapper);
	}
	public IPagination fillPagination(IPagination pagination, RowMapper rowMapper)throws DataAccessException
	{
	    //String queryResults = pagination.getQuerySQL();
	     /*
	     *MYSQL
	     *获取查询记录条数的sql
	     */
//	    if(null == pagination.getQueryCountSQL()){
//	    	queryCount = " SELECT COUNT(1) " + pagination.getQuerySQL().substring(pagination.getQuerySQL().toUpperCase().indexOf("FROM"),
//	    			pagination.getQuerySQL().toUpperCase().indexOf("LIMIT")-1);
//	    }else queryCount = pagination.getQueryCountSQL();
	    //queryCount = pagination.getQueryCountSQL();
	    
	    
	    
	    //System.out.println("结果集:"+queryResults);	    
	    /*
	     * 记录总条数
	     */
	    if(pagination.getQueryCountSQL() != null)
	        pagination.setAllCount(this.getJdbcTemplate().queryForInt(pagination.getQueryCountSQL()));

	    /**
	     * 数据集
	     */
	    pagination.setResults(this.getJdbcTemplate().query(pagination.getQuerySQL(), rowMapper));

	    pagination.setCurPageIndex(pagination.getCurPageIndex());
	    pagination.setAllPageCount(pagination.getAllPageCount());
	    return pagination;
	}
	
	public IPagination fillPagination(IPagination pagination, ParameterizedRowMapper rowMapper)throws DataAccessException
	{
	    String queryResults = pagination.getQuerySQL();
	    String queryCount = pagination.getQueryCountSQL();	    
	    if(queryCount != null)
	        pagination.setAllCount(this.getJdbcTemplate().queryForInt(queryCount));
	    
	    pagination.setResults(this.getSimpleJdbcTemplate().query(queryResults, rowMapper));
	    pagination.setCurPageIndex(pagination.getCurPageIndex());
	    pagination.setAllPageCount(pagination.getAllPageCount());
	    return pagination;
	}

	
	/**
	 * 获取序列的下一个值
	 */
	public Integer getSeqNextValue(String sequencesName){
		
		String seqstr ="select "+sequencesName+".Nextval from dual ";
		Integer val =(Integer) this.getSimpleJdbcTemplate().queryForInt(seqstr);
		
		return val;
	}
	
	/**
	 * 分页查询
	 * @param hqlEntity
	 * @param sqlCountEntity
	 * @param rowMapper
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public IPagination queryForPagination(HQLEntity hqlEntity, String sqlCountEntity, RowMapper rowMapper, int firstResult, 
			int maxResults)throws DataAccessException{
		int allCounts = 0;
		IPagination pagination = new SimplePagination(firstResult, maxResults);
		String countSql = hqlEntity.gethqlSql();
		//select子句与order by子句会影响count查询,进行简单的排除.
		countSql = "select count(*) from " + StringUtils.substringAfter(countSql, "from");
		countSql = StringUtils.substringBefore(countSql, "order by");
		
		String sql_pre =" select * from (select row_.*, rownum rownum_ from ("+hqlEntity.gethqlSql()+") row_  where rownum <= "+(firstResult+ maxResults)+")   where rownum_ > "+firstResult;
		//log.info(countSql);
		//log.info(sql_pre);
		if(hqlEntity.getParamList().size() > 0){
			allCounts = this.getJdbcTemplate().queryForInt(countSql, hqlEntity.getParamObjArray(), hqlEntity.paramTypeArray());
			pagination.setResults(this.getJdbcTemplate().query(sql_pre, hqlEntity.getParamObjArray(), rowMapper));
		}
		else{
			allCounts = this.getJdbcTemplate().queryForInt(countSql);
			pagination.setResults(this.getJdbcTemplate().query(sql_pre, rowMapper));
		}
		
		pagination.setAllCount(allCounts);
		
		return pagination;
	}
	/**
	 * mysql 分页查询
	 * @param hqlEntity
	 * @param sqlCountEntity
	 * @param rowMapper
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public IPagination queryForPaginationByMysql(HQLEntity hqlEntity, String sqlCountEntity, RowMapper rowMapper, int firstResult, 
			int maxResults)throws DataAccessException{
		int allCounts = 0;
		IPagination pagination = new SimplePagination(firstResult, maxResults);
		String countSql = hqlEntity.gethqlSql();
		//select子句与order by子句会影响count查询,进行简单的排除.
		countSql = "select count(*) from " + StringUtils.substringAfter(countSql, "from");
		countSql = StringUtils.substringBefore(countSql, "order by");
		
		String sql_pre =hqlEntity.gethqlSql()+" limit"+firstResult+","+(firstResult+maxResults);
		//log.info(countSql);
		//log.info(sql_pre);
		if(hqlEntity.getParamList().size() > 0){
			allCounts = this.getJdbcTemplate().queryForInt(countSql, hqlEntity.getParamObjArray(), hqlEntity.paramTypeArray());
			pagination.setResults(this.getJdbcTemplate().query(sql_pre, hqlEntity.getParamObjArray(), rowMapper));
		}
		else{
			allCounts = this.getJdbcTemplate().queryForInt(countSql);
			pagination.setResults(this.getJdbcTemplate().query(sql_pre, rowMapper));
		}
		
		pagination.setAllCount(allCounts);
		
		return pagination;
	}
	
	/**
	 * query To List
	 * @param hqlEntity
	 * @param rowMapper
	 * @return
	 */
	public List queryForList(SQLEntity sqlEntity,RowMapper rowMapper){
		return this.getJdbcTemplate().query(sqlEntity.parseSql(), sqlEntity.parseParams(), rowMapper);
	}
	public List queryForList(HQLEntity hqlEntity,RowMapper rowMapper){
		return this.getJdbcTemplate().query(hqlEntity.gethqlSql(), hqlEntity.getParamObjArray(), rowMapper);
	}
	/**
	 * query to Object
	 * @param hqlEntity
	 * @param rowMapper
	 * @return
	 */
	public Object queryForObject(HQLEntity hqlEntity,RowMapper rowMapper){
		return this.getJdbcTemplate().queryForObject(hqlEntity.gethqlSql(), hqlEntity.getParamObjArray(), rowMapper);
	}
	/**
	 * update
	 */
	public int update(HQLEntity hqlEntity){
		return this.getJdbcTemplate().update(hqlEntity.gethqlSql(), hqlEntity.getParamObjArray());
	}
	/**
	 * procedure
	 */
	public Map runProcedure(String pName,Map map){
		this.procReadActor = new SimpleJdbcCall(this.getDataSource()).withProcedureName(pName);
		SqlParameterSource in = new MapSqlParameterSource().addValues(map);
		Map out = procReadActor.execute(in);
		return out;
	}
}
