package dswork.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取系统properties配置文件，默认路径为："/config/config.properties"
 */
public class EnvironmentUtil
{
	private static final String SYSTEM_PROPERTIES_PATH = "/config/config.properties";
	private static Properties SYSTEM_PROPERTIES;

	private EnvironmentUtil()
	{
	}
	static
	{
		setSystemProperties(SYSTEM_PROPERTIES_PATH);
	}

	public static void setSystemProperties(String path)
	{
		SYSTEM_PROPERTIES = new Properties();
		InputStream stream = EnvironmentUtil.class.getResourceAsStream(path);
		if (stream != null)
		{
			try
			{
				SYSTEM_PROPERTIES.load(stream);
			}
			catch (Exception e)
			{
				System.out.println();
			}
			finally
			{
				try
				{
					stream.close();
				}
				catch (IOException ioe)
				{
					System.out.println(SYSTEM_PROPERTIES_PATH + "关闭流失败");
				}
			}
		}
		else
		{
			System.out.println(SYSTEM_PROPERTIES_PATH + "无法加载");
		}
	}

	/**
	 * 获得系统属性配置信息，如果没有则返回null
	 * @param name 属性名
	 * @return String
	 */
	public static final String getStringProperty(String name)
	{
		if (SYSTEM_PROPERTIES != null)
		{
			String str = SYSTEM_PROPERTIES.getProperty(name);
			if(str != null)
			{
				return String.valueOf(str).trim();
			}
		}
		return null;
	}

	/**
	 * 获得系统属性配置信息，如果没有则返回默认值(长整型)
	 * @param name 属性名
	 * @param defaultValue 默认值
	 * @return long
	 */
	public static final long getToLong(String name, long defaultValue)
	{
		try
		{
			return Long.parseLong(getStringProperty(name));
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}

	/**
	 * 获得系统属性配置信息，如果没有则返回默认值(字符串类型)
	 * @param name 属性名
	 * @param defaultValue 默认值
	 * @return String
	 */
	public static final String getToString(String name, String defaultValue)
	{
		try
		{
			String str = getStringProperty(name);
			if (str != null)
			{
				return str;
			}
		}
		catch (Exception e)
		{
		}
		return defaultValue;
	}
	
	/**
	 * 获得系统属性配置信息，如果没有则返回默认值("true"、"false")
	 * @param name 属性名
	 * @param defaultValue 默认值
	 * @return boolean
	 */
	public static final boolean getToBoolean(String name, boolean defaultValue)
	{
		try
		{
			String str = String.valueOf(getStringProperty(name));
			if(str.equals("true"))
			{
				return Boolean.TRUE;
			}
			else if(str.equals("false"))
			{
				return Boolean.FALSE;
			}
		}
		catch (Exception e)
		{
		}
		return defaultValue;
	}
}
