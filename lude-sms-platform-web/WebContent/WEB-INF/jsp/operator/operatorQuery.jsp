<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %> 

<!-- 开始查询 -->
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>
<sql:query var="rs" sql="select o.operator_id, o.name, o.login_name, o.msisdn, o.status, org.name as orgName from operator o join organization org using(organization_id) order by o.organization_id, o.name" dataSource="${smsdb}">
</sql:query>


<!-- 查询结果显示 -->
<cms:content title="操作员管理">
	<table class="viewtable" width="100%">
		<thead>
			<tr>
				<th>操作员</th><th>手机</th><th>部门</th><th>状态</th><th>操作</th>
			</tr>
		</thead>
		<tbody>
		  <c:forEach var="operator" items="${rs.rows}">
		  <tr>
		    <td>${operator.name}[${operator.login_name}]</td>
		    <td>${operator.msisdn}</td>
		    <td>${operator.orgName}</td>
		    <td><c:if test="${operator.status == 1}">启用</c:if><c:if test="${operator.status == -1}">停用</c:if></td>
		    <td>
		    	<cms:pms name="operatorRoleEdit"><a href="operatorRoleEdit.do?operator_id=${operator.operator_id}">授权</a></cms:pms>
			    <cms:pms name="operatorEdit"><a href="operatorEdit.do?operator_id=${operator.operator_id}" class="operation">修改</a></cms:pms>
			    <cms:pms name="operatorRemove"><a href="operatorRemove.do?operator_id=${operator.operator_id}" class="operation">删除</a></cms:pms>
		    </td>
		  </tr>
		  </c:forEach>		
		</tbody>
	</table>
	<cms:pms name="operatorAdd"><a href="operatorAdd.do">添加操作员</a></cms:pms>
</cms:content>

