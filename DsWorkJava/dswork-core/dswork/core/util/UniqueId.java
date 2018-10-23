package dswork.core.util;

import java.util.*;

/**
 * ID生成类
 */
public class UniqueId
{
	private static long thisId = 0;
	
	private final static int ID_SIZE = new Long(EnvironmentUtil.getToLong("dswork.id.size", 10L)).intValue();
	private final static int ID_CODE = new Long(EnvironmentUtil.getToLong("dswork.id.code", 0L)).intValue();

	/**
	 * 根据时间戳和机器码产生一个唯一ID，具有防止重复机制，需要在配置文件中增加dswork.id.size（10的N次方）和dswork.id.code（自编码0至10的N次方减1）
	 * @return long
	 */
	public synchronized static long genUniqueId()
	{
		long id = 0;
		do
		{
			id = Calendar.getInstance().getTimeInMillis() * ID_SIZE + ID_CODE;
		}
		while (id == thisId);
		thisId = id;
		return id;
	}
	
	/**
	 * 根据时间戳产生一个唯一ID，具有防止重复机制
	 * @return long
	 */
	public synchronized static long genId()
	{
		long id = 0;
		do
		{
			id = Calendar.getInstance().getTimeInMillis();
		}
		while (id == thisId);
		thisId = id;
		return id;
	}

	/**
	 * 返回GUID，格式00000000-0000-0000-0000-000000000000
	 * @return String
	 */
	public static String genGuid()
	{
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
}
