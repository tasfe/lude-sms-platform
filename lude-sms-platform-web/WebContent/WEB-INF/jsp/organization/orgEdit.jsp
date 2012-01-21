<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %>
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>

<!-- 写数据库 -->
<c:if test="${isPost}">
	<!-- 修改记录 -->
	<c:if test="${not empty param.organization_id && not empty param.org_no && not empty param.name && not empty param.parent_org_id}">		
		<sql:update dataSource="${smsdb}">
			update organization set org_no=?, name=?, parent_org_id=?, last_modify_date=now(), last_modify_time=now() where organization_id=?
			<sql:param value="${param.org_no}"/>
			<sql:param value="${param.name}"/>
			<sql:param value="${param.parent_org_id}"/>
			<sql:param value="${param.organization_id}"/>
		</sql:update>
		
		<!-- 成功后跳转到查询页面 -->
		<c:redirect url="organizationQuery.do"/>
	</c:if>
</c:if>


<!-- 如果是修改记录，则从表中读取数据 -->
<c:if test="${empty param.submit && not empty param.organization_id}">
	<sql:query var="rs" dataSource="${smsdb}">
		select * from organization where organization_id=?;
		<sql:param value="${param.organization_id}"/>
	</sql:query>
	<c:forEach var="org" items="${rs.rows}">
		<c:set var="organization_id" value="${org.organization_id}"/>
		<c:set var="parent_org_id" value="${org.parent_org_id}"/>
		<c:set var="org_no" value="${org.org_no}"/>
		<c:set var="name" value="${org.name}"/>
		
	</c:forEach>
</c:if>

<c:if test="${not empty param.submit}">
	<c:set var="organization_id" value="${param.organization_id}"/>
	<c:set var="parent_org_id" value="${param.parent_org_id}"/>
	<c:set var="org_no" value="${org.org_no}"/>	
	<c:set var="name" value="${param.name}"/>
</c:if>

<sql:query var="orgs" sql="select * from organization order by parent_org_id, name" dataSource="${smsdb}"/>

<cms:content title="机构修改">
	<br/>
	<form action="organizationEdit.do" method="post">
		<input type="hidden" name="organization_id" value="${organization_id}"/>
		<table class="viewtable" width="100%">
			<tbody>
				<tr>
					<td class="title">机构号：</td>
					<td class="input"><input type="text" name="org_no" value="${org_no}"/></td>
				</tr>			
				<tr>
					<td class="title">名称：</td>
					<td class="input"><input type="text" name="name" value="${name}"/></td>
				</tr>
				<tr>
					<td class="title">上级机构: </td>
					<td class="input">
						<select name="parent_org_id">
					      	<option value="0"></option>
						  	<c:forEach var="org" items="${orgs.rows}">
						  	  	<option value="${org.organization_id}" <c:if test="${org.organization_id == parent_org_id}">selected="selected"</c:if>>${org.name}</option>
						  	</c:forEach>
					  	</select>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="submit"><input type="submit" name="submit" value="提交"/></td>
				</tr>
			</tbody>
		</table>
	</form>
</cms:content>

