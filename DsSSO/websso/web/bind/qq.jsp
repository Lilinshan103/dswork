<%@page pageEncoding="UTF-8" import="
dswork.web.MyRequest,
dswork.websso.model.qq.QqAccessToken,
dswork.websso.model.qq.QqOpenid,
dswork.websso.model.qq.QqUserinfo,
dswork.websso.util.QqUtil
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
	QqAccessToken accessToken = QqUtil.getAccessToken(authCode);
	QqOpenid openid = null;
	if(accessToken == null)
	{
		msg = "获取QqAccessToken失败";
	}
	else
	{
		openid = QqUtil.getOpenid(accessToken.getAccesstoken());
		if(openid == null)
		{
			msg = "获取QqOpenid失败";
		}
		else
		{
			QqUserinfo user = QqUtil.getUserInfo(accessToken.getAccesstoken(), openid.getOpenid());
			if(user == null)
			{
				msg = "获取DsWebssoUser失败";
			}
			else
			{
				int sex = 1;
				if(!"男".equals(user.getGender()))
				{
					sex = 2;// 女性
				}
				request.setAttribute("name", user.getNickname());
				request.setAttribute("sex", sex);
				request.setAttribute("avatar", user.getFigureurl());
				request.setAttribute("country", "中国");
				request.setAttribute("province", "");
				request.setAttribute("city", "");
				request.setAttribute("openid", user.getOpenid());
			}
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
<head><meta charset="UTF-8" /><title>统一身份认证平台QQ认证</title></head>
<body>
<form action="${fn:escapeXml(serviceURL)}" method="post" style="display:none;" id="myform">
<c:if test="${msg==''}">
<input name="name" value="${fn:escapeXml(name)}" />
<input name="sex" value="${fn:escapeXml(sex)}" />
<input name="avatar" value="${fn:escapeXml(avatar)}" />
<input name="country" value="${fn:escapeXml(country)}" />
<input name="province" value="${fn:escapeXml(province)}" />
<input name="city" value="${fn:escapeXml(city)}" />
<input name="openidqq" value="${fn:escapeXml(openid)}" />
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