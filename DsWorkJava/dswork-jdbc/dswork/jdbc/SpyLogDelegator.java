package dswork.jdbc;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class SpyLogDelegator
{
	static final long SqlTimingWarn = 10000L;
	static final long SqlTimingWarnDebug = 60000L;

	public SpyLogDelegator()
	{
	}
	private static String nl = System.getProperty("line.separator");
	private final Logger sqlLogger = LoggerFactory.getLogger("jdbc.sqlonly");

	public boolean isJdbcLoggingEnabled()
	{
		return sqlLogger.isErrorEnabled();
	}

	public void exceptionOccured(Spy spy, String methodCall, Exception e, String sql, long execTime)
	{
		Integer spyNo = spy.getConnectionNumber();
		if(sql == null)
		{
			String classType = spy.getClassType();
			String header = spyNo + ". " + classType + "." + methodCall;
			sqlLogger.error(header, e);
		}
		else
		{
			sql = (sql == null) ? "" : sql.trim();
			sqlLogger.error(" {FAILED after " + execTime + " msec}" + nl + spyNo + ". " + sql, e);
		}
	}

	public void sqlOccured(Spy spy, long execTime, String methodCall, String sql)
	{
		if(sqlLogger.isErrorEnabled())
		{
			if(sqlLogger.isWarnEnabled())
			{
				if(execTime >= SpyLogDelegator.SqlTimingWarn)
				{
					sqlLogger.warn(buildSqlDump(spy, execTime, methodCall, sql, sqlLogger.isDebugEnabled() || (execTime > SpyLogDelegator.SqlTimingWarnDebug)));
				}
				else if(sqlLogger.isDebugEnabled())
				{
					sqlLogger.debug(buildSqlDump(spy, execTime, methodCall, sql, true));
				}
				else if(sqlLogger.isInfoEnabled())
				{
					sqlLogger.info(buildSqlDump(spy, execTime, methodCall, sql, false));
				}
			}
		}
	}

	private String buildSqlDump(Spy spy, long execTime, String methodCall, String sql, boolean debugInfo)
	{
		StringBuffer out = new StringBuffer();
		if(debugInfo)
		{
			out.append("{executed in ");
			out.append(execTime);
			out.append(" msec}");
		}
		out.append(nl);
		out.append(spy.getConnectionNumber());
		out.append(". ");
		sql = (sql == null) ? "" : sql.trim();
		out.append(sql);
		return out.toString();
	}

	public void debug(String msg)
	{
		sqlLogger.debug(msg);
	}

	public void connectionOpened(Spy spy)
	{
		sqlLogger.info(spy.getConnectionNumber() + ". Connection opened");
	}

	public void connectionClosed(Spy spy)
	{
		sqlLogger.info(spy.getConnectionNumber() + ". Connection closed ");
	}
}
