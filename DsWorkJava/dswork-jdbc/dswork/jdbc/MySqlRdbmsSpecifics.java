package dswork.jdbc;

import java.text.SimpleDateFormat;

class MySqlRdbmsSpecifics extends RdbmsSpecifics
{
	MySqlRdbmsSpecifics()
	{
		super();
	}

	String formatParameterObject(Object object)
	{
		if(object instanceof java.sql.Time)
		{
			return "'" + new SimpleDateFormat("HH:mm:ss").format(object) + "'";
		}
		else if(object instanceof java.sql.Date)
		{
			return "'" + new SimpleDateFormat("yyyy-MM-dd").format(object) + "'";
		}
		else if(object instanceof java.util.Date) // (includes java.sql.Timestamp)
		{
			return "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(object) + "'";
		}
		else
		{
			return super.formatParameterObject(object);
		}
	}
}
