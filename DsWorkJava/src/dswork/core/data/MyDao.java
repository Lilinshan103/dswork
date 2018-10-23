package dswork.core.data;

/**
 * 通用数据操作dao
 * @author skey,sille
 * @version 1.0
 */
public class MyDao extends MyDataDao implements MyDaoEntity
{
	/**
	 * 仅用于以下四个方法save(Object)、delete(Object)、update(Object)、get(Object)，根据对象获取需要操作sql的id，默认获取entity.getClass().getCanonicalName()作为命名空间
	 * @param statementName SQL的ID(不包含namespace)
	 * @return String
	 */
	protected String getStatementName(String statementName, Object entity)
	{
		return entity.getClass().getCanonicalName() + "." + statementName;
	}

	/**
	 * 新增对象，单表操作，依赖于getStatementName(String, Object)方法
	 * @param entity 需要新增的对象模型
	 * @return int
	 */
	public int save(Object entity)
	{
		return this.executeInsert(getStatementName("insert", entity), entity);
	}

	/**
	 * 根据对象删除对象，单表操作，依赖于getStatementName(String, Object)方法
	 * @param entity 需要删除的对象模型
	 * @return int
	 */
	public int delete(Object entity)
	{
		return this.executeDelete(getStatementName("delete", entity), entity);
	}

	/**
	 * 更新对象，单表操作，依赖于getStatementName(String, Object)方法
	 * @param entity 需要更新的对象模型
	 * @return int
	 */
	public int update(Object entity)
	{
		return this.executeUpdate(getStatementName("update", entity), entity);
	}

	/**
	 * 查询对象，单表操作，依赖于getStatementName(String, Object)方法
	 * @param entity 需要查询的对象模型
	 * @return Object
	 */
	public Object get(Object entity)
	{
		return this.executeSelect(getStatementName("select", entity), entity);
	}
}
