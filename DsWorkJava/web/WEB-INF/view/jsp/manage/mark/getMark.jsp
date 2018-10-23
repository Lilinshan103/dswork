<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/addAjax.jsp" %>
<script type="text/javascript">
$dswork.deleteRow = function (obj){$(obj).parent().parent().remove();};
$(function(){
	$("#showTable tr").each(function(){
		if(!$(this).hasClass("list_title")){
			$(this).addClass($(this).index()%2 == 0 ? 'list_even' : 'list_odd');
			$(this).bind("mouseover", function(){
				$(this).removeClass("list_odd").removeClass("list_over").addClass("list_over");
			});
			$(this).bind("mouseout", function(){
				$(this).removeClass("list_over").addClass($(this).index()%2 == 0 ? 'list_even' : 'list_odd');
			});
		}
	});
});
$dswork.callback = function(){
	if($dswork.result.type == 1){
		location.href = "getMark.htm?page=${fn:escapeXml(param.page)}&keyIndex=${po.id}";
	}
}
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">评论</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getDemo.htm?page=${fn:escapeXml(param.page)}">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">标题</td>
		<td class="form_input">${fn:escapeXml(po.title)}</td>
	</tr>
	<tr>
		<td class="form_title">内容</td>
		<td class="form_input">${fn:escapeXml(po.content)}</td>
	</tr>
	<tr>
		<td class="form_title">创建时间</td>
		<td class="form_input">${fn:escapeXml(po.foundtime)}</td>
	</tr>
</table>
<div class="line"></div>
<table id="showTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:5%">序号</td>
		<td>内容</td>
		<td>创建时间</td>
	</tr>
<c:forEach items="${list}" var="d" varStatus="status">
	<tr>
		<td>${status.index+1}</td>
		<td>${d.content}</td>
		<td>${d.foundtime}</td>
	</tr>
</c:forEach>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="addMark2.htm">
<input type="hidden" name="demoid" value="${po.id}" />
<table id="contactTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td>批量添加评论内容，当添加内容数量为2(save方法)-3(test方法)个时，可用于测试数据回滚</td>
		<td style="width:8%;">操作</td>
	</tr>
	<tr class="list">
		<td><input type="text" name="content" datatype="Char" style="width:400px;" maxlength="128" value="" /></td>
		<td class="menuTool"><a class="insert" onclick="$('#contactTable>tbody').append($('#cloneTable>tbody>tr:eq(0)').clone());" href="#">添加项</a></td>
	</tr>
</table>
</form>
<div style="display:none;">
<table id="cloneTable">
	<tr class="list">
		<td><input type="text" name="content" datatype="Require" style="width:400px;" maxlength="100" value="" /></td>
		<td><input type="button" class="delete" onclick="$dswork.deleteRow(this)" /></td>
	</tr>
</table>
</div>
</body>
</html>
