package dswork.core.db;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import dswork.core.page.Page;
import dswork.core.page.PageRequest;

/**
 * MyBatis基础Dao类
 * @author skey
 * @version 3.0
 * @param &lt;T&gt; 对象模型
 * @param &lt;PK&gt; 主键类
 */
@SuppressWarnings("all")
public abstract class BaseDao<T, PK extends Serializable> extends MyBatisDao implements EntityDao<T, PK>
{
	private static final long serialVersionUID = 1L;
	private static final String statement = "query";

	/**
	 * 新增对象
	 * @param entity 需要新增的对象模型
	 * @return int
	 */
	public int save(T entity)
	{
		return executeInsert("insert", entity);
	}

	/**
	 * 删除对象
	 * @param primaryKey 如果是单主键的，传入主键数据类型，如果为多主键的，可以用主键类或map
	 * @return int
	 */
	public int delete(PK primaryKey)
	{
		return executeDelete("delete", primaryKey);
	}

	/**
	 * 更新对象
	 * @param entity 需要更新的对象模型
	 * @return int
	 */
	public int update(T entity)
	{
		return executeUpdate("update", entity);
	}

	/**
	 * 查询对象
	 * @param primaryKey 如果是单主键的，传入主键数据类型，如果为多主键的，可以用主键类或map
	 * @return Object
	 */
	public Object get(PK primaryKey)
	{
		return executeSelect("select", primaryKey);
	}

	/**
	 * 取得数据条数
	 * @param pageRequest &lt;Map&gt;PageRequest.getFilters()查询参数和条件数据
	 * @return int
	 */
	public int queryCount(PageRequest pageRequest)
	{
		return queryCount(statement + "Count", pageRequest);
	}

	/**
	 * 不分页查询数据
	 * @param pageRequest Map&lt;String, Object&gt;查询参数和条件数据
	 * @return List
	 */
	public List queryList(Map<String, Object> filters)
	{
		return queryList(statement, filters);
	}

	/**
	 * 分页查询数据
	 * @param pageRequest &lt;Map&gt;PageRequest.getFilters()查询参数和条件数据
	 * @return List
	 */
	public List queryList(PageRequest pageRequest)
	{
		return queryList(statement, pageRequest);
	}

	/**
	 * 分页查询数据，statement默认为query
	 * @param pageRequest &lt;Map&gt;PageRequest.getFilters()查询参数和条件数据
	 * @return Page
	 */
	public Page queryPage(PageRequest pageRequest)
	{
		return queryPage(statement, pageRequest, statement + "Count", pageRequest);
	}

	/**
	 * 分页查询数据
	 * @param statementName 查询数据的statement，统计总数的是statement + "Count"
	 * @param pageRequest &lt;Map&gt;PageRequest.getFilters()查询参数和条件数据
	 * @return Page
	 */
	protected Page queryPage(String statementName, PageRequest pageRequest)
	{
		return queryPage(statementName, pageRequest, statementName + "Count", pageRequest);
	}
}
