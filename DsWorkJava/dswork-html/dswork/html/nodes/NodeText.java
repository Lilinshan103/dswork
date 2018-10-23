package dswork.html.nodes;

import java.io.IOException;

import dswork.html.parser.StringUtil;

/**
 * 非script和style标签
 */
public class NodeText extends Node
{
	private static final String TEXT_KEY = "text";
	String text;

	public NodeText(String text)
	{
		this.text = text;
		//System.out.println("NodeText:" + text);
	}

	public String nodeName()
	{
		return "#text";
	}

	public String text()
	{
		return StringUtil.normaliseWhitespace(getWholeText());
	}

	public NodeText text(String text)
	{
		this.text = text;
		if(attributes != null)
			attributes.put(TEXT_KEY, text);
		return this;
	}

	public String getWholeText()
	{
		return attributes == null ? text : attributes.get(TEXT_KEY);
	}

	public boolean isBlank()
	{
		return StringUtil.isBlank(getWholeText());
	}

	void outerHtmlHead(Appendable accum, int depth, Document out) throws IOException
	{
		if(out.prettyPrint() && ((siblingIndex() == 0 && parentNode instanceof Element && ((Element) parentNode).tag().formatAsBlock() && !isBlank()) || (out.outline() && siblingNodes().size() > 0 && !isBlank())))
			indent(accum, depth);
		boolean normaliseWhite = out.prettyPrint() && parent() instanceof Element && !Element.preserveWhitespace(parent());
		Entities.escape(accum, getWholeText(), out, false, normaliseWhite, false);
	}

	void outerHtmlTail(Appendable accum, int depth, Document out)
	{
	}

	@Override
	public String toString()
	{
		return outerHtml();
	}
	
	static boolean lastCharIsWhitespace(StringBuilder sb)
	{
		return sb.length() != 0 && sb.charAt(sb.length() - 1) == ' ';
	}

	private void ensureAttributes()
	{
		if(attributes == null)
		{
			attributes = new Attributes();
			attributes.put(TEXT_KEY, text);
		}
	}

	@Override
	public String attr(String attributeKey)
	{
		ensureAttributes();
		return super.attr(attributeKey);
	}

	@Override
	public Attributes attributes()
	{
		ensureAttributes();
		return super.attributes();
	}

	@Override
	public Node attr(String attributeKey, String attributeValue)
	{
		ensureAttributes();
		return super.attr(attributeKey, attributeValue);
	}

	@Override
	public boolean hasAttr(String attributeKey)
	{
		ensureAttributes();
		return super.hasAttr(attributeKey);
	}

	@Override
	public Node removeAttr(String attributeKey)
	{
		ensureAttributes();
		return super.removeAttr(attributeKey);
	}
}
