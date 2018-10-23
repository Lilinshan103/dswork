<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
$dswork.doAjax = true;
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getUser.htm?xtype=${fn:escapeXml(param.xtype)}&page=${pageModel.currentPage}";
}};
function updStatus(objid, id){
	var obj = $("#" + objid), o = document.getElementById(objid);
	$.post("updUserStatus.htm?xtype=${fn:escapeXml(param.xtype)}",{"keyIndex":id,"status":obj.attr("v")==0?1:0},function(data){
		$dswork.checkResult(data);
		if($dswork.result.type == 1){
		obj.removeClass("pause").removeClass("start");
		if(1 == obj.attr("v")){
			obj.text("启用").attr("v", 0).addClass("start");$("#td_" + objid).text("禁用").css("color", "red");
		}
		else{
			obj.text("禁用").attr("v", 1).addClass("pause");$("#td_" + objid).text("启用").css("color", "");
		}}else{alert($dswork.result.msg);}
	});
	return false;
}
$dswork.page.join = function(td, menu, id){
	if(td.attr("v") != 'true'){
	$(menu).append($('<div iconCls="menuTool-update">重置密码</div>').bind("click", function(){
		location.href = "updUserPassword1.htm?xtype=${fn:escapeXml(param.xtype)}&type=${fn:escapeXml(param.type)}&page=${pageModel.currentPage}&keyIndex=" + id;
	}));
	$(menu).append($('<div iconCls="menuTool-update">修改</div>').bind("click", function(){
		location.href = "updUser1.htm?xtype=${fn:escapeXml(param.xtype)}&type=${fn:escapeXml(param.type)}&page=${pageModel.currentPage}&keyIndex=" + id;
	}));
	}
	$(menu).append($('<div iconCls="menuTool-user">调动</div>').bind("click", function(){
		location.href = "updUserOrg1.htm?xtype=${fn:escapeXml(param.xtype)}&type=${fn:escapeXml(param.type)}&page=${pageModel.currentPage}&keyIndex=" + id;
	}));
};
$(function(){
	$dswork.page.menu("", "", "getUserById.htm?xtype=${fn:escapeXml(param.xtype)}", "${pageModel.currentPage}");
	$("#sel_type").bind("change", function(){
		$("#queryForm").submit();
	});
	$("#sel_status").bind("change", function(){
		$("#queryForm").submit();
	});
});
</script>
</head> 
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">用户列表</td>
		<td class="menuTool">
			<a class="insert" href="addUser1.htm?xtype=${fn:escapeXml(param.xtype)}&type=${fn:escapeXml(param.type)}">添加</a>
			<a class="delete" id="listFormDelAll" href="#">删除所选</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="queryForm" method="post" action="getUser.htm?xtype=${fn:escapeXml(param.xtype)}">
<table border="0" cellspacing="0" cellpadding="0" class="queryTable">
	<tr>
		<td class="input">
			&nbsp;类型：<select id="sel_type" name="type" v="${fn:escapeXml(param.type)}"><option value="">全部</option><c:forEach items="${typeList}" var="d"><option value="${fn:escapeXml(d.alias)}">${fn:escapeXml(d.name)}</option></c:forEach></select>
			&nbsp;姓名：<input type="text" class="text" id="name" name="name" value="${fn:escapeXml(param.name)}" style="width:75px;" />
			&nbsp;手机：<input type="text" class="text" id="mobile" name="mobile" value="${fn:escapeXml(param.mobile)}" style="width:75px;" />
			&nbsp;状态：<select id="sel_status" name="status" style="width:55px;" v="${fn:escapeXml(param.status)}"><option value="">全部</option><option value="1">启用</option><option value="0">禁用</option></select>
		</td>
		<td class="query"><input id="_querySubmit_" type="submit" class="button" value="查询" /></td>
	</tr>
</table>
</form>
<div class="line"></div>
<form id="listForm" method="post" action="delUser.htm">
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:2%;"><input id="chkall" type="checkbox" /></td>
		<td style="width:5%">操作</td>
		<td style="width:12%">类型</td>
		<td style="width:20%;">姓名(帐号)</td>
		<td>单位</td>
		<td style="width:12%;">部门</td>
		<td style="width:7%;">状态</td>
		<td style="width:20%;">操作</td>
	</tr>
<c:forEach items="${pageModel.result}" var="d" varStatus="status">
	<tr>
		<td><input name="keyIndex" type="checkbox" value="${d.id}" ${d.id<0?'style="display:none;" disabled="disabled"':''}/></td>
		<td class="menuTool" keyIndex="${d.id}" v="${d.id<0?'true':''}">&nbsp;</td>
		<td>${fn:escapeXml(d.typename)}</td>
		<td style="text-align:left;">&nbsp;${fn:escapeXml(d.name)}(${fn:escapeXml(d.account)})</td>
		<td>${fn:escapeXml(d.orgpname)}</td>
		<td>${fn:escapeXml(d.orgname)}</td>
		<td id="td_a_status${status.index}" style="color:${1==d.status?"":"red"}">${1==d.status?"启用":"禁用"}</td>
		<td class="menuTool">
			<a ${d.id<0?'style="display:none;"':''} id="a_status${status.index}" name="a_status" v="${d.status}" class="${1==d.status?'pause':'start'}" href="#" onclick="return updStatus('a_status${status.index}', '${d.id}');">${1==d.status?'禁用':'启用'}</a>
			<c:if test="${'root'!=d.account && 'admin'!=d.account}">
				<a class="update" href="updUser1.htm?xtype=${fn:escapeXml(param.xtype)}&type=${fn:escapeXml(param.type)}&page=${pageModel.currentPage}&keyIndex=${d.id}">修改</a>
			</c:if>
			<a class="user" href="updUserOrg1.htm?xtype=${fn:escapeXml(param.xtype)}&type=${fn:escapeXml(param.type)}&page=${pageModel.currentPage}&keyIndex=${d.id}">调动</a>
			<c:if test="${'root'==d.account||'admin'==d.account}"><a class="select" href="getUserById.htm?xtype=${fn:escapeXml(param.xtype)}&keyIndex=${d.id}">明细</a></c:if>
		</td>
	</tr>
</c:forEach>
</table>
<input name="page" type="hidden" value="${pageModel.currentPage}" />
<input name="xtype" type="hidden" value="${fn:escapeXml(param.xtype)}" />
</form>
<table border="0" cellspacing="0" cellpadding="0" class="bottomTable">
	<tr><td>${pageNav.page}</td></tr>
</table>
</body>
</html>
