<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<link rel="stylesheet" type="text/css"	href="/web/themes/default/frame.css" />
<%@include file="/commons/include/addAjax.jsp"%>
<script type="text/javascript">
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getTable.htm";
}};
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">添加</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getTable.htm?page=${fn:escapeXml(param.page)}">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="addTable2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable" id="contactTable">
	<tr>
		<td class="form_title">分类</td>
		<td class="form_input" colspan="3">
		<select name="categoryid" datatype="Require">
			<option value="">请选择类型</option>
			<c:forEach items="${categoryList}" var="type">
				<option value="${fn:escapeXml(type.id) }">${fn:escapeXml(type.name) }</option>
			</c:forEach>
		</select>
		</td>
	</tr>
	<tr>
		<td class="form_title">名称</td>
		<td class="form_input" colspan="3"><input type="text" name="name" maxlength="30" datatype="Require" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">标识【对应数据表名】</td>
		<td class="form_input" colspan="3"><input type="text" name="alias" maxlength="30" datatype="Char" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">状态(1启用,0禁用)</td>
		<td class="form_input" colspan="3">
		<select name="status" >
			<option value="1">启用</option>
			<option value="0">禁用</option>
		</select>
		
	</tr>
	<tr>
		<td class="form_title">排序</td>
		<td class="form_input" colspan="3"><input type="text" name="seq" maxlength="10" value="" /></td>
	</tr>
	<!-- <tr>
		<td class="form_title">数据结构</td>
		<td class="form_input" colspan="3" ><textarea name="datatable" style="width:400px;height:60px;"></textarea></td>
	</tr> -->
	<tr>
		<td class="form_title">扩展信息</td>
		<td class="form_input" colspan="3" ><input type="text" name="memo" maxlength="3000" value="" /></td>
	</tr>
	<tr class="list_title">
		<td>名称</td>
		<td>字段</td>
		<td>类型</td>
		<td width="8%" class="menuTool"><a class="insert"
			onclick="$('#contactTable>tbody').append($('#cloneTable>tbody>tr:eq(0)').clone());">添加</a></td>
	</tr>
</table>
</form>
<table id="cloneTable" hidden="hidden">
	<tr>
		<td><input name="tinfo" datatype="Require"></td>
		<td><input name="tname" datatype="Char"></td>
		<td><select name="ttype">
				<option value='!Require'>字符串</option>
				<option value='Require'>字符串(必填)</option>
				<option value='!Chinese'>中文</option>
				<option value='Chinese'>中文(必填)</option>
				<option value='!WebDate'>日期</option>
				<option value='WebDate'>日期(必填)</option>
				<option value='!Char'>英、数、下划线</option>
				<option value='Char'>英、数、下划线(必填)</option>
				<option value='!IdCard'>身份证号码</option>
				<option value='IdCard'>身份证号码(必填)</option>
				<option value='!Mobile'>手机号码</option>
				<option value='Mobile'>手机号码(必填)</option>
				<option value='!Money'>金额</option>
				<option value='Money'>金额(必填)</option>
				<option value='!Email'>邮件格式</option>
				<option value='Email'>邮件格式(必填)</option>
				<option value='!Integer'>纯数字</option>
				<option value='Integer'>纯数字(必填)</option>
				<option value='!UnitCode'>纯统一社会信用代码</option>
				<option value='UnitCode'>纯统一社会信用代码(必填)</option>
		</select></td>
		<td><input type="button" class="delete"
			onclick="$(this).parent().parent().remove();" /></td>
	</tr>
</table>

</body>
</html>
