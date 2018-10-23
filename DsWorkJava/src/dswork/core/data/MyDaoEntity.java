package dswork.core.data;

import java.io.Serializable;
import java.util.List;

import dswork.core.page.Page;
import dswork.core.page.PageRequest;

/**
 * 通用dao接口
 * @author skey
 * @version 1.0
 */
@SuppressWarnings("all")
public interface MyDaoEntity
{
	/**
	 * 新增对象
	 * @param entity 需要新增的对象模型
	 * @return int
	 */
	public int save(Object entity);

	/**
	 * 删除对象
	 * @param primaryKey 如果是单主键的，传入主键数据类型，如果为多主键的，可以用主键类或map
	 * @return int
	 */
	public int delete(Object entity);

	/**
	 * 更新对象
	 * @param entity 需要更新的对象模型
	 * @return int
	 */
	public int update(Object entity);

	/**
	 * 查询对象
	 * @param primaryKey 如果是单主键的，传入主键数据类型，如果为多主键的，可以用主键类或map
	 * @return Object
	 */
	public Object get(Object entity);

	/**
	 * 封装sqlSessionTemplate.insert方法
	 * @param statementName SQL的ID(包含namespace)
	 * @param parameter 参数
	 * @return int
	 */
	public int executeInsert(String statementName, Object parameter);

	/**
	 * 封装sqlSessionTemplate.delete方法
	 * @param statementName SQL的ID(包含namespace)
	 * @param parameter 参数
	 * @return int
	 */
	public int executeDelete(String statementName, Object parameter);

	/**
	 * 封装sqlSessionTemplate.update方法
	 * @param statementName SQL的ID(包含namespace)
	 * @param parameter 参数
	 * @return int
	 */
	public int executeUpdate(String statementName, Object parameter);

	/**
	 * 封装sqlSessionTemplate.selectOne方法
	 * @param statementName SQL的ID(包含namespace)
	 * @param parameter 参数
	 * @return Object
	 */
	public Object executeSelect(String statementName, Object parameter);

	/**
	 * 封装sqlSessionTemplate.selectList方法
	 * @param statementName SQL的ID(包含namespace)
	 * @param parameter 参数
	 * @return List
	 */
	public List executeSelectList(String statementName, Object parameter);

	/**
	 * 分页查询数据
	 * @param statementName 查询SQL的ID(不包含namespace)
	 * @param pageRequest PageRequest.getFilters()查询参数和条件数据
	 * @param statementNameCount 查询总数SQL的ID(包含namespace)
	 * @param pageRequestCount PageRequest.getFilters()查询参数和条件数据
	 * @return Page
	 */
	public Page queryPage(String statementName, PageRequest pageRequest, String statementNameCount, PageRequest pageRequestCount);

	/**
	 * 执行查询操作取得数据条数
	 * @param statementNameCount SQL的ID(包含namespace)
	 * @param pageRequestCount PageRequest.getFilters()查询参数和条件数据
	 * @return int
	 */
	public int queryCount(String statementNameCount, PageRequest pageRequestCount);
}
