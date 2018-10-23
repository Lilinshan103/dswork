package dswork.web;

import java.lang.reflect.Modifier;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

@HandlesTypes(MyWebInitializer.class)
public class MyServletContainerInitializer implements ServletContainerInitializer
{
	@Override
	public void onStartup(Set<Class<?>> webInitializers, ServletContext servletContext) throws ServletException
	{
		if(webInitializers != null)
		{
			for(Class<?> webClass : webInitializers)
			{
				if(!webClass.isInterface() && !Modifier.isAbstract(webClass.getModifiers()) && MyWebInitializer.class.isAssignableFrom(webClass))
				{
					try
					{
						((MyWebInitializer) webClass.newInstance()).onStartup(servletContext);
					}
					catch(Throwable ex)
					{
						throw new ServletException("Failed to instantiate MyServletContainerInitializer class", ex);
					}
				}
			}
		}
	}
}
