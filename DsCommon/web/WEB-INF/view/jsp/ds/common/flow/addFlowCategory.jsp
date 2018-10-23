<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head> 
<title></title>
<%@include file="/commons/include/addAjax.jsp"%>
<script type="text/javascript">
$dswork.callback = function(){
	try{if($dswork.result.type == 1){parent.refreshNode(true);}}catch(e){}
};
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">添加</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" onclick="parent.refreshNode(null);return false;" href="#">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="addFlowCategory2.htm">
<input type="hidden" name="pid" value="${(0 < parent.id)?parent.id:0}" />
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<c:if test="${0 < parent.id}">
	<tr>
		<td class="form_title">上级名称</td>
		<td class="form_input">${fn:escapeXml(parent.name)}</td>
	</tr>
	</c:if>
	<tr>
		<td class="form_title">名称</td>
		<td class="form_input"><input type="text" id="name" name="name" style="width:200px;" datatype="RequireTrim" maxlength="50" value="" /> <span class="imp">*</span></td>
	</tr>
</table>
</form>
</body>
</html>
