package dswork.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

@SuppressWarnings("all")
public class CallableStatementSpy extends PreparedStatementSpy implements CallableStatement
{
	private CallableStatement realCallableStatement;

	public CallableStatement getRealCallableStatement()
	{
		return realCallableStatement;
	}

	public CallableStatementSpy(String sql, ConnectionSpy connectionSpy, CallableStatement realCallableStatement)
	{
		super(sql, connectionSpy, realCallableStatement);
		this.realCallableStatement = realCallableStatement;
	}

	public String getClassType()
	{
		return "CallableStatementSpy";
	}

	public Date getDate(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getDate(parameterIndex);
	}

	public Date getDate(int parameterIndex, Calendar cal) throws SQLException
	{
		return realCallableStatement.getDate(parameterIndex, cal);
	}

	public Ref getRef(String parameterName) throws SQLException
	{
		return realCallableStatement.getRef(parameterName);
	}

	public Time getTime(String parameterName) throws SQLException
	{
		return realCallableStatement.getTime(parameterName);
	}

	public void setTime(String parameterName, Time x) throws SQLException
	{
		realCallableStatement.setTime(parameterName, x);
	}

	public Blob getBlob(int i) throws SQLException
	{
		return realCallableStatement.getBlob(i);
	}

	public Clob getClob(int i) throws SQLException
	{
		return realCallableStatement.getClob(i);
	}

	public Array getArray(int i) throws SQLException
	{
		return realCallableStatement.getArray(i);
	}

	public byte[] getBytes(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getBytes(parameterIndex);
	}

	public double getDouble(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getDouble(parameterIndex);
	}

	public int getInt(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getInt(parameterIndex);
	}

	public boolean wasNull() throws SQLException
	{
		return realCallableStatement.wasNull();
	}

	public Time getTime(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getTime(parameterIndex);
	}

	public Time getTime(int parameterIndex, Calendar cal) throws SQLException
	{
		return realCallableStatement.getTime(parameterIndex, cal);
	}

	public Timestamp getTimestamp(String parameterName) throws SQLException
	{
		return realCallableStatement.getTimestamp(parameterName);
	}

	public void setTimestamp(String parameterName, Timestamp x) throws SQLException
	{
		realCallableStatement.setTimestamp(parameterName, x);
	}

	public String getString(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getString(parameterIndex);
	}

	public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException
	{
		argTraceSet(parameterIndex, null, "<OUT>");
		realCallableStatement.registerOutParameter(parameterIndex, sqlType);
	}

	public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException
	{
		argTraceSet(parameterIndex, null, "<OUT>");
		realCallableStatement.registerOutParameter(parameterIndex, sqlType, scale);
	}

	public void registerOutParameter(int paramIndex, int sqlType, String typeName) throws SQLException
	{
		argTraceSet(paramIndex, null, "<OUT>");
		realCallableStatement.registerOutParameter(paramIndex, sqlType, typeName);
	}

	public byte getByte(String parameterName) throws SQLException
	{
		return realCallableStatement.getByte(parameterName);
	}

	public double getDouble(String parameterName) throws SQLException
	{
		return realCallableStatement.getDouble(parameterName);
	}

	public float getFloat(String parameterName) throws SQLException
	{
		return realCallableStatement.getFloat(parameterName);
	}

	public int getInt(String parameterName) throws SQLException
	{
		return realCallableStatement.getInt(parameterName);
	}

	public long getLong(String parameterName) throws SQLException
	{
		return realCallableStatement.getLong(parameterName);
	}

	public short getShort(String parameterName) throws SQLException
	{
		return realCallableStatement.getShort(parameterName);
	}

	public boolean getBoolean(String parameterName) throws SQLException
	{
		return realCallableStatement.getBoolean(parameterName);
	}

	public byte[] getBytes(String parameterName) throws SQLException
	{
		return realCallableStatement.getBytes(parameterName);
	}

