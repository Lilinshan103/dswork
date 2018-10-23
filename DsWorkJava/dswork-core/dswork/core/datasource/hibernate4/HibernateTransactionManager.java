package dswork.core.datasource.hibernate4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionDefinition;

import dswork.core.datasource.DataSourceHolder;

public class HibernateTransactionManager extends org.springframework.orm.hibernate4.HibernateTransactionManager
{
	private final static Logger log = LoggerFactory.getLogger("jdbc.sqlonly");
	private static final long serialVersionUID = 1L;

	/**
	 * 根据Spring配置的事务设置，为当前线程选择主库还是从库数据源
	 */
	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition)
	{
		if(definition.isReadOnly())
		{
			if(log.isDebugEnabled())
			{
				log.debug("DataSourceTransactionManager set slave");
			}
			DataSourceHolder.setSlave();
		}
		else
		{
			if(log.isDebugEnabled())
			{
				log.debug("DataSourceTransactionManager set master");
			}
			DataSourceHolder.setMaster();
		}
		super.doBegin(transaction, definition);
	}
	
	/**
	 * 移除本地线程事务状态信息
	 */
	@Override
	protected void doCleanupAfterCompletion(Object arg0)
	{
		super.doCleanupAfterCompletion(arg0);
		DataSourceHolder.removeDataSource();// 处理完才释放连接主从信息
	}
}
