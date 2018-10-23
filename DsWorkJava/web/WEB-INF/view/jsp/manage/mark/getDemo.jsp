<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript">
// 扩展菜单写法
$dswork.page.join = function(td, menu, id){
	$(menu).append($('<div iconCls="menuTool-user">评论</div>').bind("click", function(){
		location.href = "getMark.htm?page=${pageModel.currentPage}&keyIndex=" + id;
	}));
};
$dswork.doAjax = true;
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getDemo.htm?page=${pageModel.currentPage}";
}};
$(function(){
	$dswork.page.menu("", "", "", "${pageModel.currentPage}");
});
</script>
</head> 
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">DEMO列表</td>
	</tr>
</table>
<div class="line"></div>
<form id="queryForm" method="post" action="getDemo.htm">
<table border="0" cellspacing="0" cellpadding="0" class="queryTable">
	<tr>
		<td class="input">
			&nbsp;标题：<input type="text" name="title" value="${fn:escapeXml(param.title)}" />
			&nbsp;内容：<input type="text" name="content" value="${fn:escapeXml(param.content)}" />
			&nbsp;创建时间：<input type="text" name="foundtime" value="${fn:escapeXml(param.foundtime)}" />
		</td>
		<td class="query"><input id="_querySubmit_" type="button" class="button" value="查询" /></td>
	</tr>
</table>
</form>
<div class="line"></div>
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:5%">操作</td>
		<td>标题</td>
		<td>内容</td>
		<td>创建时间</td>
		<td style="width:15%">操作</td>
	</tr>
<c:forEach items="${pageModel.result}" var="d">
	<tr>
		<td class="menuTool" keyIndex="${d.id}">&nbsp;</td>
		<td>${d.title}</td>
		<td>${d.content}</td>
		<td>${d.foundtime}</td>
		<td class="menuTool">
			<a class="select" href="getMark.htm?keyIndex=${d.id}&page=${pageModel.currentPage}">评论</a>
		</td>
	</tr>
</c:forEach>
</table>
<table border="0" cellspacing="0" cellpadding="0" class="bottomTable">
	<tr><td>${pageNav.page}</td></tr>
</table>
<br />
</body>
</html>
