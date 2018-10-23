<%@page language="java" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp"%>
<%@include file="/commons/include/ztree.jsp"%>
<script type="text/javascript">
$dswork.callback = null;
function refreshNode(re){$dswork.ztree.refreshNode(re);}
$dswork.ztree.click = function(){
	var node = $dswork.ztree.getSelectedNode();
	if(node.status == 1){attachUrl("getUserOrg.htm?pid=" + node.id);}
	else{attachUrl("about:blank");}
	return false;
};
$dswork.ztree.root.name = "${po.id > 0?fn:escapeXml(po.name):'组织机构'}";
$dswork.ztree.root.id = ${po.id};
$dswork.ztree.root.status = ${po.status};
$dswork.ztree.url = function(treeNode){return "${ctx}/common/share/getJsonOrg.htm?pid=" + treeNode.id;};
$dswork.ztree.dataFilter = function (treeId, parentNode, data){
	var d=[];for(var i =0; i < data.length; i++){
		if(data[i].status == 1){data[i].iconSkin = "group";}
		if(data[i].status > 0){d.push(data[i]);}
	}return d;
};
$(function(){
	var $z = $dswork.ztree;
	$z.load();
	$z.expandRoot();
});
function callfn(isuser, title, id, url, uList, oList){
	var o = {title:title, url:url, args:{data:{uList:uList, oList:oList}}, width:"300", height:"400"};
	if(isuser){
		o.buttons = [{text:"保存",iconCls:"menuTool-save",handler:function(){
			var ids = $jskey.dialog.returnValue;
			if(ids != null){
				$dswork.showRequest();
				$.post("updSetUser2.htm",{userid:id, orgids:ids},function(data){$dswork.showResponse(data);});
			}
			$jskey.dialog.close();
		}}];
	}
	else{
		o.buttons = [{text:"保存",iconCls:"menuTool-save",handler:function(){
			var ids = $jskey.dialog.returnValue;
			if(ids != null){
				$dswork.showRequest();
				$.post("updSetOrg2.htm",{orgid:id, userids:ids},function(data){$dswork.showResponse(data);});
			}
			$jskey.dialog.close();
		}}];
	}
	$jskey.dialog.showDialog(o);
	return false;
}
</script>
</head>
<body class="easyui-layout treebody" fit="true">
<div region="north" style="overflow:hidden;border:0px;height:30px;">
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">用户岗位管理</td>
		<td class="menuTool">
		</td>
	</tr>
</table>
</div>
<div region="west" split="true" title="选择用户所在部门" style="width:300px;">
	<div class="treediv">
		<ul id="mytree" class="ztree tree" />
	</div>
</div>
<div region="center" style="overflow:hidden;">
	<iframe id="mainFrame" name="mainFrame" src="" frameborder="0" scrolling="auto" style="width:100%;height:100%;"></iframe>
</div>
</body>
</html>
	