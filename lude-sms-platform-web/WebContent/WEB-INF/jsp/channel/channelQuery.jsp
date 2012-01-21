<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %> 

<!-- 开始查询 -->
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>
<sql:query var="rs" sql="select * from channel" dataSource="${smsdb}">
</sql:query>

<!-- 查询结果显示 -->
<cms:content title="渠道管理">
	<table class="viewtable" width="100%">
		<thead>
			<tr>
				<th>渠道号</th><th>名称</th><th>状态</th><th>操作</th>
			</tr>
		</thead>
		<tbody>
		  <c:forEach var="channel" items="${rs.rows}">
		  <tr>
		    <td>${channel.channel_no}</td>
		    <td>${channel.name}</td>
		    <td><c:if test="${channel.status == 1}">启用</c:if><c:if test="${channel.status == -1}">停用</c:if></td>
		    <td>
		    	<cms:pms name="channelEdit"><a href="channelEdit.do?channel_id=${channel.channel_id}">修改</a></cms:pms>
		    	<cms:pms name="channelRemove"><a href="channelRemove.do?channel_id=${channel.channel_id}"  class="operation">删除</a></cms:pms>
		    </td>
		  </tr>
		  </c:forEach>		
		</tbody>
	</table>
	<cms:pms name="channelAdd"><a href="channelAdd.do">渠道添加</a></cms:pms>
</cms:content>
