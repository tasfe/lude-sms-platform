<%@ page contentType="text/html;charset=utf-8" %> 
<html>
<head>
<title>短信平台管理台</title>
<script language=JavaScript>
function logout(){
	if (confirm("您确定要退出管理台吗？"))
	top.location = "logout.do";
	return false;
}
</script>
<style type="text/css">
.admin_topbg {
	background-image: url(images/top-right.gif);
	background-repeat: repeat-x;
}

.admin_txt {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	color: #FFFFFF;
	text-decoration: none;
	height: 38px;
	width: 100%;
	position: 固定;
	line-height: 38px;
}
</style>
</head>
<body leftmargin="0" topmargin="0">
<table width="100%" height="64" border="0" cellpadding="0" cellspacing="0" class="admin_topbg">
  <tr>
    <td width="61%" height="64"><img src="images/logo.gif" width="262" height="64"></td>
    <td width="39%" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="74%" height="38" class="admin_txt">尊敬的用户：<b>${operator.name}</b>，欢迎您登陆使用！</td>
        <td width="22%"><a href="#" target="_self" onClick="logout();"><img src="images/out.gif" alt="安全退出" width="46" height="20" border="0"></a></td>
        <td width="4%">&nbsp;</td>
      </tr>
      <tr>
        <td height="19" colspan="3">&nbsp;</td>
        </tr>
    </table></td>
  </tr>
</table>
</body>
</html>
