package dswork.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 在Servlet中产生验证码图片
 */
public class MyAuthCodeServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	public static String SessionName_Randcode = "randcode";

	/**
	 * Constructor of the object.
	 */
	public MyAuthCodeServlet()
	{
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy()
	{
		super.destroy();
	}

	/**
	 * The doGet method of the servlet. <br>
	 * This method is called when a form has its tag value method equals to get.
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		int w = getInt(request, "width", 90), h = getInt(request, "height", 38), l = getInt(request, "length", 4);
		if(w < 10 || w > 300)
		{
			w = 90;
		}
		if(h < 10 || h > 300)
		{
			h = 38;
		}
		if(l < 2 || l > 64)
		{
			l = 4;
		}
		MyImage image = new MyImage();
		image.setWidth(w);
		image.setHeight(h);
		image.setLength(l);
		
		String sRand = image.getAuthCode();
		request.getSession().setAttribute(SessionName_Randcode, sRand);
		image.drawImgeToOutStream(response.getOutputStream());
	}

	/**
	 * The doPost method of the servlet. <br>
	 * This method is called when a form has its tag value method equals to get.
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * @throws ServletException if an error occurred
	 */
	public void init() throws ServletException
	{
	}
	
	private static int getInt(HttpServletRequest request, String key, int defaultValue)
	{
		try
		{
			String str = request.getParameter(key);
			return (str == null || str.trim().equals("")) ? defaultValue : Integer.parseInt(str.trim());
		}
		catch(Exception ex)
		{
			return defaultValue;
		}
	}
}
