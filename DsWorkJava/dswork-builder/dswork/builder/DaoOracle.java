package dswork.builder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoOracle extends Dao
{
	private static final String SQL_TABLE_PK = "select COLUMN_NAME from USER_CONSTRAINTS c, USER_CONS_COLUMNS col where c.CONSTRAINT_NAME=col.CONSTRAINT_NAME and c.CONSTRAINT_TYPE='P'and c.TABLE_NAME='%s'";
	private static final String SQL_TABLE_COMMENT = "select COMMENTS as CCOMMEN from USER_TAB_COMMENTS  where TABLE_TYPE='TABLE' and TABLE_NAME ='%s'";
	private static final String SQL_TABLE_COLUMN = "select "
			+ "A.COLUMN_NAME as CNAME, "
			+ "A.DATA_TYPE as CDATATYPE, "
			+ "A.DATA_LENGTH as CLENGTH, "
			+ "A.DATA_PRECISION as CPRECISION, "
			+ "A.DATA_SCALE as CSCALE, "
			+ "B.COMMENTS as CCOMMENT "
			+ "from USER_TAB_COLUMNS A, USER_COL_COMMENTS B  "
			+ "where A.COLUMN_NAME=B.COLUMN_NAME and A.TABLE_NAME=B.TABLE_NAME and A.TABLE_NAME='%s' ";
//	private static final String SQL_ALL_TABLES = "select TABLE_NAME from USER_TABLES where STATUS='VALID'";
	// A.DATA_DEFAULT
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
				col.setNullable(true);// oracle没有空字符串，只有null
				col.setPrecision(rs.getInt("CPRECISION"));
				col.setDigit(rs.getInt("CSCALE"));
				col.setComment(rs.getString("CCOMMENT"));
				col.setAuto(false);// oracle没有自增列
				// col.setKey();
				switch(col.getDatatype())
				{
					case "FLOAT":
						col.setType("float"); break;
					case "NUMBER":
					{
						if(col.getDigit() == 0)
						{
							if(col.getPrecision() < 11)
							{
								col.setType("int"); break;
							}
							col.setType("long"); break;
						}
						else
						{
							col.setType("float"); break;
						}
					}
					case "DATE":
						col.setType("date"); break;
					case "CHAR":
					case "NCHAR":
					case "VARCHAR":
					case "VARCHAR2":
					case "NVARCHAR2":
					case "CLOB":
					case "NCLOB":
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
