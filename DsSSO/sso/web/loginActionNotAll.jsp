<%@page language="java" pageEncoding="UTF-8" import="dswork.web.*,dswork.core.util.*,dswork.sso.model.*,dswork.sso.service.*"%><%!
static java.util.concurrent.ConcurrentMap<String, Long> err = new java.util.concurrent.ConcurrentHashMap<String, Long>();
%><%
String path = request.getContextPath();
response.setHeader("P3P", "CP=CAO PSA OUR");
response.setHeader("Access-Control-Allow-Origin", "*");
response.setCharacterEncoding("UTF-8");
MyRequest req = new MyRequest(request);
String account = req.getString("account").toLowerCase();
String password = req.getString("password");
String authcode = "0000";//req.getString("authcode");
String serviceURL = req.getString("service", request.getContextPath() + "/ticket.jsp");
String loginURL = req.getString("loginURL", "");

//String xaccount = ","+account+",";
//String code = req.getString("code");
try{
	AuthFactoryService service  = (AuthFactoryService)dswork.spring.BeanFactory.getBean("authFactoryService");
	String msg = "用户名或密码错误！";























			LoginUser user = service.getLoginUserByAccount(account);
			if(user != null){
				if(user.getStatus() != 1){
					msg = "用户已禁用，请联系管理员！";
				}
				else if((EncryptUtil.encryptMd5(user.getPassword()+authcode).equals(password))){










						String cookieTicket = dswork.sso.controller.AuthController.putLoginInfo(request, response, user.getAccount(), user.getName());
						try{
							service.saveLogLogin(cookieTicket, dswork.sso.controller.AuthController.getClientIp(request), user.getAccount(), user.getName(), true);
						}
						catch(Exception logex){
						}
						// 成功就跳到切换系统视图
						// 无需登录，生成ticket给应用去登录
						if(serviceURL.startsWith(request.getContextPath() + "/password")){
							response.sendRedirect(serviceURL);
						}
						else{
							response.sendRedirect(serviceURL += ((serviceURL.indexOf("?") != -1) ? "&ticket=" : "?ticket=") + TicketService.getOnceTicket(cookieTicket));
						}
						return;

				}








			}


	// 失败则转回来
	request.setAttribute("account", account);
	request.setAttribute("service", serviceURL);
	if(loginURL.length() > 0){
		loginURL = loginURL + (loginURL.indexOf("?")==-1 ? "?" : "&") + "service=" + java.net.URLEncoder.encode(serviceURL, "UTF-8");
		loginURL = loginURL + "&msg=" + java.net.URLEncoder.encode(java.net.URLEncoder.encode(msg, "UTF-8"), "UTF-8");
		response.sendRedirect(loginURL);
		return;
	}
	request.setAttribute("msg", msg);
	try{
		service.saveLogLogin("", dswork.sso.controller.AuthController.getClientIp(request), account, "", false);
	}
	catch(Exception logex){
	}
	%><%@include file="WEB-INF/view/jsp/login.jsp"%><%
}
catch(Exception e){
}
%>