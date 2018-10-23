/**
 * 流程执行明细Model
 */
package dswork.common.model;

public class IFlowPiData
{
	// 主键ID
	private Long id = 0L;
	// 流程实例ID
	private Long piid = 0L;
	// 上级任务(从哪过来的)
	private String tprev = "";
	// 任务标识
	private String talias = "";
	// 任务名称
	private String tname = "";
	// 状态(0已处理,1代办,2挂起,3取消挂起)
	private Integer status = 0;
	// 经办人ID
	private String paccount = "";
	// 经办人姓名
	private String pname = "";
	// 经办时间
	private String ptime = "";
	// 经办类型(0拒绝,1同意等等)
	private String ptype = "";
	// 意见
	private String memo = "";
	// 表单数据
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

	public String getTprev()
	{
		return tprev;
	}

	public void setTprev(String tprev)
	{
		this.tprev = tprev;
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

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getPaccount()
	{
		return paccount;
	}

	public void setPaccount(String paccount)
	{
		this.paccount = paccount;
	}

	public String getPname()
	{
		return pname;
	}

	public void setPname(String pname)
	{
		this.pname = pname;
	}

	public String getPtime()
	{
		return ptime;
	}

	public void setPtime(String ptime)
	{
		this.ptime = ptime;
	}

	public String getPtype()
	{
		return ptype;
	}

	public void setPtype(String ptype)
	{
		this.ptype = ptype;
	}

	public String getMemo()
	{
		return memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
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
