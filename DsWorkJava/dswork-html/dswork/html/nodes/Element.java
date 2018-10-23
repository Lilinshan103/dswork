package dswork.html.nodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import dswork.html.select.SelectUtil;
import dswork.html.parser.StringUtil;
import dswork.html.select.Evaluator;
import dswork.html.select.NodeTraversor;
import dswork.html.select.NodeVisitor;
import dswork.html.select.ParserQuery;

/**
 * HTML标签
 */
public class Element extends Node
{
	private Tag tag;
	private static final Pattern classSplit = Pattern.compile("\\s+");

	public Element(Tag tag, Attributes attributes)
	{
		super(attributes);
		this.tag = tag;
		//System.out.println("Element:" + tag);
	}

	public Element(Tag tag)
	{
		this(tag, new Attributes());
	}

	@Override
	public String nodeName()
	{
		return tag.getName();
	}

	public String tagName()
	{
		return tag.getName();
	}

	public Element tagName(String tagName)
	{
		tag = Tag.valueOf(tagName, true);
		return this;
	}

	public Tag tag()
	{
		return tag;
	}

	public boolean isBlock()
	{
		return tag.isBlock();
	}

	public String id()
	{
		return attributes.getIgnoreCase("id");
	}

	public Element attr(String attributeKey, String attributeValue)
	{
		super.attr(attributeKey, attributeValue);
		return this;
	}

	public Element attr(String attributeKey, boolean attributeValue)
	{
		attributes.put(attributeKey, attributeValue);
		return this;
	}

	public Map<String, String> dataset()
	{
		return attributes.dataset();
	}

	@Override
	public final Element parent()
	{
		return (Element) parentNode;
	}

	public ArrayList<Element> parents()
	{
		ArrayList<Element> parents = new ArrayList<Element>();
		accumulateParents(this, parents);
		return parents;
	}

	private static void accumulateParents(Element el, ArrayList<Element> parents)
	{
		Element parent = el.parent();
		if(parent != null && !parent.tagName().equals("#root"))
		{
			parents.add(parent);
			accumulateParents(parent, parents);
		}
	}

	public Element child(int index)
	{
		return children().get(index);
	}

	public List<Element> children()
	{
		List<Element> elements = new ArrayList<Element>(childNodes.size());
		for(Node node : childNodes)
		{
			if(node instanceof Element)
				elements.add((Element) node);
		}
		return elements;
	}

	public List<NodeText> textNodes()
	{
		List<NodeText> textNodes = new ArrayList<NodeText>();
		for(Node node : childNodes)
		{
			if(node instanceof NodeText)
			{
				textNodes.add((NodeText) node);
			}
		}
		return Collections.unmodifiableList(textNodes);
	}

	public List<NodeData> dataNodes()
	{
		List<NodeData> dataNodes = new ArrayList<NodeData>();
		for(Node node : childNodes)
		{
			if(node instanceof NodeData)
			{
				dataNodes.add((NodeData) node);
			}
		}
		return Collections.unmodifiableList(dataNodes);
	}

	public List<Element> select(String cssQuery)
	{
		return SelectUtil.select(ParserQuery.parse(cssQuery), this);
	}

	public String selectText(String cssQuery)
	{
		List<Element> arr = select(cssQuery);
		StringBuilder sb = new StringBuilder(64);
		if(arr.size() > 0)
		{
			for(Element ele : arr)
			{
				sb.append(ele.text());
			}
		}
		return sb.toString();
	}

	public String selectOwnText(String cssQuery)
	{
		List<Element> arr = select(cssQuery);
		StringBuilder sb = new StringBuilder(64);
		if(arr.size() > 0)
		{
			for(Element ele : arr)
			{
				sb.append(ele.ownText());
			}
		}
		return sb.toString();
	}

	public boolean is(String cssQuery)
	{
		return is(ParserQuery.parse(cssQuery));
	}

	public boolean is(Evaluator evaluator)
	{
		return evaluator.matches((Element) this.root(), this);
	}

