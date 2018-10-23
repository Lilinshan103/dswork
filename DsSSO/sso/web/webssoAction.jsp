<%@page language="java" pageEncoding="UTF-8" import="
dswork.web.MyRequest,
dswork.web.MyAuthCodeServlet,
dswork.core.util.EncryptUtil,
dswork.sso.model.LoginUser,
dswork.sso.service.AuthFactoryService,
dswork.sso.service.TicketService,
dswork.sso.controller.AuthController,
java.net.URLEncoder
"%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><%
MyRequest req = new MyRequest(request);
String account = req.getString("account");
String ssotoken = req.getString("ssotoken");
String loginURL = req.getString("loginURL");
String service = req.getString("service", request.getContextPath() + "/ticket.jsp");
String msg = req.getString("msg");
request.setAttribute("account", account);
request.setAttribute("loginURL", loginURL);
request.setAttribute("service", service);
try
{
	if("".equals(msg) && account.length() > 0)
	{
		AuthFactoryService authservice  = (AuthFactoryService)dswork.spring.BeanFactory.getBean("authFactoryService");
		LoginUser user = authservice.getLoginUserByAccount(account);
		if(user == null)
		{
			msg = "用户不存在";
		}
		else
		{
			if(ssotoken.equals(dswork.core.util.EncryptUtil.encryptMd5(user.getAccount() + "skeywebsso")))
			{
				if(user.getStatus() == 1)
				{
					String cookieTicket = AuthController.putLoginInfo(request, response, user.getAccount(), user.getName());
					try
					{
						authservice.saveLogLogin(cookieTicket, AuthController.getClientIp(request), user.getAccount(), user.getName(), true);
					}
					catch(Exception logex)
					{
					}
					if(service.startsWith(request.getContextPath() + "/password"))
					{
						response.sendRedirect(service);
					}
					else
					{
						response.sendRedirect(service += ((service.indexOf("?") != -1) ? "&ticket=" : "?ticket=") + TicketService.getOnceTicket(cookieTicket));
					}
					return;
				}
				else
				{
					msg = "用户已禁用";
				}
			}
			else
			{
				msg = "非合法操作";
			}
		}
		//try
		//{
		//	authservice.saveLogLogin("", AuthController.getClientIp(request), account, "", false);// 第三方登录原则上不会存在失败，只有没用户
		//}
		//catch(Exception e)
		//{
		//}
	}
}
catch(Exception e)
{
}
request.setAttribute("msg", msg);
%><!DOCTYPE html><html>
<head>
<meta charset="UTF-8" /><title>统一身份认证平台</title>
</head>
<body>
<form action="${fn:escapeXml(loginURL)}" method="post" style="display:none" id="myform">
<input name="account" value="${fn:escapeXml(account)}" />
<input name="service" value="${fn:escapeXml(service)}" />
<input name="msg" value="${fn:escapeXml(msg)}" />
</form>
<script type="text/javascript">document.getElementById('myform').submit();</script>
</body>
</html>