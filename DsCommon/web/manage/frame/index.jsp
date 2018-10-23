<%@page language="java" pageEncoding="UTF-8"%><%String path = request.getContextPath();%>
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
</style>
<script type="text/javascript">
var treedata = [
{id:100000, name:'门户管理', img:"", imgOpen:"", url:"", items:[
	{id:100002,name:'应用系统管理', img:"", imgOpen:"", url:'/ds/common/system/getSystem.htm', items:[]}
	,{id:100003,name:'组织机构管理', img:"", imgOpen:"", url:'/ds/common/org/getOrgTree.htm', items:[]}
	,{id:100004,name:'访问授权管理', img:"", imgOpen:"", url:'/ds/common/orgrole/getOrgTree.htm', items:[]}
	,{id:100006,name:'用户类型管理', img:"", imgOpen:"", url:'/ds/common/usertype/getUsertype.htm', items:[]}
	,{id:100007,name:'门户用户管理', img:"", imgOpen:"", url:'/ds/common/user/getUser.htm', items:[]}
	,{id:100008,name:'用户岗位管理', img:"", imgOpen:"", url:'/ds/common/userorg/getOrgTree.htm', items:[]}
	,{id:100009,name:'门户登录日志', img:"", imgOpen:"", url:'/common/log/getCommonLogin.jsp', items:[]}
	,{id:100010,name:'多级管理', img:"", imgOpen:"", url:'', items:[
		{id:1000101,name:'组织管理', img:"", imgOpen:"", url:'/ds/common/ex/org/getOrgTree.htm', items:[]}
		,{id:1000102,name:'用户管理', img:"", imgOpen:"", url:'/ds/common/ex/user/getOrgTree.htm', items:[]}
		,{id:1000103,name:'授权管理', img:"", imgOpen:"", url:'/ds/common/ex/userorg/getOrgTree.htm', items:[]}
	]}
	,{id:100011,name:'单系统', img:"", imgOpen:"", url:'', items:[
		{id:1000111,name:'资源管理', img:"", imgOpen:"", url:'/ds/common/func/getFuncTree.htm?systemid=1', items:[]}
		,{id:1000112,name:'角色管理', img:"", imgOpen:"", url:'/ds/common/role/getRoleTree.htm?systemid=1', items:[]}
		,{id:1000113,name:'用户管理', img:"", imgOpen:"", url:'/ds/common/single/user/getOrgTree.htm', items:[]}
		,{id:1000114,name:'组织管理', img:"", imgOpen:"", url:'/ds/common/single/org/getOrgTree.htm', items:[]}
		,{id:1000115,name:'授权管理', img:"", imgOpen:"", url:'/ds/common/single/userrole/getOrgTree.htm?systemid=1', items:[]}
	]}
]}
,{id:200000, name:'业务管理', img:"", imgOpen:"", url:"", items:[
	{id:200001,name:'字典管理', img:"", imgOpen:"", url:'/ds/common/dict/getDict.htm', items:[]}
	,{id:200005,name:'流程管理', img:"", imgOpen:"", url:'/ds/common/flow/getFlowCategoryTree.htm', items:[]}
]}
,{id:100, name:'扩展功能', img:"", imgOpen:"", url:"", items:[
	{id:901,name:'指定组织机构', img:"", imgOpen:"", url:'/ds/common/org/getOrgTree.htm?rootid=1', items:[]}
	,{id:902,name:'指定用户授权', img:"", imgOpen:"", url:'/ds/common/userorg/getOrgTree.htm?rootid=1', items:[]}
	,{id:903,name:'指定类型用户管理', img:"", imgOpen:"", url:'/ds/common/user/getUser.htm?xtype=0', items:[]}
	,{id:11,name:'流程示例', img:"", imgOpen:"", url:'', items:[
		{id:111,name:'流程测试', img:"", imgOpen:"", url:'/flow/waiting.jsp', items:[]}
	]}
]}
,{id:112,name:'字典样例', img:"", imgOpen:"", url:'/x.jsp', items:[]}
,{id:113,name:'退出', img:"", imgOpen:"", url:'/manage/frame/logout.jsp', items:[]}
];
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
			document.write("<a class='level3' target='main' href='<%=path%>" + mm.url + "'>" + mm.name + "</a><br />");
		}
	}
}
</script>
<br /><br />
</div>
</div>
</body>
</html>