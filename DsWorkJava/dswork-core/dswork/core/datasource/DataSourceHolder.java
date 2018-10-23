package dswork.core.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceHolder
{
	private final static Logger log = LoggerFactory.getLogger("jdbc.sqlonly");
	private static final Integer MASTER = 1;
	private static final Integer SLAVE = 2;
	
	// 标记数据源类型
	private static final ThreadLocal<Integer> chooseDataSource = new ThreadLocal<Integer>();
	
	/**
	 * 设置为使用主库数据源
	 */
	public static void setMaster()
	{
		if(log.isInfoEnabled())
		{
			log.info("set master datasource");
		}
		chooseDataSource.set(MASTER);
	}
	
	/**
	 * 设置为使用从库数据源
	 */
	public static void setSlave()
	{
		if(log.isInfoEnabled())
		{
			log.info("set slave datasource");
		}
		chooseDataSource.set(SLAVE);
	}
	
	/**
	 * 移除线程设置信息
	 */
	public static void removeDataSource()
	{
		chooseDataSource.remove();
	}
	
	/**
	 * 判断是否使用主库数据源
	 * @return
	 */
	public static boolean isMaster()
	{
		return chooseDataSource.get() == MASTER;
	}
	
	/**
	 * 判断是否使用从库数据源
	 * @return
	 */
	public static boolean isSlave()
	{
		return chooseDataSource.get() == SLAVE;
	}
	
	/**
	 * 判断是否没有设置数据源
	 * @return
	 */
	public static boolean isNull()
	{
		return chooseDataSource.get() == null;
	}
}
