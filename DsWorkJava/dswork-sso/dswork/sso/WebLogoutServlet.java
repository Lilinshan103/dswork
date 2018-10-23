package dswork.sso;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebLogoutServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	static Logger log = LoggerFactory.getLogger("dswork.sso.logout");

	public WebLogoutServlet()
	{
		super();
	}

	public void destroy()
	{
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			HttpSession session = request.getSession();
			session.removeAttribute(WebFilter.LOGINER);
			session.invalidate();
			String jsoncallback = request.getParameter("jsoncallback").replaceAll("<", "").replaceAll(">", "").replaceAll("\"", "").replaceAll("'", "");
			PrintWriter out = response.getWriter();
			out.print(jsoncallback + "([])");
		}
		catch(Exception ex)
		{
			log.error(ex.getMessage());
			ex.printStackTrace();
		}
		return;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}
