<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/updAjax.jsp"%>
<script type="text/javascript">
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getTable.htm?page=${page}";
}};
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">修改</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getTable.htm?page=${page}">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="updTable2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable" id="contactTable">
	<tr>
		<td class="form_title">分类</td>
		<td class="form_input" colspan="3">
			<select name="categoryid" datatype="Require" v="${fn:escapeXml(po.categoryid)}">
				<option>请选择类型</option>
				<c:forEach items="${typeList}" var="type">
					<option value="${fn:escapeXml(type.id) }">
								${fn:escapeXml(type.name) }
					</option>
				</c:forEach>
			</select>
	</tr>
	<tr>
		<td class="form_title">名称</td>
		<td class="form_input" colspan="3"><input type="text" name="name" maxlength="30" value="${fn:escapeXml(po.name)}" datatype="Require" /></td>
	</tr>
	<tr>
		<td class="form_title">标识【对应数据表名】</td>
		<td class="form_input" colspan="3"><input type="text" name="alias" maxlength="30" value="${fn:escapeXml(po.alias)}" datatype="Char" /></td>
	</tr>
	<tr>
		<td class="form_title">状态(1启用,0禁用)</td>
		<td class="form_input" colspan="3">
			<select name="status" v="${fn:escapeXml(po.status)}">
				<option value="1">启用</option>
				<option value="0">禁用</option>
			</select>
		</td>
	</tr>
	<tr>
		<td class="form_title">排序</td>
		<td class="form_input" colspan="3"><input type="text" name="seq" maxlength="10" value="${fn:escapeXml(po.seq)}" /></td>
	</tr>
	<tr>
		<td class="form_title">扩展信息</td>
		<td class="form_input" colspan="3"><input type="text" name="memo" maxlength="3000" value="${fn:escapeXml(po.memo)}" /></td>
	</tr>
	<tr class="list_title">
				<td>名称</td>
				<td>字段</td>
				<td>类型</td>
				<td width="8%" class="menuTool"><a class="insert"
					onclick="$('#contactTable>tbody').append($('#cloneTable>tbody>tr:eq(0)').clone());">添加</a></td>
			</tr>
	<c:forEach items="${map }" var="map2">
	<tr>
		<td><input name="tinfo" datatype="Require"
			value="${fn:escapeXml(map2.value.name)}"></td>
		<td><input name="tname" datatype="Char"
			value="${fn:escapeXml(map2.value.name)}"></td>
		<td><select name="ttype" v="${map2.value.datatype }">
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
			</select>
		</td>
		<td><input type="button" class="delete"
				onclick="$(this).parent().parent().remove();"/></td>
	</tr>
	</c:forEach>
</table>
<input type="hidden" name="id" value="${po.id}" />
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
