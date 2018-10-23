package dswork.flow.dom;

import java.util.LinkedHashMap;
import java.util.Map;

import dswork.flow.util.SvgUtil;

public class MyNode
{
	public static final String COLOR_START = "7BB538";
	public static final String COLOR_END = "FF6666";
	public static final String COLOR_TASK = "6699CC";
	private String name = "";
	private String alias = "";
	private String users = "";
	private int count = 1;
	private int subcount = -1;
	private String subusers = "";
	private String color = "";
	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;
	private MyPoint center = new MyPoint(0, 0);

	public String getG()
	{
		return new StringBuilder().append(x).append(",").append(y).append(",").append(width).append(",").append(height).toString();
	}

	public void setG(String g)
	{
		if(g != null)
		{
			String[] arr = g.split(",", -1);
			if(arr.length == 4)
			{
				x = Integer.parseInt(arr[0]);
				y = Integer.parseInt(arr[1]);
				width = Integer.parseInt(arr[2]);
				height = Integer.parseInt(arr[3]);
				initCenter();
			}
			else if(arr.length == 2)
			{
				x = Integer.parseInt(arr[0]);
				y = Integer.parseInt(arr[1]);
				width = 100;
				height = 50;
				initCenter();
			}
		}
	}

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUsers()
	{
		return users;
	}

	public void setUsers(String users)
	{
		this.users = users;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count <= 1 ? 1 : count;
	}

	public String getSubusers()
	{
		return subusers;
	}

	public void setSubusers(String subusers)
	{
		this.subusers = subusers;
	}

	public int getSubcount()
	{
		return subcount;
	}

	public void setSubcount(int subcount)
	{
		this.subcount = subcount <= -1 ? -1 : subcount;
	}

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
		initCenter();
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
		initCenter();
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
		initCenter();
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
		initCenter();
	}

	private void initCenter()
	{
		center.x = x + (width / 2);
		center.y = y + (height / 2);
	}

	public MyPoint getCenter()
	{
		return center;
	}

	public void toSvg(StringBuilder sb)
	{
		sb.append("<g>");
		if("start".equals(getAlias()))
		{
			sb.append(SvgUtil.getCircle(center.x, center.y, (width / 2), "#" + getColor(), 2, "#FFFFFF")).append(SvgUtil.getCircle(center.x, center.y, (width / 2) - 4, "", 0, "#" + getColor()));
		}
		else if("end".equals(getAlias()))
		{
			sb.append(SvgUtil.getCircle(center.x, center.y, (width / 2), "#" + getColor(), 2, "#FFFFFF")).append(SvgUtil.getCircle(center.x, center.y, (width / 2) - 4, "", 0, "#" + getColor()));
		}
		else
		{
			sb.append(SvgUtil.getRect(x, y, width, height, "#" + color, 1, "#CCEEFF")).append(SvgUtil.getTextMult(getName(), x, y, width, height, 14));
		}
		sb.append("</g>");
	}

	public void toXml(StringBuilder sb, String allLineNodeText)
	{
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("alias", getAlias());
		map.put("name", getName());
		map.put("users", getUsers());
		if(getCount() > 1)
		{
			map.put("count", getCount() + "");
		}
		if(getSubcount() > -1)
		{
			map.put("subcount", getSubcount() + "");
			map.put("subusers", getSubusers());
		}
		if(("start".equals(getAlias())))
		{
			if(!COLOR_START.equals(getColor()))
			{
				map.put("color", getColor());
			}
		}
		else if("end".equals(getAlias()))
		{
			if(!COLOR_END.equals(getColor()))
			{
				map.put("color", getColor());
			}
		}
		else
		{
			if(!COLOR_TASK.equals(getColor()))
			{
				map.put("color", getColor());
			}
		}
		map.put("g", getG());
		sb.append(SvgUtil.getTag("task", map, allLineNodeText));
	}
}
