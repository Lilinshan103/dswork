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
	location.href = "getDict.htm?status=${fn:escapeXml(param.status)}&page=${fn:escapeXml(param.page)}";
}};
<%--
$dswork.validCallBack = function(){
	$("#changeName").val("0");// 还原
	if($("#v1").val() != $("#v2").val()){if(confirm("您修改了引用名，是否同步更新字典项？")){$("#changeName").val("1");}}
	return true;
};--%>
$(function(){
	var v = $("#status").text();
	if(v == "1"){v = "树形集合";}
	else if(v == "0"){v = "列表集合";}
	else {v = "未知";}
	$("#status").text(v);
});
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">修改</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getDict.htm?status=${fn:escapeXml(param.status)}&page=${fn:escapeXml(param.page)}">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="updDict2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">引用名</td>
		<td class="form_input"><input id="v1" type="text" name="name" datatype="Char" maxlength="100" value="${fn:escapeXml(po.name)}" /></td>
	</tr>
	<tr>
		<td class="form_title">名称</td>
		<td class="form_input"><input type="text" name="label" datatype="Require" maxlength="100" value="${fn:escapeXml(po.label)}" /></td>
	</tr>
	<tr>
		<td class="form_title">分类</td>
		<td class="form_input" id="status">${po.status}</td>
	</tr>
</table>
<input type="hidden" name="id" value="${po.id}" />
<%--
<input type="hidden" id="changeName" name="changeName" value="0" />
<input type="hidden" id="v2" name="oldName" value="${fn:escapeXml(po.name)}" />
--%>
</form>
</body>
</html>
