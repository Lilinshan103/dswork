package dswork.common;

import dswork.common.service.DsCommonIFlowService;
import dswork.common.util.DsCommonIFlowServiceUtil;
import dswork.spring.BeanFactory;

/**
 * 在控制器调用，相当于service，本身提供事务支持。
 */
public class DsFactory
{
	public static DsCommonFactoryDict getDict()
	{
		return DsCommonFactoryDict.getInstance();
	}
	public static DsCommonFactoryOrg getOrg()
	{
		return DsCommonFactoryOrg.getInstance();
	}
	
	private static DsCommonIFlowServiceUtil factory = null;
	
	/**
	 * 相当于service，本身提供事务支持。
	 */
	public static DsCommonIFlowServiceUtil getFlow()
	{
		if(factory == null)
		{
			factory = new DsCommonIFlowServiceUtil((DsCommonIFlowService) BeanFactory.getBean("dsCommonIFlowService"));
		}
		return factory;
	}
}
