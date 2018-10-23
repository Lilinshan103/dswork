<%@page pageEncoding="UTF-8" import="
dswork.web.MyRequest,
dswork.websso.model.wechat.WechatAccessToken,
dswork.websso.model.wechat.WechatUserinfo,
dswork.websso.util.WechatUtil
"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><%
String msg = "";
try
{
	MyRequest req = new MyRequest(request);
	String serviceURL = req.getString("serviceURL", "about:blank");
	request.setAttribute("serviceURL", serviceURL);

	String authCode = req.getString("code");
	WechatAccessToken accessToken = WechatUtil.getAccessToken(authCode);
	if(accessToken == null)
	{
		msg = "获取WechatAccessToken失败";
	}
	else
	{
		WechatUserinfo user = WechatUtil.getUserInfo(accessToken.getAccesstoken(), accessToken.getOpenid());
		if(user == null)
		{
			msg = "获取WechatUserinfo失败";
		}
		else
		{
			int sex = 1;// 男性"1".equals(user.getSex())
			if("2".equals(user.getSex()))
			{
				sex = 2;// 女性
			}
			request.setAttribute("name", user.getNickname());
			request.setAttribute("sex", sex);
			request.setAttribute("avatar", user.getHeadimgurl());
			request.setAttribute("country", user.getCountry());
			request.setAttribute("province", user.getProvince());
			request.setAttribute("city", user.getCity());
			request.setAttribute("openid", user.getOpenid());
		}
	}
}
catch(Exception e)
{
	e.printStackTrace();
	msg = e.getMessage();
}
request.setAttribute("msg", msg);
%><!DOCTYPE html>
<html>
<head><meta charset="UTF-8" /><title>统一身份认证平台微信认证</title></head>
<body>
<form action="${fn:escapeXml(serviceURL)}" method="post" style="display:none;" id="myform">
<c:if test="${msg==''}">
<input name="name" value="${fn:escapeXml(name)}" />
<input name="sex" value="${fn:escapeXml(sex)}" />
<input name="avatar" value="${fn:escapeXml(avatar)}" />
<input name="country" value="${fn:escapeXml(country)}" />
<input name="province" value="${fn:escapeXml(province)}" />
<input name="city" value="${fn:escapeXml(city)}" />
<input name="openidwechat" value="${fn:escapeXml(openid)}" />
</c:if>
<c:if test="${msg!=''}">
<input name="msg" value="${fn:escapeXml(msg)}" />
</c:if>
</form>
<script type="text/javascript">
document.getElementById('myform').submit();
</script>
</body>
</html>