	public Element appendChild(Node child)
	{
		reparentChild(child);
		ensureChildNodes();
		childNodes.add(child);
		child.setSiblingIndex(childNodes.size() - 1);
		return this;
	}

	public Element prependChild(Node child)
	{
		addChildren(0, child);
		return this;
	}

	public Element insertChildren(int index, Collection<? extends Node> children)
	{
		int currentSize = childNodeSize();
		if(index < 0)
			index += currentSize + 1; // roll around
		ArrayList<Node> nodes = new ArrayList<Node>(children);
		Node[] nodeArray = nodes.toArray(new Node[nodes.size()]);
		addChildren(index, nodeArray);
		return this;
	}

	public Element appendElement(String tagName)
	{
		Element child = new Element(Tag.valueOf(tagName));
		appendChild(child);
		return child;
	}

	public Element prependElement(String tagName)
	{
		Element child = new Element(Tag.valueOf(tagName));
		prependChild(child);
		return child;
	}

	public Element appendText(String text)
	{
		NodeText node = new NodeText(text);
		appendChild(node);
		return this;
	}

	public Element prependText(String text)
	{
		NodeText node = new NodeText(text);
		prependChild(node);
		return this;
	}

	public Element append(String html)
	{
		List<Node> nodes = Node.parseFragment(html, this);
		addChildren(nodes.toArray(new Node[nodes.size()]));
		return this;
	}

	public Element prepend(String html)
	{
		List<Node> nodes = Node.parseFragment(html, this);
		addChildren(0, nodes.toArray(new Node[nodes.size()]));
		return this;
	}

	@Override
	public Element before(String html)
	{
		return (Element) super.before(html);
	}

	@Override
	public Element before(Node node)
	{
		return (Element) super.before(node);
	}

	@Override
	public Element after(String html)
	{
		return (Element) super.after(html);
	}

	@Override
	public Element after(Node node)
	{
		return (Element) super.after(node);
	}

	public Element empty()
	{
		childNodes.clear();
		return this;
	}

	@Override
	public Element wrap(String html)
	{
		return (Element) super.wrap(html);
	}

	public String cssSelector()
	{
		if(id().length() > 0)
			return "#" + id();
		// Translate HTML namespace ns:tag to CSS namespace syntax ns|tag
		String tagName = tagName().replace(':', '|');
		StringBuilder selector = new StringBuilder(tagName);
		String classes = StringUtil.join(classNames(), ".");
		if(classes.length() > 0)
			selector.append('.').append(classes);
		if(parent() == null || parent() instanceof Document) // don't add Document to selector, as will always have a
																// html node
			return selector.toString();
		selector.insert(0, " > ");
		if(parent().select(selector.toString()).size() > 1)
			selector.append(String.format(":nth-child(%d)", elementSiblingIndex() + 1));
		return parent().cssSelector() + selector.toString();
	}

	public List<Element> siblingElements()
	{
		if(parentNode == null)
			return new ArrayList<Element>(0);
		List<Element> elements = parent().children();
		List<Element> siblings = new ArrayList<Element>(elements.size() - 1);
		for(Element el : elements)
			if(el != this)
				siblings.add(el);
		return siblings;
	}

	public Element nextElementSibling()
	{
		if(parentNode == null)
			return null;
		List<Element> siblings = parent().children();
		Integer index = indexInList(this, siblings);
		if(siblings.size() > index + 1)
			return siblings.get(index + 1);
		else
			return null;
	}

	public Element previousElementSibling()
	{
		if(parentNode == null)
			return null;
		List<Element> siblings = parent().children();
		Integer index = indexInList(this, siblings);
		if(index > 0)
			return siblings.get(index - 1);
		else
			return null;
	}

	public Element firstElementSibling()
	{
		// todo: should firstSibling() exclude this?
		List<Element> siblings = parent().children();
		return siblings.size() > 1 ? siblings.get(0) : null;
	}

	public Integer elementSiblingIndex()
	{
		if(parent() == null)
			return 0;
		return indexInList(this, parent().children());
	}

