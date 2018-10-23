<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/addAjax.jsp"%>
<style type="text/css">
.calias{width:80%;}
.cname{width:80%;}
.ctask{width:75%;margin:1px 0 0 1px;}
.ccount{width:30px;margin:1px 0 0 1px;}
.cparam{width:65%;margin:1px 0 0 1px;}
tr.list td {text-align:left;padding-left:1px;}
</style>
<script type="text/javascript">
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getFlow.htm?categoryid=${fn:escapeXml(param.categoryid)}";
}};
$dswork.deleteRow = function(obj){$(obj).parent().parent().remove();};
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">添加</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getFlow.htm?categoryid=${fn:escapeXml(param.categoryid)}">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="addFlow2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<input type="hidden" name="categoryid" value="${fn:escapeXml(param.categoryid)}" />
	<input type="hidden" name="status" value="0" />
	<input type="hidden" name="deployid" value="" />
	<tr>
		<td class="form_title">流程标识</td>
		<td class="form_input"><input type="text" name="alias" style="width:200px;" maxlength="300" datatype="Char" value="" /> <span style="font-weight:bold;">保存后不可修改</span></td>
	</tr>
	<tr>
		<td class="form_title">流程名字</td>
		<td class="form_input"><input type="text" name="name" style="width:200px;" maxlength="300" datatype="Require" value="" /></td>
	</tr>
</table>
<div class="line"></div>
<table id="contactTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:18%">标识</td>
		<td style="width:18%">名称</td>
		<td style="width:35%">任务</td>
		<td style="width:24%">参数</td>
		<td class="menuTool"><a class="insert" onclick="$('#end').before($('#cloneTable>tbody>tr:eq(0)').clone());" href="#"></a></td>
	</tr>
	<tr class="list">
		<td style="text-align:center;"><input type="hidden" name="talias" value="start" />start</td>
		<td><input type="text" name="tname" class="cname" maxlength="100" datatype="Require" value="开始" /></td>
		<td><div>任务<input type="text" name="tnext" class="ctask" maxlength="4000" datatype="Require" value="end" /></div>
			<div>用户<input type="text" name="tusers" class="ctask" maxlength="4000" datatype="Require" value="" /></div>
		</td>
		<td><input type="hidden" name="tcount" value="1" />
			<div>参数<input type="text" name="tmemo" class="cparam" maxlength="4000" value="" /></div>
		</td>
		<td>&nbsp;</td>
	</tr>
	<tr class="list" id="end">
		<td style="text-align:center;"><input type="hidden" name="talias" value="end" />end</td>
		<td><input type="text" name="tname" class="cname" maxlength="100" datatype="Require" value="结束" /></td>
		<td>&nbsp;<input type="hidden" name="tnext" value="" /><input type="hidden" name="tusers" value="" /></td>
		<td>&nbsp;<input type="hidden" name="tcount" value="1" /><input type="hidden" name="tmemo" value="" /></td>
		<td>&nbsp;</td>
	</tr>
</table>
</form>
<div style="display:none;">
<table id="cloneTable">
	<tr class="list">
		<td><input type="text" name="talias" class="calias" maxlength="300" datatype="Char" value="" /></td>
		<td><input type="text" name="tname" class="cname" maxlength="300" datatype="Require" value="" /></td>
		<td><div>任务<input type="text" name="tnext" class="ctask" maxlength="4000" datatype="Require" value="" /></div>
			<div>用户<input type="text" name="tusers" class="ctask" maxlength="4000" datatype="Require" value="" /></div>
		</td>
		<td><div>合并<input type="text" name="tcount" class="ccount" maxlength="10" datatype="IntegerPlus" value="1" />个任务</div>
			<div>参数<input type="text" name="tmemo" class="cparam" maxlength="4000" value="" /></div>
		</td>
		<td style="text-align:center;"><input type="button" class="delete" onclick="$dswork.deleteRow(this)" /></td>
	</tr>
</table>
</div>
</body>
</html>
