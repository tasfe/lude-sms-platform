<%@ page contentType="text/html;charset=utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<sql:setDataSource var="smsdb" dataSource="jdbc/smsdb"/>

<!-- 查询用户是否存在 -->
<c:if test="${isPost}">	
	<c:if test="${not empty param.login_name && not empty param.password}">		
		<sql:query var="rs" dataSource="${smsdb}">
			select o.operator_id, o.login_name, o.password, o.name, o.status, o.msisdn, o.organization_id as org_id, org.name as orgName from operator o join organization org using(organization_id) where o.login_name=?
			<sql:param value="${param.login_name}"/>
		</sql:query>
		
		<c:if test="${empty rs.rows}">
			<c:set var="msg" value="用户名或密码不正确！"/>
		</c:if>
		
		<c:if test="${not empty rs.rows}">
			<jsp:useBean id="operator" scope="session" class="java.util.HashMap"/>
			<c:forEach var="o" items="${rs.rows}">
				<c:set target="${operator}" property="operator_id" value="${o.operator_id}"/>
				<c:set target="${operator}" property="name" value="${o.name}"/>
				<c:set target="${operator}" property="msisdn" value="${o.msisdn}"/>
				<c:set target="${operator}" property="org_id" value="${o.org_id}"/>
				<c:set target="${operator}" property="orgName" value="${o.orgName}"/>	
				<c:set var="password" value="${o.password}"/>				
				<c:set var="status" value="${o.status}"/>			
			</c:forEach>			
			
			<c:if test="${param.password == password && status == 1}">
				<sql:query var="prs" dataSource="${smsdb}">
					select p.key_name from permission p join role_permission using(permission_id) join operator_role using(role_id) where operator_id=?
					<sql:param value="${operator.operator_id}"/>
				</sql:query>
				
				<jsp:useBean id="permissions" scope="session" class="java.util.HashMap"/>				
				<c:forEach var="p" items="${prs.rows}">
					<c:set target="${permissions}" property="${p.key_name}" value="${p.key_name}"/>			
				</c:forEach>
				
				<!-- 成功后跳转到查询页面 -->
				<c:redirect url="main.do"/>		
			</c:if>
			
			<c:if test="${param.password != password}">
				<c:set var="msg" value="用户名或密码不正确！"/>
			</c:if>			
			<c:if test="${param.password == password && status != 1}">
				<c:set var="msg" value="该用户已被停用，请联系管理员！"/>
			</c:if>
		</c:if>
	</c:if>	
	
	<c:if test="${empty param.login_name || empty param.password}">
		<c:set var="msg" value="用户名、密码不能为空！"/>
	</c:if>
</c:if>

<html>
<title>路的短信平台管理台</title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color: #1D3647;
}
-->
</style>
<link href="images/skin.css" rel="stylesheet" type="text/css">
<body>
<table width="100%" height="166" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="42" valign="top"><table width="100%" height="42" border="0" cellpadding="0" cellspacing="0" class="login_top_bg">
      <tr>
        <td width="1%" height="21">&nbsp;</td>
        <td height="42">&nbsp;</td>
        <td width="17%">&nbsp;</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td valign="top"><table width="100%" height="532" border="0" cellpadding="0" cellspacing="0" class="login_bg">
      <tr>
        <td width="49%" align="right"><table width="91%" height="532" border="0" cellpadding="0" cellspacing="0" class="login_bg2">
            <tr>
              <td height="138" valign="top"><table width="89%" height="427" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="149">&nbsp;</td>
                </tr>
                <tr>
                  <td height="80" align="right" valign="top"><img src="images/logo.png" width="279" height="68"></td>
                </tr>
                <tr>
                  <td height="198" align="right" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td width="35%">&nbsp;</td>
                      <td height="25" colspan="2" class="left_txt"><p>1- 全部代码开源，轻松客户化...</p></td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                      <td height="25" colspan="2" class="left_txt"><p>2- 高效：短信入库每秒达3000条，短信出库每秒30条...</p></td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                      <td height="25" colspan="2" class="left_txt"><p>3- 本软件遵循GLP V3协议...</p></td>
                    </tr>
                  </table></td>
                </tr>
              </table></td>
            </tr>
            
        </table></td>
        <td width="1%" >&nbsp;</td>
        <td width="50%" valign="bottom"><table width="100%" height="59" border="0" align="center" cellpadding="0" cellspacing="0">
            <tr>
              <td width="4%">&nbsp;</td>
              <td width="96%" height="38"><span class="login_txt_bt">登陆路的短信平台管理台</span></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td height="21"><table cellSpacing="0" cellPadding="0" width="100%" border="0" id="table211" height="328">
                  <tr>
                    <td height="164" colspan="2" align="center"><form name="myform" action="login.do" method="post">
                        <table cellSpacing="0" cellPadding="0" width="100%" border="0" height="143" id="table212">
                          <tr>
                            <td width="13%" height="38" class="top_hui_text"><span class="login_txt">管理员：&nbsp;&nbsp; </span></td>
                            <td height="38" colspan="2" class="top_hui_text"><input name="login_name" class="editbox4" value="${param.login_name}" size="20">                            </td>
                          </tr>
                          <tr>
                            <td width="13%" height="35" class="top_hui_text"><span class="login_txt"> 密 码： &nbsp;&nbsp; </span></td>
                            <td height="35" colspan="2" class="top_hui_text"><input class="editbox4" type="password" size="20" name="password">
                              <img src="images/luck.gif" width="19" height="18"> </td>
                          </tr>
                          <tr>
                            <td height="35" >&nbsp;</td>
                            <td width="20%" height="35" ><input name="Submit" type="submit" class="button" id="Submit" value="登 陆"> </td>
                            <td width="67%" class="top_hui_text"> &nbsp;</td>
                          </tr>
                        </table>
                        <br>
                    </form></td>
                  </tr>
                  <tr>
                    <td width="433" height="164" align="right" valign="bottom"><img src="images/login-wel.gif" width="242" height="138"></td>
                    <td width="57" align="right" valign="bottom">&nbsp;</td>
                  </tr>
              </table></td>
            </tr>
          </table>
          </td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td height="20"><table width="100%" border="0" cellspacing="0" cellpadding="0" class="login-buttom-bg">
      <tr>
        <td align="center"><span class="login-buttom-txt">Copyright &copy; 2011-2012 www.ludesoft.com</span></td>
      </tr>
    </table></td>
  </tr>
</table>
<c:if test="${not empty msg}">
	<script type="text/javascript">alert("${msg}");</script>
</c:if>
</body>
</html>