<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %>
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>

<!-- 写数据库 -->
<c:if test="${isPost}">
	<!-- 修改记录 -->
	<c:if test="${not empty param.operator_id && not empty param.name && not empty param.login_name && not empty param.organization_id && not empty param.status}">		
		<sql:update dataSource="${smsdb}">
			update operator set name=?, login_name=?, msisdn=?, organization_id=?, status=?, last_modify_date=now(), last_modify_time=now()
			<sql:param value="${param.name}"/>
			<sql:param value="${param.login_name}"/>
			<sql:param value="${empty param.msisdn? 0 : param.msisdn}"/>
			<sql:param value="${param.organization_id}"/>
			<sql:param value="${param.status}"/>
			<c:if test="${not empty param.password}">
				, password=?
				<sql:param value="${param.password}"/>
			</c:if>
			 where operator_id=?
			<sql:param value="${param.operator_id}"/>
		</sql:update>
		
		<!-- 成功后跳转到查询页面 -->
		<c:redirect url="operatorQuery.do"/>
	</c:if>
</c:if>


<!-- 如果是修改记录，则从表中读取数据 -->
<c:if test="${empty param.submit && not empty param.operator_id}">
	<sql:query var="rs" dataSource="${smsdb}">
		select * from operator where operator_id=?;
		<sql:param value="${param.operator_id}"/>
	</sql:query>
	<c:forEach var="operator" items="${rs.rows}">
		<c:set var="operator_id" value="${operator.operator_id}"/>
		<c:set var="name" value="${operator.name}"/>
		<c:set var="login_name" value="${operator.login_name}"/>
		<c:set var="msisdn" value="${operator.msisdn}"/>
		<c:set var="organization_id" value="${operator.organization_id}"/>
		<c:set var="status" value="${operator.status}"/>
	</c:forEach>
</c:if>

<c:if test="${not empty param.submit}">
	<c:set var="operator_id" value="${param.operator_id}"/>
	<c:set var="name" value="${param.name}"/>
	<c:set var="login_name" value="${param.login_name}"/>
	<c:set var="msisdn" value="${param.msisdn}"/>
	<c:set var="organization_id" value="${param.organization_id}"/>
	<c:set var="status" value="${param.status}"/>	
</c:if>

<sql:query var="rs" dataSource="${smsdb}">
	select organization_id, parent_org_id, name, status from organization where status=1 order by parent_org_id, name
</sql:query>

<cms:content title="操作员修改">
	<br/>
	<form action="operatorEdit.do" method="post">
		<input type="hidden" name="operator_id" value="${operator_id}"/>
		<table class="viewtable" width="100%">
			<tbody>
				<tr>
					<td class="title">姓名：</td>
					<td class="input"><input type="text" name="name" value="${name}"/></td>
				</tr>
				<tr>
					<td class="title">帐号：</td>
					<td class="input"><input type="text" name="login_name" value="${login_name}"/></td>
				</tr>
				<tr>
					<td class="title">密码：</td>
					<td class="input"><input type="password" name="password" value=""/></td>
				</tr>
				<tr>
					<td class="title">手机：</td>
					<td class="input"><input type="text" name="msisdn" value="${msisdn}"/></td>
				</tr>
				<tr>
					<td class="title">机构：</td>
					<td class="input">
						<select name="organization_id">
							<option></option>
							<c:forEach var="org" items="${rs.rows}">
								<option value="${org.organization_id}" <c:if test='${org.organization_id == organization_id }'> selected="selected"</c:if>>${org.name}</option>
							</c:forEach>	  	
						</select>
					</td>
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
					<td colspan="2" class="submit"><input type="submit" name="submit" value="提交"/></td>
				</tr>
			</tbody>
		</table>
	</form>
</cms:content>


