<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/addAjax.jsp"%>
<script type="text/javascript">
$(function(){
var model = parent.getModel();
var datatable = ""
if(model != null && model != ""){
	datatable = JSON.parse(model);
	for(var i=0; i<datatable.length; i++){
		$("#contactTable").append(''
		+ '	<tr class="mtr">'
		+ '		<td><input name="talias" datatype="Char" value="' + datatable[i].talias + '"></td>'
		+ '		<td><input name="tname" datatype="Require" value="' + datatable[i].tname + '"></td>'
		+ '		<td><select name="ttype">'
		+ '				<option value="!Require" ' + (datatable[i].datatype=="!Require"?"selected":"") + ' >字符串</option>'
		+ '				<option value="Require" ' + (datatable[i].datatype=="Require"?"selected":"") + ' >字符串(必填)</option>'
		+ '				<option value="!Chinese" ' + (datatable[i].datatype=="!Chinese"?"selected":"") + ' >中文</option>'
		+ '				<option value="Chinese" ' + (datatable[i].datatype=="Chinese"?"selected":"") + ' >中文(必填)</option>'
		+ '				<option value="!WebDate" ' + (datatable[i].datatype=="!WebDate"?"selected":"") + ' >日期</option>'
		+ '				<option value="WebDate" ' + (datatable[i].datatype=="WebDate"?"selected":"") + ' >日期(必填)</option>'
		+ '				<option value="!Char" ' + (datatable[i].datatype=="!Char"?"selected":"") + ' >英、数、下划线</option>'
		+ '				<option value="Char" ' + (datatable[i].datatype=="Char"?"selected":"") + ' >英、数、下划线(必填)</option>'
		+ '				<option value="!IdCard" ' + (datatable[i].datatype=="!IdCard"?"selected":"") + ' >身份证号码</option>'
		+ '				<option value="IdCard" ' + (datatable[i].datatype=="IdCard"?"selected":"") + ' >身份证号码(必填)</option>'
		+ '				<option value="!Mobile" ' + (datatable[i].datatype=="!Mobile"?"selected":"") + ' >手机号码</option>'
		+ '				<option value="Mobile" ' + (datatable[i].datatype=="Mobile"?"selected":"") + ' >手机号码(必填)</option>'
		+ '				<option value="!Money" ' + (datatable[i].datatype=="!Money"?"selected":"") + ' >金额</option>'
		+ '				<option value="Money" ' + (datatable[i].datatype=="Money"?"selected":"") + ' >金额(必填)</option>'
		+ '				<option value="!Email" ' + (datatable[i].datatype=="!Email"?"selected":"") + ' >邮件格式</option>'
		+ '				<option value="Email" ' + (datatable[i].datatype=="Email"?"selected":"") + ' >邮件格式(必填)</option>'
		+ '				<option value="!Integer" ' + (datatable[i].datatype=="!Integer"?"selected":"") + ' >纯数字</option>'
		+ '				<option value="Integer" ' + (datatable[i].datatype=="Integer"?"selected":"") + ' >纯数字(必填)</option>'
		+ '				<option value="!UnitCode" ' + (datatable[i].datatype=="!UnitCode"?"selected":"") + ' >纯统一社会信用代码</option>'
		+ '				<option value="UnitCode" ' + (datatable[i].datatype=="UnitCode"?"selected":"") + ' >纯统一社会信用代码(必填)</option>'
		+ '		</select></td>'
		+ '		<td><input type="button" class="delete"'
		+ '			onclick="$(this).parent().parent().remove();" /></td>'
		+ '	</tr>	'
		);
	}
}
});

