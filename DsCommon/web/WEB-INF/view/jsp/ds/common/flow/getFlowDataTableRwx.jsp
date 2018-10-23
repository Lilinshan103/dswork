<%@page language="java" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
var model = parent.getModel();
$(function(){
	if(model != null && model != ""){
		var datatable = JSON.parse(model);
		//console.log(model);
		for (var i = 0; i < datatable.length; i++) {
			paintTable(datatable, datatable[i]);
		} 
		
	 	/* var datatablerwx = datatable.rwx;
		for (var i = 0; i < datatablerwx.length; i++) {
			var rwx = datatablerwx[i];
			paintTable(datatable, datatablerwx, rwx);
		} */
	}
	else{
		$("#datatable").append('<tr class="list_title"><td>无可授权信息</td></tr>');
	}
});

function paintTable(datatable, row){
	var rwx = row.rwx;
	var r = rwx.substring(0, 1);
	var w = rwx.substring(1, 2);
	var x = rwx.substring(2, 3);
	$("#datatable").append(''
	+ '<tr><td class="form_title">字段名称</td>'
	+ '<td class="form_input"><input type="text" name="tname" value="' + row.tname + '" readonly="readonly" /></td>'
	+ '<td class="form_title">字段权限</td>'
	+ '<td class="form_input">'
	+ '<input type="radio" name="' + row.tname + '_rwx" value="4" ' + (r=="4"?"checked":"") + ' />只读 '
	+ '<input type="radio" name="' + row.tname + '_rwx" value="2" ' + (w=="2"?"checked":"") + ' />编辑 '
	+ '<input type="radio" name="' + row.tname + '_rwx" value="1" ' + (x=="1"?"checked":"") + ' />隐藏 '
	+ '</td></tr>'
	);
	
	$("input[name='"+row.tname+"_rwx']").each(function(i){
		$(this).click(function(){
			var orwx = rwx;
			var nrwx = "";
        	var r = orwx.substring(0, 1);
        	var w = orwx.substring(1, 2);
        	var x = orwx.substring(2, 3);
        	var v = this.value;
            if(this.checked==true){
            	if(i==0){nrwx = "400";}
            	if(i==1){nrwx = "420";}
            	if(i==2){nrwx = "001";}
            }
            row.rwx = nrwx;
            parent.setModel(JSON.stringify(datatable));
         });
	});
}
function cancel(){
	parent.setModel(model);
	parent.$jskey.dialog.close();
}
function save(){
	parent.$jskey.dialog.close();
}
</script>
</head>
<body>
<form id="dataForm" method="post" action="updFlowDataTableRwx.htm">
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="menuTool">
			<a class="save" id="dataTableSave" onclick="save();" href="#">确定修改</a>
			<a class="close" id="dataTableSave" onclick="cancel()" href="#">取消修改</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable" id="datatable">
	<tbody></tbody>
</table>
</form>
</body>
</html>
