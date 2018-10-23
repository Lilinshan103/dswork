<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
$(function(){
	$${frame}.page.menu("del${model}.htm", "upd${model}1.htm", "get${model}ById.htm", "${'$'}{pageModel.currentPage}");
});
$${frame}.doAjax = true;
$${frame}.callback = function(){if($${frame}.result.type == 1){
	location.href = "get${model}.htm?page=${'$'}{pageModel.currentPage}";
}};
</script>
</head> 
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">${table.comment}列表</td>
		<td class="menuTool">
			<a class="insert" href="add${model}1.htm?page=${'$'}{pageModel.currentPage}">添加</a>
			<a class="delete" id="listFormDelAll" href="#">删除所选</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="queryForm" method="post" action="get${model}.htm">
<table border="0" cellspacing="0" cellpadding="0" class="queryTable">
	<tr>
		<td class="input">
<#list table.columnNokey as c>
			&nbsp;${c.comment}：<input type="text" class="text" name="${c.nameLowerCamel}" value="${'$'}{fn:escapeXml(param.${c.nameLowerCamel})}" />
</#list>
		</td>
		<td class="query"><input id="_querySubmit_" type="button" class="button" value="查询" /></td>
	</tr>
</table>
</form>
<div class="line"></div>
<form id="listForm" method="post" action="del${model}.htm">
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:2%"><input id="chkall" type="checkbox" /></td>
		<td style="width:5%">操作</td>
<#list table.columnNokey as c>
		<td>${c.comment}</td>
</#list>
	</tr>
<c:forEach items="${'$'}{pageModel.result}" var="d">
	<tr>
		<td><input name="keyIndex" type="checkbox" value="${'$'}{d.id}" /></td>
		<td class="menuTool" keyIndex="${'$'}{d.id}">&nbsp;</td>
<#list table.columnNokey as c>
		<td>${'$'}{fn:escapeXml(d.${c.nameLowerCamel})}</td>
</#list>
	</tr>
</c:forEach>
</table>
<input name="page" type="hidden" value="${'$'}{pageModel.currentPage}" />
</form>
<table border="0" cellspacing="0" cellpadding="0" class="bottomTable">
	<tr><td>${'$'}{pageNav.page}</td></tr>
</table>
</body>
</html>
