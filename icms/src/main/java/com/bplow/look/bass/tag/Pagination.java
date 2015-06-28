package com.bplow.look.bass.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.bplow.look.bass.SimplePagination;

import java.io.IOException;
import org.apache.taglibs.standard.tag.rt.core.ForEachTag;
import org.apache.taglibs.standard.tag.rt.core.OutTag;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
//import org.apache.taglibs.


public class Pagination extends BodyTagSupport {

	private String pagination;
	private String gotoPageIndex;
	
	

	public int doStartTag()throws JspException
    {
		SimplePagination ipagination = (SimplePagination)pageContext.findAttribute(pagination);
		gotoPageIndex = (String)pageContext.findAttribute(gotoPageIndex);
		//pageContext.findAttribute(name)
		//TagUtils.getInstance().lookup(pageContext, getProperty(), getScope())
		StringBuffer outHtmltxt = new StringBuffer();
		
		outHtmltxt.append("<input type=hidden name=gotoPageIndex id=gotoPageIndex value=").append(gotoPageIndex).append(" > \n");
		outHtmltxt.append("<input type=hidden name=maxResults id=maxResults value=").append(ipagination.getMaxResults()).append(" > \n");
		outHtmltxt.append("<input type=hidden name=allPageCount id=allPageCount value=").append(ipagination.getAllPageCount()).append(" > \n");
		
		outHtmltxt.append("<table width=80% align=left  cellspacing=1 cellpadding=0 border=0 style=margin:5px; ><tr><td valign=middle nowrap><input type=button name=firstBut class=button_normal title='首 页' value='首 页' ")
		.append(0 == ipagination.getCurPageIndex()? "disabled" : "")
		.append(" onClick='goPage(0)' /> </td> <td valign=middle nowrap><input type=button name=prevBut class=button_normal title='上一页' value='上一页' ")
		.append(0 == ipagination.getCurPageIndex() ? "disabled" : "").append(" onClick='goPage(").append(ipagination.getCurPageIndex() - 1)
		.append(")' /></td> <td valign=middle nowrap><input type=button name=nextBut  class=button_normal title='下一页' value='下一页' ")
		.append(0 == ipagination.getAllPageCount() ||ipagination.getCurPageIndex() == (ipagination.getAllPageCount() - 1)   ? "disabled" : "").append(" onClick='goPage(").append(ipagination.getCurPageIndex() + 1)
		.append(")' /></td> <td valign=middle nowrap><input type=button name=lastBut class=button_normal title='末 页' value='末 页' onClick='goPage(").append(ipagination.getAllPageCount() - 1)
		.append(")' ").append( 0 == ipagination.getAllPageCount() ||  ipagination.getCurPageIndex() == (ipagination.getAllPageCount() - 1) ?"disabled":"" )
		.append(" /> </td><td valign=middle nowrap>&nbsp;共").append(ipagination.getAllCount()).append("条/每页&nbsp;<input type=text name=maxResultsTxt id=maxResultsTxt   class=input_text size=2 value=").append(ipagination.getMaxResults())
		.append(" >&nbsp;条</td><td width=100% align=right valign=middle nowrap>共").append(ipagination.getAllPageCount())
		.append("页&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;跳至&nbsp;<input type=text name=gotoPageTxt id=gotoPageTxt  class=input_text size=2 value='")
		.append(ipagination.getCurPageIndex()+1).append("' />&nbsp;页&nbsp;<input type=button name=gotoBut onClick='toGotoPage()' class=button_normal title=跳转 value=跳转></input></td></tr></table>")
		.append(" \n").append(" <script type=\"text/javascript\"> \n\t")
		.append("function goPage(page){ \n\t")
		.append("var allPageCount = document.getElementById('allPageCount');var maxResultsTxt = document.getElementById('maxResultsTxt');\n\t")
		.append("if(isNaN(maxResultsTxt.value) || maxResultsTxt.value < 1){alert('请输入大于 0 的整数');maxResultsTxt.focus();return;}\n if(isNaN(page) || page < 0){alert('请输入大于 0 的整数');return;}else if((page+1)>allPageCount.value){alert('超出总页数范围');return;}document.getElementById('maxResults').value=maxResultsTxt.value;document.getElementById('gotoPageIndex').value=page;document.getElementById('gotoPageTxt').value=page+1;document.forms[0].submit();}")
		.append("function toGotoPage(){var gotoPageTxt = document.getElementById('gotoPageTxt');goPage(gotoPageTxt.value-1);}")
		.append("</script>");
		
		try {
			pageContext.getOut().write(outHtmltxt.toString());
		} catch (IOException e) {			
			e.printStackTrace();
		}
        //getJspContext().getOut().write(  );
		return 0;
    }

	
	
	
	public String getPagination() {
		return pagination;
	}
	public void setPagination(String pagination) {
		this.pagination = pagination;
	}
	public String getGotoPageIndex() {
		return gotoPageIndex;
	}
	public void setGotoPageIndex(String gotoPageIndex) {
		this.gotoPageIndex = gotoPageIndex;
	}
}
