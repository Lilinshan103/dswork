package dswork.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings("all")
public class PreparedStatementSpy extends StatementSpy implements PreparedStatement
{
	protected final List argTrace = new ArrayList();
	private static final boolean showTypeHelp = false;

	protected void argTraceSet(int i, String typeHelper, Object arg)
	{
		String tracedArg;
		try
		{
			tracedArg = rdbmsSpecifics.formatParameterObject(arg);
		}
		catch(Throwable t)
		{
			log.debug("RdbmsSpecifics threw an exception while trying to format a " + "parameter object [" + arg + "] this is very bad!!! (" + t.getMessage() + ")");
			tracedArg = arg == null ? "null" : arg.toString();
		}
		i--; // make the index 0 based
		synchronized(argTrace)
		{
			// if an object is being inserted out of sequence, fill up missing values with null...
			while(i >= argTrace.size())
			{
				argTrace.add(argTrace.size(), null);
			}
			if(!showTypeHelp || typeHelper == null)
			{
				argTrace.set(i, tracedArg);
			}
			else
			{
				argTrace.set(i, typeHelper + tracedArg);
			}
		}
	}
	private String sql;

	protected String dumpedSql()
	{
		StringBuffer dumpSql = new StringBuffer();
		int lastPos = 0;
		int Qpos = sql.indexOf('?', lastPos); // find position of first question mark
		int argIdx = 0;
		String arg;
		while(Qpos != -1)
		{
			// get stored argument
			synchronized(argTrace)
			{
				try
				{
					arg = (String) argTrace.get(argIdx);
				}
				catch(IndexOutOfBoundsException e)
				{
					arg = "?";
				}
			}
			if(arg == null)
			{
				arg = "?";
			}
			argIdx++;
			dumpSql.append(sql.substring(lastPos, Qpos)); // dump segment of sql up to question mark.
			lastPos = Qpos + 1;
			Qpos = sql.indexOf('?', lastPos);
			dumpSql.append(arg);
		}
		if(lastPos < sql.length())
		{
			dumpSql.append(sql.substring(lastPos, sql.length())); // dump last segment
		}
		return dumpSql.toString();
	}
	protected PreparedStatement realPreparedStatement;

	public PreparedStatement getRealPreparedStatement()
	{
		return realPreparedStatement;
	}
	protected RdbmsSpecifics rdbmsSpecifics;

	public PreparedStatementSpy(String sql, ConnectionSpy connectionSpy, PreparedStatement realPreparedStatement)
	{
		super(connectionSpy, realPreparedStatement); // does null check for us
		this.sql = sql;
		this.realPreparedStatement = realPreparedStatement;
		rdbmsSpecifics = connectionSpy.getRdbmsSpecifics();
	}

	public String getClassType()
	{
		return "PreparedStatementSpy";
	}

