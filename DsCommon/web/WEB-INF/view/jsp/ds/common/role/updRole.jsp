<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html> 
<head>  
<title></title>
<%@include file="/commons/include/updAjax.jsp"%>
<%@include file="/commons/include/ztree.jsp"%>
<script type="text/javascript">
var map = new $jskey.Map();
$(function(){
	$("#mytree").css("display", "none");
	document.getElementById("refresh").checked = false;
	$("#refresh").click(function(){
		var _show = $("#mytree").css("display");
		if(_show == "none"){
			$("#mytree").css("display", "");
			$("#mytree").html("");
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
				$dswork.ztree.nodeArray.push($dswork.ztree.root);
				$dswork.ztree.load();
				$dswork.ztree.expandAll(true);
			});
		}
		else{
			if(confirm("是否取消权限分配？")){
				$("#mytree").html("").css("display", "none");
				document.getElementById("refresh").checked = false;
			}
			else{
				document.getElementById("refresh").checked = true;
			}
		}
	});
});
$dswork.validCallBack = function(){
	if(document.getElementById("refresh").checked){
		var _nodes = $dswork.ztree.getCheckedNodes();
		var _ids = [];
		$(_nodes).each(function(){if(this.id > 0){_ids.push(this.id);}});
		$("#funcids").val(_ids);
	}
	else{$("#funcids").val("");}
	return true;
};
$dswork.callback = function(){
	try{if($dswork.result.type == 1){parent.refreshNode(true);}}catch(e){}
};
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">修改</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" onclick="parent.refreshNode(false);return false;" href="#">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="updRole2.htm">
<input type="hidden" name="id" value="${po.id}" />
<input type="hidden" name="systemid" value="${po.systemid}" />
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<c:if test="${0 < parent.id}">
	<tr>
		<td class="form_title">上级名称</td>
		<td class="form_input">${fn:escapeXml(parent.name)}</td>
	</tr>
	</c:if>
	<tr>
		<td class="form_title">名称</td>
		<td class="form_input"><input type="text" id="name" name="name" style="width:200px;" datatype="RequireTrim" maxlength="100" value="${fn:escapeXml(po.name)}" /> <span class="imp">*</span></td>
	</tr>
	<tr>
		<td class="form_title">备注</td>
		<td class="form_input"><textarea id="memo" name="memo" style="width:400px;height:60px;">${fn:escapeXml(po.memo)}</textarea></td>
	</tr>
	<tr>
		<td class="form_title">资源访问控制</td>
		<td class="form_input"><input type="checkbox" id="refresh" name="refresh" value="1" /></td>
	</tr>
</table>
<input type="hidden" id="funcids" name="funcids" value="" />
</form>
<ul id="mytree" class="ztree" style="display:none;" />
</body>
</html>
