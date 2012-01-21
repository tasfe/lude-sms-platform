<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<c:if test="${empty operator || empty permissions}">
	<c:redirect url="/login.do"/>
</c:if>

<html>
<head>
<title>短信平台管理台</title>
<meta http-equiv=Content-Type content=text/html;charset=gb2312>
</head>
<frameset rows="64,*"  frameborder="NO" border="0" framespacing="0">
	<frame src="admin_top.do" noresize="noresize" scrolling="no" marginwidth="0" marginheight="0"/>
	<frameset cols="200,*" id="frame" frameborder="NO" border="0" framespacing="0">
		<frame src="menu.do" noresize="noresize" scrolling="no" marginwidth="0" marginheight="0"/>
		<frame src="welcome.do" name="main" marginwidth="0" marginheight="0" scrolling="auto"/>
	</frameset>
</frameset>
