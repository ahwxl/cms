<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>修改模块信息</title>
</head>
<body>
模块信息<br />
<form action="doEditorSysModule" method="post">
<input type="hidden" name="moduleid" value="${sysModule.moduleid }"/>
模块名称：<input type="text" name="modulename" value="${sysModule.modulename }"/><br />
模块类型：<input type="text" name="moduletype" value="${sysModule.moduletype }"/><br />
父模块：<input type="text" name="pmoduleid" value="${sysModule.pmoduleid }"/><br />
<input type="submit" value=" 提  交 "  />
</form>
</body>
</html>