<%@page language="java" pageEncoding="UTF-8"%><%
String service = "/gzmice/index.jsp";
String servicex = java.net.URLEncoder.encode(java.net.URLEncoder.encode(request.getAttribute("service"), "UTF-8"), "UTF-8");
String serviceURL = "/websso/gzmice/loginAction.jsp?service=" + servicex;
request.setAttribute("serviceURL", java.net.URLEncoder.encode(serviceURL, "UTF-8"));
%>
<a href="/websso/bind/login.jsp?bind=qq&serviceURL=${fn:escapeXml(serviceURL)}" class="icon_qq"></a>
<a onclick="return false;" href="#" class="icon_weibo"></a>
<a href="/websso/bind/login.jsp?bind=alipay&serviceURL=${fn:escapeXml(serviceURL)}" class="icon_alipay"></a>
<a href="/websso/bind/login.jsp?bind=wechat&serviceURL=${fn:escapeXml(serviceURL)}" class="icon_wechat"></a>