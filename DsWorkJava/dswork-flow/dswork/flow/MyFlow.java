package dswork.flow;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dswork.flow.dom.MyLine;
import dswork.flow.dom.MyNode;
import dswork.flow.util.DomUtil;

public class MyFlow
{
	private List<MyNode> tasks;
	private List<MyLine> lines;
//	private String alias = "";
//	private String name = "";
	private int width = 0;
	private int height = 0;
	private boolean prettyPrint = false;
	
	public MyFlow(String xmlString) throws Exception
	{
		DomUtil util = new DomUtil(xmlString);
		this.tasks = util.getTasks();
		this.lines = util.getLines();
//		this.alias = util.getAlias();
//		this.name = util.getName();
		this.width = util.getWidth();
		this.height = util.getHeight();
	}
	
	public List<MyNode> getTasks()
	{
		return tasks;
	}

	public void setTasks(List<MyNode> tasks)
	{
		this.tasks = tasks;
	}

	public List<MyLine> getLines()
	{
		return lines;
	}

	public void setLines(List<MyLine> lines)
	{
		this.lines = lines;
	}

//	public String getAlias()
//	{
//		return alias;
//	}
//
//	public void setAlias(String alias)
//	{
//		this.alias = alias;
//	}
//
//	public String getName()
//	{
//		return name;
//	}
//
//	public void setName(String name)
//	{
//		this.name = name;
//	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public boolean prettyPrint()
	{
		return prettyPrint;
	}

	public void prettyPrint(boolean pretty)
	{
		prettyPrint = pretty;
	}

	public String toSvg()
	{
		String nt = prettyPrint ? "\n\t" : "";
		String n = prettyPrint ? "\n" : "";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<svg width=\"").append(width).append("\" height=\"").append(height).append("\">");
			//.append("\" alias=\"").append(getAlias()).append("\" name=\"").append(getName())
			for(MyNode node : tasks)
			{
				node.toSvg(sb.append(nt));
			}
			for(MyLine line : lines)
			{
				line.toSvg(sb.append(nt));
			}
		sb.append(n).append("</svg>");
		return sb.toString();
	}
	
	public String toXml()
	{
		String nt = prettyPrint ? "\n\t" : "";
		String n = prettyPrint ? "\n" : "";
		StringBuilder sb = new StringBuilder(512);
		// sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append(n).append("<flow>");// alias=\"").append(getAlias()).append("\" name=\"").append(getName()).append("\"
		for(MyNode node : tasks)
		{
			sb.append(n);
			StringBuilder sbl = new StringBuilder();
			for(MyLine line : lines)
			{
				if(line.getFrom().getAlias().equals(node.getAlias()))
				{
					line.toXml(sbl.append(nt));
				}
			}
			sbl.append(n);
			node.toXml(sb, sbl.toString());
		}
		sb.append(n).append("</flow>");
		return sb.toString();
	}
	
	public String getNextTask(String alias)
	{
		String next = "";
		if(!"end".equals(alias))
		{
			List<MyLine> list = new ArrayList<MyLine>();
			for(int i = 0; i < lines.size(); i++)
			{
				MyLine line = lines.get(i);
				if(line.getFrom() != null && alias.equals(line.getFrom().getAlias()))
				{
					list.add(line);
				}
			}
			Map<String, String> map = new LinkedHashMap<String, String>();
			for(int i = 0; i < list.size(); i++)
			{
				MyLine line = list.get(i);
				if(map.get(line.getForks()) == null)
				{
					map.put(line.getForks(), line.getTo().getAlias());
				}
				else
				{
					map.put(line.getForks(), map.get(line.getForks()) + "," + line.getTo().getAlias());
				}
			}
			for(Map.Entry<String, String> entry : map.entrySet())
			{
				if(next.length() == 0)
				{
					next = entry.getValue();
				}
				else
				{
					next = next + "|" + entry.getValue();
				}
			}
		}
		return next;
	}
}