	public Element lastElementSibling()
	{
		List<Element> siblings = parent().children();
		return siblings.size() > 1 ? siblings.get(siblings.size() - 1) : null;
	}

	private static <E extends Element> Integer indexInList(Element search, List<E> elements)
	{
		for(int i = 0; i < elements.size(); i++)
		{
			E element = elements.get(i);
			if(element == search)
				return i;
		}
		return null;
	}

	public List<Element> getElementsByTag(String tagName)
	{
		tagName = tagName.toLowerCase().trim();
		return SelectUtil.select(new Evaluator.Tag(tagName), this);
	}

	public Element getElementById(String id)
	{
		List<Element> elements = SelectUtil.select(new Evaluator.Id(id), this);
		if(elements.size() > 0)
			return elements.get(0);
		else
			return null;
	}

	public List<Element> getElementsByClass(String className)
	{
		return SelectUtil.select(new Evaluator.Class(className), this);
	}

	public List<Element> getElementsByAttribute(String key)
	{
		key = key.trim();
		return SelectUtil.select(new Evaluator.Attribute(key), this);
	}

	public List<Element> getElementsByAttributeStarting(String keyPrefix)
	{
		keyPrefix = keyPrefix.trim();
		return SelectUtil.select(new Evaluator.AttributeStarting(keyPrefix), this);
	}

	public List<Element> getElementsByAttributeValue(String key, String value)
	{
		return SelectUtil.select(new Evaluator.AttributeWithValue(key, value), this);
	}

	public List<Element> getElementsByAttributeValueNot(String key, String value)
	{
		return SelectUtil.select(new Evaluator.AttributeWithValueNot(key, value), this);
	}

	public List<Element> getElementsByAttributeValueStarting(String key, String valuePrefix)
	{
		return SelectUtil.select(new Evaluator.AttributeWithValueStarting(key, valuePrefix), this);
	}

	public List<Element> getElementsByAttributeValueEnding(String key, String valueSuffix)
	{
		return SelectUtil.select(new Evaluator.AttributeWithValueEnding(key, valueSuffix), this);
	}

	public List<Element> getElementsByAttributeValueContaining(String key, String match)
	{
		return SelectUtil.select(new Evaluator.AttributeWithValueContaining(key, match), this);
	}

	public List<Element> getElementsByAttributeValueMatching(String key, Pattern pattern)
	{
		return SelectUtil.select(new Evaluator.AttributeWithValueMatching(key, pattern), this);
	}

	public List<Element> getElementsByAttributeValueMatching(String key, String regex)
	{
		Pattern pattern;
		try
		{
			pattern = Pattern.compile(regex);
		}
		catch(PatternSyntaxException e)
		{
			throw new IllegalArgumentException("Pattern syntax error: " + regex, e);
		}
		return getElementsByAttributeValueMatching(key, pattern);
	}

	public List<Element> getElementsByIndexLessThan(int index)
	{
		return SelectUtil.select(new Evaluator.IndexLessThan(index), this);
	}

	public List<Element> getElementsByIndexGreaterThan(int index)
	{
		return SelectUtil.select(new Evaluator.IndexGreaterThan(index), this);
	}

	public List<Element> getElementsByIndexEquals(int index)
	{
		return SelectUtil.select(new Evaluator.IndexEquals(index), this);
	}

	public List<Element> getElementsContainingText(String searchText)
	{
		return SelectUtil.select(new Evaluator.ContainsText(searchText), this);
	}

	public List<Element> getElementsContainingOwnText(String searchText)
	{
		return SelectUtil.select(new Evaluator.ContainsOwnText(searchText), this);
	}

	public List<Element> getElementsMatchingText(Pattern pattern)
	{
		return SelectUtil.select(new Evaluator.Matches(pattern), this);
	}

	public List<Element> getElementsMatchingText(String regex)
	{
		Pattern pattern;
		try
		{
			pattern = Pattern.compile(regex);
		}
		catch(PatternSyntaxException e)
		{
			throw new IllegalArgumentException("Pattern syntax error: " + regex, e);
		}
		return getElementsMatchingText(pattern);
	}

