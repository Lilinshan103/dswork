<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
$(function(){
	$dswork.page.menu("delTable.htm", "updTable1.htm", "getTableById.htm", "${pageModel.currentPage}");
});
$dswork.doAjax = true;
$dswork.callback = function(){if($sy.result.type == 1){
	location.href = "getTable.htm?page=${pageModel.currentPage}";
}};
</script>
</head> 
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">数据表定义列表</td>
		<td class="menuTool">
			<a class="insert" href="addTable1.htm?page=${pageModel.currentPage}">添加</a>
			<a class="delete" id="listFormDelAll" href="#">删除所选</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="queryForm" method="post" action="getTable.htm">
<table border="0" cellspacing="0" cellpadding="0" class="queryTable">
	<tr>
		<td class="input">
			&nbsp;分类：
			<select name="categoryid" datatype="Require" v="${fn:escapeXml(param.categoryid)}">
			<option value="">请选择类型</option>
			<c:forEach items="${categoryList}" var="type">
				<option value="${fn:escapeXml(type.id) }">${fn:escapeXml(type.name) }</option>
			</c:forEach>
		</select>
			
			&nbsp;名称：<input type="text" class="text" name="name" value="${fn:escapeXml(param.name)}" />
			&nbsp;标识【对应数据表名】：<input type="text" class="text" name="alias" value="${fn:escapeXml(param.alias)}" />
			&nbsp;状态：
				<select name="status" v="${fn:escapeXml(param.status)}">
					<option value="1">启用</option>
					<option value="0">禁用</option>
				</select>
		</td>
		<td class="query"><input id="_querySubmit_" type="button" class="button" value="查询" /></td>
	</tr>
</table>
</form>
<div class="line"></div>
<form id="listForm" method="post" action="delTable.htm">
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:2%"><input id="chkall" type="checkbox" /></td>
		<td style="width:5%">操作</td>
		<td>分类</td>
		<td>名称</td>
		<td>标识【对应数据表名】</td>
		<td>状态</td>
		<td>排序</td>
		<td>扩展信息</td>
	</tr>
<c:forEach items="${pageModel.result}" var="d">
	<tr>
		<td><input name="keyIndex" type="checkbox" value="${d.id}" /></td>
		<td class="menuTool" keyIndex="${d.id}">&nbsp;</td>
		<td>${fn:escapeXml(d.categoryName)}</td>
		<td>${fn:escapeXml(d.name)}</td>
		<td>${fn:escapeXml(d.alias)}</td>
		<td>${fn:escapeXml(d.status)==1?'启用':'禁用'}</td>
		<td>${fn:escapeXml(d.seq)}</td>
		<td>${fn:escapeXml(d.memo)}</td>
	</tr>
</c:forEach>
</table>
<input name="page" type="hidden" value="${pageModel.currentPage}" />
</form>
<table border="0" cellspacing="0" cellpadding="0" class="bottomTable">
	<tr><td>${pageNav.page}</td></tr>
</table>
</body>
</html>
