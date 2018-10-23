<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/addAjax.jsp"%>
<script type="text/javascript">
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getSite.htm";
}};
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">添加</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getSite.htm?page=${fn:escapeXml(param.page)}">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="addSite2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">站点名称</td>
		<td class="form_input"><input type="text" name="name" maxlength="100" datatype="Require" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">链接</td>
		<td class="form_input"><input type="text" name="url" maxlength="100" style="width:400px;" datatype="!Require" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">图片</td>
		<td class="form_input"><input type="text" name="img" maxlength="100" style="width:400px;" value="" /></td>
	</tr>
	<%--
	<tr>
		<td class="form_title">目录名称</td>
		<td class="form_input"><input type="text" name="folder" maxlength="100" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">网站模板</td>
		<td class="form_input"><input type="text" name="viewsite" maxlength="100" value="" /></td>
	</tr>
	--%>
</table>
</form>
</body>
</html>
