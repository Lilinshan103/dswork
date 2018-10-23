package dswork.websso.util;

import dswork.core.util.EnvironmentUtil;

public class WebssoUtil
{
	private WebssoUtil()
	{
	}

	public static final boolean HasWeibo = EnvironmentUtil.getToBoolean("websso.weibo", false);
	public static final boolean HasWechat = EnvironmentUtil.getToBoolean("websso.wechat", false);
	public static final boolean HasQQ = EnvironmentUtil.getToBoolean("websso.qq", false);
	public static final boolean HasAlipay = EnvironmentUtil.getToBoolean("websso.alipay", false);
}
