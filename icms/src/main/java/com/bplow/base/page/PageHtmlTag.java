package com.bplow.base.page;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


@SuppressWarnings("serial")
public class PageHtmlTag extends TagSupport {

	private PageHtmlInter pageBar;//分页条


	private String PageSource;

	public PageHtmlTag() {
		pageBar = null;
	}
/**
 * 
 */
	@SuppressWarnings("static-access")
	public int doEndTag() throws JspException {
		String s = pageBar.getHTML();
		try {
			pageContext.getOut().write(s);
		} catch (IOException e) {
			throw new JspException("构造页面导航是IOerror：" + e.getMessage());
		}
		return super.EVAL_PAGE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@SuppressWarnings("static-access")
	public int doStartTag() throws JspException {
		if (pageBar != null) {
			String k = pageBar.getStartHTML();
			try {
				pageContext.getOut().write(k);
			} catch (IOException e) {
				throw new JspException("构造页面导航是IOerror：" + e.getMessage());
			}
			//return super.EVAL_PAGE;
			return super.EVAL_BODY_INCLUDE;
		} else {
			throw new JspException("页面导航对象不能为空");
		}
	}

	public String getPageSource() {
		return PageSource;
	}

	public void setPageSource(String PageSource) {
		this.PageSource = PageSource;
		this.pageBar = (PageHtmlInter) pageContext.getRequest().getAttribute(
				this.PageSource);
	}

}