package dswork.html;

import java.util.List;

import dswork.html.nodes.Document;
import dswork.html.nodes.Element;
import dswork.html.nodes.Node;
import dswork.html.parser.TreeBuilderHtml;

public class HtmlUtil
{
	private HtmlUtil()
	{
	}

	public static Document parse(String html, String location, boolean transitionLowerCase)
	{
		Document doc = new TreeBuilderHtml().parse(html, transitionLowerCase);
		doc.location(location);
		return doc;
	}

	public static Document parse(String html, String location)
	{
		return parse(html, location, true);
	}

	public static Document parse(String html)
	{
		return parse(html, "", true);
	}

	public static Document parse(String html, boolean transitionLowerCase)
	{
		return parse(html, "", transitionLowerCase);
	}

	public static List<Node> parseFragment(String fragmentHtml, Element context)
	{
		return new TreeBuilderHtml().parseFragment(fragmentHtml, context, true);
	}
}
