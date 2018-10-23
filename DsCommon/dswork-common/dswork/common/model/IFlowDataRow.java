package dswork.common.model;

public class IFlowDataRow
{
	private String datatype = "";
	private String tname = "";
	private String talias = "";
	private String rwx = "";
	private String value = "";

	public String getDatatype()
	{
		return datatype;
	}

	public void setDatatype(String datatype)
	{
		this.datatype = datatype;
	}

	public String getTname()
	{
		return tname;
	}

	public void setTname(String tname)
	{
		this.tname = tname;
	}

	public String getTalias()
	{
		return talias;
	}

	public void setTalias(String talias)
	{
		this.talias = talias;
	}

	public String getRwx()
	{
		return rwx;
	}

	public void setRwx(String rwx)
	{
		this.rwx = rwx;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
}
