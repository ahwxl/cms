package com.bplow.todo.freemark_ex.service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.bplow.look.bass.IPagination;
import com.bplow.look.bass.utils.JsonStructureDataHelp;
import com.bplow.todo.freemark_ex.dao.FreeMarkHibernateDao;
import com.bplow.todo.freemark_ex.dao.FreeMarkJdbcDao;
import com.bplow.todo.freemark_ex.dao.entity.FmCatalog;
import com.bplow.todo.freemark_ex.dao.entity.FmContent;
import com.bplow.todo.freemark_ex.dao.entity.TbFreemarkInfo;
import com.bplow.todo.sysManager.dao.entity.SysDepartment;
import com.bplow.todo.sysManager.dao.entity.SysRole;
import com.bplow.todo.sysManager.dao.entity.SysUser;
import com.fasterxml.jackson.databind.ObjectMapper;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


@Transactional
public class FreemarkService {
	
	@Autowired
	public FreeMarkJdbcDao freeMarkJdbcDao;
	@Autowired
	public FreeMarkHibernateDao freeMarkHibernateDao;
	
//	private MutableAclService mutableAclService;
	
	private Log syslog = LogFactory.getLog(FreemarkService.class);
	
	private Configuration freemarkerConfig;
	
	private Template template;
	private static final String ENCODING = "utf-8";
	/**
	 * 注入Freemarker引擎配置,构造Freemarker 邮件内容模板.
	 */
	
	public void setFreemarkerConfig(Configuration freemarkerConfig) throws IOException {
		this.freemarkerConfig = freemarkerConfig;
		//根据freemarkerConfiguration的templateLoaderPath载入文件.
		template = freemarkerConfig.getTemplate("mailTemplate.ftl", ENCODING);
		template.setEncoding("GBK");
	}
	//-----------------------------------------模板管理----------------------------------------------------
	/**
	 * 保存模板
	 */
	public void saveFreemarkTmp(TbFreemarkInfo vo){
		
		freeMarkHibernateDao.save(vo);
	}
	
	
	/*
	 *查看模板列表 
	 */
	public List getFreemarkTmp(TbFreemarkInfo vo){
		
		List list = freeMarkJdbcDao.queryForPagination(vo, 0, 10).getResults();
		System.out.println("列表大小："+list.size());
		
		return null;
	}
	
	/**
	 * ajax 
	 * 获取模板列表
	 */
	public String getFreemarkTemplListToJson(int firstResult,int maxResults){
		return null;
	}
	
	
	/**
	 * @throws TemplateException 
	 * @throws IOException 
	 * 
	 */
	public String executeFtl() throws IOException, TemplateException{
		
		Map context = new HashMap();
		context.put("userName", "张三 GGGGGGGGGGGGGGGG");
		String cnt = FreeMarkerTemplateUtils.processTemplateIntoString(template, context);
		
		System.out.println(cnt);
		
		return cnt;
	}
	
	/*
	 * 保存发布文章
	 */
	public  void saveCnt(FmContent vo) throws Exception{
		
		vo.setId(UUID.randomUUID().toString().replace("-", ""));
		vo.setOperate_date(new Date());
		vo.setIs_delete_flag("0");
		freeMarkJdbcDao.saveCnt(vo);
		
	}

	/**
	 * 获取文章
	 */
	public FmContent getCntById(String id){
		
		FmContent fmcnt = freeMarkJdbcDao.getCntById(id);
		
		return fmcnt;
	}
	

	/**
	 * 获取文章列表
	 */
	
