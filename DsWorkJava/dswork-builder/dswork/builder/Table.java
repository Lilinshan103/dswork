package dswork.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Table
{
	public class Column
	{
		private String name = "";// 列名
		private String type = "";// 类型
		private String datatype = "";// 数据库类型
		private long length = 0;// 长度
		private boolean nullable = true;
		private int precision = 0;// 整数位
		private int digit = 0;// 小数位
		private String comment = "";// 列注释
		private boolean auto = false;// 是否自增
		private String defaultvalue = "";// 类型
		private boolean key = false;// 是否主键

		public String getNameLowerCamel()
		{
			return Builder.lowerCamel(name);
		}// 小驼峰命名

		public String getNameUpperCamel()
		{
			return Builder.upperCamel(name);
		}// 大驼峰命名

		public String getNameLowerCase()
		{
			return name.toLowerCase(Locale.ENGLISH);
		}

		public String getNameUpperCase()
		{
			return name.toUpperCase(Locale.ENGLISH);
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getType()
		{
			return type;
		}

		public void setType(String type)
		{
			this.type = type;
		}

		public String getTypeLowerCamel()
		{
			return Builder.lowerCamel(type);
		}// 小驼峰命名

		public String getTypeUpperCamel()
		{
			return Builder.upperCamel(type);
		}// 大驼峰命名

		public String getDatatype()
		{
			return datatype;
		}

		public void setDatatype(String datatype)
		{
			this.datatype = datatype;
		}

		public String getComment()
		{
			return comment;
		}

		public void setComment(String comment)
		{
			this.comment = comment;
		}

		public long getLength()
		{
			return length;
		}

		public void setLength(long length)
		{
			this.length = length;
		}

		public boolean isKey()
		{
			return key;
		}

		public void setKey(boolean key)
		{
			this.key = key;
		}

		public boolean isAuto()
		{
			return auto;
		}

		public void setAuto(boolean auto)
		{
			this.auto = auto;
		}

		public int getPrecision()
		{
			return precision;
		}

		public void setPrecision(int precision)
		{
			this.precision = precision;
		}

		public int getDigit()
		{
			return digit;
		}

		public void setDigit(int digit)
		{
			this.digit = digit;
		}

		public boolean isNullable()
		{
			return nullable;
		}

		public void setNullable(boolean nullable)
		{
			this.nullable = nullable;
		}

		public String getDefaultvalue()
		{
			return defaultvalue;
		}

		public void setDefaultvalue(String defaultvalue)
		{
			this.defaultvalue = defaultvalue;
		}
	}
	private String name = "";// 表名
	private String comment = "";// 表注释
	private List<Column> column = new ArrayList<Column>();// 表列list
	private List<Column> columnKey = null;// 表列list
	private List<Column> columnNokey = null;// 表列list

	public Column addColumn()
	{
		Column c = new Column();
		column.add(c);
		return c;
	}

	public String getNameLowerCamel()
	{
		return Builder.lowerCamel(name);
	}// 小驼峰命名

	public String getNameUpperCamel()
	{
		return Builder.upperCamel(name);
	}// 大驼峰命名

	public String getNameLowerCase()
	{
		return name.toLowerCase(Locale.ENGLISH);
	}

	public String getNameUpperCase()
	{
		return name.toUpperCase(Locale.ENGLISH);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}
	
	public List<Column> getColumn()
	{
		return column;
	}
	
	public List<Column> getColumnKey()
	{
		if(columnKey == null)
		{
			columnKey = new ArrayList<Column>();
			for(Column c : column)
			{
				if(c.key)
				{
					columnKey.add(c);
				}
			}
		}
		return columnKey;
	}
	
	public List<Column> getColumnNokey()
	{
		if(columnNokey == null)
		{
			columnNokey = new ArrayList<Column>();
			for(Column c : column)
			{
				if(!c.key)
				{
					columnNokey.add(c);
				}
			}
		}
		return columnNokey;
	}
}
