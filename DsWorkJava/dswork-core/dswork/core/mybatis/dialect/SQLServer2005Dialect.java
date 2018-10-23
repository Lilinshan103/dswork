package dswork.core.mybatis.dialect;

/**
 * 可用于支持ROW_NUMBER() over(order by *)语法的数据库，如：SQLServer2005、SQLServer2008
 * @author skey
 */
public class SQLServer2005Dialect extends Dialect
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
		StringBuilder sb = new StringBuilder(sql.length() + 170);
		sb.append("select * from ( select ROW_NUMBER() over(order by m_.temp__) rn, m_.* from (")
		.append("select 0 as temp__, t_.* from (")
		.append(sql)
		.append(") t_ ")
		.append(") m_ ")
		.append(") n_ where n_.rn>").append(offset).append(" and n_.rn<=").append(offset + limit);
		return sb.toString();
	}
}
