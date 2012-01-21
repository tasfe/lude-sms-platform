<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %>
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>

<!-- 写数据库 -->
<c:if test="${isPost}">
	<!-- 修改记录 -->
	<c:if test="${not empty param.channel_id && not empty param.chnnel_no && not empty param.name && not empty param.status}">		
		<sql:update dataSource="${smsdb}">
			update channel set channel_no=?, name=?, status=?, last_modify_date=now(), last_modify_time=now() where channel_id=?
			<sql:param value="${param.channel_no}"/>
			<sql:param value="${param.name}"/>
			<sql:param value="${param.status}"/>
			<sql:param value="${param.channel_id}"/>
		</sql:update>
		
		<!-- 成功后跳转到查询页面 -->
		<c:redirect url="channelQuery.do"/>
	</c:if>
</c:if>


<!-- 如果是修改记录，则从表中读取数据 -->
<c:if test="${empty param.submit && not empty param.channel_id}">
	<sql:query var="rs" dataSource="${smsdb}">
		select * from channel where channel_id=?;
		<sql:param value="${param.channel_id}"/>
	</sql:query>
	<c:forEach var="channel" items="${rs.rows}">
		<c:set var="channel_id" value="${channel.channel_id}"/>
		<c:set var="channel_no" value="${channel.channel_no}"/>
		<c:set var="name" value="${channel.name}"/>
		<c:set var="status" value="${channel.status}"/>
	</c:forEach>
</c:if>

<c:if test="${not empty param.submit}">
	<c:set var="channel_id" value="${param.channel_id}"/>
	<c:set var="channel_no" value="${param.channel_no}"/>
	<c:set var="name" value="${param.name}"/>
	<c:set var="status" value="${param.status}"/>	
</c:if>

<cms:content title="渠道修改">
	<br/>
	<form action="channelEdit.do" method="post">
		<input type="hidden" name="channel_id" value="${channel_id}"/>
		<table class="viewtable" width="100%">
			<tbody>
				<tr>
					<td class="title">渠道号：</td>
					<td class="input"><input type="text" name="channel_no" value="${param.channel_no}"/></td>
				</tr>			
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


