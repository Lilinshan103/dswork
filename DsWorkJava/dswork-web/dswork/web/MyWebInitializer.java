package dswork.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public interface MyWebInitializer
{
	void onStartup(ServletContext context) throws ServletException;
}
