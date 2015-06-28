<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>登陆系统</title>
<style type="text/css">
body{background-color:#D3D3D3;}
.wrap{width:380px;margin:0 auto; overflow:hidden;}
.scrm{height:268px;position:relative;margin-top:260px;}
.scrm1{width:300px;margin:auto;position:absolute;border:solid 1px red;height:100px;
left:expression(eval(document.body.clientWidth)/2-150);
top:expression(eval(document.body.clientHeight)/2-25);
}
</style>
</head>
<body>
<div class="wrap">
<c:if test="${not empty param.login_error}">
      <font color="red">
        Your login attempt was not successful, try again.<br/><br/>
        Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
      </font>
</c:if>



<div class="scrm">
<!---->  <form action="/newcms/<c:url value='j_spring_security_check'/>" method="post">
登陆系统：<br />
用&nbsp;户&nbsp;名：<input type="text" name='j_username' value="keith"/><br />
用户密码：<input type="password" name='j_password' value="melbourne"/><br />
<input type='checkbox' name='_spring_security_remember_me'/>记住我<br />
<input type="submit" value="确定"/>
</form>
</div>
</wrap>
</body>
</html>