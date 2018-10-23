package dswork.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("all")
public class DriverSpy implements Driver
{
	private Driver lastUnderlyingDriverRequested;
	private static Map rdbmsSpecifics;
	static final SpyLogDelegator log = SpyLogFactory.getSpyLogDelegator();
	private static boolean hasSql2000 = false;

	private static String getStringOption(Properties props, String propName)
	{
		String propValue = props.getProperty(propName);
		if(propValue == null || propValue.length() == 0)
		{
			propValue = null;
		}
		else
		{
			log.debug("  " + propName + " = " + propValue);
		}
		return propValue;
	}
	static
	{
		log.debug("... dswork jdbc initializing ...");
		InputStream propStream = DriverSpy.class.getResourceAsStream("/config/config.properties");
		Properties props = new Properties(System.getProperties());
		if(propStream != null)
		{
			try
			{
				props.load(propStream);
			}
			catch(IOException e)
			{
				log.debug(e.getMessage());
			}
			finally
			{
				try
				{
					propStream.close();
				}
				catch(IOException e)
				{
				}
			}
		}
		Set subDrivers = new TreeSet();
		String moreDrivers = getStringOption(props, "dswork.jdbc.drivers");
		if(moreDrivers != null)
		{
			String[] arr = moreDrivers.split(",");
			for(int i = 0; i < arr.length; i++)
			{
				subDrivers.add(arr[i]);
			}
		}
		else
		{
			subDrivers.add("oracle.jdbc.driver.OracleDriver");
			subDrivers.add("oracle.jdbc.OracleDriver");
			subDrivers.add("com.mysql.jc.jdbc.Driver");// mysql j 6.x for jdk8
			subDrivers.add("com.mysql.jdbc.Driver");// mysql j 5.x及之前
			subDrivers.add("com.microsoft.jdbc.sqlserver.SQLServerDriver");// MS driver for Sql Server 2000
			subDrivers.add("com.microsoft.sqlserver.jdbc.SQLServerDriver");// MS driver for Sql Server 2005
			subDrivers.add("com.ibm.db2.jcc.DB2Driver");// 增加了DB2支持
			subDrivers.add("org.sqlite.JDBC");// 增加了sqlite支持
			subDrivers.add("org.postgresql.Driver");
			subDrivers.add("com.sybase.jdbc2.jdbc.SybDriver");
			subDrivers.add("net.sourceforge.jtds.jdbc.Driver");// sqlServer
			subDrivers.add("weblogic.jdbc.sqlserver.SQLServerDriver");// sqlServer
			subDrivers.add("com.informix.jdbc.IfxDriver");
			subDrivers.add("org.apache.derby.jdbc.ClientDriver");
			subDrivers.add("org.apache.derby.jdbc.EmbeddedDriver");
			subDrivers.add("org.hsqldb.jdbcDriver");
			subDrivers.add("org.h2.Driver");
			subDrivers.add("dm.jdbc.driver.DmDriver");//达梦
			subDrivers.add("com.gbase.jdbc.Driver");//gbase
		}
		try
		{
			DriverManager.registerDriver(new DriverSpy());
		}
		catch(SQLException s)
		{
			throw (RuntimeException) new RuntimeException("could not register DriverSpy!").initCause(s);
		}
		String driverClass;
		for(Iterator i = subDrivers.iterator(); i.hasNext();)
		{
			driverClass = (String) i.next();
			try
			{
				Class.forName(driverClass);
				if("com.microsoft.jdbc.sqlserver.SQLServerDriver".equals(driverClass))
				{
					hasSql2000 = true;
				}
				log.debug("  FOUND DRIVER " + driverClass);
			}
			catch(Throwable c)
			{
				i.remove();
			}
		}
		if(subDrivers.size() == 0)
		{
			log.debug("WARNING! couldn't find any underlying jdbc drivers.");
		}
		OracleRdbmsSpecifics oracle = new OracleRdbmsSpecifics();
		MySqlRdbmsSpecifics mySql = new MySqlRdbmsSpecifics();
		rdbmsSpecifics = new HashMap();
		rdbmsSpecifics.put("oracle.jdbc.driver.OracleDriver", oracle);
		rdbmsSpecifics.put("oracle.jdbc.OracleDriver", oracle);
		rdbmsSpecifics.put("com.mysql.jc.jdbc.Driver", mySql);// 只有jdbc4.2需要用上，因为它只支持jdk8
		rdbmsSpecifics.put("com.mysql.jdbc.Driver", mySql);
		log.debug("... dswork jdbc initialized! ...");
	}
	static RdbmsSpecifics defaultRdbmsSpecifics = new RdbmsSpecifics();

