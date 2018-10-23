<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="dswork.common.DsFactory, dswork.web.MyRequest, dswork.common.model.*, java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/upd.jsp"%>
<style type="text/css">
body {line-height:2em;}
</style>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">流程测试</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="waiting.jsp">返回待办列表</a>
		</td>
	</tr>
</table>
<form id="dataForm" method="post" action="doAction.jsp">
<%
String msg = "";
MyRequest req = new MyRequest(request);
long wid = req.getLong("wid");
try
{
  if(wid > 0){
	IFlowWaiting po = DsFactory.getFlow().getWaiting(wid);
	request.setAttribute("po", po);
	java.util.Map<String, String> map = DsFactory.getFlow().getTaskList(po.getFlowid());
	String datatable = po.getDatatable();
	Map<String, String> dtMap = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(datatable, Map.class);
	String rwxJson = dtMap.get(po.getTalias());
	if(!"".equals(rwxJson))
	{
		List<IFlowDataRow> dt = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(rwxJson, List.class);
		request.setAttribute("dt", dt);
	}
%>
	流程名称：${po.flowname}<br />
	当前任务：${po.talias}&nbsp;${po.tname}<br />
	下级任务：
	<%
	String[] arr = po.getTnext().split("\\|", -1);
	for(String s : arr)
	{
		%><select name="taskList"><%
		for(String m : s.split(",", -1)){%><option value="<%=m%>"><%=map.get(m)%></option><%}
		%></select>&nbsp;<%
	}
	%>
	<br />
	状态：<label><input type="radio"  name="resultType" value="1" checked="checked" />拟同意</label>
		&nbsp;<label><input type="radio"  name="resultType" value="0" />拟拒绝</label>
		&nbsp;<label><input type="radio"  name="resultType" value="-1" />拟作废</label>
	<br />
	意见：<textarea name="resultMsg" style="width:400px;">无</textarea><br />
	<%-- 数据结构：${po.datatable} --%>
<input type="hidden" name="wid" value="<%=wid%>" />
<input type="hidden" id="datatable" name="datatable" value="" />
</form>
<form id="formdata" method="post" action="">
<c:forEach items="${dt}" var="dt">
	<%-- <c:if test="${dt.rwx!='001'}"> --%>
	<%-- ${fn:escapeXml(dt.talias)}：<input name="${fn:escapeXml(dt.tname)}" datatype="${fn:escapeXml(dt.datatype)}" ${fn:escapeXml(dt.rwx=='400'?'readonly':'')} /><br /> --%>
	<%-- </c:if> --%>
	<div ${fn:escapeXml(dt.rwx=='001'?'style=display:none;':'')}>
	${fn:escapeXml(dt.talias)}：<input name="${fn:escapeXml(dt.tname)}" datatype="${fn:escapeXml(dt.datatype)}" ${fn:escapeXml(dt.rwx=='400'?'readonly':'')} /><br />
	</div>
</c:forEach>
<!-- <input type="button" onclick="getFormData()" value="获取表单数据" /> -->
</form>
<script type="text/javascript">
var map = new $jskey.Map();
var array = [];
function getFormData(){
	array = [];
	var d = {};
	var formdata = $("#formdata").serializeArray();
	$.each(formdata, function(){
		var m = map.get(this.name);
		if(m.rwx == "420"){
			m.value = this.value.replace(/\"/g,"&quot;");
		}
		map.put(this.name, m);
		array.push(m);
	});
	$("#datatable").val(JSON.stringify(array));
}
function init(){
<c:forEach items="${dt}" var="d">
	var row = {};
	row.datatype = "${d.datatype}";
	row.tname = "${d.tname}";
	row.talias = "${d.talias}";
	row.rwx = "${d.rwx}";
	row.value = "";
	map.put(row.tname, row);
</c:forEach>
}
$(function(){
	init();
})

$dswork.readySubmit = function(){
	getFormData();
}
</script>

<%
  }else{msg = "处理失败";}
}catch(Exception ex){
	ex.printStackTrace();
	msg = "处理失败";
}%>
<%=msg%>
<br />
</body>
</html>