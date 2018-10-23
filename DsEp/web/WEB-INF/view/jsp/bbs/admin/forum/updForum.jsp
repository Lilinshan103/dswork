<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/updAjax.jsp" %>
<script type="text/javascript">
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getForum.htm?siteid=${fn:escapeXml(param.siteid)}";
}};
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">修改</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getForum.htm?siteid=${fn:escapeXml(param.siteid)}">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="updForum2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<c:if test="${po.pid>0}">
	<tr>
		<td class="form_title">上级</td>
		<td class="form_input">
		<select id="pid" name="pid">
		<c:forEach items="${list}" var="d">
			<option value="${d.id}"${po.pid==d.id?' selected="selected"':''}>${d.label}${fn:escapeXml(d.name)}</option>
		</c:forEach>
		</select>
		</td>
	</tr>
	</c:if>
	<tr>
		<td class="form_title">${po.pid==0?"分区":"版块"}名称</td>
		<td class="form_input"><input type="text" name="name" maxlength="100" datatype="Require" value="${fn:escapeXml(po.name)}" /></td>
	</tr>
	<tr>
		<td class="form_title">摘要</td>
		<td class="form_input"><input type="text" name="summary" maxlength="100" style="width:400px;" value="${fn:escapeXml(po.summary)}" /></td>
	</tr>
	<tr>
		<td class="form_title">排序</td>
		<td class="form_input"><input type="text" name="seq" maxlength="4" datatype="Integer" value="${fn:escapeXml(po.seq)}" /></td>
	</tr>
	<tr>
		<td class="form_title">状态</td>
		<td class="form_input">
			<label><input type="radio" name="status" value="1"${fn:escapeXml(po.status == 1 ? ' checked="checked"':'')} />启用</label>
			<label><input type="radio" name="status" value="0"${fn:escapeXml(po.status == 1 ? '':' checked="checked"')} />禁用</label>
	</tr>
</table>
<input type="hidden" name="id" value="${po.id}" />
</form>
</body>
</html>
