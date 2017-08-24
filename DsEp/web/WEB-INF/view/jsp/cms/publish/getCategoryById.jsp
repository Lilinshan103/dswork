<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/updAjax.jsp" %>
<%@include file="/commons/include/editor.jsp" %>
<script type="text/javascript">
$(function(){
	$(".form_title").css("width", "8%");
	$("#btn_category").bind("click", function(){
		if(confirm("是否发布栏目\"${fn:escapeXml(po.name)}\"")){
			$dswork.doAjaxObject.show("发布中");
			$.post("build.htm",{"siteid":"${po.siteid}", "categoryid":"${po.id}"},function(data){
				$dswork.doAjaxShow(data, function(){});
			});
		}
	});
	$("#btn_site").bind("click", function(){
		if(confirm("是否发布首页")){
			$dswork.doAjaxObject.show("发布中");
			$.post("build.htm",{"siteid":"${po.siteid}"},function(data){
				$dswork.doAjaxShow(data, function(){});
			});
		}
	});
});
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">栏目内容明细</td>
		<td class="menuTool">
			<a class="graph" id="btn_category" href="javascript:void(0);">发布本栏目</a>
			<a class="look" target="_blank" href="${ctx}/cms/page/buildHTML.chtml?view=true&siteid=${po.siteid}&categoryid=${po.id}">预览本栏目</a>
			<a class="graph" id="btn_site" href="javascript:void(0);">发布首页</a>
			<a class="look" target="_blank" href="${ctx}/cms/page/buildHTML.chtml?view=true&siteid=${po.siteid}">预览首页</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">摘要</td>
		<td class="form_input">${fn:escapeXml(po.summary)}</td>
	</tr>
	<tr>
		<td class="form_title">meta关键词</td>
		<td class="form_input">${fn:escapeXml(po.metakeywords)}</td>
	</tr>
	<tr>
		<td class="form_title">meta描述</td>
		<td class="form_input">${fn:escapeXml(po.metadescription)}</td>
	</tr>
	<tr>
		<td class="form_title">来源</td>
		<td class="form_input">${fn:escapeXml(po.releasesource)}</td>
	</tr>
	<tr>
		<td class="form_title">作者</td>
		<td class="form_input">${fn:escapeXml(po.releaseuser)}</td>
	</tr>
	<tr>
		<td class="form_title">图片</td>
		<td class="form_input">${fn:escapeXml(po.img)}</td>
	</tr>
	<tr>
		<td class="form_title">发布</td>
		<td class="form_input">发布时间：${fn:escapeXml(po.releasetime)}</td>
	</tr>
	<tr>
		<td class="form_title">状态</td>
		<td class="form_input">${po.status==8?'已发布':'待发布'}</td>
	</tr>
</table>
</body>
</html>