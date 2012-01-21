<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %> 

<!-- 开始查询 -->
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>
<sql:query var="rs" sql="select * from agent" dataSource="${smsdb}">
</sql:query>

<!-- 查询结果显示 -->
<cms:content title="运营商管理">
	<table class="viewtable" width="100%">
		<thead>
			<tr>
				<th>ID</th><th>名称</th><th>状态</th><th>操作</th>
			</tr>
		</thead>
		<tbody>
		  <c:forEach var="agent" items="${rs.rows}">
		  <tr>
		    <td>${agent.agent_id}</td>
		    <td>${agent.name}</td>
		    <td><c:if test="${agent.status == 1}">启用</c:if><c:if test="${agent.status == -1}">停用</c:if></td>
		    <td>
		    	<cms:pms name="agentEdit"><a href="agentEdit.do?agent_id=${agent.agent_id}">修改</a></cms:pms>
		    	<cms:pms name="agentRemove"><a href="agentRemove.do?agent_id=${agent.agent_id}" class="operation">删除</a></cms:pms>
		    </td>
		  </tr>
		  </c:forEach>		
		</tbody>
	</table>
	<cms:pms name="agentAdd"><a href="agentAdd.do">添加运营商</a></cms:pms>
</cms:content>

