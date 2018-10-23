<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/addAjax.jsp"%>
<script type="text/javascript">
$dswork.deleteRow = function (obj){$(obj).parent().parent().remove();};
$dswork.callback = function(){
	try{if($dswork.result.type == 1){parent.refreshNode(true);}}catch(e){}
};
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">添加</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" onclick="parent.refreshNode(false);return false;" href="#">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="addDictData2.htm">
<input type="hidden" name="dictid" value="${dict.id}" />
<input type="hidden" name="pid" value="${pid}" />
<table id="contactTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:45%;">标识</td>
		<td style="width:30%;">名称</td>
		<td>备注</td>
		<td style="width:8%;">操作</td>
	</tr>
	<tr class="list">
		<td><input type="text" name="alias" datatype="Char" maxlength="128" value="" /></td>
		<td><input type="text" name="label" datatype="Require" maxlength="100" value="" /></td>
		<td><input type="text" name="memo" maxlength="100" style="width:100px;" value="" /></td>
		<td class="menuTool"><a class="insert" onclick="$('#contactTable>tbody').append($('#cloneTable>tbody>tr:eq(0)').clone());" href="#">添加项</a></td>
	</tr>
</table>
</form>
<div style="display:none;">
<table id="cloneTable">
	<tr class="list">
		<td><input type="text" name="alias" datatype="Char" maxlength="128" value="" /></td>
		<td><input type="text" name="label" datatype="Require" maxlength="100" value="" /></td>
		<td><input type="text" name="memo" maxlength="100" style="width:100px;" value="" /></td>
		<td><input type="button" class="delete" onclick="$dswork.deleteRow(this)" /></td>
	</tr>
</table>
</div>
</body>
</html>
