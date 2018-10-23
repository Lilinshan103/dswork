/**
 * 流程待办事项Model
 */
package dswork.common.model;

public class IFlowWaiting
{
	// 主键ID
	private Long id = 0L;
	// 实例ID
	private Long piid = 0L;
	// 业务流水号
	private String ywlsh = "";
	// 申办流水号
	private String sblsh = "";
	// 流程ID
	private Long flowid = 0L;
	// 流程名称
	private String flowname = "";
	// 任务标识
	private String talias = "";
	// 任务名称
	private String tname = "";
	// 合并任务个数(只有一个任务时等于1，其余大于1)
	private Integer tcount = 0;
	// 上级任务(从哪过来的)
	private String tprev = "";
	// 下级任务（以逗号分隔节点标识，以|线分隔分支任务）翻译成代码可理解为：[(可选1 || 可选2 || 可选3) && (可选A || 可选B)]
	private String tnext = "";
	// 任务开始时间
	private String tstart = "";
	// 经办人
	private String tuser = "";
	// 候选经办人
	private String tusers = "";
	// 参数
	private String tmemo = "";
	// 处理接口类
	private String tinterface = "";
	// 数据结构
	private String datatable = "";

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getPiid()
	{
		return piid;
	}

	public void setPiid(Long piid)
	{
		this.piid = piid;
	}

	public String getYwlsh()
	{
		return ywlsh;
	}

	public void setYwlsh(String ywlsh)
	{
		this.ywlsh = ywlsh;
	}

	public String getSblsh()
	{
		return sblsh;
	}

	public void setSblsh(String sblsh)
	{
		this.sblsh = sblsh;
	}

	public Long getFlowid()
	{
		return flowid;
	}

	public void setFlowid(Long flowid)
	{
		this.flowid = flowid;
	}

	public String getFlowname()
	{
		return flowname;
	}

	public void setFlowname(String flowname)
	{
		this.flowname = flowname;
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
		this.tcount = tcount;
	}

	public String getTprev()
	{
		return tprev;
	}

	public void setTprev(String tprev)
	{
		this.tprev = tprev;
	}

	public String getTnext()
	{
		return tnext;
	}

	public void setTnext(String tnext)
	{
		this.tnext = tnext;
	}

	public String getTstart()
	{
		return tstart;
	}

	public void setTstart(String tstart)
	{
		this.tstart = tstart;
	}

	public String getTuser()
	{
		return tuser;
	}

	public void setTuser(String tuser)
	{
		this.tuser = tuser;
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

	public String getTinterface()
	{
		return tinterface;
	}

	public void setTinterface(String tinterface)
	{
		this.tinterface = tinterface;
	}

	public String getDatatable()
	{
		return datatable;
	}

	public void setDatatable(String datatable)
	{
		this.datatable = datatable;
	}
}
