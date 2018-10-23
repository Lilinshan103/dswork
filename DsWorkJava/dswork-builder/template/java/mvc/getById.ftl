<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/getById.jsp"%>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">明细</td>
		<td class="menuTool">
			<a class="back" onclick="window.history.back();return false;" href="#">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
<#list table.columnNokey as c>
	<#if c.length<=4000>
	<tr>
		<td class="form_title">${c.comment}</td>
		<td class="form_input">${'$'}{fn:escapeXml(po.${c.nameLowerCamel})}</td>
	</tr>
	<#else>
	<tr>
		<td class="form_title">${c.comment}</td>
		<td class="form_input"><textarea class="readonlytext" style="width:400px;height:60px;">${'$'}{fn:escapeXml(po.${c.nameLowerCamel})}</textarea></td>
	</tr>
	</#if>
</#list>
</table>
</body>
</html>
