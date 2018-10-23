<%@page language="java" pageEncoding="UTF-8" import="common.auth.*"%><%
String path = request.getContextPath();
Auth model = AuthUtil.getLoginUser(request);
if(model == null)
{
	out.println("<script>top.location.href='" + path + "/login.html';</script>");
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>功能模块化系统，仅使用临时菜单，详细框架页面不在此包含</title>
<style type="text/css">
*{word-wrap:break-word;}
html,body,div{margin:0;padding:0;}
html,body{*position:static;height:100%;border:0;line-height:1.6;}
html{font-family:sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;}
body,a{font-family:"Microsoft Yahei","Hiragino Sans GB","Helvetica Neue",Helvetica,tahoma,arial,Verdana,sans-serif,"\5B8B\4F53";font-size:12px;color:#182a2d;-webkit-font-smoothing:antialiased;-moz-font-smoothing:antialiased;}
html{overflow:-moz-scrollbars-vertical;}
a{outline:none;text-decoration:none;cursor:pointer;text-decoration:none;}
a:link,a:visited,a:active{color:#182a2d;outline:none;}
a:hover{outline:none;color:#182a2d;text-decoration:none;}
a:focus{outline:none;}
a:hover,a:active{outline:none;}:focus{outline:none;}

label,a,a:link,a:visited,a:active{display:inline-block;font-size:14px;padding:3px 8px;line-height:1.4;}
label{border:solid 1px #bce8f1;color:#31708f;background-color:#d9edf7;}
a,a:link,a:visited,a:active{border:solid 1px #0f9ae0;color:#fff;background-color:#3bb4f2;}
a:hover {background-color:#0f9ae0;}
.level1 {margin:8px 0 0 8px;}
.level2 {margin:2px 0 0 38px;}
.level3 {margin:2px 0 0 68px;}
.level4 {margin:2px 0 0 98px;}
</style>
<script type="text/javascript">
<%if(model.isEnterprise()){%>
var treedata = [
	{id:100, name:"企业管理", img:"", imgOpen:"", url:"", items:[
		 {id:202,name:"企业用户管理", img:"", imgOpen:"", url:'/ep/user/getUser.htm', items:[]}
	]}
];
<%}%>
<%if(model.isAdmin()){%>
var treedata = [
	{id:200, name:"企业数据", img:"", imgOpen:"", url:"", items:[
		 {id:201,name:"企业管理", img:"", imgOpen:"", url:'/ep/enterprise/getEnterprise.htm', items:[]}
	]}
	,{id:300, name:"个人数据", img:"", imgOpen:"", url:"", items:[
		{id:301,name:"个人管理", img:"", imgOpen:"", url:'/person/user/getUser.htm', items:[]}
	]}
];
<%}%>
<%if(model.isUser()){%>
var treedata = [
	{id:300, name:"个人管理", img:"", imgOpen:"", url:"", items:[
	]}
];
<%}%>
var nn = [
	{id:10, name:"CMS", img:"", imgOpen:"", url:"", items:[
		 {id:12,name:"栏目管理", img:"", imgOpen:"", url:'/cms/category/getCategory.htm', items:[]}
		,{id:13,name:"栏目回收站", img:"", imgOpen:"", url:'/cms/category/getRecycledCategory.htm', items:[]}
		,{id:11,name:"专题管理", img:"", imgOpen:"", url:'/cms/special/getSpecial.htm', items:[]}
		,{id:11,name:"模板编辑", img:"", imgOpen:"", url:'/cms/template/getTemplateTree.htm', items:[]}
		,{id:12,name:"附件管理", img:"", imgOpen:"", url:'/cms/file/getFileTree.htm', items:[]}
		,{id:14,name:"附加功能", img:"", imgOpen:"", url:'', items:[
			 {id:141,name:'CMS日志', img:"", imgOpen:"", url:'/cms/log/getLog.jsp', items:[]}
			,{id:143,name:'信息总数', img:"", imgOpen:"", url:'/cms/page/main.html', items:[]}
			,{id:142,name:'批量更新', img:"", imgOpen:"", url:'/cms/page/updBatchPage.jsp', items:[]}
		]}
		,{id:15, name:"采编审核发布", img:"", imgOpen:"", url:"", items:[
	   		 {id:151,name:'信息采编', img:"", imgOpen:"", url:'/cms/edit/getCategoryTree.htm', items:[]}
			,{id:152,name:'信息审核', img:"", imgOpen:"", url:'/cms/audit/getCategoryTree.htm', items:[]}
			,{id:153,name:'信息发布', img:"", imgOpen:"", url:'/cms/publish/getCategoryTree.htm', items:[]}
			,{id:154, name:"权限管理", img:"", imgOpen:"", url:"", items:[
				 {id:1541,name:'用户授权', img:"", imgOpen:"", url:'/cms/permission/getUser.htm', items:[]}
				,{id:1542,name:'授权情况', img:"", imgOpen:"", url:'/cms/permission/getCategory.htm', items:[]}
			]}
		]}
	]}
	,{id:20, name:"BBS", img:"", imgOpen:"", url:"", items:[
	     {id:21,name:"站点管理", img:"", imgOpen:"", url:'/bbs/admin/site/getSite.htm', items:[]}
		,{id:22,name:"版块管理", img:"", imgOpen:"", url:'/bbs/admin/forum/getForum.htm', items:[]}
		,{id:23,name:"主题管理", img:"", imgOpen:"", url:'/bbs/admin/page/getPageTree.htm', items:[]}
	]}
	,{id:30, name:"退出", img:"", imgOpen:"", url:"/logout.jsp", items:[]}
];
treedata.push(nn[0]);
treedata.push(nn[1]);
treedata.push(nn[2]);
</script>
</head>
<body>
<div style="overflow:hidden;height:100%;width:100%;">
<div style="overflow:hidden;float:right;height:100%;width:80%;">
	<iframe id="main" name="main" style="height:100%;width:100%;" scrolling="auto" frameborder="0" src="about:blank"></iframe>
</div>
<div style="overflow:auto;float:left;height:100%;width:19.8%;border-right:1px solid #6ea5eb;">
<script type="text/javascript">
for(var i = 0; i < treedata.length; i++){
	var item = treedata[i];
	if(item.url == "" || item.url == "#"){
		document.write("<label class='level1'>" + item.name + "</label><br />");
	}
	else{
		document.write("<a class='level1' target='main' href='<%=path%>" + item.url + "'>" + item.name + "</a><br />");
	}
	for(var j = 0; j < item.items.length; j++){
		var m = item.items[j];
		if(m.url == "" || m.url == "#"){
			document.write("<label class='level2'>" + m.name + "</label><br />");
		}
		else{
			document.write("<a class='level2' target='main' href='<%=path%>" + m.url + "'>" + m.name + "</a><br />");
		}
		
		for(var k = 0; k < m.items.length; k++){
			var mm = m.items[k];
			if(mm.url == "" || mm.url == "#"){
				document.write("<label class='level3'>" + mm.name + "</label><br />");
			}
			else{
				document.write("<a class='level3' target='main' href='<%=path%>" + mm.url + "'>" + mm.name + "</a><br />");
			}
			for(var l = 0; l < mm.items.length; l++){
				var mmm = mm.items[l];
				document.write("<a class='level4' target='main' href='<%=path%>" + mmm.url + "'>" + mmm.name + "</a><br />");
			}
		}
	}
}
</script>
</div>
</div>
</body>
</html>