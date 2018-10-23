<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/updAjax.jsp"%>
<script type="text/javascript">
$dswork.callback = function(){
	try{if($dswork.result.type == 1){parent.refreshNode(true);}}catch(e){}
};
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">修改</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" onclick="parent.refreshNode(false);return false;" href="#">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="updDictData2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">标识</td>
		<td class="form_input"><input type="text" name="alias" datatype="Char" maxlength="128" value="${fn:escapeXml(po.alias)}" /></td>
	</tr>
	<tr>
		<td class="form_title">名称</td>
		<td class="form_input"><input type="text" name="label" datatype="Require" maxlength="100" value="${fn:escapeXml(po.label)}" /></td>
	</tr>
	<tr>
		<td class="form_title">备注</td>
		<td class="form_input"><input type="text" name="memo" maxlength="100" value="${fn:escapeXml(po.memo)}" /></td>
	</tr>
</table>
<input type="hidden" name="id" value="${po.id}" />
</form>
</body>
</html>
