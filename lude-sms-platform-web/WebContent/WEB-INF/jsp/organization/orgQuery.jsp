<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %> 

<!-- 开始查询 -->
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>
<sql:query var="rs" dataSource="${smsdb}">
	select o.organization_id, o.org_no, o.name, po.name as pName from organization o left join organization po on(o.parent_org_id=po.organization_id) where 1=1
	<c:if test="${not empty param.name}">
		AND o.name like ?
		<sql:param>${param.name}%</sql:param>
	</c:if>
	<c:if test="${not empty param.parent_org_id}">
		AND o.parent_org_id = ?
		<sql:param>${param.parent_org_id}%</sql:param>
	</c:if>	
	order by o.parent_org_id, o.name
</sql:query>

<!-- 查询结果显示 -->
<cms:content title="机构管理">
	<table class="viewtable" width="100%">
		<thead>
			<tr>
				<th>机构号</th><th>机构名称</th><th>上级机构</th><th>操作</th>
			</tr>
		</thead>
		<tbody>
		  <c:forEach var="org" items="${rs.rows}">
		  <tr>
		    <td>${org.org_no}</td>
		    <td>${org.name}</td>
		    <td>${empty org.pName? "顶级机构" : org.pName}</td>		    
		    <td>
		    	<cms:pms name="organizationEdit"><a href="organizationEdit.do?organization_id=${org.organization_id}">修改</a></cms:pms>
		    	<cms:pms name="organizationRemove"><a href="organizationRemove.do?organization_id=${org.organization_id}" class="operation">删除</a></cms:pms>
		    </td>
		  </tr>
		  </c:forEach>		
		</tbody>
	</table>
	<cms:pms name="organizationAdd"><a href="organizationAdd.do">添加机构</a></cms:pms>
</cms:content>

