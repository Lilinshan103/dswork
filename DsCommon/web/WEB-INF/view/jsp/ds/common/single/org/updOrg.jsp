<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html> 
<head>  
<title></title>
<%@include file="/commons/include/updAjax.jsp"%>
<script type="text/javascript">
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
<form id="dataForm" method="post" action="updOrg2.htm">
<input type="hidden" name="id" value="${po.id}" />
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<c:if test="${0 < parent.id}">
	<tr>
		<td class="form_title">上级名称</td>
		<td class="form_input">${fn:escapeXml(parent.name)}</td>
	</tr>
	</c:if>
	<tr>
		<td class="form_title">名称</td>
		<td class="form_input"><input type="text" id="name" name="name" style="width:200px;" datatype="RequireTrim" maxlength="50" value="${fn:escapeXml(po.name)}" /> <span class="imp">*</span></td>
	</tr>
	<tr>
		<td class="form_title">类型</td>
		<td class="form_input">
			<c:if test="${0 < po.status}">
			<c:if test="${1 != parent.status}">
			<input type="radio" id="status2" name="status" value="2" ${po.status==2?'checked="checked"':''} /><label for="status2">单位</label>
			</c:if>
			<c:if test="${0 != po.pid}">
			<input type="radio" id="status1" name="status" value="1" ${po.status!=2?'checked="checked"':''} /><label for="status1">部门</label>
			</c:if>
			</c:if>
		</td>
	</tr>
	<tr>
		<td class="form_title">职责范围</td>
		<td class="form_input"><textarea id="dutyscope" name="dutyscope" style="width:400px;height:60px;">${fn:escapeXml(po.dutyscope)}</textarea></td>
	</tr>
	<tr>
		<td class="form_title">备注</td>
		<td class="form_input"><textarea id="memo" name="memo" style="width:400px;height:40px;">${fn:escapeXml(po.memo)}</textarea></td>
	</tr>
</table>
</form>
</body>
</html>
