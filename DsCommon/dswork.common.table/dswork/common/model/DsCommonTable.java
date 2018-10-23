/**
 * 数据表定义Model
 */
package dswork.common.model;
public class DsCommonTable
{
	// ID
	private Long id = 0L;
	// 分类
	private long categoryid = 0L;
	// 名称
	private String name = "";
	// 标识【对应数据表名】
	private String alias = "";
	//类别名称
	private String categoryName = "";
	// 状态(1启用,0禁用)
	private int status = 0;
	// 排序
	private int seq = 0;
	// 数据结构
	private String datatable = "";
	// 扩展信息
	private String memo = "";

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public long getCategoryid()
	{
		return categoryid;
	}

	public void setCategoryid(long categoryid)
	{
		this.categoryid = categoryid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public int getSeq()
	{
		return seq;
	}

	public void setSeq(int seq)
	{
		this.seq = seq;
	}

	public String getDatatable()
	{
		return datatable;
	}

	public void setDatatable(String datatable)
	{
		this.datatable = datatable;
	}

	public String getMemo()
	{
		return memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}
}