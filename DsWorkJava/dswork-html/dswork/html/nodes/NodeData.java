package dswork.html.nodes;

import java.io.IOException;

/**
 * style、script等标签
 */
public class NodeData extends Node
{
	private static final String DATA_KEY = "data";

	public NodeData(String data)
	{
		super(new Attributes());
		attributes.put(DATA_KEY, data);
		//System.out.println("NodeData:" + data);
	}

	public String nodeName()
	{
		return "#data";
	}

	public String getWholeData()
	{
		return attributes.get(DATA_KEY);
	}

	public NodeData setWholeData(String data)
	{
		attributes.put(DATA_KEY, data);
		return this;
	}

	void outerHtmlHead(Appendable accum, int depth, Document out) throws IOException
	{
		accum.append(getWholeData());
	}

	void outerHtmlTail(Appendable accum, int depth, Document out)
	{
	}

	@Override
	public String toString()
	{
		return outerHtml();
	}
}
