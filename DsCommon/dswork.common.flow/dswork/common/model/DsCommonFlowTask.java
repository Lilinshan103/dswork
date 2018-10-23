/**
 * 流程任务Model
 */
package dswork.common.model;

public class DsCommonFlowTask
{
	// 主键
	private Long id = 0L;
	// 流程ID
	private Long flowid = 0L;
	// 流程发布ID，当前版本此值为空
	private String deployid = "";
	// 节点标识(start开始，end结束)
	private String talias = "";
	// 节点名称
	private String tname = "";
	// 合并任务个数(只有一个任务时等于1，其余大于1)
	private Integer tcount = 0;
	// 下级任务(以逗号分隔节点标识，以|线分隔分支任务)
	private String tnext = "";
	// 当前任务的用户ID(以逗号分隔节点标识)
	private String tusers = "";
	// 参数
	private String tmemo = "";
	// 数据结构
	private String datatable = "";
	//至少合并会签个数(不需要会签时值为-1)
	private Integer subcount = -1;
	// 当前会签的用户ID(以逗号分隔可选用户，以|线分隔并行用户)
	private String subusers = "";

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getFlowid()
	{
		return flowid;
	}

	public void setFlowid(Long flowid)
	{
		this.flowid = flowid;
	}

	public String getDeployid()
	{
		return deployid;
	}

	public void setDeployid(String deployid)
	{
		this.deployid = deployid;
	}

	public String getTalias()
	{
		return talias;
	}

	public void setTalias(String talias)
	{
		this.talias = talias;
	}

	public String getTname()
	{
		return tname;
	}

	public void setTname(String tname)
	{
		this.tname = tname;
	}

	public Integer getTcount()
	{
		return tcount;
	}

	public void setTcount(Integer tcount)
	{
		this.tcount = tcount == null || tcount <= 1 ? 1 : tcount;
	}

	public String getTnext()
	{
		return tnext;
	}

	public void setTnext(String tnext)
	{
		this.tnext = tnext;
	}

	public String getTusers()
	{
		return tusers;
	}

	public void setTusers(String tusers)
	{
		this.tusers = tusers;
	}

	public String getTmemo()
	{
		return tmemo;
	}

	public void setTmemo(String tmemo)
	{
		this.tmemo = tmemo;
	}

	public String getDatatable()
	{
		return datatable;
	}

	public void setDatatable(String datatable)
	{
		this.datatable = datatable;
	}

	public Integer getSubcount()
	{
		return subcount;
	}

	public void setSubcount(Integer subcount)
	{
		this.subcount = subcount;
	}

	public String getSubusers()
	{
		return subusers;
	}

	public void setSubusers(String subusers)
	{
		this.subusers = subusers;
	}
}
