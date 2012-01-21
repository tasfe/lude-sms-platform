<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- 删除运营商 -->
<c:if test="${not empty param.role_id}">
	<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>
	<sql:update dataSource="${smsdb}">
		delete from role where role_id = ?
		<sql:param value="${param.role_id}"/>
	</sql:update>
</c:if>

<c:redirect url="roleQuery.do"/>

