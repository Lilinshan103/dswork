<%@page language="java" pageEncoding="UTF-8"
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><%
dswork.web.MyRequest req = new dswork.web.MyRequest(request);
String msg = req.getString("msg");
String type = "2".equals(req.getString("type", "2"))?"2":"1";// 2企业1个人
String ssotoken = "";
dswork.websso.model.DsWebssoUser po = new dswork.websso.model.DsWebssoUser();
String serviceURL = java.net.URLDecoder.decode(java.net.URLDecoder.decode(req.getString("service", ""), "UTF-8"), "UTF-8");// get方式传过来的
if(serviceURL.length() == 0)
{
	msg = "参数错误";
}
else
{
	if("".equals(msg))
	{
		req.getFillObject(po);
		try
		{
			dswork.websso.service.DsWebssoUserService service = (dswork.websso.service.DsWebssoUserService)dswork.spring.BeanFactory.getBean("dsWebssoUserService");
			if(service.saveForRegister(po) == 0)
			{
				msg = "该"+(po.getOpenidqq().length() > 0 ? "QQ" : (po.getOpenidwechat().length() > 0 ? "微信" : (po.getOpenidalipay().length() > 0 ? "支付宝" : "")))+"已注册用户";
			}
			else
			{
				ssotoken = dswork.core.util.EncryptUtil.encryptMd5(po.getSsoaccount() + "skeywebsso");
			}
		}
		catch(Exception e)
		{
			msg = e.getMessage();
		}
	}
}
request.setAttribute("serviceURL", serviceURL);
request.setAttribute("msg", msg);
request.setAttribute("po", po);
request.setAttribute("ssotoken", ssotoken);
request.setAttribute("type", type);
%><!DOCTYPE html><html>
<head>
<meta charset="UTF-8" /><title>统一身份认证平台</title>
</head>
<body>
<form action="/sso/webssoAction.jsp" method="post" style="display:none" id="myform">
<input name="account" value="${fn:escapeXml(po.ssoaccount)}" />
<input name="ssotoken" value="${fn:escapeXml(ssotoken)}" />
<input name="loginURL" value="/websso/gzmice/register.jsp?type=${type}" />
<input name="service" value="${fn:escapeXml(serviceURL)}" />
<input name="msg" value="${fn:escapeXml(msg)}" />
</form>
<script type="text/javascript">
document.getElementById('myform').submit();
</script>
</body>
</html>
