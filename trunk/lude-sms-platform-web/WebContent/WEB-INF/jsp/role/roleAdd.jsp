<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %>
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>

<!-- 添加新记录 -->
<c:if test="${isPost}">	
	<c:if test="${not empty param.name && not empty param.status}">		
		<sql:update dataSource="${smsdb}">
			insert into role(name, status, description, create_date, create_time, last_modify_date,last_modify_time) values(?, ?, ?, now(), now(), now(), now())
			<sql:param value="${param.name}"/>
			<sql:param value="${param.status}"/>
			<sql:param value="${param.description}"/>
		</sql:update>
		
		<!-- 成功后跳转到查询页面 -->
		<c:redirect url="roleQuery.do"/>
	</c:if>	
</c:if>

<cms:content title="角色添加">
	<br/>
	<form action="roleAdd.do" method="post">
		<table class="viewtable" width="100%">
			<tbody>
				<tr>
					<td class="title">名称：</td>
					<td class="input"><input type="text" name="name" value="${param.name}"/></td>
				</tr>
				<tr>
					<td class="title">状态：</td>
					<td class="input">
						<select name="status">
						  	<option></option>
						  	<option value="1" <c:if test='${param.status == 1 }'> selected="selected"</c:if>>启用</option>
						  	<option value="-1" <c:if test='${param.status == -1 }'> selected="selected"</c:if>>停用</option>			  	
			  			</select>
					</td>
				</tr>
				<tr>
					<td class="title">描述：</td>
					<td class="input"><input type="text" name="description" value="${param.description}"/></td>
				</tr>				
				<tr>
					<td colspan="2" class="submit"><input type="submit" name="submit" value="提交"/></td>
				</tr>
			</tbody>
		</table>
	</form>
</cms:content>



