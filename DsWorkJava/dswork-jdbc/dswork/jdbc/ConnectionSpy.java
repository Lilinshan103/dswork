package dswork.jdbc;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.SQLClientInfoException;
import java.sql.SQLXML;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@SuppressWarnings("all")
public class ConnectionSpy implements Connection, Spy
{
	private Connection realConnection;

	public Connection getRealConnection()
	{
		return realConnection;
	}
	private SpyLogDelegator log;
	private final Integer connectionNumber;
	private static int lastConnectionNumber = 0;
	private static final Map connectionTracker = new HashMap();

	public ConnectionSpy(Connection realConnection)
	{
		this(realConnection, DriverSpy.defaultRdbmsSpecifics);
	}

	public ConnectionSpy(Connection realConnection, RdbmsSpecifics rdbmsSpecifics)
	{
		if(rdbmsSpecifics == null)
		{
			rdbmsSpecifics = DriverSpy.defaultRdbmsSpecifics;
		}
		setRdbmsSpecifics(rdbmsSpecifics);
		if(realConnection == null)
		{
			throw new IllegalArgumentException("Must pass in a non null real Connection");
		}
		this.realConnection = realConnection;
		log = SpyLogFactory.getSpyLogDelegator();
		synchronized(connectionTracker)
		{
			connectionNumber = new Integer(++lastConnectionNumber);
			connectionTracker.put(connectionNumber, this);
		}
		log.connectionOpened(this);
	}
	private RdbmsSpecifics rdbmsSpecifics;

	void setRdbmsSpecifics(RdbmsSpecifics rdbmsSpecifics)
	{
		this.rdbmsSpecifics = rdbmsSpecifics;
	}

	RdbmsSpecifics getRdbmsSpecifics()
	{
		return rdbmsSpecifics;
	}

	public Integer getConnectionNumber()
	{
		return connectionNumber;
	}

	public String getClassType()
	{
		return "ConnectionSpy";
	}

	protected void reportException(String methodCall, SQLException exception, String sql)
	{
		log.exceptionOccured(this, methodCall, exception, sql, -1L);
	}

