/**
 * www.bplow.com
 */
package com.bplow.todo.freemark_ex.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bplow.todo.freemark_ex.dao.entity.FmContent;
import com.bplow.todo.freemark_ex.service.FreemarkService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import freemarker.template.TemplateException;

/**
 * @desc 内容管理
 * @author wangxiaolei
 * @date 2018年4月28日 下午9:39:40
 */
@Controller
public class ContentController {
	
	@Autowired
	public FreemarkService freemarkService;
	
	/**
     * 显示文章列表
     * @param model
     * @return
     */
    @RequestMapping(value = "/showArticle", method = RequestMethod.GET)
    public String toImageMngPage2(Model model) {

        return "showArticle";
    }

    /**
     * 添加文章页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/showAddCntPage", method = RequestMethod.GET)
    public String toAddCntPage(Model model) {

        return "addArticle";
    }

    /**
     * 添加文章页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/showEditorCntPage", method = RequestMethod.GET)
    public String toEditorCntPage(Model model, FmContent fmContent) {

        model.addAttribute("fmContent", fmContent);

        return "editorArticle";
    }

    /**
     * 添加文章 操作
     * @param fmContent
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveContent", method = RequestMethod.POST)
    @ResponseBody
    public String saveCnt(FmContent fmContent, HttpServletRequest request,
                          HttpServletResponse response, Model model) throws Exception {
        freemarkService.saveCnt(fmContent);
        return "{success:true,info:'操作成功!'}";
    }

    /**
     * 修改文章
     * @param fmContent
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/editorContent", method = RequestMethod.POST)
    @ResponseBody
    public String editorCnt(FmContent fmContent, HttpServletRequest request,
                            HttpServletResponse response, Model model) throws Exception {
        freemarkService.editorCnt(fmContent);
        return "{success:true,info:'操作成功!'}";
    }

    @RequestMapping(value = "/queryCntById", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String queryCntById(FmContent fmContent, HttpServletRequest request,
                               HttpServletResponse response, Model model)
                                                                         throws JsonGenerationException,
                                                                         JsonMappingException,
                                                                         IOException {
        String str = freemarkService.getCntByIdToJson(fmContent.getId());
        return str;
    }
	
	/**
     * 发布内容
     * @return
     */
    @RequestMapping(value = "/doPublicCnt", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String doPublicCnt(String id, HttpServletRequest request) {

        try {
        	
            freemarkService.doPublicCnt(id, request);
            
        } catch (IOException e) {

            e.printStackTrace();
        } catch (TemplateException e) {

            e.printStackTrace();
        }

        return "{\"msg\":\"添加成功\"}";
    }

    /**
     * 删除文章
     */
    @RequestMapping(value = "/doDelCnt", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String doDelCnt(String id, HttpServletRequest request) {

        freemarkService.delCntById(id);

        return "{success:true,info:'操作成功!'}";
    }

}
