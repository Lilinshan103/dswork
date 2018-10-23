package dswork.core.data;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dswork.core.page.Page;
import dswork.core.page.PageRequest;

/**
 * 通用数据操作service
 * @author skey,sille
 * @version 1.0
 */
@SuppressWarnings("all")
public class MyService
{
	protected final Logger log = LoggerFactory.getLogger(getClass());
	private MyDaoEntity dao;
	
	/**
	 * 注入MyDaoEntity实例
	 * @param dao MyDao
	 */
	public void setMyDaoEntity(MyDaoEntity dao)
	{
		this.dao = dao;
	}
	
	/**
	 * 供service层获取MyDaoEntity实例
	 * @return MyDao
	 */
	protected MyDaoEntity getMyDaoEntity()
	{
		return dao;
	}

	/**
	 * 新增对象
	 * @param namespace SQL的namespace
	 * @param entity 需要新增的对象模型
	 * @return int
	 */
	public int save(String namespace, Object entity)
	{
		return getMyDaoEntity().executeInsert(namespace + ".insert", entity);
	}

	/**
	 * 根据主键删除对象
	 * @param namespace SQL的namespace
	 * @param primaryKey 参数，一般传入主键
	 * @return int
	 */
	public int delete(String namespace, Object primaryKey)
	{
		return getMyDaoEntity().executeDelete(namespace + ".delete", primaryKey);
	}

	/**
	 * 根据主键批量删除对象
	 * @param namespace SQL的namespace
	 * @param primaryKeys 参数数组，一般传入主键数组
	 */
	public void deleteBatch(String namespace, Object[] primaryKeys)
	{
		if(primaryKeys != null && primaryKeys.length > 0)
		{
			for(Object p : primaryKeys)
			{
				getMyDaoEntity().executeDelete(namespace + ".delete", p);
			}
		}
	}

	/**
	 * 更新对象
	 * @param namespace SQL的namespace
	 * @param entity 需要更新的对象模型
	 * @return int
	 */
	public int update(String namespace, Object entity)
	{
		return getMyDaoEntity().executeUpdate(namespace + ".update", entity);
	}

	/**
	 * 查询对象
	 * @param namespace SQL的namespace
	 * @param primaryKey 参数，一般传入主键
	 * @return Object
	 */
	public Object get(String namespace, Object primaryKey)
	{
		return (Object) getMyDaoEntity().executeSelect(namespace + ".select", primaryKey);
	}

	/**
	 * 取得全部数据
	 * @param namespace SQL的namespace
	 * @param map 查询参数和条件数据
	 * @return List
	 */
	public List<Object> queryList(String namespace, Map map)
	{
		return (List<Object>) getMyDaoEntity().executeSelectList(namespace + ".query", map);
	}

	/**
	 * 默认列表方法
	 * @param namespace SQL的namespace
	 * @param pageRequest PageRequest.getFilters()查询参数和条件数据
	 * @return List
	 */
	public List<Object> queryList(String namespace, PageRequest pageRequest)
	{
		return (List<Object>) getMyDaoEntity().executeSelectList(namespace + ".query", pageRequest.getFilters());
	}

	/**
	 * 得到分页模型
	 * @param namespace SQL的namespace
	 * @param currentPage 当前页码
	 * @param pageSize 一页显示的条数
	 * @param map 查询参数和条件数据
	 * @return Page
	 */
	public Page<Object> queryPage(String namespace, int currentPage, int pageSize, Map map)
	{
		PageRequest pageRequest = new PageRequest(currentPage, pageSize, map);
		Page<Object> page = getMyDaoEntity().queryPage(namespace + ".query", pageRequest, namespace + ".queryCount", pageRequest);
		return page;
	}

	/**
	 * 分页查询数据
	 * @param namespace SQL的namespace
	 * @param pageRequest PageRequest.getFilters()查询参数和条件数据
	 * @return Page
	 */
	public Page<Object> queryPage(String namespace, PageRequest pageRequest)
	{
		return (Page<Object>)getMyDaoEntity().queryPage(namespace + ".query", pageRequest, namespace + ".queryCount", pageRequest);
	}

	/**
	 * 执行查询操作取得数据条数
	 * @param namespace SQL的namespace
	 * @param pageRequestCount PageRequest.getFilters()查询参数和条件数据
	 * @return int
	 */
	public int queryCount(String namespace, PageRequest pageRequestCount)
	{
		return getMyDaoEntity().queryCount(namespace + ".queryCount", pageRequestCount);
	}
}
