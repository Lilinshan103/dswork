/**
 * 字典项Model
 */
package dswork.common.model;

public class DsCommonDictData
{
	// 主键
	private Long id = 0L;
	// 上级ID(本表,所属字典项)
	private Long pid = 0L;
	// 引用名
	private String name = "";
	// 名称
	private String label = "";
	// 标识
	private String alias = "";
	// 状态(1树叉,0树叶)
	private Integer status = 0;
	// 排序
	private Long seq = 0L;
	// 备注
	private String memo = "";

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getPid()
	{
		return pid;
	}

	public void setPid(Long pid)
	{
		this.pid = (pid == null || pid <= 0) ? 0 : pid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = String.valueOf(name).replaceAll("\r", "").replaceAll("\n", "");
	}

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

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public Long getSeq()
	{
		return seq;
	}

	public void setSeq(Long seq)
	{
		this.seq = seq;
	}

	public String getMemo()
	{
		return memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}

	@Override
	public String toString()
	{
		try
		{
			return new StringBuilder().append("{\"id\":").append(id).append(",\"pid\":").append(pid != null && pid > 0 ? pid : 0).append(",\"status\":").append(status).append(",\"isParent\":").append((1 == status) ? "true" : "false").append(",\"name\":\"").append(
				String.valueOf(label).replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\\\"")).append("\"}").toString();
		}
		catch(Exception e)
		{
			return "{\"id\":0,\"pid\":-1,\"status\":0,\"isParent\":true,\"name\":\"\"}";
		}
	}
}
