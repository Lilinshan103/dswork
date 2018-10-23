package dswork.builder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoMySql extends Dao
{
	private static final String SQL_TABLE_COMMENT = "select TABLE_COMMENT as CCOMMENT from information_schema.TABLES where TABLE_SCHEMA='%s' and TABLE_NAME='%s'";
	private static final String SQL_TABLE_COLUMN = "select "
			+ "COLUMN_NAME as CNAME, "
			+ "DATA_TYPE as CDATATYPE, "
			+ "CHARACTER_MAXIMUM_LENGTH as CLENGTH, "
			+ "IS_NULLABLE as CNULLABLE, "
			+ "NUMERIC_PRECISION as CPRECISION, "
			+ "NUMERIC_SCALE as CSCALE, "
			+ "COLUMN_COMMENT as CCOMMENT, "
			+ "EXTRA as CAUTO, "
			+ "COLUMN_DEFAULT as CDEFAULT,"
			+ "COLUMN_KEY as CKEY "
			+ "from information_schema.COLUMNS "
			+ "where TABLE_SCHEMA='%s' and TABLE_NAME='%s'";
//	private static final String SQL_ALL_TABLES = "select TABLE_NAME from information_schema.TABLES where TABLE_SCHEMA='%s'";

	private Connection conn;
	private String dbName;

	@Override
	public void connect(String url) throws SQLException
	{
		conn = DriverManager.getConnection(url);
		dbName = conn.getCatalog();
	}

	private void querySetTableComment(Table table)
	{
		String comment = "";
		String sql = String.format(SQL_TABLE_COMMENT, dbName, table.getName());
		try
		{
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			comment = rs.getString("CCOMMENT");
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
		String sql = String.format(SQL_TABLE_COLUMN, dbName, table.getName());
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
				col.setNullable("TRUE".equalsIgnoreCase(rs.getString("CNULLABLE")));
				col.setPrecision(rs.getInt("CPRECISION"));
				col.setDigit(rs.getInt("CSCALE"));
				col.setComment(rs.getString("CCOMMENT"));
				col.setAuto("auto_increment".equals(rs.getString("CAUTO")));
				col.setDefaultvalue(rs.getString("CDEFAULT"));
				col.setKey("PRI".equals(rs.getString("CKEY")));
				switch(col.getDatatype())
				{
					case "int":
					case "integer":
					case "tinyint":
					case "smallint":
					case "mediumint":
						col.setLength(col.getPrecision());
						col.setDefaultvalue("0");
						col.setType("int"); break;
					case "bigint":
						col.setDefaultvalue("0L");
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
					case "float":
					case "double":
						col.setDefaultvalue("0F");
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
								col.setDefaultvalue("0");
								col.setType("int"); break;
							}
							col.setDefaultvalue("0L");
							col.setType("long"); break;
						}
						else
						{
							col.setLength(col.getPrecision() + (col.getDigit() > 0 ? col.getDigit() + 1 : col.getDigit()));
							col.setDefaultvalue("0F");
							col.setType("float"); break;
						}
					}
					case "time":
						col.setLength(8);
						col.setType("String"); break;
					case "date":
						col.setLength(10);
						col.setType("String"); break;
					case "datetime":
					case "timestamp":
						col.setLength(19);
						col.setType("String"); break;
					case "json":
					case "xml":
					case "char":
					case "longtext":
					case "mediumtext":
					case "text":
					case "tinytext":
					case "varchar":
						if(col.getLength() <= 0)
						{
							col.setLength(Integer.MAX_VALUE);
						}
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
