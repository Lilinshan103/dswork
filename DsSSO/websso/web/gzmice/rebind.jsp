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
String bind = req.getString("bind", "").toLowerCase(java.util.Locale.ENGLISH);
String service = URLEncoder.encode(req.getString("service"), "UTF-8");
String msg = "";
String ticket = "";
String code = "";
String account = "";
for(Cookie c : request.getCookies())
{
	if("DS_SSO_TICKET".equals(c.getName())){ticket = c.getValue();}
	else if("DS_SSO_CODE".equals(c.getName())){code = c.getValue();}
}
if(code.length() > 0 && ticket.length() > 0)
{
	String s = dswork.core.util.EncryptUtil.decodeDes(code, ticket);
	account = s.split("#")[0];//通过cookie取sso的登陆用户account
}
if(account.length() == 0)
{
	msg = "请登录后再进行相关操作";
}
else
{
	if("weibo".equals(bind) && WebssoUtil.HasWeibo)
	{
	}
	else if("wechat".equals(bind) && WebssoUtil.HasWechat)
	{
	}
	else if("qq".equals(bind) && WebssoUtil.HasQQ)
	{
	}
	else if("alipay".equals(bind) && WebssoUtil.HasAlipay)
	{
	}
	else
	{
		if("weibo".equals(bind))
		{
			msg = "暂不支持微博账号绑定";
		}
		else if("wechat".equals(bind))
		{
			msg = "暂不支持微信账号绑定";
		}
		else if("qq".equals(bind))
		{
			msg = "暂不支持QQ账号绑定";
		}
		else if("alipay".equals(bind))
		{
			msg = "暂不支持支付宝账号绑定";
		}
		else
		{
			msg = "请勿非法操作";
			bind = "";
		}
	}
}
request.setAttribute("msg", msg);
%><!DOCTYPE html><html>
<c:if test="${msg==''}"><%
String serviceURL = "/websso/gzmice/rebindAction.jsp?bind=" + bind + "&service=" + java.net.URLEncoder.encode(java.net.URLEncoder.encode(service, "UTF-8"), "UTF-8");
String url = "/websso/bind/login.jsp?bind=" + bind + "&serviceURL=" + java.net.URLEncoder.encode(serviceURL, "UTF-8");
request.setAttribute("serviceURL", url);
%>
<head><meta charset="UTF-8" /><title>统一身份认证平台绑定跳转</title></head>
<body>
<script type="text/javascript">location.href = "${serviceURL}";</script>
</body>
</c:if>
<c:if test="${msg!=''}">
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
</c:if>
</html>