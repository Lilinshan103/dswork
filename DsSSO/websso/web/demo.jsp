<%@page language="java" pageEncoding="UTF-8" import="
java.net.URLEncoder,
dswork.websso.util.WebssoUtil
"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><c:set var="ctx" value="${pageContext.request.contextPath}"
/><%
request.setAttribute("hasQq", WebssoUtil.HAS_QQ);
request.setAttribute("hasWechat", WebssoUtil.HAS_WECHAT);
request.setAttribute("hasAlipay", WebssoUtil.HAS_ALIPAY);

String serviceURL = "/websso/demoAction.jsp";
serviceURL = URLEncoder.encode(serviceURL, "UTF-8");
request.setAttribute("serviceURL", serviceURL);
%><html>
<body>
<%-- <c:if test="${hasQq}"><a href="loginQq.jsp?serviceURL=${serviceURL}"><img alt="QQ登录" src="${ctx}/img/qq_logo.png"></a></c:if> --%>
<%-- <c:if test="${hasWechat}"><a href="loginWechat.jsp?serviceURL=${serviceURL}"><img alt="微信登录" src="${ctx}/img/wechat_logo.png"></a></c:if> --%>
<%-- <c:if test="${hasAlipay}"><a href="loginAlipay.jsp?serviceURL=${serviceURL}"><img alt="支付宝登录" src="${ctx}/img/alipay_logo.jpg"></a></c:if> --%>
<c:if test="${hasQq}"><a href="/websso/bind/login.jsp?type=qq&serviceURL=${serviceURL}">QQ</a></c:if>
<c:if test="${hasWechat}"><a href="/websso/bind/login.jsp?type=wechat&serviceURL=${serviceURL}">微信</a></c:if>
<c:if test="${hasAlipay}"><a href="/websso/bind/login.jsp?type=alipay&serviceURL=${serviceURL}">支付宝</a></c:if>
</body>
</html>