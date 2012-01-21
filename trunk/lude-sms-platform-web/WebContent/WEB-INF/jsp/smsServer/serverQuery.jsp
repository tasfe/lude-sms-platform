<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %>

<!-- 开始查询 -->
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>
<sql:query var="rs" sql="select * from sms_server" dataSource="${smsdb}">
</sql:query>

<!-- 查询结果显示 -->
<cms:content title="服务器管理">
	<table class="viewtable" width="100%">
		<thead>
			<tr>
				<th>ID</th><th>名称</th><th>状态</th><th>操作</th>
			</tr>
		</thead>
		<tbody>
		  <c:forEach var="ss" items="${rs.rows}">
		  <tr>
		    <td>${ss.sms_server_id}</td>
		    <td>${ss.name}</td>
		    <td><c:if test="${ss.status == 1}">启用</c:if><c:if test="${ss.status == -1}">停用</c:if></td>
		    <td><cms:pms name="smsServerRemove"><a href="smsServerRemove.do?sms_server_id=${ss.sms_server_id}">删除</a></cms:pms></td>
		  </tr>
		  </c:forEach>		
		</tbody>
	</table>
	<cms:pms name="smsServerAdd"><a href="smsServerAdd.do">服务器添加</a></cms:pms>
</cms:content>