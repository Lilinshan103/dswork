package common.auth;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * 认证过滤器
 * @author skey
 */
public class AuthFilter implements Filter
{
	public void init(FilterConfig config) throws ServletException
	{
		try
		{
		}
		catch(Exception e)
		{
		}
		System.out.println("AuthFilter initialization");
	}

	public void destroy()
	{
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(req);
		HttpServletResponseWrapper responseWraper = new HttpServletResponseWrapper(res);
		//String currentUrl = req.getRequestURI().replaceAll(req.getContextPath(), "");// 取得当前路径
		try
		{
			Auth model = AuthUtil.getLoginUser(req);
			// 没有登录
			if(model == null || model.getAccount() == null)
			{
				if("XMLHttpRequest".equals(String.valueOf(req.getHeader("X-Requested-With"))))
				{
					res.getWriter().print("0:登录超时！");
					return;
				}
				res.sendRedirect(req.getContextPath() + "/50x.jsp");
				return;
			}
			chain.doFilter(requestWrapper, responseWraper);
		}
		catch(Exception e)
		{
		}
	}
}
