/**
 * 组织机构Model
 */
package dswork.common.model;

import java.util.ArrayList;
import java.util.List;

public class IOrgTree
{
	// 部门ID
	private String id = "";
	// 上级ID(本表)
	private String pid = "";
	// 名称
	private String name = "";
	// 类型(2单位,1部门,0岗位)
	private Integer status = 0;
	// 是否单位部门
	private boolean isParent = false;
	// 子数据信息
	private List<IOrgTree> list = new ArrayList<IOrgTree>();

	public IOrgTree(IOrg org)
	{
		this.id = org.getId();
		this.pid = (org.getPid() == null || org.getPid().equals("")) ? "0" : org.getPid();
		this.name = org.getName();
		this.status = org.getStatus();
		this.isParent = org.getStatus() > 0;
	}

	public String getId()
	{
		return id;
	}

	public String getPid()
	{
		return pid;
	}

	public String getName()
	{
		return name;
	}

	public Integer getStatus()
	{
		return status;
	}

	public boolean isParent()
	{
		return isParent;
	}

	public List<IOrgTree> getList()
	{
		return list;
	}
	
	public void addListItem(IOrgTree model)
	{
		this.list.add(model);
	}
	
	public void clearList()
	{
		this.list.clear();
	}

	@Override
	public String toString()
	{
		try
		{
			StringBuilder sb = new StringBuilder(100);
			sb.append("{\"id\":").append(id)
					.append(",\"pid\":").append(pid)
					.append(",\"status\":").append(status)
					.append(",\"isParent\":").append(isParent ? "true" : "false")
					.append(",\"name\":\"").append(name.trim().replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\\\"")).append("\"")
				;
			if(list.size() > 0)
			{
				sb.append(",\"list\":").append(this.list.toString());
			}
			sb.append("}");
			return sb.toString();
		}
		catch(Exception e)
		{
			return "{\"id\":0,\"pid\":-1,\"status\":-1,\"isParent\":false,\"name\":\"\",\"list\":[]}";
		}
	}
}