	public void setByte(String parameterName, byte x) throws SQLException
	{
		realCallableStatement.setByte(parameterName, x);
	}

	public void setDouble(String parameterName, double x) throws SQLException
	{
		realCallableStatement.setDouble(parameterName, x);
	}

	public void setFloat(String parameterName, float x) throws SQLException
	{
		realCallableStatement.setFloat(parameterName, x);
	}

	public void registerOutParameter(String parameterName, int sqlType) throws SQLException
	{
		realCallableStatement.registerOutParameter(parameterName, sqlType);
	}

	public void setInt(String parameterName, int x) throws SQLException
	{
		realCallableStatement.setInt(parameterName, x);
	}

	public void setNull(String parameterName, int sqlType) throws SQLException
	{
		realCallableStatement.setNull(parameterName, sqlType);
	}

	public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException
	{
		realCallableStatement.registerOutParameter(parameterName, sqlType, scale);
	}

	public void setLong(String parameterName, long x) throws SQLException
	{
		realCallableStatement.setLong(parameterName, x);
	}

	public void setShort(String parameterName, short x) throws SQLException
	{
		realCallableStatement.setShort(parameterName, x);
	}

	public void setBoolean(String parameterName, boolean x) throws SQLException
	{
		realCallableStatement.setBoolean(parameterName, x);
	}

	public void setBytes(String parameterName, byte[] x) throws SQLException
	{
		realCallableStatement.setBytes(parameterName, x);
	}

	public boolean getBoolean(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getBoolean(parameterIndex);
	}

	public Timestamp getTimestamp(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getTimestamp(parameterIndex);
	}

	public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException
	{
		realCallableStatement.setAsciiStream(parameterName, x, length);
	}

	public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException
	{
		realCallableStatement.setBinaryStream(parameterName, x, length);
	}

	public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException
	{
		realCallableStatement.setCharacterStream(parameterName, reader, length);
	}

	public Object getObject(String parameterName) throws SQLException
	{
		return realCallableStatement.getObject(parameterName);
	}

	public void setObject(String parameterName, Object x) throws SQLException
	{
		realCallableStatement.setObject(parameterName, x);
	}

	public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException
	{
		realCallableStatement.setObject(parameterName, x, targetSqlType);
	}

	public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException
	{
		realCallableStatement.setObject(parameterName, x, targetSqlType, scale);
	}

	public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException
	{
		return realCallableStatement.getTimestamp(parameterIndex, cal);
	}

	public Date getDate(String parameterName, Calendar cal) throws SQLException
	{
		return realCallableStatement.getDate(parameterName, cal);
	}

	public Time getTime(String parameterName, Calendar cal) throws SQLException
	{
		return realCallableStatement.getTime(parameterName, cal);
	}

	public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException
	{
		return realCallableStatement.getTimestamp(parameterName, cal);
	}

	public void setDate(String parameterName, Date x, Calendar cal) throws SQLException
	{
		realCallableStatement.setDate(parameterName, x, cal);
	}

	public void setTime(String parameterName, Time x, Calendar cal) throws SQLException
	{
		realCallableStatement.setTime(parameterName, x, cal);
	}

	public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException
	{
		realCallableStatement.setTimestamp(parameterName, x, cal);
	}

	public short getShort(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getShort(parameterIndex);
	}

	public long getLong(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getLong(parameterIndex);
	}

	public float getFloat(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getFloat(parameterIndex);
	}

	public Ref getRef(int i) throws SQLException
	{
		return realCallableStatement.getRef(i);
	}

	/**
	 * @deprecated
	 */
	public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException
	{
		return realCallableStatement.getBigDecimal(parameterIndex, scale);
	}

	public URL getURL(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getURL(parameterIndex);
	}

	public BigDecimal getBigDecimal(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getBigDecimal(parameterIndex);
	}

	public byte getByte(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getByte(parameterIndex);
	}

