<%@page pageEncoding="UTF-8" import="
dswork.web.MyRequest,
dswork.websso.model.weibo.WeiboAccessToken,
dswork.websso.model.weibo.WeiboTokenInfo,
dswork.websso.model.weibo.WeiboUser,
dswork.websso.util.WeiboUtil
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
	WeiboAccessToken accessToken = WeiboUtil.getAccessToken(authCode);
	WeiboTokenInfo openid = null;
	if(accessToken == null)
	{
		msg = "获取WeiboAccessToken失败";
	}
	else
	{
		openid = WeiboUtil.getOpenid(accessToken.getAccesstoken());
		if(openid == null)
		{
			msg = "获取WeiboOpenid失败";
		}
		else
		{
			WeiboUser user = WeiboUtil.getUser(accessToken.getAccesstoken(), openid.getOpenid());
			if(user == null)
			{
				msg = "获取DsWebssoUser失败";
			}
			else
			{
				int sex = 1;// m男:n未知:f女
				if("f".equals(user.getGender()))
				{
					sex = 2;// 女性
				}
				String province = "";
				String city = "";
				try
				{
					String[] arr = (user.getLocation() + "").trim().split(" ", -1);
					if(arr.length == 2)
					{
						province = arr[0];
						city = arr[1];
					}
				}
				catch(Exception x)
				{
				}
				request.setAttribute("name", user.getName());
				request.setAttribute("sex", sex);
				request.setAttribute("avatar", user.getProfileImageUrl());
				request.setAttribute("country", "中国");
				request.setAttribute("province", province);
				request.setAttribute("city", city);
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
<head><meta charset="UTF-8" /><title>统一身份认证平台微博认证</title></head>
<body>
<form action="${fn:escapeXml(serviceURL)}" method="post" style="display:none;" id="myform">
<c:if test="${msg==''}">
<input name="name" value="${fn:escapeXml(name)}" />
<input name="sex" value="${fn:escapeXml(sex)}" />
<input name="avatar" value="${fn:escapeXml(avatar)}" />
<input name="country" value="${fn:escapeXml(country)}" />
<input name="province" value="${fn:escapeXml(province)}" />
<input name="city" value="${fn:escapeXml(city)}" />
<input name="openidweibo" value="${fn:escapeXml(openid)}" />
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