package dswork.common;

import dswork.common.util.DsCommonIFlowUtil;

/**
 * 在service调用，相当于dao，本身没有事务支持。
 */
public class DsFactoryUtil
{
	private static DsCommonIFlowUtil d = new DsCommonIFlowUtil();
	public static DsCommonIFlowUtil getFlow()
	{
		return d;
	}
}
