package dswork.html.nodes;

import java.io.IOException;

/**
 * 注释标签
 */
public class NodeComment extends Node
{
	private static final String COMMENT_KEY = "comment";

	public NodeComment(String data)
	{
		super(new Attributes());
		attributes.put(COMMENT_KEY, data);
		//System.out.println("NodeComment:" + data);
	}

	public String nodeName()
	{
		return "#comment";
	}

	public String getData()
	{
		return attributes.get(COMMENT_KEY);
	}

	void outerHtmlHead(Appendable accum, int depth, Document out) throws IOException
	{
		if(out.prettyPrint())
			indent(accum, depth);
		accum.append("<!--").append(getData()).append("-->");
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
