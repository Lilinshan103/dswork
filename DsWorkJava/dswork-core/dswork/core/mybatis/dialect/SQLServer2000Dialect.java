package dswork.core.mybatis.dialect;

/**
 * Dialect for SQLServer2000，查询的sql必须存在ID列，且翻页以ID顺序排序
 * @author skey
 */
public class SQLServer2000Dialect extends Dialect
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
		StringBuilder sb = new StringBuilder(sql.length() + 150);
		sb.append("select top ").append(limit).append(" n_.* from (")
			.append("select top ").append(limit).append(" m_.* from (")
				.append("select top ").append(offset + limit).append(" t_.* from (")
				.append(sql)
				.append(") t_ order by t_.ID")
			.append(") m_ order by m_.ID desc")
		.append(") n_ order by n_.ID");
		return sb.toString();
	}
}
