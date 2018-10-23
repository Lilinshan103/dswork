package dswork.core.mybatis.dialect;

/**
 * Dialect for Oracle
 * @author skey
 */
public class OracleDialect extends Dialect
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
//		boolean hasUpdate = false;
//		if(sql.toLowerCase().endsWith(" for update"))
//		{
//			sql = sql.substring(0, sql.length() - 11);
//			hasUpdate = true;
//		}
		StringBuilder sb = new StringBuilder(sql.length() + 90);
//		if(offset > 0)
//		{
			sb.append("select * from ( select rownum rn, t_.* from (")
			.append(sql)
			.append(") t_ where rownum<=").append(offset + limit)
			.append(") n_ where n_.rn>").append(offset);
//		}
//		else
//		{
//			sb.append("select * from ( ");
//			sb.append(sql);
//			sb.append(" ) where rownum <= " + limit);
//		}
		//if(hasUpdate) {sb.append(" for update");};
		return sb.toString();
	}
}
