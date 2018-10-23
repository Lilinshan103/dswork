<%@page contentType="text/html; charset=UTF-8"%><%
response.setHeader("Cache-Control","no-store");
response.setDateHeader("Expires", 0);
response.setHeader("Pragma","no-cache"); 
request.setAttribute("c", "#2a92eb");// #003c7b #b71d29 #125995 #d3880d #2a92eb
request.setAttribute("ctx", "/sso");
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><%@page language="java" pageEncoding="UTF-8"%><%
dswork.web.MyRequest req = new dswork.web.MyRequest(request);
String msg = req.getString("msg");
String type = "2".equals(req.getString("type", "2"))?"2":"1";// 2企业1个人
try
{
	if("".equals(msg) && !"get".equals(request.getMethod().toLowerCase()))
	{
		String randcode = (String) request.getSession().getAttribute(dswork.web.MyAuthCodeServlet.SessionName_Randcode);
		if(randcode == null || randcode.equals(""))
		{
			msg = "验证码已过期!";
		}
		else if(!randcode.toLowerCase().equals(req.getString("authcode").toLowerCase()))
		{
			msg = "验证码输入错误,请重新输入!";
		}
		request.getSession().setAttribute(dswork.web.MyAuthCodeServlet.SessionName_Randcode, "");
		if("".equals(msg))
		{
			dswork.websso.service.DsWebssoUserService ssoservice = (dswork.websso.service.DsWebssoUserService)dswork.spring.BeanFactory.getBean("dsWebssoUserService");
			String account = req.getString("account").toLowerCase();
			if(account.length() > 0)
			{
				if(ssoservice.getByAccount(account))
				{
					msg = "账号已存在";
				}
//				else if(service.getByIdcard(po.getIdcard()) != null)
//				{
//					msg = "身份证号已存在";
//				}
//				else if(service.getByMobile(po.getMobile()) != null)
//				{
//					msg = "手机号已存在";
//				}
//				else if(service.getByEmail(po.getEmail()) != null)
//				{
//					msg = "邮箱已存在";
//				}
				else
				{
					dswork.websso.model.DsWebssoUser m = new dswork.websso.model.DsWebssoUser();
					m.setSsoaccount(account);
					m.setType(type);
					String password = dswork.core.util.EncryptUtil.decodeDes(req.getString("password"), "dswork");
					m.setPassword(password.toUpperCase());
					m.setStatus(1);
					ssoservice.saveForRegister(m);
					msg = "success";
				}
			}
			else
			{
				msg = "账号输入错误";
			}
		}
	}
}
catch(Exception e)
{
	msg = e.getMessage();
}
request.setAttribute("msg", msg);
%><%
String service = req.getString("service", "/");
request.setAttribute("type", type);
request.setAttribute("service", service);
request.setAttribute("servicex", java.net.URLEncoder.encode(service, "UTF-8"));
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
body {background-color:#fff;font-family:tahoma,arial,"Microsoft YaHei","\5B8B\4F53",sans-serif;color:${c};font-size:16px;line-height:120%;}
i{font-family:dsworkfont;font-weight:normal;font-style:normal;}

.view {overflow:hidden;margin:0 auto;width:100%;min-width:300px;max-width:1000px;overflow:hidden;padding:8px 0;}
.title{background-color:${c};color:#fff;margin:0 0 40px 0;}
.title,
.view .title{font-weight:bold;text-align:center;font-size:32px;line-height:40px;padding:38px 0px;}
.view .title{background-color:inherit;color:${c};margin:0;}
.view .login{margin:0 auto;padding:0;width:360px;max-width:360px;border:${c} solid 0px;overflow:hidden;background-color:#fff;box-shadow:0 0 8px 0px ${c};}

.boxmsg{padding:0;display:none;}
.boxname{padding:0;}
.box{overflow:hidden;text-align:center;width:100%;margin:0 auto;padding:8px 0;border:none;}
.box .name{background-color:#fff;width:100%;padding:16px 0 8px 0;margin:0 auto;font-size:22px;line-height:22px;text-align:center;font-weight:normal;}
.box .msg{color:#ff0000;line-height:25px;}
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
.box .checkbox{vertical-align:middle;cursor:pointer;}
.box .radio{vertical-align:middle;cursor:pointer;}

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
</style>
<style type="text/css">
body {background:#fff url(/sso/themes/share/bg/wave.png) bottom center repeat-x;}
.fieldset a {margin:0 15px;}
</style>
<link rel="stylesheet" type="text/css" href="${ctx}/themes/ssomedia.css"/>
</head>
<body>
<div class="bg"></div>
<div class="title">&nbsp;统一身份认证平台</div>
<div class="view">
  <form id="w" action="register.jsp" method="post">
  <div class="login">
	<div class="box boxname"><div id="regname" class="name">${type=="2"?"企业":"个人"}用户注册</div></div>
	<div class="box"><div class="vbox">
		<b>注册类型：</b>
		<label><input name="type" type="radio" autocomplete="off" class="radio" value="2" ${type=="2"?" checked":" onclick='dotype()'"}>企业</label>
		<label><input name="type" type="radio" autocomplete="off" class="radio" value="1" ${type=="2"?" onclick='dotype()'":" checked"}>个人</label>
	</div></div>
	<div class="box"><div class="vbox">
		<span><i>&#xf1001;</i></span><input type="text" id="account" name="account" autocomplete="off" class="input" value="${fn:escapeXml(param.account)}" title="账号" placeholder="账号" />
	</div></div>
	<div class="box"><div class="vbox">
		<span><i>&#xf1002;</i></span><input type="password" id="password" name="password" autocomplete="off" class="input" value="" title="密码" placeholder="密码" />
	</div></div>
	<div class="box"><div class="vbox">
		<input type="password" id="password2" name="password2" autocomplete="off" class="input nolabel" value="" title="确认密码" placeholder="确认密码" />
	</div></div>
	<div class="box boxmsg" id="msgdiv"><div class="vbox">
		<div id="msg" class="msg"></div>
	</div></div>
	<div class="box"><div class="vbox">
		<input type="text" id="authcode" name="authcode" autocomplete="off" maxlength="4" class="input code" value="" title="验证码" placeholder="验证码" />
		<img id="mycode" alt="请点击" src="about:blank" onclick="this.src='/websso/authcode?r=' + Math.random();" />
	</div></div>
	<div class="box">
		<input type="button" class="button" value="提交注册" onclick="doclick()" />
	</div>
	<div class="box"><div class="vbox link">
		<b class="left">已有账号？</b><a href="login.jsp?service=${servicex}" class="left">立即登录</a><%--<b class="left">|</b><a href="#" class="left">忘记密码?</a>--%>
	</div></div>
	<div class="box"><div class="vbox">
		<fieldset class="fieldset">
			<legend id="regmsg" align="center" class="legend">其他方式注册<b style="color:#ff0000;">${type=='2'?'企业':'个人'}</b>用户</legend>
				<%
				String serviceURL = "/websso/gzmice/registerAction.jsp?type=" + type + "&service=" + java.net.URLEncoder.encode(java.net.URLEncoder.encode(service, "UTF-8"), "UTF-8");
				request.setAttribute("serviceURL", java.net.URLEncoder.encode(serviceURL, "UTF-8"));
				%><a href="/websso/bind/login.jsp?bind=qq&serviceURL=${serviceURL}" class="icon_qq"></a>
				<a href="/websso/bind/login.jsp?bind=weibo&serviceURL=${serviceURL}" class="icon_weibo"></a>
				<a href="/websso/bind/login.jsp?bind=alipay&serviceURL=${serviceURL}" class="icon_alipay" style="background-color:gray"></a>
				<a href="/websso/bind/login.jsp?bind=wechat&serviceURL=${serviceURL}" class="icon_wechat" style="background-color:gray"></a>
		</fieldset>
	</div></div>
  </div>
  <input type="hidden" name="service" value="${fn:escapeXml(service)}" />
  </form>
</div>
<div class="cp">
	&copy; 2014-2018 广州商务会展促进服务中心
</div>
</body>
<script type="text/javascript" src="${ctx}/js/jskey/jskey_sso.js"></script>
<script type="text/javascript">
function doclick(){
	if(docheck()){return;}
	try{_$("password").value = $jskey.decodeDes($jskey.md5(_$("password").value), "dswork");}catch(e){}
	_$("w").submit();
}
_$("authcode").value = "";
<c:if test="${fn:length(msg)>0}">
_$("msg").innerHTML = "${msg}";
<c:if test="${msg == 'success'}">
_$("msg").innerHTML = "<i>&#xf1028;</i> 注册成功，3秒后跳转到登录页面";
setTimeout(function(){location.href="login.jsp?service=${servicex}";},3000);
</c:if>
_$("msgdiv").style.display = "block";
</c:if>
function dotype(){
	location.href = "register.jsp?type=${type=='2'?'1':'2'}&service=${servicex}";
}
</script>
</html>
