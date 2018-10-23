package dswork.html.nodes;

import java.util.HashMap;
import java.util.Map;

/**
 * HTML标签
 */
public class Tag
{
	private static final Map<String, Tag> tags = new HashMap<String, Tag>(); // 所有已知标签
	private String tagName;
	private boolean isBlock = true; // block or inline
	private boolean formatAsBlock = true; // should be formatted as a block
	private boolean canContainInline = true; // only pcdata if not
	private boolean empty = false; // can hold nothing; e.g. img
	private boolean selfClosing = false; // can self close (<foo />). used for unknown tags that self close, without forcing them as empty.
	private boolean preserveWhitespace = false; // for pre, textarea, script etc
	private boolean formList = false; // a control that appears in forms: input, textarea, output etc
	private boolean formSubmit = false; // a control that can be submitted in a form: input etc

	private Tag(String tagName)
	{
		this.tagName = tagName;
	}

	public String getName()
	{
		return tagName;
	}

	public static Tag valueOf(String tagName, boolean transitionLowerCase)
	{
		Tag tag = tags.get(tagName);
		if(tag == null)
		{
			tagName = transitionLowerCase ? tagName.toLowerCase() : tagName;
			tag = tags.get(tagName);
			if(tag == null)
			{
				tag = new Tag(tagName);
				tag.isBlock = false;
			}
		}
		return tag;
	}

	public static Tag valueOf(String tagName)
	{
		return valueOf(tagName, true);
	}
	
	public boolean isBlock()
	{
		return isBlock;
	}

	public boolean formatAsBlock()
	{
		return formatAsBlock;
	}

	public boolean canContainBlock()
	{
		return isBlock();
	}

	public boolean isInline()
	{
		return !isBlock;
	}

	public boolean isData()
	{
		return !canContainInline && !isEmpty();
	}

	public boolean isEmpty()
	{
		return empty;
	}

	public boolean isSelfClosing()
	{
		return empty || selfClosing;
	}

	public boolean isKnownTag()
	{
		return tags.containsKey(tagName);
	}

	public static boolean isKnownTag(String tagName)
	{
		return tags.containsKey(tagName);
	}

	public boolean preserveWhitespace()
	{
		return preserveWhitespace;
	}

	public boolean isFormListed()
	{
		return formList;
	}

	public boolean isFormSubmittable()
	{
		return formSubmit;
	}

	public Tag setSelfClosing()
	{
		selfClosing = true;
		return this;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if(!(o instanceof Tag))
			return false;
		Tag tag = (Tag) o;
		if(!tagName.equals(tag.tagName))
			return false;
		if(canContainInline != tag.canContainInline)
			return false;
		if(empty != tag.empty)
			return false;
		if(formatAsBlock != tag.formatAsBlock)
			return false;
		if(isBlock != tag.isBlock)
			return false;
		if(preserveWhitespace != tag.preserveWhitespace)
			return false;
		if(selfClosing != tag.selfClosing)
			return false;
		if(formList != tag.formList)
			return false;
		return formSubmit == tag.formSubmit;
	}

	@Override
	public int hashCode()
	{
		int result = tagName.hashCode();
		result = 31 * result + (isBlock ? 1 : 0);
		result = 31 * result + (formatAsBlock ? 1 : 0);
		result = 31 * result + (canContainInline ? 1 : 0);
		result = 31 * result + (empty ? 1 : 0);
		result = 31 * result + (selfClosing ? 1 : 0);
		result = 31 * result + (preserveWhitespace ? 1 : 0);
		result = 31 * result + (formList ? 1 : 0);
		result = 31 * result + (formSubmit ? 1 : 0);
		return result;
	}

	@Override
	public String toString()
	{
		return tagName;
	}
	// 所有tag:
	// http://www.w3.org/TR/REC-html40/sgml/dtd.html
	private static final String[] blockTags =
	{
			"html", "head", "body", "frameset", "script", "noscript", "style", "meta", "link", "title", "frame", "noframes", "section", "nav", "aside", "hgroup", "header", "footer", "p", "h1", "h2", "h3", "h4", "h5", "h6", "ul", "ol", "pre", "div", "blockquote", "hr", "address", "figure",
			"figcaption", "form", "fieldset", "ins", "del", "dl", "dt", "dd", "li", "table", "caption", "thead", "tfoot", "tbody", "colgroup", "col", "tr", "th", "td", "video", "audio", "canvas", "details", "menu", "plaintext", "template", "article", "main", "svg", "math"
	};
	private static final String[] inlineTags =
	{
			"object", "base", "font", "tt", "i", "b", "u", "big", "small", "em", "strong", "dfn", "code", "samp", "kbd", "var", "cite", "abbr", "time", "acronym", "mark", "ruby", "rt", "rp", "a", "img", "br", "wbr", "map", "q", "sub", "sup", "bdo", "iframe", "embed", "span", "input", "select",
			"textarea", "label", "button", "optgroup", "option", "legend", "datalist", "keygen", "output", "progress", "meter", "area", "param", "source", "track", "summary", "command", "device", "area", "basefont", "bgsound", "menuitem", "param", "source", "track", "data", "bdi", "s"
	};
	private static final String[] emptyTags =
	{
			"meta", "link", "base", "frame", "img", "br", "wbr", "embed", "hr", "input", "keygen", "col", "command", "device", "area", "basefont", "bgsound", "menuitem", "param", "source", "track"
	};
	private static final String[] formatAsInlineTags =
	{
			"title", "a", "p", "h1", "h2", "h3", "h4", "h5", "h6", "pre", "address", "li", "th", "td", "script", "style", "ins", "del", "s"
	};
	private static final String[] preserveWhitespaceTags =
	{
			"pre", "plaintext", "title", "textarea"
	};
	private static final String[] formListedTags =
	{
			"button", "fieldset", "input", "keygen", "object", "output", "select", "textarea"
	};
	private static final String[] formSubmitTags =
	{
			"input", "keygen", "object", "select", "textarea"
	};
	static
	{
		// 注册到tag静态map中
		for(String tagName : blockTags)
		{
			Tag tag = new Tag(tagName);
			register(tag);
		}
		for(String tagName : inlineTags)
		{
			Tag tag = new Tag(tagName);
			tag.isBlock = false;
			tag.formatAsBlock = false;
			register(tag);
		}
		
		// mods:
		for(String tagName : emptyTags)
		{
			Tag tag = tags.get(tagName);
			tag.canContainInline = false;
			tag.empty = true;
		}
		for(String tagName : formatAsInlineTags)
		{
			Tag tag = tags.get(tagName);
			tag.formatAsBlock = false;
		}
		for(String tagName : preserveWhitespaceTags)
		{
			Tag tag = tags.get(tagName);
			tag.preserveWhitespace = true;
		}
		for(String tagName : formListedTags)
		{
			Tag tag = tags.get(tagName);
			tag.formList = true;
		}
		for(String tagName : formSubmitTags)
		{
			Tag tag = tags.get(tagName);
			tag.formSubmit = true;
		}
	}

	private static void register(Tag tag)
	{
		tags.put(tag.tagName, tag);
	}
}
