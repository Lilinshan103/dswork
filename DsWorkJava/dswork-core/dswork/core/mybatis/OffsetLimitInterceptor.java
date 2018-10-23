package dswork.core.mybatis;

import java.util.Locale;
import java.util.Properties;

import dswork.core.mybatis.dialect.Dialect;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * 为mybatis3提供基于方言(Dialect)的分页查询的插件。
 * 将拦截Executor.query()方法实现分页方言的插入。
 */
@Intercepts(
{
		@Signature(type = Executor.class, method = "query", args =
		{
				MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class
		})
})
public class OffsetLimitInterceptor implements Interceptor
{
	private final static int MAPPED_STATEMENT_INDEX = 0;
	private final static int PARAMETER_INDEX = 1;
	private final static int ROWBOUNDS_INDEX = 2;
	private Dialect dialect = null;
	private String name = null;
	private Properties properties = null;

	public Object intercept(Invocation invocation) throws Throwable
	{
		processIntercept(invocation.getArgs());
		return invocation.proceed();
	}

	void processIntercept(final Object[] queryArgs) throws Exception
	{
		/*
		 * // 以下代码可根据数据源动态加载方言，用于多数据源的情况下
		 * Dialect dialect = this.dialect;
		 * String str = DynamicDataSource.getDbType();
		 * if(!str.equals("1"))
		 * {
		 * String dialectClass = this.properties.getProperty(str);
		 * try
		 * {
		 * System.out.println("dbType是" + str + "，加载动态方言：" + dialectClass);
		 * dialect = (Dialect) Class.forName(dialectClass).newInstance();
		 * }
		 * catch (Exception e)
		 * {
		 * throw new RuntimeException("无法初始化方言：" + dialectClass, e);
		 * }
		 * }
		 */
		final RowBounds rowBounds = (RowBounds) queryArgs[ROWBOUNDS_INDEX];
		int offset = rowBounds.getOffset();
		int limit = rowBounds.getLimit();
		if(dialect.supportsLimitOffset() && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT))
		{
			MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
			Object parameter = queryArgs[PARAMETER_INDEX];
			BoundSql boundSql = ms.getBoundSql(parameter);
			String sql = boundSql.getSql().trim();
			sql = dialect.getLimitString(sql, offset, limit);
			offset = RowBounds.NO_ROW_OFFSET;
			limit = RowBounds.NO_ROW_LIMIT;
			queryArgs[ROWBOUNDS_INDEX] = new RowBounds(offset, limit);
			BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
			String prop;
			for(ParameterMapping p : boundSql.getParameterMappings())
			{
				prop = p.getProperty();
				if(boundSql.hasAdditionalParameter(prop))
				{
					newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
				}
			}
			MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
			queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
		}
	}

	// see: MapperBuilderAssistant
	private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource)
	{
		Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		String[] arr = ms.getKeyProperties();
		if(arr != null && arr.length > 0)
		{
			StringBuilder sb = new StringBuilder();
			for(String s : arr)
			{
				sb.append(",").append(s);
			}
			if(sb.length() > 0)
			{
				sb.delete(0, 1);
			}
			builder.keyProperty(sb.toString());
		}
		// setStatementTimeout()
		builder.timeout(ms.getTimeout());
		// setStatementResultMap()
		builder.parameterMap(ms.getParameterMap());
		// setStatementResultMap()
		builder.resultMaps(ms.getResultMaps());
		builder.resultSetType(ms.getResultSetType());
		// setStatementCache()
		builder.cache(ms.getCache());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		builder.useCache(ms.isUseCache());
		return builder.build();
	}

	public Object plugin(Object target)
	{
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties)
	{
		this.properties = properties;
		this.name = this.properties.getProperty("name");
		if(this.name == null)
		{
			String dialectClass = this.properties.getProperty("dialect");
			if(dialectClass == null)
			{
				dialectClass = this.properties.getProperty("1");// 兼容旧的程序
			}
			if(dialectClass != null)
			{
				try
				{
					this.dialect = (Dialect) Class.forName(dialectClass).newInstance();
				}
				catch(Exception e)
				{
					throw new RuntimeException("Dialect can not loading:" + dialectClass, e);
				}
			}
		}
		else
		{
			name = String.valueOf(name).trim().toLowerCase(Locale.ENGLISH);
			try
			{
				this.dialect = dialectMap.get(name).newInstance();
			}
			catch(Exception e)
			{
				throw new RuntimeException("Dialect can not loading:" + name, e);
			}
		}
	}

	public static class BoundSqlSqlSource implements SqlSource
	{
		BoundSql boundSql;

		public BoundSqlSqlSource(BoundSql boundSql)
		{
			this.boundSql = boundSql;
		}

		public BoundSql getBoundSql(Object parameterObject)
		{
			return boundSql;
		}
	}

	private static java.util.Map<String, Class<? extends Dialect>> dialectMap = new java.util.HashMap<String, Class<? extends Dialect>>();
	static
	{
		dialectMap.put("gbase", dswork.core.mybatis.dialect.LimitOffsetDialect.class);
		dialectMap.put("mariadb", dswork.core.mybatis.dialect.LimitOffsetDialect.class);
		dialectMap.put("mysql", dswork.core.mybatis.dialect.LimitOffsetDialect.class);
		dialectMap.put("postgresql", dswork.core.mybatis.dialect.LimitOffsetDialect.class);
		dialectMap.put("sqlite", dswork.core.mybatis.dialect.LimitOffsetDialect.class);
		
		dialectMap.put("dm", dswork.core.mybatis.dialect.OracleDialect.class);
		dialectMap.put("oracle", dswork.core.mybatis.dialect.OracleDialect.class);
		
		dialectMap.put("db2", dswork.core.mybatis.dialect.DB2Dialect.class);
		
		dialectMap.put("derby", dswork.core.mybatis.dialect.SQLServer2012Dialect.class);
		dialectMap.put("sqlserver", dswork.core.mybatis.dialect.SQLServerDialect.class);
		dialectMap.put("sqlserver2000", dswork.core.mybatis.dialect.SQLServer2000Dialect.class);
		dialectMap.put("sqlserver2005", dswork.core.mybatis.dialect.SQLServer2005Dialect.class);
		dialectMap.put("sqlserver2008", dswork.core.mybatis.dialect.SQLServer2008Dialect.class);
		dialectMap.put("sqlserver2012", dswork.core.mybatis.dialect.SQLServer2012Dialect.class);
		dialectMap.put("null", dswork.core.mybatis.dialect.Dialect.class);
	}
}
