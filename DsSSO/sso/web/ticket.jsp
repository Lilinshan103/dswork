<%@page contentType="text/html; charset=UTF-8" import="dswork.sso.service.TicketService"%><%
Cookie[] cookies = request.getCookies();
Cookie c = null;
String value = null;
String v = "cookie=" + cookies.length;
for (int i = 0; i < cookies.length; i++)
{
	c = cookies[i];
	v += ("<br />" + c.getName() + "=" + c.getValue());
	if (c.getName().equals("DS_SSO_TICKET"))
	{
		value = c.getValue();
		break;
	}
}
if(value == null)
{
	response.sendRedirect(request.getContextPath() + "/login");
	return;
}
%><!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui"/>
<title>Ticket</title>
<style>body{width:38em;margin:0 auto;font-family:Tahoma, Verdana, Arial, sans-serif, 微软雅黑;}</style>
<script type="text/javascript">
function WinNew(){window.location.href = "/portal";}
//window.setTimeout("WinNew()",5000);
</script>
</head>
<body>
<h1>用户账号是:<%=String.valueOf(TicketService.getValueByTicket(String.valueOf(value))) %>。</h1>
<!--<%=v %>-->
<p><a href="<%=request.getContextPath()%>/password">修改密码</a></p>
<p><a href="<%=request.getContextPath()%>/logout">退出系统</a></p>
<p><em>system administrator.</em></p>
</body>
</html>
