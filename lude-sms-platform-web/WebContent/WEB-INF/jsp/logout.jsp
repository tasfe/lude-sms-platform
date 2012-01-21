<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	session.removeAttribute("operator");
	session.removeAttribute("permissions");
%>

<c:redirect url="login.do"/>