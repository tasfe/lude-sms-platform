<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %>
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>

<!-- 写数据库 -->
<c:if test="${isPost}">
	<!-- 修改记录 -->
	<c:if test="${not empty param.role_id && not empty param.name && not empty param.status}">		
		<sql:update dataSource="${smsdb}">
			update role set name=?, status=?, description=?, last_modify_date=now(), last_modify_time=now() where role_id=?
			<sql:param value="${param.name}"/>
			<sql:param value="${param.status}"/>
			<sql:param value="${param.description}"/>
			<sql:param value="${param.role_id}"/>
		</sql:update>
		
		<!-- 成功后跳转到查询页面 -->
		<c:redirect url="roleQuery.do"/>
	</c:if>
</c:if>


<!-- 如果是修改记录，则从表中读取数据 -->
<c:if test="${empty param.submit && not empty param.role_id}">
	<sql:query var="rs" dataSource="${smsdb}">
		select * from role where role_id=?;
		<sql:param value="${param.role_id}"/>
	</sql:query>
	<c:forEach var="role" items="${rs.rows}">
		<c:set var="role_id" value="${role.role_id}"/>
		<c:set var="name" value="${role.name}"/>
		<c:set var="status" value="${role.status}"/>
		<c:set var="description" value="${role.description}"/>
	</c:forEach>
</c:if>

<c:if test="${not empty param.submit}">
	<c:set var="role_id" value="${param.role_id}"/>
	<c:set var="name" value="${param.name}"/>
	<c:set var="status" value="${param.status}"/>	
	<c:set var="description" value="${param.description}"/>
</c:if>


<cms:content title="运营商修改">
	<br/>
	<form action="roleEdit.do" method="post">
		<input type="hidden" name="role_id" value="${role_id}"/>
		<table class="viewtable" width="100%">
			<tbody>
				<tr>
					<td class="title">名称：</td>
					<td class="input"><input type="text" name="name" value="${name}"/></td>
				</tr>
				<tr>
					<td class="title">状态：</td>
					<td class="input">
						<select name="status">
						  	<option></option>
						  	<option value="1" <c:if test='${status == 1 }'> selected="selected"</c:if>>启用</option>
						  	<option value="-1" <c:if test='${status == -1 }'> selected="selected"</c:if>>停用</option>			  	
			  			</select>
					</td>
				</tr>
				<tr>
					<td class="title">描述：</td>
					<td class="input"><input type="text" name="description" value="${description}"/></td>
				</tr>				
				<tr>
					<td colspan="2" class="submit"><input type="submit" name="submit" value="提交"/></td>
				</tr>
			</tbody>
		</table>
	</form>
</cms:content>


