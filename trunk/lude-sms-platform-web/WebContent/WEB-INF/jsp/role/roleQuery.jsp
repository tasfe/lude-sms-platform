<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %>

<!-- 开始查询 -->
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>
<sql:query var="rs" sql="select * from role" dataSource="${smsdb}">
</sql:query>

<!-- 查询结果显示 -->
<cms:content title="角色管理">
	<table class="viewtable" width="100%">
		<thead>
			<tr>
				<th>ID</th><th>名称</th><th>状态</th><th>描述</th><th>操作</th>
			</tr>
		</thead>
		<tbody>
		  <c:forEach var="role" items="${rs.rows}">
		  <tr>
		    <td>${role.role_id}</td>
		    <td>${role.name}</td>
		    <td><c:if test="${role.status == 1}">启用</c:if><c:if test="${role.status == -1}">停用</c:if></td>
		    <td>${role.description}</td>
		    <td>
			    <cms:pms name="rolePermissionEdit"><a href="rolePermissionEdit.do?role_id=${role.role_id}" class="operation">权限</a></cms:pms>
			    <cms:pms name="roleEdit"><a href="roleEdit.do?role_id=${role.role_id}" class="operation">修改</a></cms:pms>
			    <cms:pms name="roleRemove"><a href="roleRemove.do?role_id=${role.role_id}" class="operation">删除</a></cms:pms>
		    </td>
		  </tr>
		  </c:forEach>		
		</tbody>
	</table>
	<cms:pms name="roleAdd"><a href="roleAdd.do">角色添加</a></cms:pms>
</cms:content>

