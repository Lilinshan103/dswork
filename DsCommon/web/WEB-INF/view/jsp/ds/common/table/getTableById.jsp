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
	<tr>
		<td class="form_title">分类</td>
		<td class="form_input">${fn:escapeXml(po.categoryid)}</td>
	</tr>
	<tr>
		<td class="form_title">名称</td>
		<td class="form_input">${fn:escapeXml(po.name)}</td>
	</tr>
	<tr>
		<td class="form_title">标识【对应数据表名】</td>
		<td class="form_input">${fn:escapeXml(po.alias)}</td>
	</tr>
	<tr>
		<td class="form_title">状态</td>
		<td class="form_input">${fn:escapeXml(po.status)==1?'启用':'禁用'}</td>
	</tr>
	<tr>
		<td class="form_title">排序</td>
		<td class="form_input">${fn:escapeXml(po.seq)}</td>
	</tr>
	<tr>
		<td class="form_title">扩展信息</td>
		<td class="form_input">${fn:escapeXml(po.memo)}</td>
	</tr>
	<c:forEach items="${map }" var="key1">
		<tr>
			<td class="form_title">${key1.value.info}</td>
			<td class="form_input">${key1.value.name}</td>
		</tr>
	</c:forEach>
</table>
</body>
</html>
