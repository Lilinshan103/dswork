package dswork.core.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;

@SuppressWarnings("all")
public class LikePrefixTypeHandler extends org.apache.ibatis.type.BaseTypeHandler
{
	static final String like = "%";

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException
	{
		ps.setString(i, parameter + like);
	}

	@Override
	public Void getNullableResult(ResultSet rs, String columnName) throws SQLException
	{
		throw new SQLException();
	}

	@Override
	public Void getNullableResult(ResultSet rs, int columnIndex) throws SQLException
	{
		throw new SQLException();
	}

	@Override
	public Void getNullableResult(CallableStatement cs, int columnIndex) throws SQLException
	{
		throw new SQLException();
	}
}
