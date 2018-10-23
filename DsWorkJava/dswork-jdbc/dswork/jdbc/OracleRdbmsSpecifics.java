package dswork.jdbc;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

class OracleRdbmsSpecifics extends RdbmsSpecifics
{
	OracleRdbmsSpecifics()
	{
		super();
	}

	String formatParameterObject(Object object)
	{
		if(object instanceof Timestamp)
		{
			return "to_timestamp('" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(object) + "', 'yyyy-MM-dd hh24:mi:ss.ff3')";
		}
		else if(object instanceof Date)
		{
			return "to_date('" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(object) + "', 'yyyy-MM-dd hh24:mi:ss')";
		}
		else
		{
			return super.formatParameterObject(object);
		}
	}
}