	public Object getObject(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getObject(parameterIndex);
	}

	public Object getObject(int i, Map map) throws SQLException
	{
		return realCallableStatement.getObject(i, map);
	}

	public String getString(String parameterName) throws SQLException
	{
		return realCallableStatement.getString(parameterName);
	}

	public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException
	{
		realCallableStatement.registerOutParameter(parameterName, sqlType, typeName);
	}

	public void setNull(String parameterName, int sqlType, String typeName) throws SQLException
	{
		realCallableStatement.setNull(parameterName, sqlType, typeName);
	}

	public void setString(String parameterName, String x) throws SQLException
	{
		realCallableStatement.setString(parameterName, x);
	}

	public BigDecimal getBigDecimal(String parameterName) throws SQLException
	{
		return realCallableStatement.getBigDecimal(parameterName);
	}

	public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException
	{
		return realCallableStatement.getObject(parameterName, map);
	}

	public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException
	{
		realCallableStatement.setBigDecimal(parameterName, x);
	}

	public URL getURL(String parameterName) throws SQLException
	{
		return realCallableStatement.getURL(parameterName);
	}

	public RowId getRowId(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getRowId(parameterIndex);
	}

	public RowId getRowId(String parameterName) throws SQLException
	{
		return realCallableStatement.getRowId(parameterName);
	}

	public void setRowId(String parameterName, RowId x) throws SQLException
	{
		realCallableStatement.setRowId(parameterName, x);
	}

	public void setNString(String parameterName, String value) throws SQLException
	{
		realCallableStatement.setNString(parameterName, value);
	}

	public void setNCharacterStream(String parameterName, Reader reader, long length) throws SQLException
	{
		realCallableStatement.setNCharacterStream(parameterName, reader, length);
	}

	public void setNClob(String parameterName, NClob value) throws SQLException
	{
		realCallableStatement.setNClob(parameterName, value);
	}

	public void setClob(String parameterName, Reader reader, long length) throws SQLException
	{
		realCallableStatement.setClob(parameterName, reader, length);
	}

	public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException
	{
		realCallableStatement.setBlob(parameterName, inputStream, length);
	}

	public void setNClob(String parameterName, Reader reader, long length) throws SQLException
	{
		realCallableStatement.setNClob(parameterName, reader, length);
	}

	public NClob getNClob(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getNClob(parameterIndex);
	}

	public NClob getNClob(String parameterName) throws SQLException
	{
		return realCallableStatement.getNClob(parameterName);
	}

	public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException
	{
		realCallableStatement.setSQLXML(parameterName, xmlObject);
	}

	public SQLXML getSQLXML(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getSQLXML(parameterIndex);
	}

	public SQLXML getSQLXML(String parameterName) throws SQLException
	{
		return realCallableStatement.getSQLXML(parameterName);
	}

	public String getNString(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getNString(parameterIndex);
	}

	public String getNString(String parameterName) throws SQLException
	{
		return realCallableStatement.getNString(parameterName);
	}

	public Reader getNCharacterStream(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getNCharacterStream(parameterIndex);
	}

	public Reader getNCharacterStream(String parameterName) throws SQLException
	{
		return realCallableStatement.getNCharacterStream(parameterName);
	}

	public Reader getCharacterStream(int parameterIndex) throws SQLException
	{
		return realCallableStatement.getCharacterStream(parameterIndex);
	}

	public Reader getCharacterStream(String parameterName) throws SQLException
	{
		return realCallableStatement.getCharacterStream(parameterName);
	}

	public void setBlob(String parameterName, Blob x) throws SQLException
	{
		realCallableStatement.setBlob(parameterName, x);
	}

	public void setClob(String parameterName, Clob x) throws SQLException
	{
		realCallableStatement.setClob(parameterName, x);
	}

	public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException
	{
		realCallableStatement.setAsciiStream(parameterName, x, length);
	}

