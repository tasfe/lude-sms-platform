<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %>

<!-- 开始查询 -->
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>
<sql:query var="countRS" dataSource="${smsdb}">
	select count(*) as count from mt_queue m where 1=1
	<c:if test="${not empty param.msisdn}">
		AND m.msisdn = ?
	 	<sql:param value="${param.msisdn}"/>
	</c:if>
	<c:if test="${not empty param.startDate}">
		AND m.create_date >= ?
		<fmt:parseDate  var="sDate" value="${param.startDate}" pattern="yyyy-MM-dd"/>
	 	<sql:dateParam value="${sDate}"/>
	</c:if>	
	<c:if test="${not empty param.endDate}">
		AND m.create_date <= ?
		<fmt:parseDate  var="eDate" value="${param.endDate}" pattern="yyyy-MM-dd"/>
		<sql:dateParam value="${eDate}"/>
	</c:if>
</sql:query>

<c:forEach var="row" items="${countRS.rows}">
	<c:set var="totalCount" value="${row.count}"/>
</c:forEach>
<c:set var="totalPage" value="${(totalCount -1)/100 + 1}"/>

<c:if test="${empty param.currPage}">
	<c:set var="index" value="0"/>
	<c:set var="currPage" value="1"/>
</c:if>
<c:if test="${not empty param.currPage}">
	<c:set var="index" value="${(param.currPage-1) * 100 + 1}"/>
	<c:set var="currPage" value="${param.currPage}"/>
</c:if>

<sql:query var="rs" dataSource="${smsdb}" startRow="${index}" maxRows="100">
	select m.msisdn, m.content, m.order_datetime, m.create_date, m.mt_status, m.final_status from mt_queue m where 1=1
	<c:if test="${not empty param.msisdn}">
		AND m.msisdn = ?
	 	<sql:param value="${param.msisdn}"/>
	</c:if>
	<c:if test="${not empty param.startDate}">
		AND m.create_date >= ?
		<fmt:parseDate  var="sDate" value="${param.startDate}" pattern="yyyy-MM-dd"/>
	 	<sql:dateParam value="${sDate}"/>
	</c:if>	
	<c:if test="${not empty param.endDate}">
		AND m.create_date <= ?
		<fmt:parseDate  var="eDate" value="${param.endDate}" pattern="yyyy-MM-dd"/>
		<sql:dateParam value="${eDate}"/>
	</c:if>		
	order by m.mt_id desc
</sql:query>

<cms:content title="查看短信">
	<form action="mtQuery.do" method="post">
		<table class="viewtable" width="100%">
			<tbody>
				<tr>
					<td class="title">手机号码：</td>
					<td class="input"><input type="text" name="msisdn" value="${param.msisdn}"/></td>
				</tr>
				<tr>
					<td class="title">开始日期：</td>
					<td class="input"><input type="text" name="startDate" value="${param.startDate}"/></td>
				</tr>
				<tr>
					<td class="title">结束日期：</td>
					<td class="input"><input type="text" name="endDate" value="${param.endDate}"/></td>
				</tr>				
				<tr>
					<td colspan="2" class="submit"><input type="submit" name="submit" value="提交"/></td>
				</tr>
			</tbody>
		</table>
	</form>

	<br/><span style="color:red; font-size: 12px; padding-left: 5px">查询结果</span>
	<table class="viewtable" width="100%">
		<thead>
			<tr>
				<th width="85px">手机号码</th><th>短信内容</th><th width="90px">预约时间</th><th width="90px">创建日期</th><th width="80px">提交状态</th><th width="80px">发送状态</th>
			</tr>
		</thead>
		<tbody valign="top">
			<c:forEach var="mt" items="${rs.rows}">
			<tr>
				<td>${mt.msisdn}</td>
				<td>${mt.content}</td>
				<td>${mt.order_datetime}</td>
				<td>${mt.create_date}</td>
				<td>${mt.mt_status}</td>
				<td>${mt.final_status}</td>
			</tr>
			</c:forEach>		
		</tbody>
	</table>
	
	<c:if test="${totalCount > 0}">
	<form action="mtQuery.do" method="post">
		<input type="hidden" name="msisdn" value="${param.msisdn}"/>
		<input type="hidden" name="startDate" value="${param.startDate}"/>
		<input type="hidden" name="endDate" value="${param.endDate}"/>
		当前是第${currPage}页， 共${totalPage}页[${totalCount}条]，跳到
		<select name="currPage" onchange="javascript:submit()">
			<c:forEach var="page" begin="1" end="${totalPage}" step="1">
				<option value="${page}" <c:if test='${page == currPage}'> selected="selected"</c:if>>第${page}页</option>
			</c:forEach>			
		</select>
	</form>	
	</c:if>
</cms:content>

