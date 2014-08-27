/**
 * 栏目Model
 */
package dswork.ep.model;

import java.util.ArrayList;
import java.util.List;

public class DsCmsCategory
{
	// 主键
	private Long id = 0L;
	// 父ID
	private Long pid = 0L;
	// 站点ID
	private Long siteid = 0L;
	// 栏目名称
	private String name = "";
	// 目录名称
	private String folder = "";
	// 类型(0列表，1单页，2外链)
	private Integer status = 0;
	// 链接
	private String url = "";
	// 图片
	private String img = "";
	// 网站模板
	private String viewsite = "";
	// APP模板
	private String viewapp = "";
	// 排序
	private Integer seq = 0;
	// 级别
	private int level = 0;
	// 树形标识
	private String label = "";
	// 子栏目
	private List<DsCmsCategory> list = new ArrayList<DsCmsCategory>();

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

	public Long getSiteid()
	{
		return siteid;
	}

	public void setSiteid(Long siteid)
	{
		this.siteid = siteid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getFolder()
	{
		return folder;
	}

	public void setFolder(String folder)
	{
		this.folder = folder;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getImg()
	{
		return img;
	}

	public void setImg(String img)
	{
		this.img = img;
	}

	public String getViewsite()
	{
		return viewsite;
	}

	public void setViewsite(String viewsite)
	{
		this.viewsite = viewsite;
	}

	public String getViewapp()
	{
		return viewapp;
	}

	public void setViewapp(String viewapp)
	{
		this.viewapp = viewapp;
	}

	public Integer getSeq()
	{
		return seq;
	}

	public void setSeq(Integer seq)
	{
		this.seq = seq;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public List<DsCmsCategory> getList()
	{
		return list;
	}

	public void clearList()
	{
		this.list.clear();
	}

	public void add(DsCmsCategory item)
	{
		this.list.add(item);
	}
}
