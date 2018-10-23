package dswork.sso;

import javax.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
@WebServlet(name="WebLogoutServlet", loadOnStartup=1, urlPatterns={"/logout"})
public class MyWebLogoutServlet extends WebLogoutServlet
{
}
