<%@page language="java" pageEncoding="UTF-8" import="dswork.spring.BeanFactory,dswork.core.page.*,dswork.web.*,common.any.AnyDao"%><%!
public boolean isNotEmpty(PageRequest pr, String key){
	Object o = pr.getFilters().get(key);
	return o != null && String.valueOf(o).length() > 0;
}
public PageRequest genSQL(PageRequest pr, boolean isCount){
	StringBuilder sql = new StringBuilder(50);
	sql.append("select");
	if(isCount){
		sql.append(" count(1)");
	}
	else{
		sql.append(" id, logintime, logouttime, timeouttime, pwdtime, ip, account, name, status");
	}
	sql.append(" from DS_COMMON_LOGIN where 1=1");
	if(isNotEmpty(pr, "logintime_begin")){
		sql.append(" and LOGINTIME>=#{logintime_begin}");
	}
	if(isNotEmpty(pr, "logintime_end")){
		sql.append(" and LOGINTIME<=#{logintime_end}");
	}
	if(isNotEmpty(pr, "account")){
		sql.append(" and ACCOUNT like #{account, typeHandler=LikeTypeHandler}");
	}
	if(isNotEmpty(pr, "name")){
		sql.append(" and NAME like #{name, typeHandler=LikeTypeHandler}");
	}
	if(isNotEmpty(pr, "status")){
		sql.append(" and STATUS=#{status}");
	}
	if(isNotEmpty(pr, "pwdtime")){
		sql.append(" and PWDTIME is not null");
	}
	sql.append(" order by ID desc");
	pr.getFilters().put("sql", sql.toString());
	return pr;
}
public PageRequest getPageRequest(HttpServletRequest request)
{
	int pagesize = 10;
	MyRequest req = new MyRequest(request);
	PageRequest pr = new PageRequest();
	pr.setFilters(req.getParameterValueMap(false, false));
	pr.setCurrentPage(req.getInt("page", 1));
	try
	{
		pagesize = Integer.parseInt(String.valueOf(request.getSession().getAttribute("dswork_session_pagesize")).trim());
	}
	catch(Exception ex)
	{
		pagesize = 10;
	}
	pagesize = req.getInt("pageSize", pagesize);
	request.getSession().setAttribute("dswork_session_pagesize", pagesize);
	pr.setPageSize(pagesize);
	return pr;
}
%><%
AnyDao dao = (AnyDao)BeanFactory.getBean("anyDao");
PageRequest pr = getPageRequest(request);
PageRequest prcount = getPageRequest(request);
Page pageModel = dao.queryPage(genSQL(pr, false), genSQL(prcount, true));
request.setAttribute("pageModel", pageModel);
request.setAttribute("pageNav", new PageNav<java.util.Map<String, Object>>(request, pageModel));
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<title></title>
	<%@include file="/commons/include/get.jsp" %>
	<style type="text/css">
		td.L {text-align:left;padding-left:2px;}
	</style>
</head> 
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">登录日志</td>
	</tr>
</table>
<div class="line"></div>
<form id="queryForm" method="post" action="getCommonLogin.jsp">
<table border="0" cellspacing="0" cellpadding="0" class="queryTable">
	<tr>
		<td class="input">
			&nbsp;账号:<input name="account" style="width:100px;" value="${fn:escapeXml(param.account)}" />
			姓名:<input name="name" style="width:80px;" value="${fn:escapeXml(param.name)}" />
			状态：<select name="status" style="width:60px;" v="${fn:escapeXml(param.status)}"><option value="">全部</option><option value="1">成功</option><option value="0">失败</option></select>
			登录时间：<input type="text" name="logintime_begin" class="WebDate" format="yyyy-MM-dd HH:mm:ss" style="width:145px;" value="${fn:escapeXml(param.logintime_begin)}" />
			至&nbsp;<input type="text" name="logintime_end" class="WebDate" format="yyyy-MM-dd HH:mm:ss" style="width:145px;" value="${fn:escapeXml(param.logintime_end)}" />
		</td>
		<td class="query"><input id="_querySubmit_" type="submit" class="button" value="查询" /></td>
	</tr>
</table>
</form>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:23%;">姓名(账号)</td>
		<td style="width:18%;">登录时间</td>
		<td style="width:10%;">状态</td>
		<td style="width:15%;">访问来源</td>
		<td style="width:8%;">超时退出</td>
		<td style="width:8%;">密码操作</td>
		<td style="width:18%;">退出时间</td>
	</tr>
<c:forEach items="${pageModel.result}" var="d" varStatus="status">
	<tr class="${status.index%2==0?'list_even':'list_odd'}">
		<td class="L">${d.name}(${d.account})</td>
		<td>${d.logintime}</td>
		<td>登录${d.status == 1 ? '成功' : '失败'}</td>
		<td>${d.ip}</td>
		<td>${d.timeouttime==''||d.timeouttime==null ? '否' : '是'}</td>
		<td>${d.pwdtime==''||d.pwdtime==null ? '无' : '修改密码'}</td>
		<td>${d.logouttime=='0' ? '未知' : d.logouttime}</td>
	</tr>
</c:forEach>
</table>
<table border="0" cellspacing="0" cellpadding="0" class="bottomTable">
	<tr><td>${pageNav.page}</td></tr>
</table>
</body>
</html>
