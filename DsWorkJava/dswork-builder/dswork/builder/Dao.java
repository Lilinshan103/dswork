package dswork.builder;

import java.sql.SQLException;

public abstract class Dao
{
	public abstract void connect(String url) throws SQLException;
	public abstract Table query(String tableName);
	public abstract void close();
}