	public boolean isClosed() throws SQLException
	{
		try
		{
			return realConnection.isClosed();
		}
		catch(SQLException s)
		{
			String methodCall = "isClosed()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public SQLWarning getWarnings() throws SQLException
	{
		try
		{
			return realConnection.getWarnings();
		}
		catch(SQLException s)
		{
			String methodCall = "getWarnings()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public Savepoint setSavepoint() throws SQLException
	{
		try
		{
			return realConnection.setSavepoint();
		}
		catch(SQLException s)
		{
			String methodCall = "setSavepoint()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException
	{
		try
		{
			realConnection.releaseSavepoint(savepoint);
		}
		catch(SQLException s)
		{
			String methodCall = "releaseSavepoint(" + savepoint + ")";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public void rollback(Savepoint savepoint) throws SQLException
	{
		try
		{
			realConnection.rollback(savepoint);
		}
		catch(SQLException s)
		{
			String methodCall = "rollback(" + savepoint + ")";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public DatabaseMetaData getMetaData() throws SQLException
	{
		try
		{
			return realConnection.getMetaData();
		}
		catch(SQLException s)
		{
			String methodCall = "getMetaData()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public void clearWarnings() throws SQLException
	{
		try
		{
			realConnection.clearWarnings();
		}
		catch(SQLException s)
		{
			String methodCall = "clearWarnings()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public Statement createStatement() throws SQLException
	{
		try
		{
			Statement statement = realConnection.createStatement();
			return (Statement) new StatementSpy(this, statement);
		}
		catch(SQLException s)
		{
			String methodCall = "createStatement()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException
	{
		try
		{
			Statement statement = realConnection.createStatement(resultSetType, resultSetConcurrency);
			return (Statement) new StatementSpy(this, statement);
		}
		catch(SQLException s)
		{
			String methodCall = "createStatement(" + resultSetType + ", " + resultSetConcurrency + ")";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
	{
		try
		{
			Statement statement = realConnection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
			return (Statement) new StatementSpy(this, statement);
		}
		catch(SQLException s)
		{
			String methodCall = "createStatement(" + resultSetType + ", " + resultSetConcurrency + ", " + resultSetHoldability + ")";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public void setReadOnly(boolean readOnly) throws SQLException
	{
		try
		{
			realConnection.setReadOnly(readOnly);
		}
		catch(SQLException s)
		{
			String methodCall = "setReadOnly(" + readOnly + ")";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException
	{
		try
		{
			PreparedStatement statement = realConnection.prepareStatement(sql);
			return (PreparedStatement) new PreparedStatementSpy(sql, this, statement);
		}
		catch(SQLException s)
		{
			String methodCall = "prepareStatement(" + sql + ")";
			reportException(methodCall, s, sql);
			throw s;
		}
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException
	{
		try
		{
			PreparedStatement statement = realConnection.prepareStatement(sql, autoGeneratedKeys);
			return (PreparedStatement) new PreparedStatementSpy(sql, this, statement);
		}
		catch(SQLException s)
		{
			String methodCall = "prepareStatement(" + sql + ", " + autoGeneratedKeys + ")";
			reportException(methodCall, s, sql);
			throw s;
		}
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
	{
		try
		{
			PreparedStatement statement = realConnection.prepareStatement(sql, resultSetType, resultSetConcurrency);
			return (PreparedStatement) new PreparedStatementSpy(sql, this, statement);
		}
		catch(SQLException s)
		{
			String methodCall = "prepareStatement(" + sql + ", " + resultSetType + ", " + resultSetConcurrency + ")";
			reportException(methodCall, s, sql);
			throw s;
		}
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
	{
		try
		{
			PreparedStatement statement = realConnection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
			return (PreparedStatement) new PreparedStatementSpy(sql, this, statement);
		}
		catch(SQLException s)
		{
			String methodCall = "prepareStatement(" + sql + ", " + resultSetType + ", " + resultSetConcurrency + ", " + resultSetHoldability + ")";
			reportException(methodCall, s, sql);
			throw s;
		}
	}

	public PreparedStatement prepareStatement(String sql, int columnIndexes[]) throws SQLException
	{
		try
		{
			PreparedStatement statement = realConnection.prepareStatement(sql, columnIndexes);
			return (PreparedStatement) new PreparedStatementSpy(sql, this, statement);
		}
		catch(SQLException s)
		{
			String methodCall = "prepareStatement(" + sql + ", " + columnIndexes + ")";
			reportException(methodCall, s, sql);
			throw s;
		}
	}

	public Savepoint setSavepoint(String name) throws SQLException
	{
		try
		{
			return realConnection.setSavepoint(name);
		}
		catch(SQLException s)
		{
			String methodCall = "setSavepoint(" + name + ")";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public PreparedStatement prepareStatement(String sql, String columnNames[]) throws SQLException
	{
		try
		{
			PreparedStatement statement = realConnection.prepareStatement(sql, columnNames);
			return (PreparedStatement) new PreparedStatementSpy(sql, this, statement);
		}
		catch(SQLException s)
		{
			String methodCall = "prepareStatement(" + sql + ", " + columnNames + ")";
			reportException(methodCall, s, sql);
			throw s;
		}
	}

	public Clob createClob() throws SQLException
	{
		try
		{
			return realConnection.createClob();
		}
		catch(SQLException s)
		{
			String methodCall = "createClob()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public Blob createBlob() throws SQLException
	{
		try
		{
			return realConnection.createBlob();
		}
		catch(SQLException s)
		{
			String methodCall = "createBlob()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public NClob createNClob() throws SQLException
	{
		try
		{
			return realConnection.createNClob();
		}
		catch(SQLException s)
		{
			String methodCall = "createNClob()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public SQLXML createSQLXML() throws SQLException
	{
		try
		{
			return realConnection.createSQLXML();
		}
		catch(SQLException s)
		{
			String methodCall = "createSQLXML()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public boolean isValid(int timeout) throws SQLException
	{
		try
		{
			return realConnection.isValid(timeout);
		}
		catch(SQLException s)
		{
			String methodCall = "isValid(" + timeout + ")";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public void setClientInfo(String name, String value) throws SQLClientInfoException
	{
		try
		{
			realConnection.setClientInfo(name, value);
		}
		catch(SQLClientInfoException s)
		{
			String methodCall = "setClientInfo(" + name + ", " + value + ")";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public void setClientInfo(Properties properties) throws SQLClientInfoException
	{
		try
		{
			realConnection.setClientInfo(properties);
		}
		catch(SQLClientInfoException s)
		{
			String methodCall = "setClientInfo(" + properties + ")";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public String getClientInfo(String name) throws SQLException
	{
		try
		{
			return realConnection.getClientInfo(name);
		}
		catch(SQLException s)
		{
			String methodCall = "getClientInfo(" + name + ")";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public Properties getClientInfo() throws SQLException
	{
		try
		{
			return realConnection.getClientInfo();
		}
		catch(SQLException s)
		{
			String methodCall = "getClientInfo()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public Array createArrayOf(String typeName, Object[] elements) throws SQLException
	{
		try
		{
			return realConnection.createArrayOf(typeName, elements);
		}
		catch(SQLException s)
		{
			String methodCall = "createArrayOf(" + typeName + ", " + elements + ")";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public Struct createStruct(String typeName, Object[] attributes) throws SQLException
	{
		try
		{
			return realConnection.createStruct(typeName, attributes);
		}
		catch(SQLException s)
		{
			String methodCall = "createStruct(" + typeName + ", " + attributes + ")";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public boolean isReadOnly() throws SQLException
	{
		try
		{
			return realConnection.isReadOnly();
		}
		catch(SQLException s)
		{
			String methodCall = "isReadOnly()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public void setHoldability(int holdability) throws SQLException
	{
		try
		{
			realConnection.setHoldability(holdability);
		}
		catch(SQLException s)
		{
			String methodCall = "setHoldability(" + holdability + ")";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public CallableStatement prepareCall(String sql) throws SQLException
	{
		try
		{
			CallableStatement statement = realConnection.prepareCall(sql);
			return (CallableStatement) new CallableStatementSpy(sql, this, statement);
		}
		catch(SQLException s)
		{
			String methodCall = "prepareCall(" + sql + ")";
			reportException(methodCall, s, sql);
			throw s;
		}
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
	{
		try
		{
			CallableStatement statement = realConnection.prepareCall(sql, resultSetType, resultSetConcurrency);
			return (CallableStatement) new CallableStatementSpy(sql, this, statement);
		}
		catch(SQLException s)
		{
			String methodCall = "prepareCall(" + sql + ", " + resultSetType + ", " + resultSetConcurrency + ")";
			reportException(methodCall, s, sql);
			throw s;
		}
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
	{
		try
		{
			CallableStatement statement = realConnection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
			return (CallableStatement) new CallableStatementSpy(sql, this, statement);
		}
		catch(SQLException s)
		{
			String methodCall = "prepareCall(" + sql + ", " + resultSetType + ", " + resultSetConcurrency + ", " + resultSetHoldability + ")";
			reportException(methodCall, s, sql);
			throw s;
		}
	}

	public void setCatalog(String catalog) throws SQLException
	{
		try
		{
			realConnection.setCatalog(catalog);
		}
		catch(SQLException s)
		{
			String methodCall = "setCatalog(" + catalog + ")";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public String nativeSQL(String sql) throws SQLException
	{
		try
		{
			return realConnection.nativeSQL(sql);
		}
		catch(SQLException s)
		{
			String methodCall = "nativeSQL(" + sql + ")";
			reportException(methodCall, s, sql);
			throw s;
		}
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException
	{
		try
		{
			return realConnection.getTypeMap();
		}
		catch(SQLException s)
		{
			String methodCall = "getTypeMap()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException
	{
		try
		{
			realConnection.setAutoCommit(autoCommit);
		}
		catch(SQLException s)
		{
			String methodCall = "setAutoCommit(" + autoCommit + ")";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public String getCatalog() throws SQLException
	{
		try
		{
			return realConnection.getCatalog();
		}
		catch(SQLException s)
		{
			String methodCall = "getCatalog()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public void setTypeMap(java.util.Map<String, Class<?>> map) throws SQLException
	{
		try
		{
			realConnection.setTypeMap(map);
		}
		catch(SQLException s)
		{
			String methodCall = "setTypeMap(" + map + ")";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public void setTransactionIsolation(int level) throws SQLException
	{
		try
		{
			realConnection.setTransactionIsolation(level);
		}
		catch(SQLException s)
		{
			String methodCall = "setTransactionIsolation(" + level + ")";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public boolean getAutoCommit() throws SQLException
	{
		try
		{
			return realConnection.getAutoCommit();
		}
		catch(SQLException s)
		{
			String methodCall = "getAutoCommit()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public int getHoldability() throws SQLException
	{
		try
		{
			return realConnection.getHoldability();
		}
		catch(SQLException s)
		{
			String methodCall = "getHoldability()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public int getTransactionIsolation() throws SQLException
	{
		try
		{
			return realConnection.getTransactionIsolation();
		}
		catch(SQLException s)
		{
			String methodCall = "getTransactionIsolation()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public void commit() throws SQLException
	{
		try
		{
			realConnection.commit();
		}
		catch(SQLException s)
		{
			String methodCall = "commit()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public void rollback() throws SQLException
	{
		try
		{
			realConnection.rollback();
		}
		catch(SQLException s)
		{
			String methodCall = "rollback()";
			reportException(methodCall, s, null);
			throw s;
		}
	}

	public void close() throws SQLException
	{
		try
		{
			realConnection.close();
		}
		catch(SQLException s)
		{
			String methodCall = "close()";
			reportException(methodCall, s, null);
			throw s;
		}
		finally
		{
			synchronized(connectionTracker)
			{
				connectionTracker.remove(connectionNumber);
			}
			log.connectionClosed(this);
		}
	}

	// jdbc4
	public <T> T unwrap(Class<T> iface) throws SQLException
	{
		try
		{
			Connection.class.getMethod("unwrap", Class.class);
			try
			{
				return (iface != null && (iface == Connection.class || iface == Spy.class)) ? (T) this : realConnection.unwrap(iface);
			}
			catch(SQLException s)
			{
				String methodCall = "unwrap(" + (iface == null ? "null" : iface.getName()) + ")";
				reportException(methodCall, s, null);
				throw s;
			}
		}
		catch(NoSuchMethodException e)
		{
			System.out.println("ConnectionSpy ignore unwrap(Class<T> iface)");
		}
		return null;
	}

	// jdbc4
	public boolean isWrapperFor(Class<?> iface) throws SQLException
	{
		try
		{
			Connection.class.getMethod("isWrapperFor", Class.class);
			try
			{
				return (iface != null && (iface == Connection.class || iface == Spy.class)) || realConnection.isWrapperFor(iface);
			}
			catch(SQLException s)
			{
				String methodCall = "isWrapperFor(" + (iface == null ? "null" : iface.getName()) + ")";
				reportException(methodCall, s, null);
				throw s;
			}
		}
		catch(NoSuchMethodException e)
		{
			System.out.println("ConnectionSpy ignore isWrapperFor(Class<T> iface)");
		}
		return false;
	}

	// jdbc4.1
	public void setSchema(String schema) throws SQLException
	{
		try
		{
			Connection.class.getMethod("setSchema", String.class);
			try
			{
				realConnection.setSchema(schema);
			}
			catch(SQLException s)
			{
				String methodCall = "setSchema(" + schema + ")";
				reportException(methodCall, s, null);
				throw s;
			}
		}
		catch(NoSuchMethodException e)
		{
			System.out.println("ConnectionSpy ignore setSchema(String schema)");
		}
	}

	// jdbc4.1
	public String getSchema() throws SQLException
	{
		try
		{
			Connection.class.getMethod("getSchema");
			try
			{
				return realConnection.getSchema();
			}
			catch(SQLException s)
			{
				String methodCall = "getSchema()";
				reportException(methodCall, s, null);
				throw s;
			}
		}
		catch(NoSuchMethodException e)
		{
			System.out.println("ConnectionSpy ignore getSchema()");
		}
		return "";
	}

	// jdbc4.1
	public void abort(java.util.concurrent.Executor executor) throws SQLException
	{
		try
		{
			Connection.class.getMethod("abort", java.util.concurrent.Executor.class);
			try
			{
				realConnection.abort(executor);
			}
			catch(SQLException s)
			{
				String methodCall = "abort(" + executor + ")";
				reportException(methodCall, s, null);
				throw s;
			}
		}
		catch(NoSuchMethodException e)
		{
			System.out.println("ConnectionSpy ignore abort(Executor executor)");
		}
	}

	// jdbc4.1
	public void setNetworkTimeout(java.util.concurrent.Executor executor, int milliseconds) throws SQLException
	{
		try
		{
			Connection.class.getMethod("setNetworkTimeout", java.util.concurrent.Executor.class, int.class);
			try
			{
				realConnection.setNetworkTimeout(executor, milliseconds);
			}
			catch(SQLException s)
			{
				String methodCall = "setNetworkTimeout(" + executor + "," + milliseconds + ")";
				reportException(methodCall, s, null);
				throw s;
			}
		}
		catch(NoSuchMethodException e)
		{
			System.out.println("ConnectionSpy ignore setNetworkTimeout(Executor executor, int milliseconds)");
		}
	}

	// jdbc4.1
	public int getNetworkTimeout() throws SQLException
	{
		try
		{
			Connection.class.getMethod("getNetworkTimeout");
			try
			{
				return realConnection.getNetworkTimeout();
			}
			catch(SQLException s)
			{
				String methodCall = "getNetworkTimeout()";
				reportException(methodCall, s, null);
				throw s;
			}
		}
		catch(NoSuchMethodException e)
		{
			System.out.println("ConnectionSpy ignore getNetworkTimeout()");
		}
		return 0;
	}
}
