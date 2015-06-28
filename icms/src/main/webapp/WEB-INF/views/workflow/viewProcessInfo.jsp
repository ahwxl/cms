<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>显示流程信息</title>
</head>
<body>
流程信息<br />
<form action="completeProcessInstanc" method="post">
<input type="hidden" name="processId" value="${processInstance.id }"/>
流程编码：<input type="text" name="" value="${processInstance.id }"/><br />
上步操作人：<input type="text" name="" value=""/><br />
当前状态：<input type="text" name="" value="${currentTask.name }"/><br />
下一步操作：<input type="text" name=""  value=""/><br />
<input type="submit" value=" 提  交 "  />
</form>
</body>
</html>