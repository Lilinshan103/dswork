package dswork.core.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dswork.core.page.Page;
import dswork.core.page.PageRequest;

/**
 * 通用service接口，基于getSqlNamespace()返回值作为全局命名空间
 * @author skey,sille
 * @version 1.0
 * @param &lt;T&gt; 对象模型
 * @param &lt;PK&gt; 主键类
 */
@SuppressWarnings("all")
public abstract class MyDataService<T, PK extends Serializable>
{
	private static final long serialVersionUID = 1L;
	protected final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 供service层获取MyDaoEntity实例
	 * @return MyDaoEntity
	 */
	protected abstract MyDaoEntity getMyEntityDao();

	/**
	 * 获取MyBatis配置sql的通用命名空间
	 * @return String
	 */
	protected abstract String getSqlNamespace();

	/**
	 * 新增对象
	 * @param entity 需要新增的对象模型
	 * @return int
	 */
	public int save(T entity)
	{
		return getMyEntityDao().executeInsert(getSqlNamespace() + ".insert", entity);
	}

	/**
	 * 根据主键删除对象
	 * @param p 如果是单主键的，传入主键数据类型，如果为多主键的，可以用主键类或map
	 * @return int
	 */
	public int delete(PK p)
	{
		return getMyEntityDao().executeDelete(getSqlNamespace() + ".delete", p);
	}

	/**
	 * 根据主键批量删除对象
	 * @param primaryKeys 主键数组(如果是单主键的，传入主键数据类型，如果为多主键的，可以用主键类或map)
	 */
	public void deleteBatch(PK[] primaryKeys)
	{
		if(primaryKeys != null && primaryKeys.length > 0)
		{
			for(PK p : primaryKeys)
			{
				getMyEntityDao().executeDelete(getSqlNamespace() + ".delete", p);
			}
		}
	}

	/**
	 * 更新对象
	 * @param entity 需要更新的对象模型
	 * @return int
	 */
	public int update(T entity)
	{
		return getMyEntityDao().executeUpdate(getSqlNamespace() + ".update", entity);
	}

	/**
	 * 查询对象
	 * @param primaryKey 如果是单主键的，传入主键数据类型，如果为多主键的，可以用主键类或map
	 * @return T对象模型
	 */
	public T get(PK primaryKey)
	{
		return (T) getMyEntityDao().executeSelect(getSqlNamespace() + ".select", primaryKey);
	}

	/**
	 * 取得全部数据
	 * @param map 查询参数和条件数据
	 * @return List
	 */
	public List<T> queryList(Map map)
	{
		return (List<T>) getMyEntityDao().executeSelectList(getSqlNamespace() + ".query", map);
	}

	/**
	 * 默认列表方法
	 * @param pageRequest PageRequest.getFilters()查询参数和条件数据
	 * @return List
	 */
	public List<T> queryList(PageRequest pageRequest)
	{
		return (List<T>) getMyEntityDao().executeSelectList(getSqlNamespace() + ".query", pageRequest.getFilters());
	}

	/**
	 * 得到分页模型
	 * @param currentPage 当前页码
	 * @param pageSize 一页显示的条数
	 * @param map 查询参数和条件数据
	 * @return Page
	 */
	public Page<T> queryPage(int currentPage, int pageSize, Map map)
	{
		PageRequest pageRequest = new PageRequest();
		pageRequest.setFilters(map);
		pageRequest.setCurrentPage(currentPage);
		pageRequest.setPageSize(pageSize);
		Page<T> page = getMyEntityDao().queryPage(getSqlNamespace() + ".query", pageRequest, getSqlNamespace() + ".queryCount", pageRequest);
		return page;
	}

	/**
	 * 分页查询数据
	 * @param pageRequest PageRequest.getFilters()查询参数和条件数据
	 * @return Page
	 */
	public Page<T> queryPage(PageRequest pageRequest)
	{
		return getMyEntityDao().queryPage(getSqlNamespace() + ".query", pageRequest, getSqlNamespace() + ".queryCount", pageRequest);
	}


	/**
	 * 执行查询操作取得数据条数
	 * @param pageRequestCount PageRequest.getFilters()查询参数和条件数据
	 * @return int
	 */
	public int queryCount(PageRequest pageRequestCount)
	{
		return getMyEntityDao().queryCount(getSqlNamespace() + ".queryCount", pageRequestCount);
	}
}
