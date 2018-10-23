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
	location.href = "getEnterprise.htm";
}};
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">添加</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getEnterprise.htm?page=${fn:escapeXml(param.page)}">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="addEnterprise2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">企业名称：</td>
		<td class="form_input"><input type="text" name="name" maxlength="100" style="width:400px;" datatype="Require" value="" /> <span class="imp">*</span></td>
	</tr>
	<tr>
		<td class="form_title">企业编码：</td>
		<td class="form_input"><input type="text" name="qybm" maxlength="100" style="width:100px;" datatype="Char" datatype="RequireTrim"  value="" /> <span class="imp">*</span> <span style="font-weight:bold;">添加后不可修改</span></td>
	</tr>
	<tr>
		<td class="form_title">状态：</td>
		<td class="form_input"><select name="status" style="width:300px;">
			<option value="1">正常运营</option>
			<option value="0">禁用</option>
			<option value="2">待审</option>
			<option value="3">已注销</option>
		</select></td>
	</tr>
	<tr>
		<td class="form_title">类型：</td>
		<td class="form_input"><select name="type" style="width:300px;">
			<option value="有限责任公司">有限责任公司</option>
			<option value="股份有限公司">股份有限公司</option>
			<option value="内资企业">内资企业</option>
			<option value="国有企业">国有企业</option>
			<option value="集体企业">集体企业</option>
			<option value="股份合作企业">股份合作企业</option>
			<option value="联营企业">联营企业</option>
			<option value="私营企业">私营企业</option>
			<option value="其他企业">其他企业</option>
			<option value="港、澳、台商投资企业">港、澳、台商投资企业</option>
			<option value="合资经营企业（港或澳、台资）">合资经营企业（港或澳、台资）</option>
			<option value="合作经营企业（港或澳、台资）">合作经营企业（港或澳、台资）</option>
			<option value="港、澳、台商独资经营企业">港、澳、台商独资经营企业</option>
			<option value="港、澳、台商投资股份有限公司">港、澳、台商投资股份有限公司</option>
			<option value="其他港、澳、台商投资企业">其他港、澳、台商投资企业</option>
			<option value="外商投资企业">外商投资企业</option>
			<option value="中外合资经营企业">中外合资经营企业</option>
			<option value="中外合作经营企业">中外合作经营企业</option>
			<option value="外资企业">外资企业</option>
			<option value="外商投资股份有限公司">外商投资股份有限公司</option>
			<option value="其他外商投资企业">其他外商投资企业</option>
		</select></td>
	</tr>
	<tr>
		<td class="form_title">帐号：</td>
		<td class="form_input"><input type="text" name="account" datatype="Char" maxlength="50" value="" /> <span class="imp">*</span> <span style="font-weight:bold;">添加后不可修改</span></td>
	</tr>
	<tr>
		<td class="form_title">姓名：</td>
		<td class="form_input"><input type="text" name="username" datatype="Require" maxlength="25" value="" /> <span class="imp">*</span></td>
	</tr>
	<tr>
		<td class="form_title">密码：</td>
		<td class="form_input"><input type="password" id="password" name="password" style="width:130px;" datatype="Require" maxlength="32" value="000000" /> <span class="imp">*</span> <span style="font-weight:bold;">默认密码为：000000</span></td>
	</tr>
	<tr>
		<td class="form_title">确认密码：</td>
		<td class="form_input"><input type="password" name="password2" style="width:130px;" datatype="Repeat" to="password" msg="两次输入的密码不一致" value="000000" /> <span class="imp">*</span></td>
	</tr>
	<tr>
		<td class="form_title">身份证号：</td>
		<td class="form_input"><input type="text" name="idcard" style="width:200px;" datatype="!IdCard" maxlength="18" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">邮箱：</td>
		<td class="form_input"><input type="text" name="email" readonly="readonly" style="width:200px;" datatype="!Email" maxlength="250" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">手机：</td>
		<td class="form_input"><input type="text" name="mobile" datatype="!Mobile" maxlength="50" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">电话：</td>
		<td class="form_input"><input type="text" name="phone" datatype="!Phone" maxlength="50" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">传真：</td>
		<td class="form_input"><input type="text" name="fax" datatype="!Fax" maxlength="50" value="" /></td>
	</tr>
</table>
<input type="hidden" name="ssxq" value="" />
</form>
</body>
</html>
