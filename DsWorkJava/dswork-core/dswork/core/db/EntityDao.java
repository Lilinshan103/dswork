package dswork.core.db;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import dswork.core.page.Page;
import dswork.core.page.PageRequest;

/**
 * 基类接口
 * @author skey
 * @version 3.0
 * @param &lt;T&gt; 对象模型
 * @param &lt;PK&gt; 主键类
 */
public interface EntityDao<T, PK extends Serializable>
{
	/**
	 * 新增对象
	 * @param entity 需要新增的对象模型
	 * @return int
	 */
	public int save(T entity);

	/**
	 * 删除对象
	 * @param primaryKey 如果是单主键的，传入主键数据类型，如果为多主键的，可以用主键类或map
	 * @return int
	 */
	public int delete(PK primaryKey);

	/**
	 * 更新对象
	 * @param entity 需要更新的对象模型
	 * @return int
	 */
	public int update(T entity);

	/**
	 * 查询对象
	 * @param primaryKey 如果是单主键的，传入主键数据类型，如果为多主键的，可以用主键类或map
	 * @return Object
	 */
	public Object get(PK primaryKey);
	
	/**
	 * 默认列表方法
	 * @param filters Map&lt;String, Object&gt;查询参数和条件数据
	 * @return List&lt;T&gt;
	 */
	public List<T> queryList(Map<String, Object> filters);
	
	/**
	 * 默认列表方法
	 * @param pageRequest PageRequest.getFilters()查询参数和条件数据
	 * @return List&lt;T&gt;
	 */
	public List<T> queryList(PageRequest pageRequest);

	/**
	 * MyBatis默认分页方法
	 * @param pageRequest PageRequest.getFilters()查询参数和条件数据
	 * @return Page&lt;T&gt;
	 */
	public Page<T> queryPage(PageRequest pageRequest);
}
