<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/getById.jsp"%>
<%@include file="/commons/include/ztree.jsp"%>
<script type="text/javascript">
var map = new $jskey.Map();
var isload = false;
$(function(){
	$("#mytree").css("display", "none");
	document.getElementById("refresh").checked = false;
	$("#refresh").click(function(){
		var _show = $("#mytree").css("display");
		if(_show == "none"){
			document.getElementById("refresh").checked = true;
			if(isload){
				$("#mytree").css("display", "");
				return;
			}
			$("#mytree").html("").css("display", "");
			isload = true;
			$.post("getRoleFuncJson.htm",{systemid:"${po.systemid}", roleid:"${po.id}"},function(data){
				if(data == "[]"){
					alert("操作失败，请刷新重试");
					$("#mytree").html("").css("display", "none");
					document.getElementById("refresh").checked = false;
					return;
				}
				$dswork.ztree.config.async.enable = false;
				$dswork.ztree.config.check.enable = true;
				$dswork.ztree.root = {id:0, pid:-1, isParent:true, name:"功能", checked:true};
				try{$dswork.ztree.nodeArray = eval(data);}
				catch(ex){$dswork.ztree.nodeArray = [];}
				$dswork.ztree.nodeArray.push($dswork.ztree.root);
				for(var i = ($dswork.ztree.nodeArray.length - 1); i >=0; i--){
					map.put($dswork.ztree.nodeArray[i].id, $dswork.ztree.nodeArray[i]);
				}
				var notRoot = true, pid = 0, node = null;
				for(var i = ($dswork.ztree.nodeArray.length - 1); i >=0; i--){
					node = $dswork.ztree.nodeArray[i];
					notRoot = true;
					if(node.checked){
						while(notRoot){
							if(node == null){notRoot = false;break;}
							pid = node.pid;
							if(pid == null || pid <= 0){notRoot = false;break;}
							node = map.get(pid);
							node.checked = true;
						}
					}
				}
				for(var i = ($dswork.ztree.nodeArray.length - 1); i >=0; i--){
					if(!$dswork.ztree.nodeArray[i].checked) $dswork.ztree.nodeArray.splice(i, 1);
				}
				$dswork.ztree.load();
				$dswork.ztree.expandAll(true);
			});
		}
		else{
			$("#mytree").css("display", "none");
		}
	});
});
$dswork.ztree.beforeCheck = function(treeId, treeNode){return false;};
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">明细</td>
		<td class="menuTool">
			<a class="back" onclick="parent.$dswork.ztree.click();return false;" href="#">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">名称</td>
		<td class="form_input">${fn:escapeXml(po.name)}</td>
	</tr>
	<tr>
		<td class="form_title">备注</td>
		<td class="form_input"><textarea id="memo" name="memo" style="width:400px;height:60px;" class="readonlytext" readonly="readonly">${fn:escapeXml(po.memo)}</textarea></td>
	</tr>
	<tr>
		<td class="form_title">资源访问控制</td>
		<td class="form_input"><input type="checkbox" id="refresh" name="refresh" value="1" /></td>
	</tr>
</table>
<ul id="mytree" class="ztree" style="display:none;" />
</body>
</html>
