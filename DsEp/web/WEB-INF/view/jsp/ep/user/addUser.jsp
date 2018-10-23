<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.Date" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/addAjax.jsp" %>
<script type="text/javascript">
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getUser.htm";
}};
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">添加</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getUser.htm?page=${fn:escapeXml(param.page)}">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="addUser2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">帐号：</td>
		<td class="form_input"><input type="text" id="account" name="account" datatype="Char" maxlength="50" value="" /> <span class="imp">*</span> <span style="font-weight:bold;">添加后不可修改</span></td>
	</tr>
	<tr>
		<td class="form_title">姓名：</td>
		<td class="form_input"><input type="text" id="name" name="name" datatype="Require" maxlength="25" value="" /> <span class="imp">*</span></td>
	</tr>
	<tr>
		<td class="form_title">密码：</td>
		<td class="form_input"><input type="password" id="password" name="password" style="width:130px;" datatype="Require" maxlength="32" value="000000" /> <span class="imp">*</span> <span style="font-weight:bold;">默认密码为：000000</span></td>
	</tr>
	<tr>
		<td class="form_title">确认密码：</td>
		<td class="form_input"><input type="password" id="password2" name="password2" style="width:130px;" datatype="Repeat" to="password" msg="两次输入的密码不一致" value="000000" /> <span class="imp">*</span></td>
	</tr>
	<tr>
		<td class="form_title">身份证号：</td>
		<td class="form_input"><input type="text" id="idcard" name="idcard" style="width:200px;" datatype="!IdCard" maxlength="18" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">邮箱：</td>
		<td class="form_input"><input type="text" id="email" name="email" style="width:200px;" datatype="!Email" maxlength="250" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">手机：</td>
		<td class="form_input"><input type="text" id="mobile" name="mobile" datatype="!Mobile" maxlength="50" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">电话：</td>
		<td class="form_input"><input type="text" id="phone" name="phone" datatype="!Phone" maxlength="50" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">传真：</td>
		<td class="form_input"><input type="text" name="fax" maxlength="100" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">工作证号：</td>
		<td class="form_input"><input type="text" name="workcard" maxlength="100" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">CA证书的KEY：</td>
		<td class="form_input"><input type="text" name="cakey" maxlength="100" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">所属单位：</td>
		<td class="form_input"><input type="text" name="ssdw" maxlength="100" style="width:200px;" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">所属部门：</td>
		<td class="form_input"><input type="text" name="ssbm" maxlength="100" style="width:200px;" value="" /></td>
	</tr>
</table>
</form>
</body>
</html>
