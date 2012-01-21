<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %>
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>

<!-- 添加新记录 -->
<c:if test="${isPost}">	
	<c:if test="${not empty param.org_no && not empty param.name && not empty param.parent_org_id}">		
		<sql:update dataSource="${smsdb}">
			insert into organization(org_no, name, parent_org_id, create_date, create_time, last_modify_date,last_modify_time) values(?, ?, ?, now(), now(), now(), now())
			<sql:param value="${param.org_no}"/>
			<sql:param value="${param.name}"/>
			<sql:param value="${param.parent_org_id}"/>
		</sql:update>
		
		<!-- 成功后跳转到查询页面 -->
		<c:redirect url="organizationQuery.do"/>
	</c:if>	
</c:if>

<sql:query var="orgs" sql="select * from organization order by parent_org_id, name" dataSource="${smsdb}"/>

<!-- 页面展示 -->
<cms:content title="机构添加">
	<br/>
	<form action="organizationAdd.do" method="post">
		<table class="viewtable" width="100%">
			<tbody>
				<tr>
					<td class="title">机构号：</td>
					<td class="input"><input type="text" name="org_no" value="${param.org_no}"/></td>
				</tr>			
				<tr>
					<td class="title">名称：</td>
					<td class="input"><input type="text" name="name" value="${param.name}"/></td>
				</tr>
				<tr>
					<td class="title">上级机构: </td>
					<td class="input">
						<select name="parent_org_id">
					      	<option value="0"></option>
						  	<c:forEach var="org" items="${orgs.rows}">
						  	  	<option value="${org.organization_id}" <c:if test="${org.organization_id == param.organization_id}">selected="selected"</c:if>>${org.name}</option>
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


