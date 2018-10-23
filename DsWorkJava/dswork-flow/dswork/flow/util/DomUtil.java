package dswork.flow.util;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import dswork.flow.dom.MyLine;
import dswork.flow.dom.MyNode;
import dswork.flow.dom.MyPoint;

public class DomUtil
{
	private List<MyNode> tasks = new ArrayList<MyNode>();
	private List<MyLine> lines = new ArrayList<MyLine>();
	private String alias = "";
	private String name = "";
	private int width = 0;
	private int height = 0;

	public List<MyNode> getTasks()
	{
		return tasks;
	}

	public List<MyLine> getLines()
	{
		return lines;
	}

	public String getAlias()
	{
		return alias;
	}

	public String getName()
	{
		return name;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	private Map<String, MyNode> taskMap = new HashMap<String, MyNode>();
	private Map<MyLine, String> toMap = new HashMap<MyLine, String>();
	
	public DomUtil(String xmlString) throws Exception
	{
		try
		{
			parseFlow((Element)parseBuilder().parse(new ByteArrayInputStream(xmlString.getBytes("UTF-8"))).getElementsByTagName("flow").item(0));
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	private DocumentBuilder parseBuilder() throws ParserConfigurationException
	{
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder;
	}

	private void parseFlow(Element e)
	{
		NodeList nl = e.getElementsByTagName("task");
		for(int i = 0; i < nl.getLength(); i++)
		{
			MyNode node = parseNode((Element)nl.item(i));
			taskMap.put(node.getAlias(), node);
			tasks.add(node);
		}
		for(Map.Entry<MyLine, String> entry : toMap.entrySet())
		{
			entry.getKey().setTo(taskMap.get(entry.getValue()));
		}
		width += 5;
		height += 5;
	}

	private MyNode parseNode(Element e)
	{
		MyNode node = new MyNode();
		node.setAlias(e.getAttribute("alias"));
		node.setName(e.getAttribute("name"));
		node.setUsers(e.getAttribute("users"));
		int count = 1;
		try
		{
			count  = Integer.parseInt(e.getAttribute("count"));
		}
		catch(Exception ex)
		{
			count = 1;
		}
		node.setCount(count);
		int subcount = -1;
		try
		{
			subcount  = Integer.parseInt(e.getAttribute("subcount"));
		}
		catch(Exception ex)
		{
			subcount = -1;
		}
		node.setSubcount(subcount);
		node.setSubusers(e.getAttribute("subusers"));
		if(e.getAttribute("color") != null && !"".equals(e.getAttribute("color")))
		{
			node.setColor(e.getAttribute("color"));
		}
		else
		{
			if("start".equals(node.getAlias()))
			{
				node.setColor(MyNode.COLOR_START);
			}
			else if("end".equals(node.getAlias()))
			{
				node.setColor(MyNode.COLOR_END);
			}
			else
			{
				node.setColor(MyNode.COLOR_TASK);
			}
		}
		
		node.setG(e.getAttribute("g"));
		if(width < node.getX() + node.getWidth())
		{
			width = node.getX() + node.getWidth();
		}
		if(height < node.getY() + node.getHeight())
		{
			height = node.getY() + node.getHeight();
		}
		NodeList nl = e.getElementsByTagName("line");
		for(int i = 0; i < nl.getLength(); i++)
		{
			MyLine line = parseLine((Element)nl.item(i));
			line.setFrom(node);
			lines.add(line);
		}
		return node;
	}

	private MyLine parseLine(Element e)
	{
		MyLine line = new MyLine();
		toMap.put(line, e.getAttribute("to"));
		if(e.getAttribute("color") != null && !"".equals(e.getAttribute("color")))
		{
			line.setColor(e.getAttribute("color"));
		}
		else
		{
			line.setColor(MyLine.COLOR_LINE);
		}
		line.setForks(e.getAttribute("forks"));
		line.setStyle(0);
		line.setPoints(e.getAttribute("p"));
		for(MyPoint p : line.getPointsList())
		{
			if(width < p.x)
			{
				width = p.x;
			}
			if(height < p.y)
			{
				height = p.y;
			}
		}
		return line;
	}
}