	public List<Element> getElementsMatchingOwnText(Pattern pattern)
	{
		return SelectUtil.select(new Evaluator.MatchesOwn(pattern), this);
	}

	public List<Element> getElementsMatchingOwnText(String regex)
	{
		Pattern pattern;
		try
		{
			pattern = Pattern.compile(regex);
		}
		catch(PatternSyntaxException e)
		{
			throw new IllegalArgumentException("Pattern syntax error: " + regex, e);
		}
		return getElementsMatchingOwnText(pattern);
	}

	public List<Element> getAllElements()
	{
		return SelectUtil.select(new Evaluator.AllElements(), this);
	}

	public String text()
	{
		final StringBuilder accum = new StringBuilder();
		new NodeTraversor(new NodeVisitor()
		{
			public void head(Node node, int depth)
			{
				if(node instanceof NodeText)
				{
					NodeText textNode = (NodeText) node;
					appendNormalisedText(accum, textNode);
				}
				else if(node instanceof Element)
				{
					Element element = (Element) node;
					if(accum.length() > 0 && (element.isBlock() || element.tag.getName().equals("br")) && !NodeText.lastCharIsWhitespace(accum))
						accum.append(" ");
				}
			}

			public void tail(Node node, int depth)
			{
			}
		}).traverse(this);
		return accum.toString().trim();
	}

	public String ownText()
	{
		StringBuilder sb = new StringBuilder();
		ownText(sb);
		return sb.toString().trim();
	}

	private void ownText(StringBuilder accum)
	{
		for(Node child : childNodes)
		{
			if(child instanceof NodeText)
			{
				NodeText textNode = (NodeText) child;
				appendNormalisedText(accum, textNode);
			}
			else if(child instanceof Element)
			{
				appendWhitespaceIfBr((Element) child, accum);
			}
		}
	}

	private static void appendNormalisedText(StringBuilder accum, NodeText textNode)
	{
		String text = textNode.getWholeText();
		if(preserveWhitespace(textNode.parentNode))
			accum.append(text);
		else
			StringUtil.appendNormalisedWhitespace(accum, text, NodeText.lastCharIsWhitespace(accum));
	}

	private static void appendWhitespaceIfBr(Element element, StringBuilder accum)
	{
		if(element.tag.getName().equals("br") && !NodeText.lastCharIsWhitespace(accum))
			accum.append(" ");
	}

	static boolean preserveWhitespace(Node node)
	{
		// looks only at this element and one level up, to prevent recursion & needless stack searches
		if(node != null && node instanceof Element)
		{
			Element element = (Element) node;
			return element.tag.preserveWhitespace() || element.parent() != null && element.parent().tag.preserveWhitespace();
		}
		return false;
	}

	public Element text(String text)
	{
		empty();
		NodeText textNode = new NodeText(text);
		appendChild(textNode);
		return this;
	}

	public boolean hasText()
	{
		for(Node child : childNodes)
		{
			if(child instanceof NodeText)
			{
				NodeText textNode = (NodeText) child;
				if(!textNode.isBlank())
					return true;
			}
			else if(child instanceof Element)
			{
				Element el = (Element) child;
				if(el.hasText())
					return true;
			}
		}
		return false;
	}

	public String data()
	{
		StringBuilder sb = new StringBuilder();
		for(Node childNode : childNodes)
		{
			if(childNode instanceof NodeData)
			{
				NodeData data = (NodeData) childNode;
				sb.append(data.getWholeData());
			}
			else if(childNode instanceof NodeComment)
			{
				NodeComment comment = (NodeComment) childNode;
				sb.append(comment.getData());
			}
			else if(childNode instanceof Element)
			{
				Element element = (Element) childNode;
				String elementData = element.data();
				sb.append(elementData);
			}
		}
		return sb.toString();
	}

	public String className()
	{
		return attr("class").trim();
	}

	public Set<String> classNames()
	{
		String[] names = classSplit.split(className());
		Set<String> classNames = new LinkedHashSet<String>(Arrays.asList(names));
		classNames.remove(""); // if classNames() was empty, would include an empty class
		return classNames;
	}

