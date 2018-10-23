package config.sso;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;

public class WebInitializer implements WebApplicationInitializer
{
	@Override
	public void onStartup(ServletContext context) throws ServletException
	{
		context.addListener("dswork.sso.listener.SessionListener");
		javax.servlet.ServletRegistration.Dynamic ssoServlet = context.addServlet("ssoServlet", "org.springframework.web.servlet.DispatcherServlet");
		ssoServlet.setLoadOnStartup(0);
		ssoServlet.setInitParameter("contextConfigLocation", "classpath*:/config/sso/sso-servlet.xml");
		ssoServlet.addMapping("/login", "/loginAction", "/logout", "/password", "/passwordAction", "/api/*");
	}
}
