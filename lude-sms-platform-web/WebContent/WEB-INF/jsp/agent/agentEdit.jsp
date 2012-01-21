<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %>
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>

<!-- 写数据库 -->
<c:if test="${isPost}">
	<!-- 修改记录 -->
	<c:if test="${not empty param.agent_id && not empty param.name && not empty param.status}">		
		<sql:update dataSource="${smsdb}">
			update agent set name=?, status=?, last_modify_date=now(), last_modify_time=now() where agent_id=?
			<sql:param value="${param.name}"/>
			<sql:param value="${param.status}"/>
			<sql:param value="${param.agent_id}"/>
		</sql:update>
		
		<!-- 成功后跳转到查询页面 -->
		<c:redirect url="agentQuery.do"/>
	</c:if>
</c:if>


<!-- 如果是修改记录，则从表中读取数据 -->
<c:if test="${empty param.submit && not empty param.agent_id}">
	<sql:query var="rs" dataSource="${smsdb}">
		select * from agent where agent_id=?;
		<sql:param value="${param.agent_id}"/>
	</sql:query>
	<c:forEach var="agent" items="${rs.rows}">
		<c:set var="agent_id" value="${agent.agent_id}"/>
		<c:set var="name" value="${agent.name}"/>
		<c:set var="status" value="${agent.status}"/>
	</c:forEach>
</c:if>

<c:if test="${not empty param.submit}">
	<c:set var="agent_id" value="${param.agent_id}"/>
	<c:set var="name" value="${param.name}"/>
	<c:set var="status" value="${param.status}"/>	
</c:if>

<cms:content title="运营商修改">
	<br/>
	<form action="agentEdit.do" method="post">
		<input type="hidden" name="agent_id" value="${agent_id}"/>
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
					<td colspan="2" class="submit"><input type="submit" name="submit" value="提交"/></td>
				</tr>
			</tbody>
		</table>
	</form>
</cms:content>



