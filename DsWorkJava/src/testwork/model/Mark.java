/**
 * 样例Model
 */
package testwork.model;

public class Mark
{
	// 主键
	private Long id = 0L;
	// 标题
	private Long demoid = 0L;
	// 内容
	private String content = "";
	// 创建时间
	private String foundtime = "";

	public Long getId()
	{
		return id;
	}

	public Mark setId(Long id)
	{
		this.id = id;
		return this;
	}

	public Long getDemoid()
	{
		return demoid;
	}

	public Mark setDemoid(Long demoid)
	{
		this.demoid = demoid;
		return this;
	}

	public String getContent()
	{
		return content;
	}

	public Mark setContent(String content)
	{
		this.content = content;
		return this;
	}

	public String getFoundtime()
	{
		return foundtime;
	}

	public Mark setFoundtime(String foundtime)
	{
		this.foundtime = foundtime;
		return this;
	}
}
