<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %>
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>

<!-- 添加新记录 -->
<c:if test="${isPost}">	
	<c:if test="${not empty param.name && not empty param.login_name && not empty param.password && not empty param.organization_id}">	
		<sql:query var="operators" dataSource="${smsdb}">
			select name, login_name from operator where login_name=?
			<sql:param value="${param.login_name}"/>
		</sql:query>	
		
		<c:if test="${empty operators.rows}">
			<sql:update dataSource="${smsdb}">
				insert into operator(name, login_name, password, status, organization_id, create_date, create_time, last_modify_date,last_modify_time) values(?, ?, ?, ?, 1, now(), now(), now(), now())
				<sql:param value="${param.name}"/>
				<sql:param value="${param.login_name}"/>
				<sql:param value="${param.password}"/>
				<sql:param value="${param.organization_id}"/>
			</sql:update>
			
			<!-- 成功后跳转到查询页面 -->
			<c:redirect url="operatorQuery.do"/>		
		</c:if>
		<c:if test="${not empty operators.rows}">
			<c:set var="msg" value="该操作员已经存在！"/>
		</c:if>		
	</c:if>	
</c:if>

<sql:query var="rs" dataSource="${smsdb}">
	select organization_id, parent_org_id, name, status from organization where status=1 order by parent_org_id, name
</sql:query>

<!-- 页面展示 -->
<cms:content title="操作员添加">
	<br/>
	<form action="operatorAdd.do" method="post">
		<table class="viewtable" width="100%">
			<tbody>
				<tr>
					<td class="title">姓名：</td>
					<td class="input"><input type="text" name="name" value="${param.name}"/></td>
				</tr>
				<tr>
					<td class="title">帐号：</td>
					<td class="input"><input type="text" name="login_name" value="${param.login_name}"/></td>
				</tr>
				<tr>
					<td class="title">密码：</td>
					<td class="input"><input type="password" name="password" value="${param.password}"/></td>
				</tr>								
				<tr>
					<td class="title">机构：</td>
					<td class="input">
						<select name="organization_id">
							<option></option>
							<c:forEach var="org" items="${rs.rows}">
								<option value="${org.organization_id}">${org.name}</option>
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
	<c:if test="${not empty msg}">
		<script type="text/javascript">alert("${msg}");</script>
	</c:if>
</cms:content>
