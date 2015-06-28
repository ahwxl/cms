<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>修改系统菜单</title>
</head>
<body>
菜单信息<br />
<form action="doEditorSysMenu" method="post">
<input type="hidden" name="menuid" value="${sysMenu.menuid }"/>
菜单名称：<input type="text" name="menuname" value="${sysMenu.menuname }"/><br />
连接URL：<input type="text" name="menuurl" value="${sysMenu.menuurl }"/><br />
所属模块：<input type="text" name="moduleid" value="${sysMenu.moduleid }"/><br />
状态：<input type="text" name="enabled"  value="${sysMenu.enabled }"/><br />
<input type="submit" value=" 提  交 "  />
</form>
</body>
</html>