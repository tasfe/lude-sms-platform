<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>菜单</title>

<script src="js/prototype.lite.js" type="text/javascript"></script>
<script src="js/moo.fx.js" type="text/javascript"></script>
<script src="js/moo.fx.pack.js" type="text/javascript"></script>
<style>
body {
	font:12px Arial, Helvetica, sans-serif;
	color: #000;
	background-color: #EEF2FB;
	margin: 0px;
}
#container {
	width: 182px;
}
H1 {
	font-size: 12px;
	margin: 0px;
	width: 182px;
	cursor: pointer;
	height: 30px;
	line-height: 20px;	
}
H1 a {
	display: block;
	width: 182px;
	color: #000;
	height: 30px;
	text-decoration: none;
	moz-outline-style: none;
	background-image: url(images/menu_bgs.gif);
	background-repeat: no-repeat;
	line-height: 30px;
	text-align: center;
	margin: 0px;
	padding: 0px;
}
.content{
	width: 182px;
	height: 26px;
	
}
.MM ul {
	list-style-type: none;
	margin: 0px;
	padding: 0px;
	display: block;
}
.MM li {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	line-height: 26px;
	color: #333333;
	list-style-type: none;
	display: block;
	text-decoration: none;
	height: 26px;
	width: 182px;
	padding-left: 0px;
}
.MM {
	width: 182px;
	margin: 0px;
	padding: 0px;
	left: 0px;
	top: 0px;
	right: 0px;
	bottom: 0px;
	clip: rect(0px,0px,0px,0px);
}
.MM a:link {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	line-height: 26px;
	color: #333333;
	background-image: url(images/menu_bg1.gif);
	background-repeat: no-repeat;
	height: 26px;
	width: 182px;
	display: block;
	text-align: center;
	margin: 0px;
	padding: 0px;
	overflow: hidden;
	text-decoration: none;
}
.MM a:visited {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	line-height: 26px;
	color: #333333;
	background-image: url(images/menu_bg1.gif);
	background-repeat: no-repeat;
	display: block;
	text-align: center;
	margin: 0px;
	padding: 0px;
	height: 26px;
	width: 182px;
	text-decoration: none;
}
.MM a:active {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	line-height: 26px;
	color: #333333;
	background-image: url(images/menu_bg1.gif);
	background-repeat: no-repeat;
	height: 26px;
	width: 182px;
	display: block;
	text-align: center;
	margin: 0px;
	padding: 0px;
	overflow: hidden;
	text-decoration: none;
}
.MM a:hover {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	line-height: 26px;
	font-weight: bold;
	color: #006600;
	background-image: url(images/menu_bg2.gif);
	background-repeat: no-repeat;
	text-align: center;
	display: block;
	margin: 0px;
	padding: 0px;
	height: 26px;
	width: 182px;
	text-decoration: none;
}
</style>
</head>
<c:set var="operator_id" value="${operator.operator_id}"/>
<sql:query var="menus" dataSource="${smsdb}">
	select m.url, m.title, ms.menu_sort_id as sort_id, ms.name as sortName from menu m join menu_sort ms on(m.menu_sort_id=ms.menu_sort_id) join permission p on(m.permission=p.key_name) join role_permission rp on(p.permission_id=rp.permission_id) join operator_role opr on(rp.role_id=opr.role_id) where opr.operator_id=? order by ms.index, m.index
	<sql:param value="${operator_id}"/>
</sql:query>
<c:set var="sort_id" value="0"/>
<c:set var="isStart" value="${true}"/>
	
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#EEF2FB">
	<tr>
		<td width="182" valign="top">
			<div id="container">
    		<c:if test="${not empty menus.rows}">	
    			<c:forEach var="menu" items="${menus.rows}">
					<c:if test="${menu.sort_id != sort_id}">
						<c:set var="sort_id" value="${menu.sort_id}"/>	
						
						<c:if test="${!isStart}">
							</ul></div> 
							<h1 class="type"><a href="javascript:void(0)">${menu.sortName}</a></h1>
							<div class="content">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr><td><img src="images/menu_topline.gif" width="182" height="5" /></td></tr>
								</table>
								<ul class="MM">							
						</c:if>				
						<c:if test="${isStart}">
							<h1 class="type"><a href="javascript:void(0)">${menu.sortName}</a></h1>
							<div class="content">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr><td><img src="images/menu_topline.gif" width="182" height="5" /></td></tr>
								</table>
								<ul class="MM">	
							<c:set var="isStart" value="${false}"/>
						</c:if>						
					</c:if>    				
					<li><a href="${menu.url}" target="main">${menu.title}</a></li>
				</c:forEach>
				</ul></div> 
			</c:if>				
			</div>	
		</td>
	</tr>
</table>
<script type="text/javascript">
	var contents = document.getElementsByClassName('content');
	var toggles = document.getElementsByClassName('type');

	var myAccordion = new fx.Accordion(
		toggles, contents, {opacity: true, duration: 400}
	);
	myAccordion.showThisHideOpen(contents[0]);
</script>
</body>
</html>