	public String getCntList(FmContent vo,int firstResult,int maxResults){
		
		syslog.info("【获取文章列表】，【1】");
		
		
		IPagination ipagination =  freeMarkJdbcDao.getCntList(vo, firstResult, maxResults);
		
		List cntlist = ipagination.getResults();
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("{ topics:[");
		int num = 1;
		for(Iterator<FmContent> fmcIterator = cntlist.iterator();fmcIterator.hasNext();){
			FmContent fmCntVo = fmcIterator.next();
			if(num == 1){
				sb.append("");
				num += 1;
			}else{
				sb.append(",");
			}
			sb.append("{cnt_id:'").append(fmCntVo.getId()).append("',cnt_caption:'").append(fmCntVo.getCnt_caption())
			  .append("',catalog_id:'").append(fmCntVo.getCatalog_id()).append("',operate_date:'").append(fmCntVo.getOperate_date()).append("'}");
			
		}
		sb.append("],totalCount:").append(ipagination.getAllCount()).append("}");
		
		return sb.toString();
	}
	
	/**
	 * 发布内容
	 * @throws IOException 
	 * @throws TemplateException 
	 */
	public String doPublicCnt(String id,HttpServletRequest request) throws IOException, TemplateException{
		//获取文章内容
		FmContent fmcnt = freeMarkJdbcDao.getCntById(id);
		
		//获取模板
		Template tmp = freemarkerConfig.getTemplate("article_1.ftl", ENCODING);
		tmp.setEncoding("utf-8");
		
		//
		List cataloglist = freeMarkHibernateDao.getFmCatalogList(new FmCatalog("0000"));
		
		Map context = new HashMap();
		context.put("caption", fmcnt.getCnt_caption());
		context.put("content", fmcnt.getContent());
		context.put("catalogList",cataloglist);
		String cnt = FreeMarkerTemplateUtils.processTemplateIntoString(tmp, context);
		
		
		
		
		System.out.println(cnt);
		String fileaddr = request.getRealPath("OutputHTML/web/"+id+".html");
		File newhtmlfile = new File(fileaddr);
		
		
		FileUtils.writeStringToFile(newhtmlfile, cnt);
		
		return "ok";
	}
	
	//添加用户权限
	public String addUserRight(){
		
		/*Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
			  String username = ((UserDetails)principal).getUsername();
		} else {
			  String username = principal.toString();
		}*/
		
		//mutableAclService.
		
		
		
		
		
		return null;
	}
	
	
	protected String getUsername() {
        /*Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername();
        } else {
            return auth.getPrincipal().toString();
        }*/
		return null;
    }
	
	
	//--------------------------------目录管理----------------------------------------------
	
	public void saveFmCatalog(FmCatalog vo ){
		
		vo.setCatalogId(UUID.randomUUID().toString().replace("-", ""));
		//vo.setpCatalogId("0000");
		vo.setOperateDate(new Date());
		freeMarkHibernateDao.save(vo);
	}
	
	/**
	 * 获取 目录对象 根据id
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public String getFmCatalogByIdToJson(FmCatalog vo) throws Exception{
		
		FmCatalog tmpvo = freeMarkHibernateDao.getFmCatalog(vo);
		
		ObjectMapper mapper = new ObjectMapper();
        StringWriter sw = new StringWriter();
        mapper.writeValue(sw, tmpvo);
		
		return sw.toString();
	}
	/**
	 * 修改目录
	 */
	public void editorFmCatalog(FmCatalog vo){
		
		FmCatalog tmpvo = freeMarkHibernateDao.getFmCatalog(vo);
		
		tmpvo.setCatalogName(vo.getCatalogName());
		tmpvo.setCatalogType(vo.getCatalogType());
		tmpvo.setImageSrc(vo.getImageSrc());
		tmpvo.setpCatalogId(vo.getpCatalogId());
		tmpvo.setCatalogDesc(vo.getCatalogDesc());
		
		freeMarkHibernateDao.editorFmCatalog(tmpvo);
		
	}
	
	
	
