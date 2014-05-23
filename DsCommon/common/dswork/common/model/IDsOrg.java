/**
 * 组织机构Model
 */
package dswork.common.model;

public class IDsOrg
{
	// 部门ID
	private String id = "";
	// 上级ID(本表,所属组织)
	private String pid = "";
	// 名称
	private String name = "";
	// 类型(2单位,1部门,0岗位)
	private Integer status = 0;

	public void setName(String name)
	{
		this.name = String.valueOf(name).trim();
	}

	public String getName()
	{
		return this.name;
	}

	public String getPid()
	{
		return pid;
	}

	public void setPid(String alias)
	{
		this.pid = alias;
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
			return new StringBuilder().append("{id:").append(id).append(",pid:").append(pid).append(",status:").append(status).append(",isParent:").append(status>0?"true":"false").append(",name:\"").append(name.trim().replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\\\"")).append("\"}").toString();
		}
		catch (Exception e)
		{
			return "{id:0,pid:-1,status:-1,isParent:false,name:\"\"}";
		}
	}
}
