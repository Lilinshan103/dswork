<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/addAjax.jsp"%>
<%@include file="/commons/include/ztree.jsp"%>
<script type="text/javascript">
$dswork.callback = function(){if($dswork.result.type == 1){
	if(!confirm("是否继续添加用户？")){location.href = "getUser.htm?xtype=${fn:escapeXml(param.xtype)}&orgpid=${param.orgpid}&orgid=${param.orgid}";}
}};
$dswork.readySubmit = function(){
	$("#password").val($("#password1").val());
	$("#typename").val($("#type option:selected").text());
	$("#exname").val($("#exalias option:selected").text());
};
var map = new $jskey.Map();
var typekey = "";
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
	$("#orgpname").bind("click", function(e){
		$dswork.showTree({id:"treeid1",width:400,height:200,root:{id:"${orgpid}", name:"${orgpid==0?'组织机构':orgpname}"${orgpid==0?'':',nocheck:false'}}
			,left:$(this).offset().left, top:$(this).offset().top+20
			,url:function(node){return "${ctx}/common/share/getJsonOrg.htm?pid="+node.id;}
			,check:function(id, node){if(node.id==0){return false;}else{$("#orgpname").val(node.name);$("#orgpid").val(node.id);$("#orgname").val();$("#orgid").val();}}
			,dataFilter:function(id, pnode, data){var d=[];for(var i =0; i < data.length; i++){if(data[i].status == 2){d.push(data[i]);}}return d;}
		});
	});
	$("#orgname").bind("click", function(e){
		var rootid = $("#orgpid").val();
		if(rootid == ""){alert("请先选择单位");return false;}
		$dswork.showTree({id:"treeid2",width:400,height:200,root:{id:rootid, name:"选择部门-"+$("#orgpname").val()}
			,left:$(this).offset().left, top:$(this).offset().top+20
			,url:function(node){return "${ctx}/common/share/getJsonOrg.htm?pid="+node.id;}
			,check:function(id, node){if(node.id==0 || node.status != 1){return false;}else{$("#orgname").val(node.name);$("#orgid").val(node.id);}}
			,dataFilter:function(id, pnode, data){var d=[];for(var i =0; i < data.length; i++){if(data[i].status == 1){d.push(data[i]);}}return d;}
		});
	});
	selectTypeOption();
});
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">添加</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getUser.htm?xtype=${fn:escapeXml(param.xtype)}&type=${fn:escapeXml(param.type)}&orgpid=${param.orgpid}&orgid=${param.orgid}&page=${fn:escapeXml(param.page)}">返回</a> 
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="addUser2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">帐号</td>
		<td class="form_input"><input type="text" id="account" name="account" datatype="Char" maxlength="50" value="" /> <span class="imp">*</span> <span style="font-weight:bold;">添加后不可修改</span></td>
	</tr>
	<tr>
		<td class="form_title">姓名</td>
		<td class="form_input"><input type="text" id="name" name="name" datatype="Name" maxlength="25" value="" /> <span class="imp">*</span></td>
	</tr>
	<tr>
		<td class="form_title">类型</td>
		<td class="form_input"><select id="type" name="type" onchange="selectTypeOption();"><c:forEach items="${typeList}" var="d">
			<option value="${fn:escapeXml(d.alias)}">${fn:escapeXml(d.name)}</option>
		</c:forEach></select>
		<select id="exalias" name="exalias" style="min-width:100px;"></select>
		 <span class="imp">*</span></td>
	</tr>
	<tr>
		<td class="form_title">密码</td>
		<td class="form_input"><input type="password" id="password1" style="width:130px;" datatype="Require" maxlength="32" value="000000" /> <span class="imp">*</span> <span style="font-weight:bold;">默认码为：000000</span></td>
	</tr>
	<tr>
		<td class="form_title">确认密码</td>
		<td class="form_input"><input type="password" id="password2" style="width:130px;" datatype="Repeat" to="password1" msg="两次输入的密码不一致" value="000000" /> <span class="imp">*</span></td>
	</tr>
	<tr>
		<td class="form_title">身份证号</td>
		<td class="form_input"><input type="text" id="idcard" name="idcard" style="width:200px;" datatype="!IdCard" maxlength="18" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">所属单位</td>
		<td class="form_input"><input type="text" id="orgpname" name="orgpname" readonly="readonly" datatype="Require" value="${orgpname}" /><input type="hidden" id="orgpid" name="orgpid" value="${orgpid}" /></td>
	</tr>
	<tr>
		<td class="form_title">所属部门</td>
		<td class="form_input"><input type="text" id="orgname" name="orgname" readonly="readonly" datatype="Require" value="" /><input type="hidden" id="orgid" name="orgid" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">邮箱</td>
		<td class="form_input"><input type="text" id="email" name="email" readonly="readonly" style="width:200px;" datatype="!Email" maxlength="250" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">手机</td>
		<td class="form_input"><input type="text" id="mobile" name="mobile" datatype="!Mobile" maxlength="50" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">电话</td>
		<td class="form_input"><input type="text" id="phone" name="phone" datatype="!Phone" maxlength="50" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">工作证号</td>
		<td class="form_input"><input type="text" id="workcard" name="workcard" style="width:200px;" datatype="!Require" maxlength="64" value="" /></td>
	</tr>
</table>
<input type="hidden" id="password" name="password" value="" />
<input type="hidden" id="typename" name="typename" value="" />
<input type="hidden" id="exname" name="exname" value="" />
</form>
</body>
</html>
