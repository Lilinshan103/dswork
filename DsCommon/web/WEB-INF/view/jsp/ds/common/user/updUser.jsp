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
	location.href = "getUser.htm?xtype=${fn:escapeXml(param.xtype)}&page=${page}";
}};
$dswork.readySubmit = function(){
	$("#password").val($("#password1").val());
	$("#typename").val($("#type option:selected").text());
	$("#exname").val($("#exalias option:selected").text());
};
var map = new $jskey.Map();
var typekey = "${fn:escapeXml(po.exalias)}";
<c:forEach items="${typeList}" var="d">
if(1 > 0){
	var arr = [];
	<c:forEach items="${d.resourcesList}" var="x">
	arr.push({"ralias":"${fn:escapeXml(x.alias)}","rname":"${fn:escapeXml(x.name)}"});
	</c:forEach>
	map.put("${fn:escapeXml(d.alias)}", arr);
}
</c:forEach>
function initSelectResources(xarr){
	var o = $("#exalias");
	o.empty();
	if(xarr == null || xarr.length == 0){
		o.append("<option value=''>暂无可选项</option>");
	}
	else{
		for(var i=0; i<xarr.length;i++){
			o.append($("<option>").val(xarr[i].ralias).text(xarr[i].rname));
		}
		try{o.val(typekey);}catch(e){}
		try{if(o.val() != typekey){o.prop("selectedIndex", 0);}}catch(e){}
	}
}
function selectTypeOption(){
	var str = $("#type option:selected").val();
	initSelectResources(map.get(str));
}
$(function(){
	selectTypeOption();
});
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">修改</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getUser.htm?xtype=${fn:escapeXml(param.xtype)}&type=${fn:escapeXml(param.type)}&page=${page}">返回</a> 
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="updUser2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">帐号</td>
		<td class="form_input">${fn:escapeXml(po.account)}</td>
	</tr>
	<tr>
		<td class="form_title">姓名</td>
		<td class="form_input"><input type="text" id="name" name="name" datatype="Name" maxlength="25" value="${fn:escapeXml(po.name)}" /> <span class="imp">*</span></td>
	</tr>
	<tr>
		<td class="form_title">类型</td>
		<td class="form_input"><select id="type" name="type" v="${fn:escapeXml(po.type)}" onchange="selectTypeOption();"><c:forEach items="${typeList}" var="d">
			<option value="${fn:escapeXml(d.alias)}">${fn:escapeXml(d.name)}</option>
		</c:forEach></select>
		<select id="exalias" name="exalias" style="min-width:100px;"></select>
		 <span class="imp">*</span></td>
	</tr>
	<tr>
		<td class="form_title">手机</td>
		<td class="form_input"><input type="text" id="mobile" name="mobile" datatype="!Mobile" maxlength="50" value="${fn:escapeXml(po.mobile)}" /></td>
	</tr>
	<tr>
		<td class="form_title">电话</td>
		<td class="form_input"><input type="text" id="phone" name="phone" datatype="!Phone" maxlength="50" value="${fn:escapeXml(po.phone)}" /></td>
	</tr>
	<tr>
		<td class="form_title">邮箱</td>
		<td class="form_input"><input type="text" id="email" name="email" style="width:200px;" datatype="!Email" maxlength="250" value="${fn:escapeXml(po.email)}" /></td>
	</tr>
	<tr>
		<td class="form_title">身份证号</td>
		<td class="form_input"><input type="text" id="idcard" name="idcard" style="width:200px;" datatype="!IdCard" maxlength="18" value="${fn:escapeXml(po.idcard)}" /></td>
	</tr>
	<tr>
		<td class="form_title">工作证号</td>
		<td class="form_input"><input type="text" id="workcard" name="workcard" style="width:200px;" datatype="!Require" maxlength="64" value="${fn:escapeXml(po.workcard)}" /></td>
	</tr>
	<tr>
		<td class="form_title">创建时间</td>
		<td class="form_input">${fn:escapeXml(po.createtime)}</td>
	</tr>
</table>
<input type="hidden" name="id" value="${po.id}" />
<input type="hidden" id="typename" name="typename" value="${fn:escapeXml(po.typename)}" />
<input type="hidden" id="exname" name="exname" value="${fn:escapeXml(po.exname)}" />
</form>
</body>
</html>
