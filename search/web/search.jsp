<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0,minimal-ui"/>
<title></title>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/jskey_page.js"></script>
<script type="text/javascript" src="js/laytpl.js"></script>
<style type="text/css">
a:link,a:visited,a:active{outline:none;text-decoration:underline;}
a:hover{outline:none;text-decoration:underline;}
span.keyvalue {color:red;}
span.title {line-height:25px;font-size:16px;}
span.summary {line-height:20px;font-size:12px;color:black;}
</style>
</head>
<body>
<div id="search" style="margin:10px 0;"></div>
<div id="dpage"></div>
</body>
<script id="tpl" type="text/tmpl">
{{# if(d.tip == ''){ }}
为您找到{{ d.keytype.length>0?"“<span style='color:red;font-weight:bold;'>"+d.keytype+"</span>”":"" }}关于“<span style="color:red;font-weight:bold;">{{ d.keyvalue }}</span>”的相关结果约<span style="font-size:14px;font-weight:bold;color:red;"> {{ d.size }} </span>个 <br/><br/>
{{# } }}
{{# for(var i=0,len=d.rows.length; i<len; i++){ }}
<div style="border-top:1px solid #ddd;padding:10px;">
	<a target="_blank" href="{{ d.rows[i].url }}"><span class="title">{{ d.rows[i].title }}</span></a>
	<br/><span class="summary">{{ d.rows[i].summary }}</span>
</div>
{{# } }}
</script>
<script type="text/javascript">
function searchFnForKey(keyname, keyvalue){
	var v = "";
	var _a = keyvalue.split("?");
	if(_a.length > 1){
		_a = _a[1].split("&");
		for(var i = 0; i < _a.length; i++){
			var x = _a[i].split("=");
			if(x.length > 1 && x[0] == keyname){v = x[1];}
		}
	}
	return v;
}
var isTop = top.location.href == location.href;
var keyvalue = top.location.href;
var keytype = top.location.href;
var tip = top.location.href;
keyvalue = searchFnForKey("v", keyvalue);
keytype = searchFnForKey("type", keytype);
tip = searchFnForKey("tip", tip);
if(keyvalue.length == 0 && !isTop){
	keyvalue = location.href;
	keytype = location.href;
	tip = location.href;
	keyvalue = searchFnForKey("v", keyvalue);
	keytype = searchFnForKey("type", keytype);
	tip = searchFnForKey("tip", tip);
}
keyvalue = decodeURIComponent(decodeURIComponent(keyvalue));
keyvalue = keyvalue.replace("\"", "").replace("<", "").replace(">", "").replace("'", "");
if(keyvalue.indexOf(":") >= 0){
	keytype = keyvalue.substring(0, keyvalue.indexOf(":")) || keytype;
	keyvalue = keyvalue.substring(keyvalue.indexOf(":") + 1);
}
if(keyvalue == "" && keytype == ""){document.getElementById('search').innerHTML = "请输入关键字";}
else{
	var tpl = document.getElementById('tpl').innerHTML;
	var u = "searchJson.jsp?v="+encodeURIComponent(encodeURIComponent(keyvalue))+(keytype==''?'':"&type="+encodeURIComponent(encodeURIComponent(keytype)))+"&page=";
	try{if(!isTop){
		parent.$('#vv').val((keytype.length>0?keytype+":":"") + keyvalue);
	}}catch(ex){
	}
	$jskey.page({object:'dpage',template:1,fn:function(e){
		$.getJSON(u+e.page+"&r="+(new Date().getTime()), function(res){
			e.size = res.size;
			e.page = res.page;
			e.pagesize = res.pagesize;
			e.redo();
			res.keyvalue = keyvalue;
			res.keytype = keytype;
			res.tip = tip;
			laytpl(tpl).render(res, function(render){
				document.getElementById('search').innerHTML = render;
				<%--
				var x = document.getElementsByTagName("span");
				for(var i = 0; i < x.length; i++){
					if(x[i].className == 'myxx' && typeof (x[i]) != "undefined"){
						var xt = x[i].innerHTML;
						var reg = new RegExp(res.keyvalue, "g");
						xt = xt.replace(reg, "<span style=\"color:red;font-weight:bold;\">" + res.keyvalue + "</span>");
						x[i].innerHTML = xt;
					}
				}
				--%>
				try{if(!isTop){parent.document.getElementById("zwiframe").style.height = ($(document.body).height() + 100) + "px";}}catch(ex){}
			});
		});
	}});
}
</script>
</html>