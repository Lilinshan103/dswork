<%@page language="java" pageEncoding="UTF-8"
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><c:set var="ctx" value="${pageContext.request.contextPath}"
/><%
dswork.web.MyRequest req = new dswork.web.MyRequest(request);
String service = req.getString("service", "/");
service = java.net.URLEncoder.encode(service, "UTF-8");
request.setAttribute("service", service);
%><!DOCTYPE html>
<html>
<head>
<title>注册</title>
<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
body{margin:0;padding:0;background-color:#ececec;font-family:"Segoe UI","Lucida Grande",Helvetica,Arial,"Microsoft YaHei",FreeSans,Arimo,"Droid Sans","wenquanyi micro hei","Hiragino Sans GB","Hiragino Sans GB W3",FontAwesome,sans-serif;}
.logo{background-color:#37bafe;}
.logo>div{padding:50px 0;}
.logo>div>a{display:block;width:150px;margin:0 auto;}
.logo>div>a>img{width:100%;}
.main{max-width:640px;margin:0 auto;}
.card{position:relative;top:-25px;width:80%;margin:0 auto;background-color:#fff;border-radius:10px;box-shadow:1px 5px 8px #D9CDB4;}
.card .content{padding:20px;}
.card .content .item{margin-bottom:20px;border-bottom:1px solid #ececec;}
.card .content .item img{width:25px;height:25px;}
.card .content .item input{height:25px;width:50%;margin-left:10px;font-size:20px;border:0;}
.card .content .item input:focus{outline:none;}
.card .content .item .msg{color:#f00;display:none;}
.card .content .button{display:block;border:0;padding:0.2em 1em 0.3em 1em;margin:0 auto;line-height:1.5em;cursor:pointer;background-color:#2996ff;color:#fff;border-radius:3px;}
.card .content .button{width:80%;height:40px;font-size:20px;}
.card .content .info{text-align:center;color:#777;}
</style>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
<script type="text/javascript" src="js/jskey_des.js"></script>
<script type="text/javascript" src="js/validator.js"></script>
<script type="text/javascript">
if(top.location != this.location){top.location = this.location;}
$(function(){
	$('#authcode').click();
	$('input[name="account"]').focus();
	$('form').submit(function(){
		if($(this).validate()){
			var $pwd = $(this).find('input[name="password"]');
			$pwd.val($jskey.encodeDes($pwd.val(), 'dswork'));
			$(this).ajaxSubmit({success:function(result){
				var ss = result.split(':');
				if(ss[0] != 0){
					alert('注册成功，请登录');
					location.href='login.jsp?service=${service}';
					return;
				}
				alert(ss[1]);
				location.reload();
			}});
		}
		return false;
	});
});
</script>
</head>
<body style="background-color:#fff;">
<div class="logo">
	<div><a><img src="image/logo.png"></a></div>
</div>
<div class="main">
	<div class="card">
		<div class="content">
		<form action="registerAction.jsp" method="post">
			<div class="item"><img src="image/icon_account.png"><input name="account" type="text" value="" data-type="char" data-show="msg1" placeholder="账号"><div class="msg" id="msg1">账号必须为字母或数字</div></div>
			<div class="item"><img src="image/icon_password.png"><input name="password" type="password" id="password" value="" data-type="char" data-show="msg2" placeholder="密码"><div class="msg" id="msg2">密码必须为字母或数字</div></div>
			<div class="item"><img src="image/icon_password.png"><input name="" type="password" value="" data-equal="password" data-show="msg3" placeholder="确认密码"><div class="msg" id="msg3">两次密码不匹配</div></div>
			<div class="item"><img src="image/icon_name.png"><input name="name" type="text" value="" data-type="zhname" data-show="msg4" placeholder="姓名"><div class="msg" id="msg4">姓名为1-6个汉字</div></div>
			<div class="item"><img src="image/icon_phone.png"><input name="mobile" type="text" value="" data-type="mobile" data-show="msg5" placeholder="手机"><div class="msg" id="msg5">手机号错误</div></div>
			<div class="item"><img src="image/icon_email.png"><input name="email" type="text" value="" data-type="email" data-show="msg6" placeholder="邮箱"><div class="msg" id="msg6">邮箱错误</div></div>
			<div class="item"><img src="image/icon_idcard.png"><input name="idcard" type="text" value="" data-type="idcard" data-show="msg7" placeholder="身份证号"><div class="msg" id="msg7">身份证号错误</div></div>
			<div class="item"><img src="image/icon_authcode.png"><input name="authcode" type="text" value="" data-type="require" data-show="msg8" placeholder="验证码" style="width:40%;"><img id="authcode" alt="请点击" style="vertical-align:middle;width:40%;height:30px;" src="about:blank" onclick="this.src='${ctx}/authcode?r=' + Math.random();" /><div class="msg" id="msg8">验证码不能为空</div></div>
			<div><input type="submit" class="button" value="注册"></div>
			<div class="info">已有账号？<a href="login.jsp?service=${service}">登录</a></div>
		</form>
		</div>
	</div>
</div>
</body>
</html>