<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="com.bplow.report.util.DisplayProperty"%>
<%@ page language="java" import="org.apache.commons.beanutils.BasicDynaBean"%>
<%@ page language="java" import="com.bplow.report.vo.Report"%>
<%@ page language="java" import="com.bplow.report.vo.ReportParameter"%>

<%@ page language="java" import="java.io.FileInputStream"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFCellStyle"%>
<%@ page language="java" import="java.awt.Color"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFPalette"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFFont"%>
<%@ page language="java" import="com.bplow.report.util.ExcelHelp"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="${ctx}/res/css/ext_icon.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/res/extjs/resources/css/ext-all.css" />
<script type="text/javascript" src="${ctx}/res/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="${ctx}/res/extjs/ext-all.js"></script>
<script type="text/javascript" src="${ctx}/res/extjs/ux/ProgressBarPager.js"></script>
<script type="text/javascript" src="${ctx}/res/extjs/ux/PanelResizer.js"></script>
<script type="text/javascript" src="${ctx}/report/fontReportShow.js"></script>
<title>报表展示</title>
</head>
<body>

<% 
List results = (List)request.getSession().getAttribute("queryReportResults");
DisplayProperty[] properties = (DisplayProperty[])request.getSession().getAttribute("queryReportProperties");
Report report = (Report)request.getSession().getAttribute("report");
Map reportPV = (Map)request.getSession().getAttribute("reportPV");

%>
	<br/>查询条件：
	<form action="showRepoortDetail" name="form1" method="post">
	<c:forEach var="obj" items="${reportPV}">
     ${obj.key.name }：
     <c:if test="${obj.key.type eq 'Text' }">
     	<input type="text" id="${obj.key.name }" name="${obj.key.name }" value="${obj.value }"/>
     </c:if>
     <c:if test="${obj.key.type eq 'Date' }">
     	<input type="text" id="${obj.key.name }" name="${obj.key.name }" value="${obj.value }"/>
     </c:if>
     <c:if test="${obj.key.type eq 'Query' or obj.key.type eq 'List' or obj.key.type eq 'Boolean' }">
     	<select name="${obj.key.name }"  <s:if test="${obj.key.multipleSelect }">size="4" multiple</s:if> >        
		    <option value="" SELECTED>(None)</option>
		  <c:forEach var="val" items="${obj.key.values }">
            <option value="${val.id }" <c:if test="${val.id eq obj.value }">selected</c:if> >${val.id }</option>
		  </c:forEach>
        </select>
     </c:if>
     &nbsp;&nbsp;
    </c:forEach>
    <input type="button" value="查询" onclick="showRepoortDetailJs('<%=report.getId()%>')">
	</form>
	<br/>查询结果：
	
	<table id="tab" border="1" width="90%">
	<thead id="headTab">
	<%
	String excelFilePath = request.getSession().getServletContext().getRealPath("/")+"reportTemplate/"+report.getFile();
	//构造 HSSFWorkbook 对象，strPath 传入文件路径
	HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(excelFilePath));
	//读取文件中的第一张表格
	HSSFSheet sheet = wb.getSheetAt(0);
	//定义 row、cell
	HSSFRow row;
	// HSSFCell cell;
	String cell;
	HSSFCellStyle cellStyle = null;
	HSSFPalette palette = wb.getCustomPalette(); //类HSSFPalette用于求的颜色的国际标准形式
	HSSFFont font = null;
	String foregroundColor = null;
	String fontColor = null;
	
	for(int i=sheet.getFirstRowNum();i<sheet.getPhysicalNumberOfRows()-1;i++) {	//循环行
		row = sheet.getRow(i);	//获取该行
	%>
	<tr>
	<%
		for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {	//循环每行的列
			cell = row.getCell(j).toString();	//获取该单元格的值
			if(cell == null || "".equals(cell)) {	//单元格的值如果为空，则填充成该行内，离该单元格最近的前面的那个非空单元格的值
				for(int k=j;k>row.getFirstCellNum();k--) {
					if(cell == null || "".equals(cell)) {
						cell = row.getCell(k-1).toString();
					}
				}
			}
			cellStyle = row.getCell(j).getCellStyle();
			foregroundColor = ExcelHelp.convertToStardColor(palette.getColor(cellStyle.getFillForegroundColor()));
			font = wb.getFontAt(cellStyle.getFontIndex());
			fontColor = ExcelHelp.convertToStardColor(palette.getColor(font.getColor()));
	%>
	<td align="center" style="font-weight:bold;background-color:<%=foregroundColor%>;color:<%=fontColor%>"><%=cell %></td>
	<%
		}
	%>
	</tr>
	<%
	}
	%>
	</thead>
	<tbody id="bodyTab">
	<% 
	for(int i=0;i<results.size();i++) { 
		BasicDynaBean basicDynaBean = (BasicDynaBean)results.get(i);
	%>
	<tr>
		<% 
		for(int j=0;j<properties.length;j++) { 
		%>
		<td align="center"><%=basicDynaBean.get(properties[j].getName()) %></td>
		<%
		} 
		%>
	</tr>
	<%
	} 
	%>
	</tbody>
	<tfoot>
	<tr>
		<td colspan="<%=properties.length%>" align="center">
			<div id="barcon" name="barcon"></div>
		</td>
	</tr>
	</tfoot>
	</table>
	<br/><br/><br/><br/>
	<a href="exportReport?id=<%=report.getId()%>">导出Excel</a>
	
