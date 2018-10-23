<%@page language="java" pageEncoding="UTF-8" import="
dswork.web.MyRequest,
java.net.URLEncoder,
dswork.websso.util.WebssoUtil
"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%
try
{
	MyRequest req = new MyRequest(request);
	String bind = req.getString("bind", "").toLowerCase(java.util.Locale.ENGLISH);
	request.setAttribute("bind", bind);
	String serviceURL = URLEncoder.encode(req.getString("serviceURL"), "UTF-8");
	String authorizeURL = "about:blank";
	String url,appid,returnURL;
	if("weibo".equals(bind) && WebssoUtil.HasWeibo)
	{
		url = "https://api.weibo.com/oauth2/authorize?client_id=%s&response_type=code&redirect_uri=%s";
		returnURL = URLEncoder.encode(dswork.websso.util.WeiboUtil.RETURNURL + "?serviceURL=" + serviceURL, "UTF-8");
		appid = dswork.websso.util.WeiboUtil.APPID;
	}
	else if("wechat".equals(bind) && WebssoUtil.HasWechat)
	{
		url = "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=1#wechat_redirect";
		returnURL = URLEncoder.encode(dswork.websso.util.WechatUtil.RETURNURL + "?serviceURL=" + serviceURL, "UTF-8");
		appid = dswork.websso.util.WechatUtil.APPID;
	}
	else if("qq".equals(bind) && WebssoUtil.HasQQ)
	{
		url = "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=%s&redirect_uri=%s&state=1&scope=get_user_info";
		returnURL = URLEncoder.encode(dswork.websso.util.QqUtil.RETURNURL + "?serviceURL=" + serviceURL, "UTF-8");
		appid = dswork.websso.util.QqUtil.APPID;
	}
	else if("alipay".equals(bind) && WebssoUtil.HasAlipay)
	{
		url = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=%s&scope=auth_user&redirect_uri=%s";
		returnURL = URLEncoder.encode(dswork.websso.util.AlipayUtil.RETURNURL + "?serviceURL=" + serviceURL, "UTF-8");
		appid = dswork.websso.util.AlipayUtil.APPID;
	}
	else
	{
		url = "%s%s";
		appid = "";
		returnURL = "about:blank";
	}
	authorizeURL = String.format(url, appid, returnURL);
	request.setAttribute("authorizeURL", authorizeURL);
}
catch(Exception e)
{
	e.printStackTrace();
}
%><!DOCTYPE html>
<html>
<head><meta charset="UTF-8" /><title>统一身份认证平台跳转</title></head>
<body>
<script type="text/javascript">
var authorize = "${authorizeURL}";
<c:if test="${bind=='qq'}">
	function isPc(){return navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i)==null}
	if(!isPc()){
		authorize = authorize + "&display=mobile";
	}
<%--</c:if><c:if test="${type=='wechat'}">
</c:if><c:if test="${type=='alipay'}">--%>
</c:if>
location.href = authorize;
</script>
</body>
</html>