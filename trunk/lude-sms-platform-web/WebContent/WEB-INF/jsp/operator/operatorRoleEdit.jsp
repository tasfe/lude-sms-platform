<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %>
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>

<!-- 写数据库 -->
<c:if test="${isPost}">
	<!-- 修改记录 -->
	<c:if test="${not empty param.operator_id}">	
		<sql:transaction dataSource="${smsdb}">
			<sql:update>
				delete from operator_role where operator_id=?
				<sql:param value="${param.operator_id}"/>
			</sql:update>	
			<c:forEach var="id" items="${paramValues.roleIds}">
				<sql:update>
					insert into operator_role(operator_id, role_id, status, create_date, create_time, last_modify_date, last_modify_time) values(?, ?, 1, now(), now(), now(), now())
					<sql:param value="${param.operator_id}"/>
					<sql:param value="${id}"/>
				</sql:update>					
			</c:forEach>						
		</sql:transaction>
	</c:if>
</c:if>


<!-- 从表中读取数据 -->
<sql:query var="operators" dataSource="${smsdb}">
	select o.operator_id, o.login_name, o.password, o.name, o.status, o.msisdn, o.organization_id as org_id, org.name as orgName from operator o join organization org using(organization_id) where o.operator_id=?
	<sql:param value="${param.operator_id}"/>
</sql:query>

<c:forEach var="operator" items="${operators.rows}">
	<c:set var="name" value="${operator.name}"/>
	<c:set var="login_name" value="${operator.login_name}"/>
	<c:set var="orgName" value="${operator.orgName}"/>
</c:forEach>


<sql:query var="allRoles" dataSource="${smsdb}">
	select r.role_id, r.name, r.status, r.description, opr.operator_id as exist from role r left join operator_role opr on(opr.operator_id=? and r.role_id=opr.role_id) order by r.name
	<sql:param value="${param.operator_id}"/>
</sql:query>

<cms:content title="操作员角色">
<form action="operatorRoleEdit.do" method="post">
	<input type="hidden" name="operator_id" value="${param.operator_id}"/><br/>
	机构：${orgName}<br/>
	操作员：${name}[${login_name}]<br/><br/><br/>
	

	<c:if test="${not empty allRoles.rows}">
		<table class="viewtable" width="100%">
			<thead>
				<tr>
					<th width="70px">选择</th><th width="150px">角色名称</th><th>角色描述</th>
				</tr>
			</thead>
			<tbody>		
				<c:forEach var="role" items="${allRoles.rows}">					
					<tr>
						<td><input type="checkbox" name="roleIds" value="${role.role_id}" <c:if test="${not empty role.exist}">checked="checked"</c:if>/></td>
						<td>${role.name}</td>
						<td>${role.description}</td>
					</tr>	
				</c:forEach>
			</tbody>
		</table>
	</c:if>
	<input type="submit" name="submit" value="提交"/>
</form>		
</cms:content>


