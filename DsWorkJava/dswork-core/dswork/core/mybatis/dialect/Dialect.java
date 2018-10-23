package dswork.core.mybatis.dialect;

/**
 * 默认不支持分页
 * @author skey
 */
public class Dialect
{
	/**
	 * 是否支持分页，limit和offset
	 * @return boolean
	 */
	public boolean supportsLimitOffset()
	{
		return false;
	}

	/**
	 * 获取count语句
	 * @param sql 原始sql语句
	 * @return String
	 */
	public String getCountSql(String sql)
	{
		return "select count(1) amount from (" + sql + ") as a";
	}
	
	/**
	 * 将sql变成分页sql
	 * @param sql 原始sql语句
	 * @param offset 跳过行数
	 * @param limit 返回行数
	 * @return String
	 */
	public String getLimitString(String sql, int offset, int limit)
	{
		throw new UnsupportedOperationException("不支持分页！" + sql);
	}
}