	public void setTime(int parameterIndex, Time x) throws SQLException
	{
		argTraceSet(parameterIndex, "(Time)", x);
		realPreparedStatement.setTime(parameterIndex, x);
	}

	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException
	{
		argTraceSet(parameterIndex, "(Time)", x);
		realPreparedStatement.setTime(parameterIndex, x, cal);
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException
	{
		argTraceSet(parameterIndex, "(Reader)", "<Reader of length " + length + ">");
		realPreparedStatement.setCharacterStream(parameterIndex, reader, length);
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException
	{
		argTraceSet(parameterIndex, null, null);
		realPreparedStatement.setNull(parameterIndex, sqlType);
	}

	public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException
	{
		argTraceSet(paramIndex, null, null);
		realPreparedStatement.setNull(paramIndex, sqlType, typeName);
	}

	public void setRef(int i, Ref x) throws SQLException
	{
		argTraceSet(i, "(Ref)", x);
		realPreparedStatement.setRef(i, x);
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException
	{
		argTraceSet(parameterIndex, "(boolean)", x ? Boolean.TRUE : Boolean.FALSE);
		realPreparedStatement.setBoolean(parameterIndex, x);
	}

	public void setBlob(int i, Blob x) throws SQLException
	{
		argTraceSet(i, "(Blob)", x == null ? null : ("<Blob of size " + x.length() + ">"));
		realPreparedStatement.setBlob(i, x);
	}

	public void setClob(int i, Clob x) throws SQLException
	{
		argTraceSet(i, "(Clob)", x == null ? null : ("<Clob of size " + x.length() + ">"));
		realPreparedStatement.setClob(i, x);
	}

	public void setArray(int i, Array x) throws SQLException
	{
		argTraceSet(i, "(Array)", "<Array>");
		realPreparedStatement.setArray(i, x);
	}

	public void setByte(int parameterIndex, byte x) throws SQLException
	{
		argTraceSet(parameterIndex, "(byte)", new Byte(x));
		realPreparedStatement.setByte(parameterIndex, x);
	}

	/**
	 * @deprecated
	 */
	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException
	{
		argTraceSet(parameterIndex, "(Unicode InputStream)", "<Unicode InputStream of length " + length + ">");
		realPreparedStatement.setUnicodeStream(parameterIndex, x, length);
	}

	public void setShort(int parameterIndex, short x) throws SQLException
	{
		argTraceSet(parameterIndex, "(short)", new Short(x));
		realPreparedStatement.setShort(parameterIndex, x);
	}

	public boolean execute() throws SQLException
	{
		String methodCall = "execute()";
		String dumpedSql = dumpedSql();
		long tstart = System.currentTimeMillis();
		try
		{
			boolean result = realPreparedStatement.execute();
			reportSql(System.currentTimeMillis() - tstart, dumpedSql, methodCall);
			return result;
		}
		catch(SQLException s)
		{
			reportException(methodCall, s, dumpedSql, System.currentTimeMillis() - tstart);
			throw s;
		}
	}

	public void setInt(int parameterIndex, int x) throws SQLException
	{
		argTraceSet(parameterIndex, "(int)", new Integer(x));
		realPreparedStatement.setInt(parameterIndex, x);
	}

	public void setLong(int parameterIndex, long x) throws SQLException
	{
		argTraceSet(parameterIndex, "(long)", new Long(x));
		realPreparedStatement.setLong(parameterIndex, x);
	}

	public void setFloat(int parameterIndex, float x) throws SQLException
	{
		argTraceSet(parameterIndex, "(float)", new Float(x));
		realPreparedStatement.setFloat(parameterIndex, x);
	}

	public void setDouble(int parameterIndex, double x) throws SQLException
	{
		argTraceSet(parameterIndex, "(double)", new Double(x));
		realPreparedStatement.setDouble(parameterIndex, x);
	}

	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException
	{
		argTraceSet(parameterIndex, "(BigDecimal)", x);
		realPreparedStatement.setBigDecimal(parameterIndex, x);
	}

	public void setURL(int parameterIndex, URL x) throws SQLException
	{
		argTraceSet(parameterIndex, "(URL)", x);
		realPreparedStatement.setURL(parameterIndex, x);
	}

	public void setString(int parameterIndex, String x) throws SQLException
	{
		argTraceSet(parameterIndex, "(String)", x);
		realPreparedStatement.setString(parameterIndex, x);
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException
	{
		argTraceSet(parameterIndex, "(byte[])", "<byte[]>");
		realPreparedStatement.setBytes(parameterIndex, x);
	}

	public void setDate(int parameterIndex, Date x) throws SQLException
	{
		argTraceSet(parameterIndex, "(Date)", x);
		realPreparedStatement.setDate(parameterIndex, x);
	}

	public ParameterMetaData getParameterMetaData() throws SQLException
	{
		return realPreparedStatement.getParameterMetaData();
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException
	{
		argTraceSet(parameterIndex, "(RowId)", x);
		realPreparedStatement.setRowId(parameterIndex, x);
	}

	public void setNString(int parameterIndex, String value) throws SQLException
	{
		argTraceSet(parameterIndex, "(String)", value);
		realPreparedStatement.setNString(parameterIndex, value);
	}

	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException
	{
		argTraceSet(parameterIndex, "(Reader)", "<Reader of length " + length + ">");
		realPreparedStatement.setNCharacterStream(parameterIndex, value, length);
	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException
	{
		argTraceSet(parameterIndex, "(NClob)", "<NClob>");
		realPreparedStatement.setNClob(parameterIndex, value);
	}

	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException
	{
		argTraceSet(parameterIndex, "(Reader)", "<Reader of length " + length + ">");
		realPreparedStatement.setClob(parameterIndex, reader, length);
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException
	{
		argTraceSet(parameterIndex, "(InputStream)", "<InputStream of length " + length + ">");
		realPreparedStatement.setBlob(parameterIndex, inputStream, length);
	}

	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException
	{
		argTraceSet(parameterIndex, "(Reader)", "<Reader of length " + length + ">");
		realPreparedStatement.setNClob(parameterIndex, reader, length);
	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException
	{
		argTraceSet(parameterIndex, "(SQLXML)", xmlObject);
		realPreparedStatement.setSQLXML(parameterIndex, xmlObject);
	}

	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException
	{
		argTraceSet(parameterIndex, "(Date)", x);
		realPreparedStatement.setDate(parameterIndex, x, cal);
	}

	public ResultSet executeQuery() throws SQLException
	{
		String methodCall = "executeQuery()";
		String dumpedSql = dumpedSql();
		long tstart = System.currentTimeMillis();
		try
		{
			ResultSet r = realPreparedStatement.executeQuery();
			reportSql(System.currentTimeMillis() - tstart, dumpedSql, methodCall);
			return r;
		}
		catch(SQLException s)
		{
			reportException(methodCall, s, dumpedSql, System.currentTimeMillis() - tstart);
			throw s;
		}
	}

	private String getTypeHelp(Object x)
	{
		if(x == null)
		{
			return "(null)";
		}
		else
		{
			return "(" + x.getClass().getName() + ")";
		}
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException
	{
		argTraceSet(parameterIndex, getTypeHelp(x), x);
		realPreparedStatement.setObject(parameterIndex, x, targetSqlType, scale);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException
	{
		argTraceSet(parameterIndex, "(Ascii InputStream)", "<Ascii InputStream of length " + length + ">");
		realPreparedStatement.setAsciiStream(parameterIndex, x, length);
	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException
	{
		argTraceSet(parameterIndex, "(Binary InputStream)", "<Binary InputStream of length " + length + ">");
		realPreparedStatement.setBinaryStream(parameterIndex, x, length);
	}

	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException
	{
		argTraceSet(parameterIndex, "(Reader)", "<Reader of length " + length + ">");
		realPreparedStatement.setCharacterStream(parameterIndex, reader, length);
	}

	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException
	{
		argTraceSet(parameterIndex, "(Ascii InputStream)", "<Ascii InputStream>");
		realPreparedStatement.setAsciiStream(parameterIndex, x);
	}

	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException
	{
		argTraceSet(parameterIndex, "(Binary InputStream)", "<Binary InputStream>");
		realPreparedStatement.setBinaryStream(parameterIndex, x);
	}

	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException
	{
		argTraceSet(parameterIndex, "(Reader)", "<Reader>");
		realPreparedStatement.setCharacterStream(parameterIndex, reader);
	}

	public void setNCharacterStream(int parameterIndex, Reader reader) throws SQLException
	{
		argTraceSet(parameterIndex, "(Reader)", "<Reader>");
		realPreparedStatement.setNCharacterStream(parameterIndex, reader);
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException
	{
		argTraceSet(parameterIndex, "(Reader)", "<Reader>");
		realPreparedStatement.setClob(parameterIndex, reader);
	}

	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException
	{
		argTraceSet(parameterIndex, "(InputStream)", "<InputStream>");
		realPreparedStatement.setBlob(parameterIndex, inputStream);
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException
	{
		argTraceSet(parameterIndex, "(Reader)", "<Reader>");
		realPreparedStatement.setNClob(parameterIndex, reader);
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException
	{
		argTraceSet(parameterIndex, getTypeHelp(x), x);
		realPreparedStatement.setObject(parameterIndex, x, targetSqlType);
	}

	public void setObject(int parameterIndex, Object x) throws SQLException
	{
		argTraceSet(parameterIndex, getTypeHelp(x), x);
		realPreparedStatement.setObject(parameterIndex, x);
	}

	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException
	{
		argTraceSet(parameterIndex, "(Date)", x);
		realPreparedStatement.setTimestamp(parameterIndex, x);
	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException
	{
		argTraceSet(parameterIndex, "(Timestamp)", x);
		realPreparedStatement.setTimestamp(parameterIndex, x, cal);
	}

	public int executeUpdate() throws SQLException
	{
		String methodCall = "executeUpdate()";
		String dumpedSql = dumpedSql();
		long tstart = System.currentTimeMillis();
		try
		{
			int result = realPreparedStatement.executeUpdate();
			reportSql(System.currentTimeMillis() - tstart, dumpedSql, methodCall);
			return result;
		}
		catch(SQLException s)
		{
			reportException(methodCall, s, dumpedSql, System.currentTimeMillis() - tstart);
			throw s;
		}
	}

	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException
	{
		argTraceSet(parameterIndex, "(Ascii InputStream)", "<Ascii InputStream of length " + length + ">");
		realPreparedStatement.setAsciiStream(parameterIndex, x, length);
	}

	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException
	{
		argTraceSet(parameterIndex, "(Binary InputStream)", "<Binary InputStream of length " + length + ">");
		realPreparedStatement.setBinaryStream(parameterIndex, x, length);
	}

	public void clearParameters() throws SQLException
	{
		synchronized(argTrace)
		{
			argTrace.clear();
		}
		realPreparedStatement.clearParameters();
	}

	public ResultSetMetaData getMetaData() throws SQLException
	{
		return realPreparedStatement.getMetaData();
	}

	public void addBatch() throws SQLException
	{
		currentBatch.add(dumpedSql());
		realPreparedStatement.addBatch();
	}

	public <T> T unwrap(Class<T> iface) throws SQLException
	{
		return (iface != null && (iface == PreparedStatement.class || iface == Statement.class || iface == Spy.class)) ? (T) this : realPreparedStatement.unwrap(iface);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException
	{
		return (iface != null && (iface == PreparedStatement.class || iface == Statement.class || iface == Spy.class)) || realPreparedStatement.isWrapperFor(iface);
	}
}
