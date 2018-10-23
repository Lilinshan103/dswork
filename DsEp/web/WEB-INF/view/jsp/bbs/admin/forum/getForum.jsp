<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<c:if test="${siteid<0}">
<head>
<title></title>
<%@include file="/commons/include/get.jsp" %>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">版块管理：没有可管理的站点</td>
	</tr>
</table>
</body>
</c:if>

<c:if test="${siteid>=0}">
<head>
<title></title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript">
$(function(){
	$dswork.page.menu("delForum.htm?siteid=${siteid}", "updForum1.htm?siteid=${siteid}", "", "");
	$("#listFormSave").click(function(){
		if($jskey.validator.Validate("listForm", $dswork.validValue || 3)){
			if(confirm("确定保存吗？")){$("#listForm").ajaxSubmit($dswork.doAjaxOption);}
		}
	});
	$("#site").bind("click", function(){
		if($(this).val()!="${siteid}"){
			location.href = "getForum.htm?siteid="+$(this).val();
		}
	});
});
$dswork.doAjax = true;
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getForum.htm?siteid=${siteid}";
}};
</script>
<style type="text/css">
.v{padding-left:3px;}
.k{padding:0px;margin:0px;}
.k input{border:none;background-color:transparent;text-align:center;}
</style>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">
			切换站点：<select id="site"><c:forEach items="${siteList}" var="d"><option value="${d.id}"<c:if test="${d.id==siteid}"> selected="selected"</c:if>>${fn:escapeXml(d.name)}</option></c:forEach></select>
			&nbsp;<input type="button" class="button" value="打开首页" onclick="window.open('${ctx}/bbs/index.htm?siteid=${siteid}');" />
		</td>
		<td class="menuTool">
			<a class="insert" href="addForum1.htm?siteid=${siteid}">添加</a>
			<a class="save" id="listFormSave" href="#">保存</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="listForm" method="post" action="updForumBatch.htm?siteid=${siteid}">
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:5%">操作</td>
		<td style="width:6%">排序</td>
		<td style="width:50%">名称</td>
		<td style="width:8%">ID</td>
		<td>状态</td>
		<%--<td>模板</td>--%>
	</tr>
<c:forEach items="${list}" var="d">
	<tr>
		<td class="menuTool" keyIndex="${d.id}">&nbsp;</td>
		<td><input name="seq" type="text" style="width:30px;" maxlength="4" datatype="Integer" value="${d.seq}" /></td>
		<td class="v" style="text-align:left;">${d.label}<input name="name" type="text" maxlength="100" datatype="Require" value="${fn:escapeXml(d.name)}" /></td>
		<td class="k"><input name="keyIndex" type="text" style="width:60px;" readonly="readonly" value="${d.id}" /></td>
		<td>${fn:escapeXml(d.status==1?"已启用":"已禁用")}</td>
		<%--<td>${fn:escapeXml(d.viewsite)}</td>--%>
	</tr>
</c:forEach>
</table>
</form>
</body>
</c:if>
</html>
