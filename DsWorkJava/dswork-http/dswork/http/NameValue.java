package dswork.http;

/**
 * 自定义NameValue
 * @author skey
 * @version 1.0
 */
public class NameValue
{
	private String name;
	private String value;

	/**
	 * 构造方法
	 * @param name String
	 * @param value String
	 */
	public NameValue(String name, String value)
	{
		this.name = String.valueOf(name);
		this.value = value;
	}
	
	public void setName(String name)
	{
		this.name = String.valueOf(name);
	}

	public String getName()
	{
		return this.name;
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return this.value;
	}
}
