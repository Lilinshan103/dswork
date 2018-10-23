<%@page language="java" pageEncoding="UTF-8" import="
dswork.web.MyRequest,
java.net.URLEncoder,
dswork.websso.util.WebssoUtil
"%><%
response.setHeader("Cache-Control","no-store");
response.setDateHeader("Expires", 0);
response.setHeader("Pragma","no-cache"); 
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%
MyRequest req = new MyRequest(request);
String msg = "";
String ticket = "";
String code = "";
String account = "";
dswork.websso.model.DsWebssoUser u = null;
for(Cookie c : request.getCookies())
{
	if("DS_SSO_TICKET".equals(c.getName())){ticket = c.getValue();}
	else if("DS_SSO_CODE".equals(c.getName())){code = c.getValue();}
}
if(code.length() > 0 && ticket.length() > 0)
{
	String s = dswork.core.util.EncryptUtil.decodeDes(code, ticket);
	account = s.split("#")[0].toLowerCase(java.util.Locale.ENGLISH);//通过cookie取sso的登陆用户account
}
if(account.length() == 0)
{
	msg = "请登录后再进行相关操作";
}
else
{
	dswork.websso.service.DsWebssoUserService ssoservice = (dswork.websso.service.DsWebssoUserService)dswork.spring.BeanFactory.getBean("dsWebssoUserService");
	u = ssoservice.getBySsoaccount(account);
	if(u == null)
	{
		u = new dswork.websso.model.DsWebssoUser();
	}
}
request.setAttribute("msg", msg);
%><!DOCTYPE html><html>
<%if(msg.length() == 0){%>
<head><meta charset="UTF-8" /><title>统一身份认证平台绑定跳转</title></head>
<body>
<%
String serviceURL = "/websso/gzmice/rebindAction.jsp";
request.setAttribute("serviceURL", java.net.URLEncoder.encode(serviceURL, "UTF-8"));
%>
<%if(u.getOpenidqq().length() > 0){ %>
<br>当前已绑定QQ：<%=u.getOpenidqq() %>
<%} if(u.getOpenidalipay().length() > 0){ %>
<br>当前已绑定支付宝：<%=u.getOpenidalipay() %>
<%} if(u.getOpenidwechat().length() > 0){ %>
<br>当前已绑定微信：<%=u.getOpenidwechat() %>
<%} %>
<br>
<a href="/websso/gzmice/rebind.jsp?bind=qq&serviceURL=${serviceURL}" class="icon_qq">绑定QQ</a>
<a href="/websso/gzmice/rebind.jsp?bind=alipay&serviceURL=${serviceURL}" class="icon_alipay">绑定支付宝</a>
<a href="/websso/gzmice/rebind.jsp?bind=wechat&serviceURL=${serviceURL}" class="icon_wechat">绑定微信</a>
</body>
<%}else{ %>
<head>
<meta charset="UTF-8" />
<title>Error</title>
<style>body{width: 40em;margin: 0 auto;font-family: Tahoma, Verdana, Arial, sans-serif, 微软雅黑;}</style>
</head>
<body>
<h1>发生错误。</h1>
<p>${msg}。</p>
<p><em>system administrator.</em></p>
</body>
<%} %>
</html>