	public Element classNames(Set<String> classNames)
	{
		attributes.put("class", StringUtil.join(classNames, " "));
		return this;
	}

	public boolean hasClass(String className)
	{
		final String classAttr = attributes.getIgnoreCase("class");
		final int len = classAttr.length();
		final int wantLen = className.length();
		if(len == 0 || len < wantLen)
		{
			return false;
		}
		// if both lengths are equal, only need compare the className with the attribute
		if(len == wantLen)
		{
			return className.equalsIgnoreCase(classAttr);
		}
		// otherwise, scan for whitespace and compare regions (with no string or arraylist allocations)
		boolean inClass = false;
		int start = 0;
		for(int i = 0; i < len; i++)
		{
			if(Character.isWhitespace(classAttr.charAt(i)))
			{
				if(inClass)
				{
					// white space ends a class name, compare it with the requested one, ignore case
					if(i - start == wantLen && classAttr.regionMatches(true, start, className, 0, wantLen))
					{
						return true;
					}
					inClass = false;
				}
			}
			else
			{
				if(!inClass)
				{
					// we're in a class name : keep the start of the substring
					inClass = true;
					start = i;
				}
			}
		}
		// check the last entry
		if(inClass && len - start == wantLen)
		{
			return classAttr.regionMatches(true, start, className, 0, wantLen);
		}
		return false;
	}

	public Element addClass(String className)
	{
		Set<String> classes = classNames();
		classes.add(className);
		classNames(classes);
		return this;
	}

	public Element removeClass(String className)
	{
		Set<String> classes = classNames();
		classes.remove(className);
		classNames(classes);
		return this;
	}

	public Element toggleClass(String className)
	{
		Set<String> classes = classNames();
		if(classes.contains(className))
			classes.remove(className);
		else
			classes.add(className);
		classNames(classes);
		return this;
	}

	public String val()
	{
		if(tagName().equals("textarea"))
			return text();
		else
			return attr("value");
	}

	public Element val(String value)
	{
		if(tagName().equals("textarea"))
			text(value);
		else
			attr("value", value);
		return this;
	}

	void outerHtmlHead(Appendable accum, int depth, Document out) throws IOException
	{
		if(out.prettyPrint() && (tag.formatAsBlock() || (parent() != null && parent().tag().formatAsBlock()) || out.outline()))
		{
			if(accum instanceof StringBuilder)
			{
				if(((StringBuilder) accum).length() > 0)
					indent(accum, depth);
			}
			else
			{
				indent(accum, depth);
			}
		}
		accum.append("<").append(tagName());
		attributes.html(accum, out);
		if(childNodes.isEmpty() && tag.isSelfClosing())
		{
			if(tag.isEmpty())
				accum.append('>');
			else
				accum.append(" />");
		}
		else
			accum.append(">");
	}

	void outerHtmlTail(Appendable accum, int depth, Document out) throws IOException
	{
		if(!(childNodes.isEmpty() && tag.isSelfClosing()))
		{
			if(out.prettyPrint() && (!childNodes.isEmpty() && (tag.formatAsBlock() || (out.outline() && (childNodes.size() > 1 || (childNodes.size() == 1 && !(childNodes.get(0) instanceof NodeText)))))))
			{
				indent(accum, depth);
			}
			accum.append("</").append(tagName()).append(">");
		}
	}

	public String html()
	{
		StringBuilder accum = new StringBuilder();
		html(accum);
		return rootDocument().prettyPrint() ? accum.toString().trim() : accum.toString();
	}

	private void html(StringBuilder accum)
	{
		for(Node node : childNodes)
			node.outerHtml(accum);
	}

	@Override
	public <T extends Appendable> T html(T appendable)
	{
		for(Node node : childNodes)
			node.outerHtml(appendable);
		return appendable;
	}

	public Element html(String html)
	{
		empty();
		append(html);
		return this;
	}

	public String toString()
	{
		return outerHtml();
	}

	@Override
	public Element clone()
	{
		return (Element) super.clone();
	}
}
