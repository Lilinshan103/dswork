<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="dswork.common.DsFactory, dswork.web.MyRequest, dswork.common.model.*"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/upd.jsp"%>
<style type="text/css">
body {line-height:2em;}
</style>
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">流程测试</td>
		<td class="menuTool">
			<a class="back" href="waiting.jsp">返回待办任务</a>
		</td>
	</tr>
</table>
<%
String msg = "";
MyRequest req = new MyRequest(request);
long wid = req.getLong("wid");
try
{
  if(wid > 0){
	IFlowWaiting po = DsFactory.getFlow().getWaiting(wid);
	String[] taskList = req.getStringArray("taskList");
	String resultType = req.getString("resultType");
	String resultMsg = req.getString("resultMsg");
	String datatable = req.getString("datatable");
	if(DsFactory.getFlow().process(po.getId(), taskList, "admin", "管理员", resultType, resultMsg, datatable))
		{msg = "处理成功";}
	else
		{msg = "处理失败";}
  }else{msg = "处理失败";}
}catch(Exception ex){
	msg = "处理失败";
}%>
<%=msg%>
<br />
</body>
</html>