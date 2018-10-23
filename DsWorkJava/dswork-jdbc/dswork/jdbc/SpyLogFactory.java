package dswork.jdbc;

public class SpyLogFactory
{
	private SpyLogFactory()
	{
	}
	private static final SpyLogDelegator logger = new SpyLogDelegator();

	public static SpyLogDelegator getSpyLogDelegator()
	{
		return logger;
	}
}