	public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException
	{
		realCallableStatement.setBinaryStream(parameterName, x, length);
	}

	public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException
	{
		realCallableStatement.setCharacterStream(parameterName, reader, length);
	}

	public void setAsciiStream(String parameterName, InputStream x) throws SQLException
	{
		realCallableStatement.setAsciiStream(parameterName, x);
	}

	public void setBinaryStream(String parameterName, InputStream x) throws SQLException
	{
		realCallableStatement.setBinaryStream(parameterName, x);
	}

	public void setCharacterStream(String parameterName, Reader reader) throws SQLException
	{
		realCallableStatement.setCharacterStream(parameterName, reader);
	}

	public void setNCharacterStream(String parameterName, Reader reader) throws SQLException
	{
		realCallableStatement.setNCharacterStream(parameterName, reader);
	}

	public void setClob(String parameterName, Reader reader) throws SQLException
	{
		realCallableStatement.setClob(parameterName, reader);
	}

	public void setBlob(String parameterName, InputStream inputStream) throws SQLException
	{
		realCallableStatement.setBlob(parameterName, inputStream);
	}

	public void setNClob(String parameterName, Reader reader) throws SQLException
	{
		realCallableStatement.setNClob(parameterName, reader);
	}

	public void setURL(String parameterName, URL val) throws SQLException
	{
		realCallableStatement.setURL(parameterName, val);
	}

	public Array getArray(String parameterName) throws SQLException
	{
		return realCallableStatement.getArray(parameterName);
	}

	public Blob getBlob(String parameterName) throws SQLException
	{
		return realCallableStatement.getBlob(parameterName);
	}

	public Clob getClob(String parameterName) throws SQLException
	{
		return realCallableStatement.getClob(parameterName);
	}

	public Date getDate(String parameterName) throws SQLException
	{
		return realCallableStatement.getDate(parameterName);
	}

	public void setDate(String parameterName, Date x) throws SQLException
	{
		realCallableStatement.setDate(parameterName, x);
	}

	// jdbc4
	public <T> T unwrap(Class<T> iface) throws SQLException
	{
		try
		{
			CallableStatement.class.getMethod("unwrap", Class.class);
			return (iface != null && (iface == CallableStatement.class || iface == PreparedStatement.class || iface == Statement.class || iface == Spy.class)) ? (T) this : realCallableStatement.unwrap(iface);
		}
		catch(NoSuchMethodException e)
		{
			System.out.println("CallableStatementSpy ignore getObject(int parameterIndex, Class<T> type)");
		}
		return null;
	}

	// jdbc4
	public boolean isWrapperFor(Class<?> iface) throws SQLException
	{
		try
		{
			CallableStatement.class.getMethod("isWrapperFor", Class.class);
			return (iface != null && (iface == CallableStatement.class || iface == PreparedStatement.class || iface == Statement.class || iface == Spy.class)) || realCallableStatement.isWrapperFor(iface);
		}
		catch(NoSuchMethodException e)
		{
			System.out.println("CallableStatementSpy ignore getObject(int parameterIndex, Class<T> type)");
		}
		return false;
	}
	
	// jdbc 4.1
	public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException
	{
		try
		{
			CallableStatement.class.getMethod("getObject", int.class, Class.class);
			realCallableStatement.getObject(parameterIndex, type);
		}
		catch(NoSuchMethodException e)
		{
			System.out.println("CallableStatementSpy ignore getObject(int parameterIndex, Class<T> type)");
		}
		return null;
	}
	
	// jdbc 4.1
	public <T> T getObject(String parameterName, Class<T> type) throws SQLException
	{
		try
		{
			CallableStatement.class.getMethod("getObject", String.class, Class.class);
			realCallableStatement.getObject(parameterName, type);
		}
		catch(NoSuchMethodException e)
		{
			System.out.println("CallableStatementSpy ignore getObject(String parameterName, Class<T> type)");
		}
		return null;
	}
}
