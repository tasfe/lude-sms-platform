<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %>
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>

<!-- 写数据库 -->
<c:if test="${isPost}">
	<!-- 修改记录 -->
	<c:if test="${not empty param.role_id && not empty paramValues.permissionIds}">	
		<sql:transaction dataSource="${smsdb}">
			<sql:update>
				delete from role_permission where role_id=?
				<sql:param value="${param.role_id}"/>
			</sql:update>	
			<c:forEach var="id" items="${paramValues.permissionIds}">
				<sql:update>
					insert into role_permission(role_id, permission_id, status, create_date, create_time, last_modify_date, last_modify_time) values(?, ?, 1, now(), now(), now(), now())
					<sql:param value="${param.role_id}"/>
					<sql:param value="${id}"/>
				</sql:update>					
			</c:forEach>						
		</sql:transaction>
	</c:if>
</c:if>


<!-- 从表中读取数据 -->
<sql:query var="roles" dataSource="${smsdb}">
	select role_id, name, status, description from role where role_id=?
	<sql:param value="${param.role_id}"/>
</sql:query>

<c:forEach var="role" items="${roles.rows}">
	<c:set var="name" value="${role.name}"/>
	<c:set var="status" value="${role.status}"/>
	<c:set var="description" value="${role.description}"/>
</c:forEach>


<sql:query var="allPermissions" dataSource="${smsdb}">
	select p.permission_id, p.key_name, p.title, ps.permission_sort_id as sort_id, ps.name as sortName, rp.permission_id as exist from permission p join permission_sort ps using(permission_sort_id) left join role_permission rp on(rp.role_id=? and p.permission_id=rp.permission_id) order by ps.index, p.index
	<sql:param value="${param.role_id}"/>
</sql:query>


<cms:content title="角色权限">
<style type="text/css">
	#content ul li{
		list-style: none;
		padding-left: 20px;
	}
</style>
<form action="rolePermissionEdit.do" method="post">
	<input type="hidden" name="role_id" value="${param.role_id}"/><br/>
	角色名称：${name}<br/>
	角色描述：${description}<br/><br/>	
	
	<c:set var="sort_id" value="0"/>
	<c:set var="isStart" value="${true}"/>
	<c:if test="${not empty allPermissions.rows}">
		<c:forEach var="p" items="${allPermissions.rows}">
			<c:if test="${p.sort_id != sort_id}">
				<c:set var="sort_id" value="${p.sort_id}"/>	
				
				<c:if test="${!isStart}">
					</ul><ul><span>${p.sortName}</span>
				</c:if>				
				<c:if test="${isStart}">
					<ul><span>${p.sortName}</span>
					<c:set var="isStart" value="${false}"/>
				</c:if>						
			</c:if>
			<li><input type="checkbox" name="permissionIds" value="${p.permission_id}" <c:if test="${not empty p.exist}">checked="checked"</c:if>/>${p.title}[${p.key_name}]</li>		
		</c:forEach>
		</ul>	
	</c:if>
	<input type="submit" name="submit" value="提交"/>
</form>		
</cms:content>


