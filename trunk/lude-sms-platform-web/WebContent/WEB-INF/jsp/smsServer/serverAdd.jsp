<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %>
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>

<!-- 添加新记录 -->
<c:if test="${isPost}">	
	<c:if test="${not empty param.sms_server_id && not empty param.name}">
		<sql:query var="exist" sql="select * from sms_server where sms_server_id=?" dataSource="${smsdb}">
			<sql:param value="${param.sms_server_id}"/>
		</sql:query>	
		
		<c:if test="${empty exist.rows}">
			<sql:update dataSource="${smsdb}">
				insert into sms_server(sms_server_id, name, status, create_date, create_time, last_modify_date,last_modify_time) values(?, ?, 1, now(), now(), now(), now())
				<sql:param value="${param.sms_server_id}"/>
				<sql:param value="${param.name}"/>
			</sql:update>
			
			<!-- 成功后跳转到查询页面 -->
			<c:redirect url="smsServerQuery.do"/>		
		</c:if>	
		
		<c:set var="msg">服务器ID：'${param.sms_server_id}'已经存在.</c:set>
	</c:if>	
</c:if>

<!-- 页面展示 -->
<cms:content title="服务器添加">
	<br/>
	<form action="smsServerAdd.do" method="post">
		<table class="viewtable" width="100%">
			<tbody>
				<tr>
					<td class="title">ID：</td>
					<td class="input"><input type="text" name="sms_server_id" value="${param.sms_server_id}"/></td>
				</tr>
				<tr>
					<td class="title">名称：</td>
					<td class="input"><input type="text" name="name" value="${param.name}"/></td>
				</tr>
				<tr>
					<td colspan="2" class="submit"><input type="submit" name="submit" value="提交"/></td>
				</tr>
			</tbody>
		</table>
	</form>
	<c:if test="${not empty msg}">
		<script type="text/javascript">alert("${msg}");</script>
	</c:if>
</cms:content>



