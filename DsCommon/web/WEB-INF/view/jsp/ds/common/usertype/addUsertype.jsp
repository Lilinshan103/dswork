<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/addAjax.jsp"%>
<script type="text/javascript">
$dswork.deleteRow = function (obj){$(obj).parent().parent().remove();};
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getUsertype.htm";
}};
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">添加</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getUsertype.htm?page=${fn:escapeXml(param.page)}">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="addUsertype2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">名称</td>
		<td class="form_input"><input type="text" name="name" datatype="Name" maxlength="100" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">数字标识</td>
		<td class="form_input"><input type="text" name="alias" datatype="Integer" maxlength="10" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">扩展信息</td>
		<td class="form_input"><textarea name="memo" style="width:400px;height:60px;"></textarea></td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">用户资源列表</td>
		<td class="menuTool">
			<a class="insert" onclick="$('#contactTable>tbody').append($('#cloneTable>tbody>tr:eq(0)').clone());" href="#">添加</a>
		</td>
	</tr>
</table>
<input type="hidden" name="status" maxlength="100" value="1" />
<div class="line"></div>
<table id="contactTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:60%;">资源编码</td>
		<td style="width:32%;">资源名称</td>
		<td style="width:8%;">操作</td>
	</tr>
</table>
</form>
<div style="display:none;">
<table id="cloneTable">
	<tr class="list">
		<td><input name="ralias" type="text" datatype="Require" maxlength="100" title="示例：0" /></td>
		<td><input name="rname" type="text" maxlength="100" title="示例：系统用户" /></td>
		<td><input type="button" class="delete" onclick="$dswork.deleteRow(this)" /></td>
	</tr>
</table>
</div>
</body>
</html>
