/**
 * 数据表分类Model
 */
package dswork.common.model;
public class DsCommonTableCategory
{
	// ID
	private Long id = 0L;
	// 名称
	private String name = "";
	// 排序
	private int seq = 0;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getSeq()
	{
		return seq;
	}

	public void setSeq(int seq)
	{
		this.seq = seq;
	}
}