<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html> 
<head>  
<title></title>
<%@include file="/commons/include/updAjax.jsp"%>
<script type="text/javascript">
$dswork.validCallBack = function(){
	var rulist = document.getElementsByName("rurl");
	var rplist = document.getElementsByName("rparam");
	if($jskey.radio.getValue("status", "0") == "0"){//判断不是菜单
		if(rulist.length <= 1){// 一个是隐藏的
			alert("不作为菜单时，权限资源不能为空");return false;
		}
	}
	else{
		var _o = document.getElementById("uri");
		_o.value = _o.value.trim();
		if(_o.value == ""){
			alert("作为菜单时，地址不能为空");
			try{_o.focus();}catch(e){}return false;
		}
	}
	return true;
};
$dswork.deleteRow = function (obj){$(obj).parent().parent().remove();};
$dswork.callback = function(){
	try{if($dswork.result.type == 1){parent.refreshNode(true);}}catch(e){}
};
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">修改</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" onclick="parent.refreshNode(false);return false;" href="#">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="updFunc2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<c:if test="${0 < parent.id}">
	<tr>
		<td class="form_title">上级名称</td>
		<td class="form_input">${fn:escapeXml(parent.name)}</td>
	</tr>
	</c:if>
	<tr>
		<td class="form_title">名称</td>
		<td class="form_input"><input type="text" id="name" name="name" style="width:200px;" datatype="RequireTrim" maxlength="100" value="${fn:escapeXml(po.name)}" /> <span class="imp">*</span></td>
	</tr>
	<tr>
		<td class="form_title">标识</td>
		<td class="form_input"><input type="text" id="alias" name="alias" style="width:400px;" datatype="!Char" maxlength="100" value="${fn:escapeXml(po.alias)}" /></td>
	</tr>
	<tr>
		<td class="form_title">地址</td>
		<td class="form_input"><input type="text" id="uri" name="uri" style="width:400px;" maxlength="200" value="${fn:escapeXml(po.uri)}" /></td>
	</tr>
	<%--<tr>
		<td class="form_title">图标</td>
		<td class="form_input">
			<img id="img_show" />
			<a id="a_scan" href="#" onclick="return false;">浏览</a>
		</td>
	</tr>--%>
	<tr>
		<td class="form_title">显示到菜单</td>
		<td class="form_input">
			<c:if test="${0 < count}">
			是，下级节点不为空<input type="radio" id="status1" name="status" value="1" checked="checked" style="display:none;" />
			</c:if>
			<c:if test="${0 >= count}">
			<input type="radio" id="status1" name="status" value="1" ${1==po.status?'checked="checked"':''}/><label for="status1">是</label>
			<input type="radio" id="status0" name="status" value="0" ${1==po.status?'':'checked="checked"'}/><label for="status0">否</label>
			</c:if>
		</td>
	</tr>
	<tr>
		<td class="form_title">扩展信息</td>
		<td class="form_input"><textarea id="memo" name="memo" style="width:400px;height:60px;">${fn:escapeXml(po.memo)}</textarea></td>
	</tr>
</table>
<input type="hidden" name="id" value="${po.id}" />
<div class="line"></div>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">权限资源[参数]列表</td>
		<td class="menuTool">
			<a class="insert" onclick="$('#contactTable>tbody').append($('#cloneTable>tbody>tr:eq(0)').clone());" href="#">添加</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<table id="contactTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:60%;">权限资源</td>
		<td style="width:32%;">参数(可为空)</td>
		<td style="width:8%;">操作</td>
	</tr>
	<c:if test="${0<fn:length(list)}">
	<c:forEach items="${list}" var="d" varStatus="status">
	<tr>
		<td><input name="rurl" type="text" datatype="Require" maxlength="100" title="示例：/manage/demo/getDemo.htm" value="${fn:escapeXml(d.url)}" /></td>
		<td><input name="rparam" type="text" maxlength="100" title="示例：title=v1&amp;content=v2" value="${fn:escapeXml(d.param)}"　/></td>
		<td><input type="button" class="delete" onclick="$dswork.deleteRow(this)" /></td>
	</tr>
	</c:forEach>
	</c:if>
</table>
</form>
<div style="display:none;">
<table id="cloneTable">
	<tr class="list">
		<td><input name="rurl" type="text" datatype="Require" maxlength="100" title="示例：/manage/demo/getDemo.htm" /></td>
		<td><input name="rparam" type="text" maxlength="100" title="示例：title=v1&amp;content=v2" /></td>
		<td><input type="button" class="delete" onclick="$dswork.deleteRow(this)" /></td>
	</tr>
</table>
</div>
</body>
</html>
