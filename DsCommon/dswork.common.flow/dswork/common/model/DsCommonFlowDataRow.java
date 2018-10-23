package dswork.common.model;

public class DsCommonFlowDataRow
{
	private String datatype = "";
	private String tname = "";
	private String talias = "";
	private String rwx = "";

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
}
