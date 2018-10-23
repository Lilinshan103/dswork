package dswork.sso;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dswork.sso.http.HttpUtil;
import dswork.sso.model.IUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebFilter implements Filter
{
	static com.google.gson.Gson gson = AuthGlobal.getGson();
	static Logger log = LoggerFactory.getLogger("dswork.sso");
	
	private static boolean use = false;// 用于判断是否加载sso模块

	private final static String TICKET = "ticket";// url中传来的sessionKey的变量名
	public final static String LOGINER = "sso.web.loginer";// sessionUser在session中的key

	private final static String DS_SSO_TICKET = "DS_SSO_TICKET";
	private final static String DS_SSO_CODE = "DS_SSO_CODE";
	
	private static String thirdLoginURL = "";
	private static String loginURL = "";// 登入页面
	private static String logoutURL = "";// 登出页面
	private static String passwordURL = "";// 修改密码页面
	private static String webURL = "";// SSO项目根地址
	private static boolean sameDomain = false;
	private static String systemURL = "";// 设置全局重定向地址
	private static Set<String> ignoreURLSet = new HashSet<String>();// 无需验证页面

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession();
		String relativeURI = request.getServletPath();// 相对地址
//		String relativeURI = request.getRequestURI();// request.getRequestURI()=request.getContextPath()+request.getServletPath()
//		if(request.getContextPath().length() > 0)
//		{
//			relativeURI = relativeURI.replaceFirst(request.getContextPath(), "");
//		}
		
		@SuppressWarnings("unchecked")
		Map<String, String> ouser = (Map<String, String>)session.getAttribute(LOGINER);
		if(log.isInfoEnabled())
		{
			log.info("当前访问地址：" + request.getContextPath() + relativeURI);
			log.info(ouser != null ? "当前登录用户是：" + ouser.get("account") : "当前没有登录用户");
		}
		
		// 和sso项目在同一域名情况下，可直接忽略ticket模式
		if(sameDomain)
		{
			String ssoTicket = getValueByCookie(request, DS_SSO_TICKET);
			String ssoCode = getValueByCookie(request, DS_SSO_CODE);
			// 使用cookie模式登录，否则还用原方式
			if(ssoTicket != null && ssoCode != null)
			{
				if(ssoTicket.equals(String.valueOf(session.getAttribute(TICKET))))// 一样的ticket
				{
					if(ouser != null)// 已登录
					{
						chain.doFilter(request, response);
						return;
					}
				}
				try
				{
					String msg = dswork.sso.util.EncryptUtil.decodeDes(ssoCode, ssoTicket);
					String[] msgs = msg.split("#", -1);
					if(msgs.length > 1)
					{
						if(doValidateAccount(session, AuthFactory.getUser(msgs[0]), ssoTicket))
						{
							chain.doFilter(request, response);
							return;
						}
					}
				}
				catch(Exception e)
				{
				}
			}
		}
		
		// 判断是否有ticket
		String ticket = request.getParameter(TICKET);
		if(ticket == null)// 如果不经由ticket的链接过来，则进此处判断
		{
			if(log.isInfoEnabled())
			{
				log.info("request中的ticket为空");
			}
			if(ouser != null)// 已登录
			{
				chain.doFilter(request, response);
				return;
			}
		}
		else// ticket非空,由链接传来ticket
		{
			if(ticket.equals(String.valueOf(session.getAttribute(TICKET))))// 一样的ticket，这里的session放的ticket不是ssoticket
			{
				if(session.getAttribute(LOGINER) != null)// 已登录
				{
					chain.doFilter(request, response);
					return;
				}
			}
			else
			{
				if(log.isInfoEnabled())
				{
					log.info("session和request中的ticket不相等");
					if("null".equals(String.valueOf(session.getAttribute(TICKET))))
					{
						log.info("session中的ticket为空");
					}
				}
			}
			if(doValidate(session, ticket))
			{
				chain.doFilter(request, response);
				return;// 验证成功
			}
		}
		// String relativeURI = request.getRequestURI();// 相对地址
		// if(request.getContextPath().length() > 0){relativeURI = relativeURI.replaceFirst(request.getContextPath(), "");}
		if(ignoreURLSet.contains(relativeURI))// 判断是否为无需验证页面
		{
			chain.doFilter(request, response);// 是无需验证页面
			return;
		}
		response.setHeader("P3P", "CP=CAO PSA OUR");
		response.setHeader("Access-Control-Allow-Origin", "*");
		if("XMLHttpRequest".equals(String.valueOf(request.getHeader("X-Requested-With"))))
		{
			response.getWriter().print("{\"status\":0,\"code\":\"401\",msg:\"Unauthorized\"}");// status：0失败，1成功
			return;
		}
		response.sendRedirect(getLoginURL(request));
		return;
	}

	public static String getAccount(HttpSession session)
	{
		Map<String, String> m = getLoginer(session);
		String account = String.valueOf(m.get("account"));
		return (account.length() > 0 && !account.equals("null")) ? account : "";
	}

	@SuppressWarnings("all")
	private static String getLoginURL(HttpServletRequest request)
	{
		String ssoroot = request.getParameter("ssoroot");
		if((ssoroot == null || !"false".equals(ssoroot)) && systemURL.length() > 0)
		{
			return getLoginURL("");
		}
		String url = "";
		try
		{
			String params = "";
			Enumeration e = request.getParameterNames();
			String key = "";
			String value = "";
			while(e.hasMoreElements())
			{
				key = String.valueOf(e.nextElement());
				value = request.getParameter(key);
				if(value != null && !value.equals("") && !key.equals(TICKET) && !key.equals("service"))
				{
					params += "&" + key + "=" + getValueByParameter(request, key);
				}
			}
			url = request.getRequestURL().toString();
			//params += "&ex=" + System.currentTimeMillis();
			if(params.length() > 0)
			{
				url = url + (url.indexOf("?") == -1 ? "?" : "&") + params.substring(1);
			}
		}
		catch(Exception e)
		{
			url = "";
		}
		return getLoginURL(url);
	}
	
	/**
	 * @note 获取指定用户的基本信息
	 * @param ticket 登录凭证
	 * @return Loginer
	 */
	@SuppressWarnings("all")
	public static Map<String, String> getLoginer(HttpSession session)
	{
		Map<String, String> m = null;
		try
		{
			m = (Map<String, String>) session.getAttribute(LOGINER);
		}
		catch(Exception e)
		{
		}
		if(m == null)
		{
			m = new java.util.HashMap<String, String>();
		}
		return m;
	}
	
	public static String getWebURL()
	{
		return webURL;
	}
	
	public static String getPasswordURL(String url)
	{
		String _url = passwordURL;
		try
		{
			if(url == null || url.length() == 0)
			{
				if(systemURL.length() > 0)
				{
					_url = passwordURL + "?service=" + URLEncoder.encode(systemURL, "UTF-8") + (thirdLoginURL.length() > 0 ? "&loginURL=" + URLEncoder.encode(thirdLoginURL, "UTF-8") : "");
				}
			}
			else
			{
				_url = passwordURL + "?service=" + URLEncoder.encode(url, "UTF-8") + (thirdLoginURL.length() > 0 ? "&loginURL=" + URLEncoder.encode(thirdLoginURL, "UTF-8") : "");
			}
		}
		catch(Exception e)
		{
			_url = passwordURL;
		}
		return _url;
	}
	
	public static String getLoginURL(String url)
	{
		String _url = loginURL;
		try
		{
			if(url == null || url.length() == 0)
			{
				if(systemURL.length() > 0)
				{
					_url = loginURL + (loginURL.indexOf("?") == -1 ? "?" : "&") + "service=" + URLEncoder.encode(systemURL, "UTF-8") + (thirdLoginURL.length() > 0 ? "&loginURL=" + URLEncoder.encode(thirdLoginURL, "UTF-8") : "");
				}
			}
			else
			{
				_url = loginURL + (loginURL.indexOf("?") == -1 ? "?" : "&") + "service=" + URLEncoder.encode(url, "UTF-8") + (thirdLoginURL.length() > 0 ? "&loginURL=" + URLEncoder.encode(thirdLoginURL, "UTF-8") : "");
			}
		}
		catch(Exception e)
		{
			_url = loginURL;
		}
		return _url;
	}
	
	public static String getLogoutURL(String url)
	{
		String _url = logoutURL;
		try
		{
			if(url == null || url.length() == 0)
			{
				if(systemURL.length() > 0)
				{
					_url = logoutURL + (logoutURL.indexOf("?") == -1 ? "?" : "&") + "service=" + URLEncoder.encode(systemURL, "UTF-8") + (thirdLoginURL.length() > 0 ? "&loginURL=" + URLEncoder.encode(thirdLoginURL, "UTF-8") : "");
				}
			}
			else
			{
				_url = logoutURL + (logoutURL.indexOf("?") == -1 ? "?" : "&") + "service=" + URLEncoder.encode(url, "UTF-8") + (thirdLoginURL.length() > 0 ? "&loginURL=" + URLEncoder.encode(thirdLoginURL, "UTF-8") : "");
			}
		}
		catch(Exception e)
		{
			_url = logoutURL;
		}
		return _url;
	}

	private boolean doValidate(HttpSession session, String ticket)
	{
		IUser user = null;
		try
		{
			String u = AuthGlobal.getURL() + "/getLoginer?ticket=" + ticket;
			String v = new HttpUtil().create(u, u.startsWith("https:")).connect().trim();
			if(log.isDebugEnabled())
			{
				log.debug("WebFilter:url=" + u + ", json:" + v);
			}
			user = gson.fromJson(v, IUser.class);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return doValidateAccount(session, user, ticket);
	}
	
	private boolean doValidateAccount(HttpSession session, IUser user, String ticket)
	{
		try
		{
			if(user != null && user.getAccount().length() > 0 && !"null".equals(user.getAccount()))
			{
				Map<String, String> m = new java.util.HashMap<String, String>();
				m.put("id", user.getId() + "");
				m.put("account", user.getAccount());
				m.put("name", user.getName());
				m.put("idcard", user.getIdcard());
				m.put("workcard", user.getWorkcard());
				m.put("orgid", user.getOrgid() + "");
				m.put("orgpid", user.getOrgpid() + "");
				m.put("type", user.getType() + "");
				m.put("typename", user.getTypename() + "");
				m.put("exalias", user.getExalias() + "");
				m.put("exname", user.getExname() + "");
				session.setAttribute(LOGINER, m);
				session.setAttribute(TICKET, ticket);
				if(log.isDebugEnabled())
				{
					log.debug("sameDomain=" + (sameDomain) + ", account=" + user.getAccount() + ", ticket=" + ticket);
				}
				return true;
			}
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		logout(session);
		return false;
	}

	public static void logout(HttpSession session)
	{
		session.removeAttribute(LOGINER);
		session.removeAttribute(TICKET);
	}
	
	public static boolean isUse()
	{
		return WebFilter.use;
	}

	public void init(FilterConfig config) throws ServletException
	{
		String ssoURL, ssoName, ssoPassword, ignoreURL, hasSameDoamin;
		ssoURL = String.valueOf(config.getInitParameter("ssoURL")).trim();
		if("null".equals(ssoURL))// 不使用web.xml中的配置
		{
			String configFile = String.valueOf(config.getServletContext().getInitParameter("dsworkSSOConfiguration")).trim();
			java.util.Properties CONFIG = new java.util.Properties();
			java.io.InputStream stream = WebFilter.class.getResourceAsStream(configFile);
			if (stream != null)
			{
				try{CONFIG.load(stream);}
				catch (Exception e){}
				finally{try{stream.close();}catch (IOException ioe){}}
			}
			ssoURL = String.valueOf(CONFIG.getProperty("sso.ssoURL")).trim();
			ssoName = String.valueOf(CONFIG.getProperty("sso.ssoName")).trim();
			ssoPassword = String.valueOf(CONFIG.getProperty("sso.ssoPassword")).trim();
			webURL = String.valueOf(CONFIG.getProperty("sso.webURL")).trim();
			loginURL = String.valueOf(CONFIG.getProperty("sso.loginURL")).trim();
			logoutURL = String.valueOf(CONFIG.getProperty("sso.logoutURL")).trim();
			if(!"null".equals(webURL))
			{
				if("null".equals(loginURL))
				{
					loginURL  = webURL + "/login";
				}
				else if(!loginURL.equals(webURL + "/login"))
				{
					thirdLoginURL = loginURL;
				}
				if("null".equals(logoutURL))
				{
					logoutURL = webURL + "/logout";
				}
				passwordURL = webURL + "/password";
			}
			systemURL = String.valueOf(CONFIG.getProperty("sso.systemURL")).trim();
			hasSameDoamin = String.valueOf(CONFIG.getProperty("sso.sameDomain")).trim();
			ignoreURL = String.valueOf(CONFIG.getProperty("sso.ignoreURL")).trim();
		}
		else
		{
			ssoURL = String.valueOf(config.getInitParameter("ssoURL")).trim();
			ssoName = String.valueOf(config.getInitParameter("ssoName")).trim();
			ssoPassword = String.valueOf(config.getInitParameter("ssoPassword")).trim();
			webURL = String.valueOf(config.getInitParameter("webURL")).trim();
			loginURL = String.valueOf(config.getInitParameter("loginURL")).trim();
			logoutURL = String.valueOf(config.getInitParameter("logoutURL")).trim();
			if(!"null".equals(webURL))
			{
				if("null".equals(loginURL))
				{
					loginURL  = webURL + "/login";
				}
				else if(!loginURL.equals(webURL + "/login"))
				{
					thirdLoginURL = loginURL;
				}
				if("null".equals(logoutURL))
				{
					logoutURL = webURL + "/logout";
				}
				passwordURL = webURL + "/password";
			}
			systemURL = String.valueOf(config.getInitParameter("systemURL")).trim();
			hasSameDoamin = String.valueOf(config.getInitParameter("sameDomain")).trim();
			ignoreURL = String.valueOf(config.getInitParameter("ignoreURL")).trim();
		}
		AuthGlobal.init(ssoURL, ssoName, ssoPassword);
		WebFilter.use = true;
		if("null".equals(systemURL)){systemURL = "";}
		ignoreURLSet.clear();
		if(ignoreURL.length() > 0)
		{
			String[] values = ignoreURL.trim().split(",");
			for(String value : values){if(value.trim().length() > 0){ignoreURLSet.add(value.trim());}}
		}
		if(hasSameDoamin.equals("true")){sameDomain = true;}// 和sso在同一域名下时，可跳过ticket远程访问，直接读取cookie
	}

	public void destroy()
	{
	}

	/**
	 * 根据参数名称取得值
	 * @param parameter
	 * @return
	 */
	private static String getValueByParameter(HttpServletRequest request, String parameter)
	{
		String[] values = request.getParameterValues(parameter);
		String value = "";
		if(values.length == 1)
		{
			value = request.getParameter(parameter);
			if(value == null)
			{
				return "";
			}
			value = value.trim();
		}
		else
		{
			int k = values.length;
			for(int i = 0; i < values.length; i++)
			{
				if(i == k - 1)
				{
					value += values[i].trim();
				}
				else
				{
					value += values[i].trim() + ",";
				}
			}
		}
		return value;
	}
	
	private static String getValueByCookie(HttpServletRequest request, String name)
	{
		Cookie cookies[] = request.getCookies();
		String value = null;
		if(cookies != null)
		{
			Cookie cookie = null;
			for(int i = 0; i < cookies.length; i++)
			{
				cookie = cookies[i];
				if(cookie.getName().equals(name))
				{
					value = cookie.getValue();
					break;
				}
			}
		}
		return value;
	}
}
