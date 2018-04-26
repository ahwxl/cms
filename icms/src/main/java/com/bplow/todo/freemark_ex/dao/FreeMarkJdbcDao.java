package com.bplow.todo.freemark_ex.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

import com.bplow.look.bass.BaseJdbcDaoSupport;
import com.bplow.look.bass.IPagination;
import com.bplow.look.bass.dao.usertype.HQLEntity;
import com.bplow.look.bass.dao.usertype.SQLEntity;
import com.bplow.todo.freemark_ex.dao.entity.FmContent;
import com.bplow.todo.freemark_ex.dao.entity.TbFreemarkInfo;

@SuppressWarnings("deprecation")
public class FreeMarkJdbcDao extends BaseJdbcDaoSupport{

	@Autowired
	private LobHandler defaultLobHandler;
	
	private Log log = LogFactory.getLog(FreeMarkJdbcDao.class);
	
	/** 
	 * 分页
	 * 模板列表
	 */
    public IPagination queryForPagination(TbFreemarkInfo vo, int firstResult, int maxResults){
		
		SQLEntity sqlEntity = new SQLEntity();
		sqlEntity.append(" select a.id, a.tmpl_name  from tb_freemarktmpl a ");
		
		sqlEntity.append(" ");

		log.info("模板列表查看sql:"+sqlEntity.toString());
		return this.queryForPaginationByMySql(sqlEntity.toString(), null, new RowMapper<TbFreemarkInfo>() {
		    public TbFreemarkInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	TbFreemarkInfo vo = new TbFreemarkInfo();
		    	vo.setId(rs.getString("id"));
		    	vo.setTmpl_name(rs.getString("tmpl_name"));
			    return vo;
		    }
		}, firstResult, maxResults);
	}
	
	
	/**
	 * 保存发布文章
	 */
	public void saveCnt(final FmContent vo) throws IOException{
		
		final InputStream cntinputStream = new ByteArrayInputStream(vo.getContent().getBytes("GBK"));
	    final int cntlength = vo.getContent().getBytes("GBK").length;
		this.getJdbcTemplate().execute(
		"INSERT INTO fm_content (id,content,cnt_caption,second_caption,is_delete_flag,catalog_id,operate_date,click_num) VALUES (?,?,?,?,?,?,?,?)",
		new AbstractLobCreatingPreparedStatementCallback(defaultLobHandler) {
			protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException {
			   ps.setString(1, vo.getId());
			   lobCreator.setBlobAsBinaryStream(ps, 2, cntinputStream, cntlength);
			   ps.setString(3, vo.getCnt_caption());
			   ps.setString(4, vo.getSecond_caption());
			   ps.setString(5, vo.getIs_delete_flag());
			   ps.setString(6, vo.getCatalog_id());
			   ps.setDate(7, new java.sql.Date(vo.getOperate_date().getTime()));
			   ps.setInt(8, 0);
			}
		}
		);
	}
	/**
	 * 修改文章内容
	 * @param vo
	 * @throws IOException
	 */
	public void updateCnt(final FmContent vo)throws IOException{
		final InputStream cntinputStream = new ByteArrayInputStream(vo.getContent().getBytes("GBK"));
	    final int cntlength = vo.getContent().getBytes("GBK").length;
		this.getJdbcTemplate().execute(
		"update fm_content b set b.cnt_caption = ?,b.second_caption=?,b.content=?,b.catalog_id =?,b.operate_date=?  where b.id = ? ",
		new AbstractLobCreatingPreparedStatementCallback(defaultLobHandler) {
			protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException {
			   ps.setString(1, vo.getCnt_caption());
			   ps.setString(2, vo.getSecond_caption());
			   lobCreator.setBlobAsBinaryStream(ps, 3, cntinputStream, cntlength);
			   ps.setString(4, vo.getCatalog_id());
			   ps.setDate(5, new java.sql.Date(vo.getOperate_date().getTime()));
			   ps.setString(6, vo.getId());
			}
		}
		);
	}
	
	public void delCnt(final String id){
		
		this.getJdbcTemplate().execute(
		"UPDATE fm_content a set a.is_delete_flag ='1',a.operate_date = now()  where a.id = ?",
		new AbstractLobCreatingPreparedStatementCallback(defaultLobHandler) {
			protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException {
			   ps.setString(1, id);
			}
		}
		);
	}
	
	
	/**
	 * 查询 单个文章
	 * @param id
	 * @return
	 */
	public FmContent getCntById(String id){
		
		String sql = "select a.id, a.content,a.operate_date,a.cnt_caption,a.catalog_id  from fm_content a where a.id = ? ";
		
		return this.getJdbcTemplate().queryForObject(sql, new RowMapper<FmContent>() {
		    public FmContent mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	FmContent fmContentTmp = new FmContent();
		    	fmContentTmp.setId(rs.getString("id"));
		    	fmContentTmp.setCnt_caption( rs.getString("cnt_caption"));
		    	byte[] blobBytes = lobHandler.getBlobAsBytes(rs, "content");
		    	try {
		    		fmContentTmp.setContent(new String(blobBytes,"GBK"));
				} catch (IOException e) {
					e.printStackTrace();
				}
		    	
		    	fmContentTmp.setOperate_date(rs.getDate("operate_date"));
		    	fmContentTmp.setCatalog_id(rs.getString("catalog_id"));
			    return fmContentTmp;
		    }
		} , id);
		
	}
	
	/**
	 * 获取文章列表根据目录编码
	 */
	public IPagination getCntList(FmContent vo,int firstResult,int maxResults){
		
		SQLEntity sqlEntity = new SQLEntity();
		sqlEntity.append(" select a.id, a.content,a.operate_date,a.cnt_caption,a.catalog_id  from fm_content a where a.is_delete_flag ='0' ");
		
		if(StringUtils.isNotBlank(vo.getCatalog_id())){
			sqlEntity.append(" and a.catalog_id = ? ",vo.getCatalog_id());
		}
		if(StringUtils.isNotBlank(vo.getCnt_caption())){
			sqlEntity.append(" and a.cnt_caption like ? ","%"+vo.getCnt_caption()+"%");
		}
		
		sqlEntity.append(" order by a.operate_date desc ");

		System.out.println("查询文章列表sql:"+sqlEntity.toString());
		return this.queryForPaginationByMySql(sqlEntity.toString(), null, new RowMapper<FmContent>() {
		    public FmContent mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	FmContent fmContentTmp = new FmContent();
		    	fmContentTmp.setId(rs.getString("id"));
		    	fmContentTmp.setCnt_caption( rs.getString("cnt_caption"));
		    	//vo.setContent(rs.getString("content"));
		    	byte[] blobBytes = lobHandler.getBlobAsBytes(rs, "content");
		    	try {
		    		fmContentTmp.setContent(IOUtils.toString(blobBytes));
				} catch (IOException e) {					
					e.printStackTrace();
				}
		    	
		    	fmContentTmp.setOperate_date(rs.getDate("operate_date"));
		    	fmContentTmp.setCatalog_id(rs.getString("catalog_id"));
			    return fmContentTmp;
		    }
		}, firstResult, maxResults);
		
		
	}
	
	
	/**
	 * 获取所有二级菜单
	 */
	@SuppressWarnings("rawtypes")
    public List getAllSecondMenu(FmContent fmcontent){
		HQLEntity hql = new HQLEntity();
		hql.append(" SELECT a.catalog_id p_catalog_id,a.catalog_name p_catalog_name,b.catalog_id,b.catalog_name FROM fm_catalog a left join fm_catalog b on ");
		hql.append("a.catalog_id=b.parent_catalog_id where a.parent_catalog_id='0000' order by a.order_id asc");
		
		return this.queryForList(hql,new RowMapper<FmContent>() {
		    public FmContent mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	FmContent fmContentTmp = new FmContent();
		    	fmContentTmp.setCatalog_id(rs.getString("catalog_id"));
		    	fmContentTmp.setCnt_caption(rs.getString("catalog_name"));
			    return fmContentTmp;
		    }
		});
	}
	
}
