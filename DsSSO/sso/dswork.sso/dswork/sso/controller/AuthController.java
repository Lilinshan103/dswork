/**
 * 获取用户信息
 */
package dswork.sso.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.core.util.EncryptUtil;
import dswork.sso.listener.SessionListener;
import dswork.sso.model.LoginUser;
import dswork.sso.service.AuthFactoryService;
import dswork.sso.service.TicketService;
import dswork.web.MyAuthCodeServlet;
import dswork.web.MyCookie;
import dswork.web.MyRequest;

@Controller
public class AuthController
{
	static Logger log = LoggerFactory.getLogger(AuthController.class.getName());
	@Autowired
	private AuthFactoryService service;

	/**
	 * 用户登录系统的入口
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		response.setHeader("P3P", "CP=CAO PSA OUR");
		response.setHeader("Access-Control-Allow-Origin", "*");
		//response.setHeader("P3P", "CP='CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR'");
		//response.setHeader("P3P", "CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
		MyRequest req = new MyRequest(request);
		String serviceURL = req.getString("service", request.getContextPath() + "/ticket.jsp");
		String loginURL = req.getString("loginURL", "");
		if(log.isDebugEnabled())
		{
			log.debug(serviceURL);
		}
		MyCookie cookie = new MyCookie(request, response);
		String cookieTicket = cookie.getValue(SessionListener.DS_SSO_TICKET);
		if(cookieTicket != null)// 有cookie存在
		{
			String value;
			value = TicketService.getValueByTicket(cookieTicket);
			if(value != null)
			{
				// 无需登录，生成ticket给应用去登录
				if(serviceURL.startsWith(request.getContextPath() + "/password"))
				{
					response.sendRedirect(serviceURL);
				}
				else
				{
					response.sendRedirect(serviceURL += ((serviceURL.indexOf("?") != -1) ? "&ticket=" : "?ticket=") + TicketService.getOnceTicket(cookieTicket));
				}
				return null;
			}
			removeLoginInfo(request, response);// 把相关信息删除
		}
		request.setAttribute("service", serviceURL);
		request.setAttribute("loginURL", loginURL);
		String msg = req.getString("msg");
		try
		{
			if(msg.length() > 0)
			{
				msg = java.net.URLDecoder.decode(msg, "UTF-8");// 防止get方式(两次转码)传递，尝试再转码一次
			}
		}
		catch (Exception e)
		{
		}
		request.setAttribute("msg", msg);
		return "/login.jsp";
	}

	/**
	 * 用户登录系统的验证方法
	 */
	@RequestMapping("/loginAction")
	public String loginAction(HttpServletRequest request, HttpServletResponse response)
	{
		response.setHeader("P3P", "CP=CAO PSA OUR");
		response.setHeader("Access-Control-Allow-Origin", "*");
		MyRequest req = new MyRequest(request);
		String account = req.getString("account").toLowerCase();
		String password = req.getString("password");
		String authcode = req.getString("authcode");
		String serviceURL = req.getString("service", request.getContextPath() + "/ticket.jsp");
		String loginURL = req.getString("loginURL", "");
		if(log.isDebugEnabled())
		{
			log.debug(serviceURL);
		}
		try
		{
			String msg = "用户名或密码错误！";
			String randcode = String.valueOf(request.getSession().getAttribute(MyAuthCodeServlet.SessionName_Randcode)).trim();
			if(randcode.equals("null") || randcode.equals(""))
			{
				msg = "验证码已过期";
			}
			else if(!randcode.toLowerCase().equals(authcode.toLowerCase()))
			{
				msg = "验证码输入错误,请重新输入";
			}
			else
			{
				request.getSession().setAttribute(MyAuthCodeServlet.SessionName_Randcode, "");// 对了再清除
				
				LoginUser user = service.getLoginUserByAccount(account);
				if(user != null)
				{
					if(user.getStatus() != 1)// Status:1允许，0禁止
					{
						msg = "用户已禁用，请联系管理员！";
					}
					else if((EncryptUtil.encryptMd5(user.getPassword()+authcode).equals(password)))
					{
						String cookieTicket = putLoginInfo(request, response, user.getAccount(), user.getName());
						try
						{
							service.saveLogLogin(cookieTicket, getClientIp(request), user.getAccount(), user.getName(), true);
						}
						catch(Exception logex)
						{
						}
						// 成功就跳到切换系统视图
						// 无需登录，生成ticket给应用去登录
						if(serviceURL.startsWith(request.getContextPath() + "/password"))
						{
							response.sendRedirect(serviceURL);
						}
						else
						{
							response.sendRedirect(serviceURL += ((serviceURL.indexOf("?") != -1) ? "&ticket=" : "?ticket=") + TicketService.getOnceTicket(cookieTicket));
						}
						return null;
					}
				}
			}
			// 失败则转回来
			request.setAttribute("account", account);
			request.setAttribute("service", serviceURL);
			if(loginURL.length() > 0)
			{
				loginURL = loginURL + (loginURL.indexOf("?")==-1 ? "?" : "&") + "service=" + java.net.URLEncoder.encode(serviceURL, "UTF-8");
				loginURL = loginURL + "&msg=" + java.net.URLEncoder.encode(java.net.URLEncoder.encode(msg, "UTF-8"), "UTF-8");
				response.sendRedirect(loginURL);
				return null;
			}
			// request.setAttribute("loginURL", loginURL);
			request.setAttribute("msg", msg);
			try
			{
				service.saveLogLogin("", getClientIp(request), account, "", false);
			}
			catch(Exception logex)
			{
			}
			return "/login.jsp";
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 用户登出系统
	 */
	@RequestMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			MyCookie cookie = new MyCookie(request, response);
			String cookieTicket = String.valueOf(cookie.getValue(SessionListener.DS_SSO_TICKET));
			service.saveLogLogout(String.valueOf(cookieTicket), false, false);
			if(!cookieTicket.equals("null") && cookieTicket.length() > 0)
			{
				removeLoginInfo(request, response);// 试着删除
			}
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		MyRequest req = new MyRequest(request);
		String loginURL = req.getString("loginURL", "");
		if(loginURL.length() > 0)
		{
			loginURL = loginURL + (loginURL.indexOf("?") == -1 ? "?" : "&") + "service=";
		}
		else
		{
			loginURL = request.getContextPath() + "/login?service=";
		}
		String serviceURL = java.net.URLEncoder.encode(req.getString("service", request.getContextPath() + "/ticket.jsp"), "UTF-8");
		response.sendRedirect(loginURL + String.valueOf(serviceURL));
		return;
	}
	
	/**
	 * 已登录用户修改密码的入口
	 */
	@RequestMapping("/password")
	public String password(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		response.setHeader("P3P", "CP=CAO PSA OUR");
		response.setHeader("Access-Control-Allow-Origin", "*");
		//response.setHeader("P3P", "CP='CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR'");
		//response.setHeader("P3P", "CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
		MyRequest req = new MyRequest(request);
		String loginURL = req.getString("loginURL", "");
		String serviceURL = req.getString("service", request.getContextPath() + "/password");
		if(log.isDebugEnabled())
		{
			log.debug(serviceURL);
		}
		MyCookie cookie = new MyCookie(request, response);
		String cookieTicket = cookie.getValue(SessionListener.DS_SSO_TICKET);
		if(cookieTicket != null)// 有cookie存在
		{
			String _account = TicketService.getValueByTicket(cookieTicket);
			if(_account != null)
			{
				request.setAttribute("account", _account);
				request.setAttribute("service", serviceURL);
				request.setAttribute("loginURL", loginURL);
				request.setAttribute("msg", "");
				return "/password.jsp";
			}
		}
		removeLoginInfo(request, response);// 把相关信息删除
		serviceURL = request.getContextPath() + "/password?service=" + java.net.URLEncoder.encode(serviceURL, "UTF-8");// 登录后回来修改页并可以再重定向回原页
		if(loginURL.length() > 0)
		{
			loginURL = loginURL + (loginURL.indexOf("?") == -1 ? "?" : "&") + "service=";
		}
		else
		{
			loginURL = request.getContextPath() + "/login?service=";
		}
		response.sendRedirect(loginURL + String.valueOf(java.net.URLEncoder.encode(serviceURL, "UTF-8")));
		return null;
	}

	/**
	 * 已登录用户修改密码的处理
	 */
	@RequestMapping("/passwordAction")
	public String passwordAction(HttpServletRequest request, HttpServletResponse response)
	{
		response.setHeader("P3P", "CP=CAO PSA OUR");
		response.setHeader("Access-Control-Allow-Origin", "*");
		MyRequest req = new MyRequest(request);
		String account = req.getString("account").toLowerCase();
		String oldpassword = req.getString("oldpassword");
		String newpassword = req.getString("password");
		String authcode = req.getString("authcode");
		String serviceURL = req.getString("service", request.getContextPath() + "/password");
		if(log.isDebugEnabled())
		{
			log.debug(serviceURL);
		}
		try
		{
			MyCookie cookie = new MyCookie(request, response);
			String cookieTicket = cookie.getValue(SessionListener.DS_SSO_TICKET);
			if(cookieTicket != null)// 有cookie存在
			{
				String _account = TicketService.getValueByTicket(cookieTicket);
				newpassword = EncryptUtil.decodeDes(newpassword, authcode);// 解密输入的新密码
				if(_account != null && _account.equals(account))
				{
					String msg = "";
					String randcode = String.valueOf(request.getSession().getAttribute(MyAuthCodeServlet.SessionName_Randcode)).trim();
					if(randcode.equals("null") || randcode.equals(""))
					{
						msg = "验证码已过期";
					}
					else if(newpassword == null || newpassword.length() < 1)
					{
						msg = "新密码输入错误,请重新输入";
					}
					else if(!randcode.toLowerCase().equals(authcode.toLowerCase()))
					{
						msg = "验证码输入错误,请重新输入";
					}
					else
					{
						request.getSession().setAttribute(dswork.web.MyAuthCodeServlet.SessionName_Randcode, "");// 对了再清除
						LoginUser user = service.getLoginUserByAccount(_account);
						if(user != null)
						{
							if(user.getStatus() != 1)// Status:1允许，0禁止
							{
								msg = "用户已禁用，请联系管理员！";
							}
							else if((EncryptUtil.encryptMd5(user.getPassword()+authcode).equals(oldpassword)))
							{
								service.updatePassword(_account, newpassword);
								// 成功就跳到切换系统视图
								if(serviceURL.startsWith(request.getContextPath() + "/password"))
								{
									request.setAttribute("serviceURL", serviceURL);
									request.setAttribute("returnPassword", "true");
								}
								else
								{
									request.setAttribute("serviceURL", serviceURL + ((serviceURL.indexOf("?") != -1) ? "&ticket=" : "?ticket=") + TicketService.getOnceTicket(cookieTicket));
									request.setAttribute("returnPassword", "false");
								}
								return "/passwordSuccess.jsp";
							}
							else
							{
								msg = "原密码输入错误,请重新输入";
							}
						}
					}
					// 失败则转回来
					request.setAttribute("account", _account);
					request.setAttribute("service", serviceURL);
					request.setAttribute("msg", msg);
					return "/password.jsp";
				}
			}
			removeLoginInfo(request, response);// 把相关信息删除
			serviceURL = request.getContextPath() + "/password?service=" + java.net.URLEncoder.encode(serviceURL, "UTF-8");// 登录后回来修改页并可以再重定向回原页
			String loginURL = req.getString("loginURL", "");
			if(loginURL.length() > 0)
			{
				loginURL = loginURL + (loginURL.indexOf("?") == -1 ? "?" : "&") + "service=";
			}
			else
			{
				loginURL = request.getContextPath() + "/login?service=";
			}
			response.sendRedirect(loginURL + String.valueOf(java.net.URLEncoder.encode(serviceURL, "UTF-8")));
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}

	public static String putLoginInfo(HttpServletRequest request, HttpServletResponse response, String account, String name)
	{
		String ticket = String.valueOf(request.getSession().getAttribute(SessionListener.DS_SSO_TICKET));
		TicketService.removeSession(ticket);// 如果有，删除原session带的信息
		MyCookie cookie = new MyCookie(request, response);
		ticket = TicketService.saveSession(account);
		request.getSession().setAttribute(SessionListener.DS_SSO_TICKET, ticket);// 这里主要用在session侦听中，超时退出时用来获取ticket
		cookie.addCookie(SessionListener.DS_SSO_TICKET, ticket, -1, "/", null, false, true);// 更新
		// 使用ticket作为密码进行des加密(账号#姓名)
		cookie.addCookie(SessionListener.DS_SSO_CODE, dswork.core.util.EncryptUtil.encodeDes((account+"#"+name), ticket), -1, "/", null, false, true);// 更新
		return ticket;
	}

	public static void removeLoginInfo(HttpServletRequest request, HttpServletResponse response)
	{
		MyCookie cookie = new MyCookie(request, response);
		String cookieTicket = String.valueOf(cookie.getValue(SessionListener.DS_SSO_TICKET));
		TicketService.removeSession(cookieTicket);//  如果有，删除原cookie带的信息，此处为cookie存在信息时才调用
		cookie.delCookie(SessionListener.DS_SSO_TICKET);
		cookie.delCookie(SessionListener.DS_SSO_CODE);// 删除账号#姓名
	}

	public static String getClientIp(HttpServletRequest request)
	{
		String ip = request.getHeader("X-Real-IP");
		if(ip != null && ip.length() > 0 && !"null".equalsIgnoreCase(ip) && !"unKnown".equalsIgnoreCase(ip))
		{
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if(ip != null && ip.length() > 0 && !"null".equalsIgnoreCase(ip) && !"unKnown".equalsIgnoreCase(ip))
		{
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if(index != -1)
			{
				return ip.substring(0, index);
			}
			else
			{
				return ip;
			}
		}
		return request.getRemoteAddr();
	}
}
