/**
 * 样例Model，改造了试验性set返回值，测试中
 */
package testwork.model;

public class Demo
{
	// 主键
	private Long id = 0L;
	// 标题
	private String title = "";
	// 内容
	private String content = "";
	// 创建时间
	private String foundtime = "";

	public Long getId()
	{
		return id;
	}

	public Demo setId(Long id)
	{
		this.id = id;
		return this;
	}

	public String getTitle()
	{
		return title;
	}

	public Demo setTitle(String title)
	{
		this.title = title;
		return this;
	}

	public String getContent()
	{
		return content;
	}

	public Demo setContent(String content)
	{
		this.content = content;
		return this;
	}

	public String getFoundtime()
	{
		return foundtime;
	}

	public Demo setFoundtime(String foundtime)
	{
		this.foundtime = foundtime;
		return this;
	}
}
