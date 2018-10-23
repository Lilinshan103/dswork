<%@page language="java" pageEncoding="UTF-8" import="
dswork.web.MyRequest,
java.net.URLEncoder
"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%
MyRequest req = new MyRequest(request);
dswork.websso.model.DsWebssoUser po = new dswork.websso.model.DsWebssoUser();
req.getFillObject(po);
String bind = req.getString("bind", "").toLowerCase(java.util.Locale.ENGLISH);
String service = URLEncoder.encode(req.getString("service"), "UTF-8");
String msg = "";
String ticket = "";
String code = "";
String account = "";
String uname = "qq".equals(bind) ? "QQ" : ("wechat".equals(bind) ? "微信" : ("alipay".equals(bind) ? "支付宝" : ("weibo".equals(bind) ? "微博" : "")));
try
{
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
		dswork.websso.model.DsWebssoUser u = ssoservice.getBySsoaccount(account);
		if(u == null)
		{
			po.setSsoaccount(account);
			if(ssoservice.saveForRebind(po) <= 0)
			{
				msg = "请重新登录后再尝试";// 只有插入时账号已存在且没异常的情况下，才会出现
			}
		}
		else
		{
			dswork.websso.model.DsWebssoUser tmp = ssoservice.getByOpendid(po);
			if(tmp != null)// 账号已经绑定，否则为未绑定
			{
				// 账号已经绑定其他账号，否则为重新绑定
				if(tmp.getId().longValue() != u.getId().longValue())
				{
					msg = "该" + uname + "账号已绑定其他用户";
				}
			}
			if("qq".equals(bind))
			{
				u.setOpenidqq(po.getOpenidqq());
			}
			else if("weibo".equals(bind))
			{
				u.setOpenidweibo(po.getOpenidweibo());
			}
			else if("wechat".equals(bind))
			{
				u.setOpenidwechat(po.getOpenidwechat());
			}
			else if("alipay".equals(bind))
			{
				u.setOpenidalipay(po.getOpenidalipay());
			}
			else
			{ 
				msg = "请勿非法操作";
				bind = "";
			}
			if(bind.length() > 0)
			{
				if(po.getName().length() > 0){u.setName(po.getName());}
				if(po.getCountry().length() > 0){u.setCountry(po.getCountry());}
				if(po.getProvince().length() > 0){u.setProvince(po.getProvince());}
				if(po.getCity().length() > 0){u.setCity(po.getCity());}
				if(po.getMobile().length() > 0){u.setMobile(po.getMobile());}
				ssoservice.updateForRebind(u);
				msg = "";
			}
		}
	}
}
catch(Exception e)
{
	e.printStackTrace();
	msg = e.getMessage();
}
%><!DOCTYPE html><html>
<head><meta charset="UTF-8" /><title>统一身份认证平台绑定绑定</title></head>
<head>
<meta charset="UTF-8" />
<title>Error</title>
<style>body{width: 40em;margin: 0 auto;font-family: Tahoma, Verdana, Arial, sans-serif, 微软雅黑;}</style>
</head>
<body>
<h1><%=msg.length()==0?"绑定成功":"发生错误"%>。</h1>
<p><%=msg.length()==0?"已成功绑定新的"+uname+"账号":msg%>。</p>
<p><em>system administrator.</em></p>
</body>
</body>
</html>