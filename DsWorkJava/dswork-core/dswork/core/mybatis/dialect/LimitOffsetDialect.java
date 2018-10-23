package dswork.core.mybatis.dialect;

/**
 * 可用于支持(limit m offset n)语法的数据库，如：MySQL、PostgreSQL、SQLite、GBase、H2
 * @author skey
 */
public class LimitOffsetDialect extends Dialect
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
		StringBuilder sb = new StringBuilder(sql.length() + 21).append(sql).append(" limit ");
		//if(offset > 0)
		//{
		//	sb.append(offset).append(",");
		//}
		// limit b,a === limit a offset b
		sb.append(limit);
		//if(offset > 0)
		//{
			sb.append(" offset ").append(offset);
		//}
		return sb.toString();
	}
}
