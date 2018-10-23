/**
 * @描述：功能结构 
 */
package dswork.sso.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IFunc implements Serializable
{
	private static final long serialVersionUID = 1L;
	// 主键
	private Long id = 0L;
	// 上级ID(本表)
	private Long pid = 0L;
	// 所属系统ID
	private Long systemid = 0L;
	// 名称
	private String name = "";
	// 标识
	private String alias = "";
	// 对应的URI
	private String uri = "";
	// 显示图标
	private String img = "";
	// 状态(0不是菜单,1菜单,不是菜单不能添加下级)
	private Integer status = 0;
	// 排序
	private Integer seq = 0;
	// 扩展信息
	private String memo = "";
	// 资源集合
	private String resources = "";
	// 资源集合转化的List
	private List<IRes> resList = null;

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getId()
	{
		return this.id;
	}

	public void setPid(Long pid)
	{
		this.pid = (pid == null || pid <= 0) ? 0 : pid;
	}

	public Long getPid()
	{
		return this.pid;
	}

	public void setSystemid(Long systemid)
	{
		this.systemid = (systemid == null || systemid <= 0) ? 0 : systemid;
	}

	public Long getSystemid()
	{
		return this.systemid;
	}

	public void setName(String name)
	{
		this.name = String.valueOf(name).trim();
	}

	public String getName()
	{
		return this.name;
	}

	public void setAlias(String alias)
	{
		this.alias = String.valueOf(alias).trim();
	}

	public String getAlias()
	{
		return this.alias;
	}

	public void setUri(String uri)
	{
		this.uri = String.valueOf(uri).trim();
	}

	public String getUri()
	{
		return this.uri;
	}

	public void setImg(String img)
	{
		this.img = img;
	}

	public String getImg()
	{
		return this.img;
	}

	public void setSeq(Integer seq)
	{
		this.seq = seq;
	}

	public Integer getSeq()
	{
		return this.seq;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}

	public String getMemo()
	{
		return this.memo;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = (status == null || status.intValue() != 1) ? 0 : 1;
	}

	public String getResources()
	{
		return resources;
	}

	public void setResources(String resources)
	{
		this.resources = resources;
		this.resList = null;
	}

	public List<IRes> getResList()
	{
		if(resList == null)
		{
			resList = new ArrayList<IRes>();
			if(resources != null && resources.length() > 0)
			{
				String[] list = resources.split("\n", -1);
				int index = -1;
				for(String s : list)
				{
					IRes o = new IRes();
					index = s.indexOf("|");
					if(index != -1)
					{
						o.setUrl(s.substring(0, index));
						o.setParam(s.substring(index + 1));
					}
					else
					{
						o.setUrl(s);
					}
					resList.add(o);
				}
			}
		}
		return resList;
	}

	public void setResList(List<IRes> resList)
	{
		this.resList = resList;
	}
}
