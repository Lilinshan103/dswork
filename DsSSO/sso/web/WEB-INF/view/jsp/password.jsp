<%@page contentType="text/html; charset=UTF-8"%><%
response.setHeader("Cache-Control","no-store");
response.setDateHeader("Expires", 0);
response.setHeader("Pragma","no-cache"); 
request.setAttribute("c", "#003c7b");// #003c7b #71d29 #125995 #d3880d #2A92EB
request.setAttribute("ctx", request.getContextPath());
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui"/>
<title>统一身份认证平台</title>
<script type="text/javascript">if(top.location != this.location){top.location = this.location;}</script>
<link rel="stylesheet" type="text/css" href="${ctx}/themes/share/fonts/dsworkfont.css"/>
<style type="text/css">
html,body{height:100%;margin:0px auto;}
body {background-color:#fff;font-family:arial,"microsoft yahei","宋体";color:${c};font-size:16px;line-height:120%;}
i{font-family:dsworkfont;font-weight:normal;font-style:normal;}

.view {overflow:hidden;margin:0 auto;width:100%;min-width:300px;max-width:1000px;overflow:hidden;padding:8px 0;}
.title,
.view .title{font-weight:bold;text-align:center;font-size:32px;line-height:40px;padding:38px 0px;}
.view .login{margin:0 auto;padding:0;width:360px;max-width:360px;border:${c} solid 0px;overflow:hidden;background-color:#fff;box-shadow:0 0 8px 0px ${c};}

.boxmsg{padding:0;display:none;}
.boxname{padding:0;}
.box{overflow:hidden;text-align:center;width:100%;margin:0 auto;padding:8px 0;border:none;}
.box .name{background-color:#fff;width:100%;padding:16px 0 8px 0;margin:0 auto;font-size:22px;line-height:22px;text-align:center;font-weight:normal;}
.box .errmsg{color:#ff0000;line-height:25px;}
.box .vbox{margin:0 auto;padding:0;overflow:hidden;text-align:left;vertical-align:top;width:275px;}
.box .vbox .input{border-radius:0 6px 6px 0;vertical-align:middle;height:48px;line-height:48px;background-color:#edf2f6;border:#d6e5ef 1px solid;border-left:none;width:194px;outline:none;padding:0 0 0 12px;}
.box .vbox .input:focus{border-color:${c};}
.box .vbox .input::placeholder{color:#999;}
.box .vbox .nolabel{border:#d6e5ef 1px solid;border-radius:6px;margin:0 0 0 66px;}
.box .vbox .nolabel:focus{border-color:${c};}
.box .vbox .code{border:#d6e5ef 1px solid;border-radius:6px;width:120px;margin:0 12px 0 0;}
.box .vbox .code:focus{border-color:${c};}
.box .vbox span {border-radius:6px 0 0 6px;vertical-align:middle;height:48px;line-height:48px;background-color:${c};border:${c} 1px solid;font-size:24px;margin:0;padding:0 20px;display:inline-block;color:#fff;}
.box .vbox img{width:120px;height:46px;border:none;cursor:pointer;vertical-align:middle;}
.box .button{background-color:${c};color:#fff;width:280px;height:50px;line-height:50px;cursor:pointer;border:none;border-radius:6px;-webkit-appearance:none;}
.box .button:hover{filter:alpha(opacity:80);opacity:0.8;}
.box .checkbox{vertical-align:middle;}
.box .radio{vertical-align:middle;}

.box .left{float:left;}
.box .right{float:right;}
.box b{font-weight:normal;font-style:normal;text-decoration:none;}
.box label{font-size:16px;font-weight:normal;line-height:18px;cursor:pointer;}
.box .link a,
.box .link a:link,
.box .link a:visited,
.box .link a:active,
.box .link label,
.box .link b{color:${c};font-size:12px;text-decoration:none;outline:none;}
.box .link b{margin:0 5px;}
.box .link a:hover{filter:alpha(opacity:80);opacity:0.8;text-decoration:none;}

.cp{color:#666;font-size:12px;width:80%;overflow:hidden;text-align:center;padding:15px 0;margin:20px auto 0 auto;border:none;border-top:solid #ccc 1px;}
.cp a,
.cp a:link,
.cp a:visited,
.cp a:active{font-size:12px;font-weight:normal;font-family:arial;color:${c};text-decoration:underline;outline:none;}


.title{background-color:${c};color:#fff;margin:0 0 40px 0;}
.view .title{background-color:inherit;color:${c};margin:0;}
/*
.bg{width:100%;height:730px;position:absolute;top:110px;left:0;z-index:-1;background:url(${ctx}/themes/share/bg/login.gif) no-repeat top center;}
.view .login{float:right;margin:0 47px 0 auto;}
*/
body {background:#fff url(${ctx}/themes/share/bg/wave.png) bottom center repeat-x;}
</style>
<style type="text/css">
.fieldset{margin:0;border:0;border-top:#e0e0e0 1px solid;padding:5px 0 10px 0;}
.fieldset .legend{text-align:center;color:#999;margin:0 auto;padding:0;font-size:12px;}
.icon_qq,
.icon_weibo,
.icon_alipay,
.icon_wechat{margin:0 auto;padding:0;display:inline-block;width:30px;height:30px;vertical-align:middle;border-radius:50%;font-size:normal;font-weight:normal;outline:0 none;}
.icon_qq    {background:#0288d1 url(${ctx}/themes/share/bg/icons.png)   0px 6px no-repeat;}
.icon_weibo {background:#d32f2f url(${ctx}/themes/share/bg/icons.png) -30px 6px no-repeat;}
.icon_alipay{background:#00aaee url(${ctx}/themes/share/bg/icons.png) -60px 6px no-repeat;}
.icon_wechat{background:#00d20d url(${ctx}/themes/share/bg/icons.png) -90px 6px no-repeat;}
@media only screen and (max-width:999px){.title{text-align:center;}}
@media only screen and (max-width:767px){body {background:#fff;}.title{font-size:28px;padding:30px 0px;}.view .login{float:none;margin:0 auto;}}
@media only screen and (max-width:480px){.bg{background:none;}.boxname{display:none;}.title{font-size:24px;line-height:30px;margin:0 0 16px 0;}.view .title{padding:30px 0px 20px 0;margin:0;}.view .login{float:none;margin:0 auto;width:100%;border-left:none;border-right:none;box-shadow:none;}}
@media only screen and (max-width:361px){.title{font-size:20px;}.box .name{font-size:20px;}}
</style>
</head>
<body>
<div class="bg"></div>
<div class="title">&nbsp;统一身份认证平台</div>
<div class="view">
  <form id="w" action="${ctx}/passwordAction" method="post">
  <div class="login">
	<div class="box boxname"><div class="name">密码重置</div></div>
	<div class="box"><div class="vbox">
		<span><i>&#xf1001;</i></span><input type="text" id="account" name="account" autocomplete="off" class="input" readonly="readonly" value="${fn:escapeXml(account)}" title="账号" placeholder="账号" />
	</div></div>
	<div class="box"><div class="vbox">
		<span><i>&#xf1002;</i></span><input type="password" id="oldpassword" name="oldpassword" autocomplete="off" class="input" value="" title="原密码" placeholder="原密码" />
	</div></div>
	<div class="box"><div class="vbox">
		<span><i>&#xf1002;</i></span><input type="password" id="password" name="password" autocomplete="off" class="input" value="" title="新密码" placeholder="新密码" />
	</div></div>
	<div class="box"><div class="vbox">
		<input type="password" id="password2" name="password2" autocomplete="off" class="input nolabel" value="" title="确认密码" placeholder="确认密码" />
	</div></div>
	<div class="box boxmsg" id="errmsgdiv"><div class="vbox">
		<div id="errmsg" class="errmsg"></div>
	</div></div>
	<div class="box"><div class="vbox">
		<input type="text" id="authcode" name="authcode" autocomplete="off" maxlength="4" class="input code" value="" title="验证码" placeholder="验证码" />
		<img id="mycode" alt="请点击" src="about:blank" onclick="this.src='${ctx}/authcode?r=' + Math.random();" />
	</div></div>
	<div class="box">
		<input type="button" class="button" value="提 交" onclick="doclick()" />
	</div>
	<div class="box">
		&nbsp;
	</div>
  </div>
  <input type="hidden" name="service" value="${fn:escapeXml(service)}" />
  <input type="hidden" name="loginURL" value="${fn:escapeXml(loginURL)}" />
  </form>
</div>
<div class="cp">
	&copy; 2014-2018 249725997@qq.com
</div>
</body>
<script type="text/javascript" src="${ctx}/js/jskey/jskey_md5.js"></script>
<script type="text/javascript" src="${ctx}/js/jskey/jskey_des.js"></script>
<script type="text/javascript">
function _$(id){return document.getElementById(id);}
function _uncheck(id){
	var s = "";
	if(id == "oldpassword"){if(!_$(id).value){s="原密码不能为空";}}
	if(id == "password"){if(!_$(id).value){s="新密码不能为空";}else{if(_$(id+'2').value&&_$(id).value!=_$(id+'2').value){s = "确认密码不一致";}}}
	if(id == "password2"){if(_$(id).value!=_$('password').value){s = "确认密码不一致";}}
	if(id == "authcode"){if(!_$(id).value){s="验证码不能为空";}}
	if(s != ""){s = "<i>&#xf1010;</i> "+s+"<br>";_$("errmsg").innerHTML = s;_$("errmsgdiv").style.display = "block";return true;}else{_$("errmsgdiv").style.display= "none";return false;}
}
function doclick(){
	if(_uncheck("oldpassword") || _uncheck("password") || _uncheck("password2") || _uncheck("authcode")){return;}
	try{_$('oldpassword').value = $jskey.md5($jskey.md5(_$('oldpassword').value)+_$('authcode').value);}catch(e){}
	try{_$('password').value = $jskey.encodeDes(_$('password').value, _$('authcode').value);}catch(e){}
	_$("w").submit();
}
_$("mycode").click();
_$("authcode").value = "";

function registEvent($e, et, fn){$e.attachEvent ? $e.attachEvent("on"+et, fn) : $e.addEventListener(et, fn, false);}
function registKey(id){registEvent(_$(id), "keydown", function(event){if(event.keyCode == 13){doclick();}});registEvent(_$(id), "keyup", function(event){_uncheck(this.getAttribute("id"));});}
registKey("oldpassword");
registKey("password");
registKey("password2");
registKey("authcode");
<c:if test="${errorMsg != ''}">
_$("errmsg").innerHTML = "${errorMsg}";
_$("errmsgdiv").style.display = "block";
</c:if>
</script>
</html>
