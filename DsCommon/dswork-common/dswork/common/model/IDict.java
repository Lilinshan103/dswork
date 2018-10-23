/**
 * 字典项Model
 */
package dswork.common.model;

public class IDict
{
	// 标识
	private String alias = "";
	// 名称
	private String label = "";
	// 上级标识
	private String pid = "0";
	// 状态(1树叉,0树叶)
	private Integer status = 0;

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = String.valueOf(label).replaceAll("\r", "").replaceAll("\n", "");
	}

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public String getPid()
	{
		return pid;
	}

	public void setPid(String pid)
	{
		this.pid = pid;// 当pid为null时，mybatis并不会进行set方法
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	@Override
	public String toString()
	{
		try
		{
			return new StringBuilder().append("{id:\"").append(alias)
					.append("\",pid:\"").append(String.valueOf(pid))
					.append("\",status:").append(status)
					.append(",isParent:").append((1 == status) ? "true" : "false")
					.append(",name:\"").append(String.valueOf(label).replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\\\""))
					.append("\"}").toString();
		}
		catch(Exception e)
		{
			return "{id:0,pid:-1,status:0,isParent:true,name:\"\"}";
		}
	}
}
