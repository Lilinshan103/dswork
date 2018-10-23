/**
 * 流程实例Model
 */
package dswork.common.model;

public class IFlowPi
{
	// 主键ID(流程实例ID)
	private Long id = 0L;
	// 业务流水号
	private String ywlsh = "";
	// 申办流水号
	private String sblsh = "";
	// 流程标识
	private String alias = "";
	// 流程ID(对应deployid)
	private Long flowid = 0L;
	// 流程发布ID
	private String deployid = "";
	// 时限天数
	private Integer piday = 0;
	// 时限天数类型(0日历日,1工作日)
	private Integer pidaytype = 0;
	// 开始时间
	private String pistart = "";
	// 结束时间
	private String piend = "";
	// 挂起开始时间
	private String piupstart = "";
	// 挂起结束时间
	private String piupend = "";
	// 流程状态(1申请,2运行,3挂起,0结束)
	private Integer status = 0;
	// 承办人账号
	private String caccount = "";
	// 承办人
	private String cname = "";
	// 当前任务标识，以逗号分隔
	private String pialias = "";
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

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
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

	public Integer getPiday()
	{
		return piday;
	}

	public void setPiday(Integer piday)
	{
		this.piday = piday;
	}

	public Integer getPidaytype()
	{
		return pidaytype;
	}

	public void setPidaytype(Integer pidaytype)
	{
		this.pidaytype = pidaytype;
	}

	public String getPistart()
	{
		return pistart;
	}

	public void setPistart(String pistart)
	{
		this.pistart = pistart;
	}

	public String getPiend()
	{
		return piend;
	}

	public void setPiend(String piend)
	{
		this.piend = piend;
	}

	public String getPiupstart()
	{
		return piupstart;
	}

	public void setPiupstart(String piupstart)
	{
		this.piupstart = piupstart;
	}

	public String getPiupend()
	{
		return piupend;
	}

	public void setPiupend(String piupend)
	{
		this.piupend = piupend;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getCaccount()
	{
		return caccount;
	}

	public void setCaccount(String caccount)
	{
		this.caccount = caccount;
	}

	public String getCname()
	{
		return cname;
	}

	public void setCname(String cname)
	{
		this.cname = cname;
	}

	public String getPialias()
	{
		return pialias;
	}

	public void setPialias(String pialias)
	{
		this.pialias = pialias;
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
