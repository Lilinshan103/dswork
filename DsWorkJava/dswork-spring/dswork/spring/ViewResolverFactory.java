package dswork.spring;

import java.util.Locale;
import java.util.Map;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/**
 * SpringMVC的ViewResolver
 * @author skey
 * @version 2
 */
public class ViewResolverFactory implements ViewResolver
{
	private Map<String, ViewResolver> viewResolvers;

	public void setViewResolvers(Map<String, ViewResolver> viewResolvers)
	{
		this.viewResolvers = viewResolvers;
	}

	public View resolveViewName(String viewName, Locale locale) throws Exception
	{
		int n = viewName.lastIndexOf('.');
		if(n == (-1))
		{
			throw new Exception("No such ViewResolver.");
		}
		String suffix = viewName.substring(n + 1);
		ViewResolver resolver = viewResolvers.get(suffix);
		// viewName=viewName.substring(0,n);
		if(resolver != null)
			return resolver.resolveViewName(viewName, locale);
		throw new Exception("No ViewResolver for " + suffix);
	}
}
