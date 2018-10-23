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
String authcode = req.getString("authcode");
String serviceURL = req.getString("service", request.getContextPath() + "/ticket.jsp");
String loginURL = req.getString("loginURL", "");

//String xaccount = ","+account+",";
//String code = req.getString("code");
try{
	AuthFactoryService service  = (AuthFactoryService)dswork.spring.BeanFactory.getBean("authFactoryService");
	String msg = "用户名或密码错误！";
	String randcode = String.valueOf(request.getSession().getAttribute(MyAuthCodeServlet.SessionName_Randcode)).trim();
	if(randcode.equals("null") || randcode.equals("")){
		msg = "验证码已过期";
	}
	else if(!randcode.toLowerCase().equals(authcode.toLowerCase())){
		msg = "验证码输入错误,请重新输入";
	}
	else{
		request.getSession().setAttribute(MyAuthCodeServlet.SessionName_Randcode, "");// 对了再清除
		Long count = err.get(account);
		if(count == null || count.longValue() < 0){
			count = 0L;
		}
		else if(count.longValue() >= 5){
			if(System.currentTimeMillis() - err.get(account) > 600000){
				err.put(account, null);
				count = 0L;
			}
			else{
				msg = "您的密码输入错误过多，请稍后再试!";
			}
		}
		if(count.longValue() < 5){
			LoginUser user = service.getLoginUserByAccount(account);
			if(user != null){
				if(user.getStatus() != 1){
					msg = "用户已禁用，请联系管理员！";
				}
				else if((EncryptUtil.encryptMd5(user.getPassword()+authcode).equals(password))){
					err.put(account, null);
					//boolean gkcode = true;
					//if(",admin,99999,hxp,www,".indexOf(xaccount) == -1){
					//	code = EncryptUtil.decodeDes(code, "login");
					//	if(!dswork.sso.controller.AuthCodeUtil.getCode().equals(code)){
					//		gkcode = false;
					//		msg = "管控密码错误！";
					//	}
					//}
					//if(gkcode){
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
					//}
				}
				else{
					count++;
					err.put(account, count);
					if(count.longValue() >= 5){
						err.put(account, System.currentTimeMillis());// 大于5次则变成记录时间
						msg = "您的密码输入错误过多，请10分钟后再试!";
					}
				}
			}
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