package dswork.core.mybatis.dialect;

/**
 * Dialect for SQLServer2012
 * @author skey
 */
public class SQLServer2012Dialect extends Dialect
{
	/**
	 * 是否支持分页，limit和offset
	 * @return boolean
	 */
	public boolean supportsLimitOffset()
	{
		return true;
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
		StringBuilder sb = new StringBuilder(sql.length() + 36);
		sb.append(sql)
		.append(" offset ")
		.append(offset)
		.append(" rows fetch next ")
		.append(limit)
		.append(" rows only");
		return sb.toString();
	}
}