function dataTableSave(){
	//if(confirm("确定保存吗？")){
		var flag = true;
		var message1 = [];
		var message2 = [];
		for (var i = 0; i < $(".listTable [name='talias']").length - 1; i++) {
			var v = $($(".listTable [name='talias']")[i]).val();
			var m = 0;
			for (var j = i + 1; j < $(".listTable [name='talias']").length; j++) {
				if (v == $($(".listTable [name='talias']")[j]).val()) {
					if(v == ""){
						alert("不能为空");
						return;
					}
					message1.push(v);
				}
			}
		}
		for (var i = 0; i < $(".listTable [name='tname']").length - 1; i++) {
			var v = $($(".listTable [name='tname']")[i]).val();
			var m = 0;
			for (var j = i + 1; j < $(".listTable [name='tname']").length; j++) {
				if (v == $($(".listTable [name='tname']")[j]).val()) {
					if(v == ""){
						alert("不能为空");
						return;
					}
					message2.push(v);
				}
			}
		}
		message1 = removeArrayRepElement(message1);
		message2 = removeArrayRepElement(message2);
		if(message1!=""){
			if(message2!=""){
				alert("名称" + message1.join(",") + "字段" + message2.join(",")+"重复");
				flag = false;
			}
			else{
				alert("名称" + message1.join(",") + "重复");
				flag = false;
			}
		}else{
			if(message2!=""){
				alert("字段" + message2.join(",") + "重复");
				flag = false;
			}
		}
		function removeArrayRepElement(arr){
		     for (var i = 0; i < arr.length; i++) {
		          for (var j = 0; j < arr.length; j++) {
		              if (arr[i] == arr[j] && i != j) {//将后面重复的数删掉
		                  arr.splice(j, 1);
		              }
		          }
		     }
		     return arr;
		}
		
		if(flag){
			var array = [];
			$(".listTable .mtr").each(function() {
				var row = {};
				row.datatype = $(this).find("select[name=ttype]").val();
				row.tname = $(this).find("input[name=tname]").val().replace(/\"/g,"&quot;");
				row.talias = $(this).find("input[name=talias]").val().replace(/\"/g,"&quot;");
				row.rwx = "400";
				array.push(row);
			});
			parent.setModel(JSON.stringify(array));
			//console.log(JSON.stringify(array));
			parent.$jskey.dialog.close();
		}
	//}
}
function cancel(){
	parent.$jskey.dialog.close();
}
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="menuTool">
			<a class="save" id="dataTableSave" onclick="dataTableSave();return false;" href="#">确定修改</a>
			<a class="close" id="close" onclick="cancel()" href="#">取消修改</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="addTable2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable" id="contactTable">
	<tr class="list_title">
		<td>名称</td>
		<td>字段</td>
		<td>类型</td>
		<td width="8%" class="menuTool"><a class="insert"
			onclick="$('#contactTable>tbody').append($('#cloneTable>tbody>tr:eq(0)').clone());">添加</a></td>
	</tr>
</table>
</form>
<table id="cloneTable" hidden="hidden">
	<tr class="mtr">
		<td><input name="talias" datatype="Char"></td>
		<td><input name="tname" datatype="Require"></td>
		<td><select name="ttype">
				<option value='!Require'>字符串</option>
				<option value='Require'>字符串(必填)</option>
				<option value='!Chinese'>中文</option>
				<option value='Chinese'>中文(必填)</option>
				<option value='!WebDate'>日期</option>
				<option value='WebDate'>日期(必填)</option>
				<option value='!Char'>英、数、下划线</option>
				<option value='Char'>英、数、下划线(必填)</option>
				<option value='!IdCard'>身份证号码</option>
				<option value='IdCard'>身份证号码(必填)</option>
				<option value='!Mobile'>手机号码</option>
				<option value='Mobile'>手机号码(必填)</option>
				<option value='!Money'>金额</option>
				<option value='Money'>金额(必填)</option>
				<option value='!Email'>邮件格式</option>
				<option value='Email'>邮件格式(必填)</option>
				<option value='!Integer'>纯数字</option>
				<option value='Integer'>纯数字(必填)</option>
				<option value='!UnitCode'>纯统一社会信用代码</option>
				<option value='UnitCode'>纯统一社会信用代码(必填)</option>
		</select></td>
		<td><input type="button" class="delete"
			onclick="$(this).parent().parent().remove();" /></td>
	</tr>
</table>
</body>
</html>
