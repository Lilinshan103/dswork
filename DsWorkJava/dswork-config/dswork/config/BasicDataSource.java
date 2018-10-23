package dswork.config;

public class BasicDataSource extends org.apache.commons.dbcp.BasicDataSource
{
	private String filters;
	private int maxPoolPreparedStatementPerConnectionSize = -1;

	public String getFilters()
	{
		return filters;
	}

	public void setFilters(String filters)
	{
		this.filters = filters;
	}

	public int getMaxPoolPreparedStatementPerConnectionSize()
	{
		return maxPoolPreparedStatementPerConnectionSize;
	}

	public void setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize)
	{
		this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
	}
}
