package dswork.sso;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 该类已经没有作用，需要移除使用
 * @author skey
 */
@Deprecated
public class WebLogoutFilter implements Filter
{
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException
	{
		chain.doFilter(req, resp);
	}

	public void init(FilterConfig config) throws ServletException
	{
	}

	public void destroy()
	{
	}
}
