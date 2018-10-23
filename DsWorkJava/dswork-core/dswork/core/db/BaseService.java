package dswork.core.db;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dswork.core.page.Page;
import dswork.core.page.PageRequest;

/**
 * 服务基础类
 * @author skey
 * @version 3.0
 * @param &lt;T&gt; 对象模型
 * @param &lt;PK&gt; 主键类
 */
@SuppressWarnings("all")
public abstract class BaseService<T, PK extends Serializable>
{
	private static final long serialVersionUID = 1L;
	protected final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 需要被子类覆盖
	 * @return EntityDao
	 */
	protected abstract EntityDao<T, PK> getEntityDao();

	/**
	 * 新增对象
	 * @param entity 需要新增的对象模型
	 * @return int
	 */
	public int save(T entity)
	{
		return getEntityDao().save(entity);
	}

	/**
	 * 根据主键删除对象
	 * @param primaryKey 如果是单主键的，传入主键数据类型，如果为多主键的，可以用主键类或map
	 * @return int
	 */
	public int delete(PK primaryKey)
	{
		return getEntityDao().delete(primaryKey);
	}

	/**
	 * 根据主键批量删除对象
	 * @param primaryKeys 主键数组(如果是单主键的，传入主键数据类型，如果为多主键的，可以用主键类或map)
	 */
	public void deleteBatch(PK[] primaryKeys)
	{
		if (primaryKeys != null && primaryKeys.length > 0)
		{
			for (PK p : primaryKeys)
			{
				getEntityDao().delete(p);
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
		return getEntityDao().update(entity);
	}

	/**
	 * 查询对象
	 * @param primaryKey 如果是单主键的，传入主键数据类型，如果为多主键的，可以用主键类或map
	 * @return T对象模型
	 */
	@SuppressWarnings("unchecked")
	public T get(PK primaryKey)
	{
		return (T) getEntityDao().get(primaryKey);
	}

	/**
	 * 取得全部数据
	 * @param map 查询参数和条件数据
	 * @return List
	 */
	public List<T> queryList(Map map)
	{
		List<T> list = getEntityDao().queryList(map);
		return list;
	}

	/**
	 * 默认列表方法
	 * @param pageRequest PageRequest.getFilters()查询参数和条件数据
	 * @return List
	 */
	public List<T> queryList(PageRequest pageRequest)
	{
		return getEntityDao().queryList(pageRequest);
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
		PageRequest request = new PageRequest();
		request.setFilters(map);
		request.setCurrentPage(currentPage);
		request.setPageSize(pageSize);
		Page<T> page = getEntityDao().queryPage(request);
		return page;
	}

	/**
	 * 默认分页方法
	 * @param pageRequest PageRequest.getFilters()查询参数和条件数据
	 * @return Page
	 */
	public Page<T> queryPage(PageRequest pageRequest)
	{
		return getEntityDao().queryPage(pageRequest);
	}
}
