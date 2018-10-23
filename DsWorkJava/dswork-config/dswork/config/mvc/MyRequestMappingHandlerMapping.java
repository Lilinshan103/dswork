package dswork.config.mvc;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class MyRequestMappingHandlerMapping extends RequestMappingHandlerMapping
{
	@Override
	protected RequestMappingInfo getMappingForMethod(java.lang.reflect.Method method, Class<?> handlerType)
	{
		RequestMappingInfo info = null;
		// 4.3.x后使用的其实是AnnotatedElementUtils.findMergedAnnotation, 以下写法兼容4.0.x
		RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
		if(requestMapping != null)
		{
			String methodName = method.getName();
			info = createRequestMappingInfo(requestMapping, getCustomMethodCondition(method), methodName);
			RequestMapping typeAnnotation = AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);
			if(typeAnnotation != null)
			{
				info = createRequestMappingInfo(typeAnnotation, getCustomTypeCondition(handlerType), methodName).combine(info);
			}
		}
		return info;
	}

	protected RequestMappingInfo createRequestMappingInfo(RequestMapping annotation, RequestCondition<?> customCondition, String methodName)
	{
		String[] patterns = resolveEmbeddedValuesInPatterns(annotation.value());
		if(patterns != null && (patterns.length == 0))
		{
			patterns = new String[]{methodName};
		}
		return new RequestMappingInfo(
			new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(), useSuffixPatternMatch(), useTrailingSlashMatch(), getFileExtensions()), 
			new RequestMethodsRequestCondition(annotation.method()), 
			new ParamsRequestCondition(annotation.params()),
			new HeadersRequestCondition(annotation.headers()), 
			new ConsumesRequestCondition(annotation.consumes(), annotation.headers()), 
			new ProducesRequestCondition(annotation.produces(), annotation.headers(), getContentNegotiationManager()), 
			customCondition
		);
	}
}
