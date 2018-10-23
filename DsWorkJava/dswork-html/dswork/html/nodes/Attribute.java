package dswork.html.nodes;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class Attribute implements Map.Entry<String, String>, Cloneable
{
	private static final String[] booleanAttributes =
	{
			"allowfullscreen", "async", "autofocus", "checked", "compact", "declare", "default", "defer", "disabled", "formnovalidate", "hidden", "inert", "ismap", "itemscope", "multiple", "muted", "nohref", "noresize", "noshade", "novalidate", "nowrap", "open", "readonly", "required", "reversed",
			"seamless", "selected", "sortable", "truespeed", "typemustmatch"
	};
	private String key;
	private String value;

	public Attribute(String key, String value)
	{
		this.key = key.trim();
		this.value = value;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key.trim();
	}

	public String getValue()
	{
		return value;
	}

	public String setValue(String value)
	{
		String old = this.value;
		this.value = value;
		return old;
	}

	public String html()
	{
		StringBuilder accum = new StringBuilder();
		try
		{
			html(accum, null);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return accum.toString();
	}

	protected void html(Appendable accum, Document out) throws IOException
	{
		accum.append(key);
		if(!shouldCollapseAttribute())
		{
			accum.append("=\"");
			accum.append(value);
			accum.append('"');
		}
	}

	@Override
	public String toString()
	{
		return html();
	}

	protected boolean isDataAttribute()
	{
		return key.startsWith(Attributes.dataPrefix) && key.length() > Attributes.dataPrefix.length();
	}

	protected final boolean shouldCollapseAttribute()
	{
		return ("".equals(value) || value.equalsIgnoreCase(key)) && isBooleanAttribute();
	}

	protected boolean isBooleanAttribute()
	{
		return Arrays.binarySearch(booleanAttributes, key) >= 0;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if(!(o instanceof Attribute))
			return false;
		Attribute attribute = (Attribute) o;
		if(key != null ? !key.equals(attribute.key) : attribute.key != null)
			return false;
		return !(value != null ? !value.equals(attribute.value) : attribute.value != null);
	}

	@Override
	public int hashCode()
	{
		int result = key != null ? key.hashCode() : 0;
		result = 31 * result + (value != null ? value.hashCode() : 0);
		return result;
	}

	@Override
	public Attribute clone()
	{
		try
		{
			return (Attribute) super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			throw new RuntimeException(e);
		}
	}
}
