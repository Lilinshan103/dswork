package dswork.core.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 重载AbstractRoutingDataSource数据源，用于判断选择主从数据源
 * @author skey
 */
public class DataSource extends AbstractRoutingDataSource
{
	/**
	 * 主库数据源
	 */
	private javax.sql.DataSource masterDataSource;
	/**
	 * 从库数据源
	 */
	private javax.sql.DataSource slaveDataSource;

	@Override
	protected Object determineCurrentLookupKey()
	{
		return null;
	}

	@Override
	protected javax.sql.DataSource determineTargetDataSource()
	{
		if(DataSourceHolder.isNull())
		{
			DataSourceHolder.setMaster();
			return masterDataSource;
		}
		return DataSourceHolder.isSlave() ? slaveDataSource : masterDataSource;// 没有设置的都默认为主库数据源
	}

	@Override
	public void afterPropertiesSet()
	{
	}

	/**
	 * 获取主库数据源
	 * @return DataSource
	 */
	public javax.sql.DataSource getMasterDataSource()
	{
		return masterDataSource;
	}

	/**
	 * 设置主库数据源
	 * @return DataSource
	 */
	public void setMasterDataSource(javax.sql.DataSource masterDataSource)
	{
		this.masterDataSource = masterDataSource;
	}

	/**
	 * 获取从库数据源
	 * @return DataSource
	 */
	public javax.sql.DataSource getSlaveDataSource()
	{
		return slaveDataSource;
	}

	/**
	 * 设置从库数据源
	 * @return DataSource
	 */
	public void setSlaveDataSource(javax.sql.DataSource slaveDataSource)
	{
		this.slaveDataSource = slaveDataSource;
	}
}
