<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %>
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>

<!-- 添加新记录 -->
<c:if test="${isPost}">	
	<c:if test="${not empty param.channel_no && not empty param.name && not empty param.status}">		
		<sql:update dataSource="${smsdb}">
			insert into channel(channel_no, name, status, create_date, create_time, last_modify_date,last_modify_time) values(?, ?, ?, now(), now(), now(), now())
			<sql:param value="${param.channel_no}"/>
			<sql:param value="${param.name}"/>
			<sql:param value="${param.status}"/>
		</sql:update>
		
		<!-- 成功后跳转到查询页面 -->
		<c:redirect url="channelQuery.do"/>
	</c:if>	
</c:if>

<!-- 页面展示 -->
<cms:content title="渠道添加">
	<br/>
	<form action="channelAdd.do" method="post">
		<table class="viewtable" width="100%">
			<tbody>
				<tr>
					<td class="title">渠道号：</td>
					<td class="input"><input type="text" name="channel_no" value="${param.channel_no}"/></td>
				</tr>			
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
					<td colspan="2" class="submit"><input type="submit" name="submit" value="提交"/></td>
				</tr>
			</tbody>
		</table>
	</form>
</cms:content>