    /**
     * 删除对象
     */
     public void delFmCatalog(FmCatalog vo){
    	
		freeMarkHibernateDao.delFmCatalog(vo);
		
	}
	/**
	 * 获取目录列表
	 * @param vo
	 * @return
	 */
     public String getFmCatalogList(String pcatalogid){
    	 
    	 FmCatalog vo = new FmCatalog();
    	 vo.setpCatalogId(pcatalogid);
    	 List list = freeMarkHibernateDao.getFmCatalogList(vo);
    	 
    	 StringBuffer rtstr = new StringBuffer();
    	 rtstr.append("[");
    	 int i = 1;
    	 for(Iterator<FmCatalog> fmcatalogItr = list.iterator();fmcatalogItr.hasNext();){
    		 FmCatalog tmpvo = fmcatalogItr.next();   		 
    		 if(i == 1){
    			rtstr.append("{");
    		    i +=1;
    		 }
    		 else rtstr.append(",{");
    		 rtstr.append("text:'").append(tmpvo.getCatalogName()).append("',id:'").append(tmpvo.getCatalogId())
    		 .append("',cls:'").append(tmpvo.getCatalogType()).append("'}");
    		 
    	 }
    	 rtstr.append("]");
    	 return rtstr.toString();
    	 
     }
     
     public String getFmCatalogListJsonGridData(String pcatalogid) throws Exception{
    	 
    	 FmCatalog vo = new FmCatalog();
    	 vo.setpCatalogId(pcatalogid);
    	 List list = freeMarkHibernateDao.getFmCatalogList(vo);
    	 
    	 String jsonGridData = new JsonStructureDataHelp(list).getJsonByList();
    	 
    	 return jsonGridData;
     }
     
     /**
      * 发布栏目信息
      * @param vo
      * @return
      * @throws Exception
      */
     public String doPublicCatalogAction(FmCatalog vo){
    	 
    	 return "";
     }
     
	
	//------------------------------角色管理------------------------------------------------
	
	//obtain all roles
     public String getAllRoleJson(SysRole vo) throws Exception{
    	 
    	 List list = freeMarkHibernateDao.getAllRoles(vo);
    	 
    	 
    	 String jsonGridData = new JsonStructureDataHelp(list).getJsonByList();
    	 
    	 return jsonGridData;
     }
	
   //------------------------------用户管理------------------------------------------------
 	
 	//obtain all users
    public String getAllUserJson(SysUser vo)throws Exception{
    	
    	IPagination ipagination = freeMarkHibernateDao.getAllUsers(vo,vo.getStart(),vo.getLimit());
   	 
   	 
   	    String jsonGridData = new JsonStructureDataHelp(ipagination.getResults(),ipagination.getAllCount(),vo.getLimit()).getJsonByList();
    	
    	return jsonGridData;
    } 
	
    //---------------------------------组织机构管理-------------------------------------------------
    public void saveDepartment(SysDepartment vo){
    	vo.setDepartid(UUID.randomUUID().toString().replace("-", ""));
    	freeMarkHibernateDao.saveDepartment(vo);
    }
    public void delDepartment(SysDepartment vo){
    	freeMarkHibernateDao.delDepartment(vo);
    }
    public void updateDepartment(SysDepartment vo){
    	freeMarkHibernateDao.updateDepartment(vo);
    }
    
    /**
     * 组织结构
     * 返回 tree
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    public String getDepartmentListTreeJson(SysDepartment vo) throws Exception{
    	List list = freeMarkHibernateDao.getDepartmentListByPid(vo);
    	String jsonTreeData = new JsonStructureDataHelp(list).getJsonTreeByList().replace("departid", "id").replace("departname", "text")
    			.replace("sortid", "cls");
    	
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
    	List list = freeMarkHibernateDao.getDepartmentListByPid(vo);
    	String jsonTreeData = new JsonStructureDataHelp(list).getJsonByList();
    	
    	return jsonTreeData;
    }
    
    /**
     * page
     */
    public String getDepartmentListPage(SysDepartment vo)throws Exception{
    	
    	IPagination ipagination = freeMarkHibernateDao.getDepartmentListPage(vo);
    	
    	return ipagination.getJsonByList();
    }
    
	
}
