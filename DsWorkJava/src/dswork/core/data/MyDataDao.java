package dswork.core.data;

import java.util.List;
//import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.support.DaoSupport;
import org.springframework.util.Assert;
import org.mybatis.spring.SqlSessionTemplate;

import dswork.core.datasource.DataSourceHolder;
import dswork.core.page.Page;
import dswork.core.page.PageRequest;

/**
 * MyBatis抽象类
 * @author skey
 */
@SuppressWarnings("all")
public abstract class MyDataDao extends DaoSupport
{
	private static final long serialVersionUID = 1L;
	protected final Logger log = LoggerFactory.getLogger(getClass());
	/**
	 * 注入SqlSessionTemplate
	 * @param sqlSessionTemplate SqlSessionTemplate
	 */
	private SqlSessionTemplate sqlSessionTemplate;

	/**
	 * 实现abstract
	 */
	protected void checkDaoConfig() throws IllegalArgumentException
	{
		Assert.notNull(sqlSessionTemplate, "sqlSessionTemplate must be not null");
	}

	/**
	 * 注入SqlSessionTemplate
	 * @param sqlSessionTemplate SqlSessionTemplate
	 */
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate)
	{
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	/**
	 * 不建议直接使用，建议使用execute*和query*方法代替
	 * @return SqlSessionTemplate
	 */
	protected SqlSessionTemplate getSqlSessionTemplate()
	{
		return sqlSessionTemplate;
	}

	/**
	 * 封装sqlSessionTemplate.insert方法
	 * @param statementName SQL的ID(包含namespace)
	 * @param parameter 参数
	 * @return int
	 */
	public int executeInsert(String statementName, Object parameter)
	{
		// if(DataSourceHolder.isNull()){DataSourceHolder.setMaster();}// 没有设置默认就是主库数据源
		if(DataSourceHolder.isSlave())
		{
			throw new org.springframework.dao.InvalidDataAccessApiUsageException("Write operations are not allowed in read-only mode");
		}
		return getSqlSessionTemplate().insert(statementName, parameter);
	}

	/**
	 * 封装sqlSessionTemplate.delete方法
	 * @param statementName SQL的ID(包含namespace)
	 * @param parameter 参数
	 * @return int
	 */
	public int executeDelete(String statementName, Object parameter)
	{
		// if(DataSourceHolder.isNull()){DataSourceHolder.setMaster();}// 没有设置默认就是主库数据源
		if(DataSourceHolder.isSlave())
		{
			throw new org.springframework.dao.InvalidDataAccessApiUsageException("Write operations are not allowed in read-only mode");
		}
		return getSqlSessionTemplate().delete(statementName, parameter);
	}

	/**
	 * 封装sqlSessionTemplate.update方法
	 * @param statementName SQL的ID(包含namespace)
	 * @param parameter 参数
	 * @return int
	 */
	public int executeUpdate(String statementName, Object parameter)
	{
		// if(DataSourceHolder.isNull()){DataSourceHolder.setMaster();}// 没有设置默认就是主库数据源
		if(DataSourceHolder.isSlave())
		{
			throw new org.springframework.dao.InvalidDataAccessApiUsageException("Write operations are not allowed in read-only mode");
		}
		return getSqlSessionTemplate().update(statementName, parameter);
	}

	/**
	 * 封装sqlSessionTemplate.selectOne方法
	 * @param statementName SQL的ID(包含namespace)
	 * @param parameter 参数
	 * @return Object
	 */
	public Object executeSelect(String statementName, Object parameter)
	{
		if(DataSourceHolder.isNull()){DataSourceHolder.setSlave();}// 没有设置默认使用从库数据源
		return getSqlSessionTemplate().selectOne(statementName, parameter);
	}

	/**
	 * 封装sqlSessionTemplate.selectList方法
	 * @param statementName SQL的ID(包含namespace)
	 * @param parameter 参数
	 * @return List
	 */
	public List executeSelectList(String statementName, Object parameter)
	{
		if(DataSourceHolder.isNull()){DataSourceHolder.setSlave();}// 没有设置默认使用从库数据源
		return getSqlSessionTemplate().selectList(statementName, parameter);
	}

	/**
	 * 不分页查询数据
	 * @param statementName SQL的ID(包含namespace)
	 * @param pageRequest PageRequest.getFilters()查询参数和条件数据
	 * @return List
	 */
	public List queryList(String statementName, PageRequest pageRequest)
	{
		if(DataSourceHolder.isNull()){DataSourceHolder.setSlave();}// 没有设置默认使用从库数据源
		return getSqlSessionTemplate().selectList(statementName, pageRequest.getFilters());
	}

	/**
	 * 分页查询数据
	 * @param statementName 查询SQL的ID(不包含namespace)
	 * @param pageRequest PageRequest.getFilters()查询参数和条件数据
	 * @param statementNameCount 查询总数SQL的ID(包含namespace)
	 * @param pageRequestCount PageRequest.getFilters()查询参数和条件数据
	 * @return Page
	 */
	public Page queryPage(String statementName, PageRequest pageRequest, String statementNameCount, PageRequest pageRequestCount)
	{
		if(DataSourceHolder.isNull()){DataSourceHolder.setSlave();}// 没有设置默认使用从库数据源
		int totalCount = queryCount(statementNameCount, pageRequestCount);
		Page page = new Page(pageRequest.getCurrentPage(), pageRequest.getPageSize(), totalCount);
		// if(page.getTotalCount() <= 0){page.setResult(new ArrayList(0));}else{}//没数据的话不影响性能，而实际上又不可能没有数据
		List list = getSqlSessionTemplate().selectList(statementName, pageRequest.getFilters(), new RowBounds(page.getFirstResultIndex(), page.getPageSize()));
		page.setResult(list);
		return page;
	}

	/**
	 * 执行查询操作取得数据条数
	 * @param statementNameCount SQL的ID(包含namespace)
	 * @param pageRequestCount PageRequest.getFilters()查询参数和条件数据
	 * @return int
	 */
	public int queryCount(String statementNameCount, PageRequest pageRequestCount)
	{
		if(DataSourceHolder.isNull()){DataSourceHolder.setSlave();}// 没有设置默认使用从库数据源
		Object o = this.getSqlSessionTemplate().selectOne(statementNameCount, pageRequestCount.getFilters());
		return MyDataDao.queryCountProcess(o);// xml里配置是int或long都可以
	}

	private final static int queryCountProcess(Object o)
	{
		int totalCount = 0;
		try
		{
			totalCount = ((Integer) o).intValue();
		}
		catch(Exception e)
		{
			try
			{
				totalCount = ((Long) o).intValue();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				totalCount = 0;
			}
		}
		return totalCount;
	}
}