	static RdbmsSpecifics getRdbmsSpecifics(Connection conn)
	{
		String driverName = "";
		try
		{
			DatabaseMetaData dbm = conn.getMetaData();
			driverName = dbm.getDriverName();
		}
		catch(SQLException s)
		{
		}
		log.debug("driver name is " + driverName);
		RdbmsSpecifics r = (RdbmsSpecifics) rdbmsSpecifics.get(driverName);
		if(r == null)
		{
			return defaultRdbmsSpecifics;
		}
		else
		{
			return r;
		}
	}

	public DriverSpy()
	{
	}

	public int getMajorVersion()
	{
		if(lastUnderlyingDriverRequested == null)
		{
			return 1;
		}
		else
		{
			return lastUnderlyingDriverRequested.getMajorVersion();
		}
	}

	public int getMinorVersion()
	{
		if(lastUnderlyingDriverRequested == null)
		{
			return 0;
		}
		else
		{
			return lastUnderlyingDriverRequested.getMinorVersion();
		}
	}

	public boolean jdbcCompliant()
	{
		return lastUnderlyingDriverRequested != null && lastUnderlyingDriverRequested.jdbcCompliant();
	}

	public boolean acceptsURL(String url) throws SQLException
	{
		Driver d = getDriverLoading(url);
		if(d != null)
		{
			lastUnderlyingDriverRequested = d;
			return true;
		}
		else
		{
			return false;
		}
	}

	private Driver getDriverLoading(String url) throws SQLException
	{
		Enumeration e = DriverManager.getDrivers();
		Driver d;
		int i = 0;
		Driver d2 = null;
		while(e.hasMoreElements())
		{
			d = (Driver) e.nextElement();
			if(d.getClass() == DriverSpy.class)
			{
				continue;// 跳过自己
			}
			if(d.acceptsURL(url))
			{
				if(hasSql2000)// 兼容2000以上时，跳过2000的驱动
				{
					if(i == 0 && url.startsWith("jdbc:sqlserver"))
					{
						d2 = d;
						i++;
						continue;
					}
				}
				return d;
			}
		}
		return d2;// 万一跳过第一个后没有匹配时
	}

	public Connection connect(String url, Properties info) throws SQLException
	{
		Driver d = getDriverLoading(url);
		if(d == null)
		{
			return null;
		}
		lastUnderlyingDriverRequested = d;
		Connection c = d.connect(url, info);
		if(c == null)
		{
			throw new SQLException("invalid or unknown driver url: " + url);
		}
		if(log.isJdbcLoggingEnabled())
		{
			ConnectionSpy cspy = new ConnectionSpy(c);
			RdbmsSpecifics r = null;
			String dclass = d.getClass().getName();
			if(dclass != null && dclass.length() > 0)
			{
				r = (RdbmsSpecifics) rdbmsSpecifics.get(dclass);
			}
			if(r == null)
			{
				r = defaultRdbmsSpecifics;
			}
			cspy.setRdbmsSpecifics(r);
			return cspy;
		}
		else
		{
			return c;
		}
	}

	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException
	{
		Driver d = getDriverLoading(url);
		if(d == null)
		{
			return new DriverPropertyInfo[0];
		}
		lastUnderlyingDriverRequested = d;
		return d.getPropertyInfo(url, info);
	}

	// jdbc4.1
	public java.util.logging.Logger getParentLogger() throws java.sql.SQLFeatureNotSupportedException
	{
		try
		{
			Driver.class.getMethod("getParentLogger");
			return lastUnderlyingDriverRequested.getParentLogger();
		}
		catch(NoSuchMethodException e)
		{
			System.out.println("CallableStatementSpy ignore getParentLogger()");
		}
		return null;
	}
}