</body>
</html>
<script type="text/javascript">
	goPage(1,15);
	/**
	 * 分页函数
	 * pno--页数
	 * psize--每页显示记录数
	 * 分页部分是从真实数据行开始，因而存在加减某个常数，以确定真正的记录数
	 * 纯js分页实质是数据行全部加载，通过是否显示属性完成分页功能
	 **/
	function goPage(pno,psize){
		var itable = document.getElementById("bodyTab");
		var num = itable.rows.length;//表格行数
		var totalPage = 0;//总页数
		var pageSize = psize;//每页显示行数
		if((num-1)/pageSize > parseInt((num-1)/pageSize)){   
	   		 totalPage=parseInt((num-1)/pageSize)+1;   
	   	}else{   
	   		totalPage=parseInt((num-1)/pageSize);   
	   	}   
		var currentPage = pno;//当前页数
		var startRow = (currentPage - 1) * pageSize;//开始显示的行   
	   	var endRow = currentPage * pageSize;//结束显示的行   
	   	endRow = (endRow > num)? num : endRow;
		
		for(var i=0;i<num;i++){
			var irow = itable.rows[i];
			if(i>=startRow&&i<endRow){
				irow.style.display = "block";	
			}else{
				irow.style.display = "none";
			}
		}
		var pageEnd = document.getElementById("pageEnd");
		var tempStr = "共"+num+"条记录 分"+totalPage+"页 当前第"+currentPage+"页";
		if(currentPage>1){
			tempStr += "<a href=\"#\" onClick=\"goPage("+(currentPage-1)+","+psize+")\">上一页</a>"
		}else{
			tempStr += "上一页";	
		}
		if(currentPage<totalPage){
			tempStr += "<a href=\"#\" onClick=\"goPage("+(currentPage+1)+","+psize+")\">下一页</a>";
		}else{
			tempStr += "下一页";	
		}
		if(currentPage>1){
			tempStr += "<a href=\"#\" onClick=\"goPage("+(1)+","+psize+")\">首页</a>";
		}else{
			tempStr += "首页";
		}
		if(currentPage<totalPage){
			tempStr += "<a href=\"#\" onClick=\"goPage("+(totalPage)+","+psize+")\">尾页</a>";
		}else{
			tempStr += "尾页";
		}
		document.getElementById("barcon").innerHTML = tempStr;
		
	}
	
	function showRepoortDetailJs(id) {
	form1.action='showRepoortDetail?id='+id;
	form1.submit();
} 
</script>