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
	location.href = "getUser.htm?xtype=${fn:escapeXml(param.xtype)}&type=${fn:escapeXml(param.type)}&orgpid=${param.orgpid}&orgid=${param.orgid}&page=${fn:escapeXml(param.page)}";
}};
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">修改</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getUser.htm?xtype=${fn:escapeXml(param.xtype)}&type=${fn:escapeXml(param.type)}&orgpid=${param.orgpid}&orgid=${param.orgid}&page=${fn:escapeXml(param.page)}">返回</a> 
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="updUserPassword2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">帐号</td>
		<td class="form_input">${fn:escapeXml(po.account)}</td>
	</tr>
	<tr>
		<td class="form_title">重置密码</td>
		<td class="form_input"><input type="password" id="password" name="password" style="width:130px;" datatype="Require" maxlength="32" value="000000" /> <span class="imp">*</span> <span style="font-weight:bold;">默认重置密码为：000000</span></td>
	</tr>
	<tr>
		<td class="form_title">确认密码</td>
		<td class="form_input"><input type="password" id="password2" name="password2" style="width:130px;" datatype="Repeat" to="password" msg="两次输入的密码不一致" value="000000" /> <span class="imp">*</span></td>
	</tr>
</table>
<input type="hidden" name="id" value="${po.id}" />
</form>
</body>
</html>
