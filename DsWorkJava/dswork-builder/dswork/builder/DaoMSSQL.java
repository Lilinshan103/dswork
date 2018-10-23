package dswork.builder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoMSSQL extends Dao
{
	private static final String SQL_TABLE_PK = "sp_pkeys N'%s'";
	private static final String SQL_TABLE_COMMENT = "select cast(b.value as varchar(500)) as CCOMMEN from sys.tables a, sys.extended_properties b where a.type='U' and a.object_id=b.major_id and b.minor_id=0 and a.name='%s'";
	private static final String SQL_TABLE_COLUMN = "select "
			+ "a.name as CNAME, "
			+ "c.name as CDATATYPE, "
			+ "a.max_length as CLENGTH, "
			+ "a.is_nullable as CNULLABLE, "
			+ "a.precision as CPRECISION, "
			+ "a.scale as CSCALE, "
			+ "(select cast(value as varchar(500)) from sys.extended_properties where sys.extended_properties.major_id = a.object_id and sys.extended_properties.minor_id = a.column_id"
			+ ") as CCOMMENT, "
			+ "(select count(*) from sys.identity_columns where sys.identity_columns.object_id = a.object_id and a.column_id = sys.identity_columns.column_id"
			+ ") as CAUTO "
			+ "from sys.columns a, sys.tables b, sys.types c "
			+ "where a.object_id = b.object_id and a.system_type_id=c.system_type_id and b.name='%s' and c.name<>'sysname' order by a.column_id;";
//	private static final String SQL_ALL_TABLES = "select name from sys.tables where type='U' and name<>'sysdiagrams'";
	
	private Connection conn;
	// private String dbName;

	@Override
	public void connect(String url) throws SQLException
	{
		conn = DriverManager.getConnection(url);
		// dbName = conn.getCatalog();
	}
	
	private void querySetTablePk(Table table)
	{
		String sql = String.format(SQL_TABLE_PK, table.getName());
		try
		{
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			String pk = null;
			while(rs.next())
			{
				pk = rs.getString("COLUMN_NAME");
				for(Table.Column c : table.getColumn())
				{
					if(c.getName().equalsIgnoreCase(pk))
					{
						c.setKey(true);
						break;
					}
				}
			}
			rs.close();
			stmt.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void querySetTableComment(Table table)
	{
		String comment = "";
		String sql = String.format(SQL_TABLE_COMMENT, table.getName());
		try
		{
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			comment = rs.getString("CCOMMEN");
			rs.close();
			stmt.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		table.setComment(comment);
	}

	private void querySetTableColumn(Table table)
	{
		String sql = String.format(SQL_TABLE_COLUMN, table.getName());
		try
		{
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				Table.Column col = table.addColumn();
				col.setName(rs.getString("CNAME"));
				col.setDatatype(rs.getString("CDATATYPE"));
				col.setLength(rs.getLong("CLENGTH"));
				col.setNullable("1".equalsIgnoreCase(rs.getString("CNULLABLE")));
				col.setPrecision(rs.getInt("CPRECISION"));
				col.setDigit(rs.getInt("CSCALE"));
				col.setComment(rs.getString("CCOMMENT"));
				col.setAuto(!"0".equals(rs.getString("CAUTO")));
				// col.setKey();
				switch(col.getDatatype())
				{
					case "smallint":
					case "int":
					case "tinyint":
						col.setLength(col.getPrecision());
						col.setType("int"); break;
					case "bigint":
						col.setLength(col.getPrecision());
						if("id".equalsIgnoreCase(col.getName()))
						{
							col.setType("Long");
						}
						else
						{
							col.setType("long");
						}
						break;
					case "real":
					case "smallmoney":
					case "money":
						col.setLength(col.getPrecision() + (col.getDigit() > 0 ? col.getDigit() + 1 : col.getDigit()));
						col.setType("float"); break;
					case "decimal":
					case "numeric":
					{
						if(col.getDigit() == 0)
						{
							col.setLength(col.getPrecision());
							if(col.getPrecision() < 11)
							{
								col.setType("int"); break;
							}
							col.setType("long"); break;
						}
						else
						{
							col.setLength(col.getPrecision() + (col.getDigit() > 0 ? col.getDigit() + 1 : col.getDigit()));
							col.setType("float"); break;
						}
					}
					case "bit":
						col.setLength(1);
						col.setType("boolean"); break;
					case "smalldatetime":
					case "datetime":
						col.setType("date"); break;
					case "char":
					case "nchar":
					case "varchar":
					case "nvarchar":
					case "timestamp":
					case "text":
					case "ntext":
					case "xml":
					case "uniqueidentifier":
						col.setType("String"); break;
					default:
						break;
				}
			}
			rs.close();
			stmt.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public Table query(String tableName)
	{
		Table table = new Table();
		table.setName(tableName);
		querySetTableComment(table);
		querySetTableColumn(table);
		querySetTablePk(table);
		return table;
	}

	@Override
	public void close()
	{
		try
		{
			if(conn != null)
			{
				conn.close();
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
}
