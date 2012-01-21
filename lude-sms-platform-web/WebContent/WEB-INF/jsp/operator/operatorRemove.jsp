<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- 删除操作员 -->
<c:if test="${not empty param.operator_id}">
	<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>
	<sql:update dataSource="${smsdb}">
		delete from operator where operator_id = ?
		<sql:param value="${param.operator_id}"/>
	</sql:update>
</c:if>

<c:redirect url="operatorQuery.do"/>

