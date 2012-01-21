<%@page import="com.lude.sms.mt.Sender"%>
<%@page import="java.util.Map"%>
<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="customTag" %>
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>

<c:if test="${isPost}">	
<c:if test="${not empty param.msisdns && not empty param.content}">		
<%
		    //发送短信
			String msisdns = request.getParameter("msisdns");
			String content = request.getParameter("content");
			String priority = request.getParameter("priority");
			String orderTime = request.getParameter("orderTime");
			
			String channelId = "101";//渠道是管理台
			Map operator = (Map)session.getAttribute("operator");	
			String organizationId = "" + operator.get("org_id");
			String operatorId = "" + operator.get("operator_id");
			Sender sender = new Sender();
			
			try
			{
			    sender.simpleMt(msisdns, content, priority, orderTime, organizationId, channelId, operatorId);
			    request.setAttribute("msg", "本次发送短信成功!");
			}
			catch(Exception e)
			{
			    request.setAttribute("msg", e.getMessage());
			}
%>
</c:if>	
</c:if>

<!-- 页面展示 -->
<cms:content title="发送短信">
	<br/>
	<form action="simpleMt.do" method="post">
		<table class="viewtable" width="100%">
			<tbody>
				<tr>
					<td class="title">手机号码：</td>
					<td class="input"><input type="text" name="msisdns" size="80" value="${param.msisdns}"/>多个号码以英文分号(;)分隔</td>
				</tr>
				<tr>
					<td class="title">短信内容：</td>
					<td class="input"><textarea name="content" cols="70" rows="5">${param.content}</textarea></td>
				</tr>		
				<tr>
					<td class="title">预约时间：</td>
					<td class="input"><input type="text" name="orderTime" size="19" value="${param.orderTime}"/>格式：yyyy-MM-dd hh:mm:ss</td>
				</tr>	
				<tr>
					<td class="title">发送优先级：</td>
					<td class="input">
						<select name="priority">
							<option value="5">第五级</option>
							<option value="4" <c:if test='${param.priority == 4 }'> selected="selected"</c:if>>第四级</option>
							<option value="3" <c:if test='${param.priority == 3 }'> selected="selected"</c:if>>第三级</option>
							<option value="2" <c:if test='${param.priority == 2 }'> selected="selected"</c:if>>第二级</option>
							<option value="1" <c:if test='${param.priority == 1 }'> selected="selected"</c:if>>第一级</option>
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


