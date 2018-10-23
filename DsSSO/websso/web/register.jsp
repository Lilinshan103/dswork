<%@page language="java" pageEncoding="UTF-8" import="
java.net.URLEncoder,
dswork.websso.util.WebssoUtil,
dswork.web.MyRequest
"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%
request.setAttribute("hasQq", WebssoUtil.HAS_QQ);
request.setAttribute("hasWechat", WebssoUtil.HAS_WECHAT);
request.setAttribute("hasAlipay", WebssoUtil.HAS_ALIPAY);

MyRequest req = new MyRequest(request);
String msg = req.getString("msg");
String type = "2".equals(req.getString("type", "2"))?"2":"1";// 2企业1个人
String serviceURL = "/websso/registerAction.jsp?type=" + type + "&service=" + URLEncoder.encode(URLEncoder.encode(req.getString("serviceURL"), "UTF-8"), "UTF-8");
request.setAttribute("serviceURL", URLEncoder.encode(serviceURL, "UTF-8"));
%><html>
<body>
<c:if test="${hasQq}"><a href="/websso/bind/login.jsp?bind=qq&serviceURL=${serviceURL}">QQ</a></c:if>
<c:if test="${hasWechat}"><a href="/websso/bind/login.jsp?bind=wechat&serviceURL=${serviceURL}">微信</a></c:if>
<c:if test="${hasAlipay}"><a href="/websso/bind/login.jsp?bind=alipay&serviceURL=${serviceURL}">支付宝</a></c:if>
</body>
</html>