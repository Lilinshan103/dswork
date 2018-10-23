package dswork.html.nodes;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.List;

import dswork.html.parser.StringUtil;

/**
 * HTML文档
 */
public class Document extends Element
{
	private String location;
	private boolean transitionLowerCase = true;
	public Document(String location)
	{
		this(location, true);
	}

	public Document(String location, boolean transitionLowerCase)
	{
		super(Tag.valueOf("#root", transitionLowerCase));
		this.location = location.toLowerCase();
		this.transitionLowerCase = transitionLowerCase;
	}

	public String location()
	{
		return location;
	}

	public Document location(String location)
	{
		this.location = location;
		return this;
	}
	public boolean isTransitionLowerCase()
	{
		return this.transitionLowerCase;
	}

	/**
	 * Accessor to the document's {@code head} element.
	 * @return {@code head}
	 */
	public Element head()
	{
		return findFirstElementByTagName("head", this);
	}

	/**
	 * Accessor to the document's {@code body} element.
	 * @return {@code body}
	 */
	public Element body()
	{
		return findFirstElementByTagName("body", this);
	}

	/**
	 * Get the string contents of the document's {@code title} element.
	 * @return Trimmed title, or empty string if none set.
	 */
	public String title()
	{
		List<Element> elements = getElementsByTag("title");
		Element titleEl = elements.size() == 0 ? null : elements.get(0);
		return titleEl != null ? StringUtil.normaliseWhitespace(titleEl.text()).trim() : "";
	}

	/**
	 * Set the document's {@code title} element. Updates the existing element, or adds {@code title} to {@code head} if
	 * not present
	 * @param title string to set as title
	 */
	public void title(String title)
	{
		List<Element> elements = getElementsByTag("title");
		Element titleEl = elements.size() == 0 ? null : elements.get(0);
		if(titleEl == null)
		{ // add to head
			head().appendElement("title").text(title);
		}
		else
		{
			titleEl.text(title);
		}
	}

	/**
	 * Create a new Element, with this document's base uri. Does not make the new element a child of this document.
	 * @param tagName element tag name (e.g. {@code a})
	 * @return new element
	 */
	public Element createElement(String tagName)
	{
		return new Element(Tag.valueOf(tagName, this.transitionLowerCase));
	}

	private Element findFirstElementByTagName(String tag, Node node)
	{
		if(node.nodeName().equals(tag))
			return (Element) node;
		else
		{
			for(Node child : node.childNodes)
			{
				Element found = findFirstElementByTagName(tag, child);
				if(found != null)
					return found;
			}
		}
		return null;
	}

	@Override
	public String outerHtml()
	{
		return super.html(); // no outer wrapper tag
	}

	@Override
	public Element text(String text)
	{
		body().text(text);
		return this;
	}

	@Override
	public String nodeName()
	{
		return "#document";
	}

	@Override
	public Document clone()
	{
		Document clone = (Document) super.clone();
		return clone;
	}

	private Charset charset = Charset.forName("UTF-8");
	public Charset charset()
	{
		return charset;
	}
	public void charset(String charset)
	{
		this.charset = Charset.forName(charset);
		List<Element> elements = select("meta[charset]");
		Element metaCharset = elements.size() == 0 ? null : elements.get(0);
		if(metaCharset != null)
		{
			metaCharset.attr("charset", charset().displayName());
		}
		else
		{
			Element head = head();
			if(head != null)
			{
				head.appendElement("meta").attr("charset", charset().displayName());
			}
		}
		elements = select("meta[name=charset]");
		for(Element ele : elements)
		{
			ele.remove();
		}
	}
	CharsetEncoder encoder()
	{
		return charset.newEncoder();
	}
	
	
	
	private boolean prettyPrint = true;
	private boolean outline = false;

	public boolean prettyPrint()
	{
		return prettyPrint;
	}

	public void prettyPrint(boolean pretty)
	{
		prettyPrint = pretty;
	}

	public boolean outline()
	{
		return outline;
	}

	public void outline(boolean outlineMode)
	{
		outline = outlineMode;
	}
}
