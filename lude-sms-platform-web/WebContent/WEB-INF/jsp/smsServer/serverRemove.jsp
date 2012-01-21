<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- 删除运短信平台服务器 -->
<c:if test="${not empty param.sms_server_id}">
	<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>
	<sql:update dataSource="${smsdb}">
		delete from sms_server where sms_server_id = ?
		<sql:param value="${param.sms_server_id}"/>
	</sql:update>
</c:if>

<c:redirect url="smsServerQuery.do"/>

