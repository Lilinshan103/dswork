package dswork.html.parser;

import java.util.ArrayList;
import java.util.List;

import dswork.html.nodes.*;

/**
 * HTML Tree Builder; creates a DOM from Tokens.
 */
public class TreeBuilderHtml extends TreeBuilder
{
	// tag searches
	public static final String[] TagsSearchInScope = new String[]
	{
			"applet", "caption", "html", "table", "td", "th", "marquee", "object"
	};
	private static final String[] TagSearchList = new String[]
	{
			"ol", "ul"
	};
	private static final String[] TagSearchButton = new String[]
	{
			"button"
	};
	private static final String[] TagSearchTableScope = new String[]
	{
			"html", "table"
	};
	private static final String[] TagSearchSelectScope = new String[]
	{
			"optgroup", "option"
	};
	private static final String[] TagSearchEndTags = new String[]
	{
			"dd", "dt", "li", "option", "optgroup", "p", "rp", "rt"
	};
	private static final String[] TagSearchSpecial = new String[]
	{
			"address", "applet", "area", "article", "aside", "base", "basefont", "bgsound", "blockquote", "body", "br", "button", "caption", "center", "col", "colgroup", "command", "dd", "details", "dir", "div", "dl", "dt", "embed", "fieldset", "figcaption", "figure", "footer", "form", "frame",
			"frameset", "h1", "h2", "h3", "h4", "h5", "h6", "head", "header", "hgroup", "hr", "html", "iframe", "img", "input", "isindex", "li", "link", "listing", "marquee", "menu", "meta", "nav", "noembed", "noframes", "noscript", "object", "ol", "p", "param", "plaintext", "pre", "script",
			"section", "select", "style", "summary", "table", "tbody", "td", "textarea", "tfoot", "th", "thead", "title", "tr", "ul", "wbr", "xmp"
	};
	private TreeBuilderHtmlState state; // the current state
	private TreeBuilderHtmlState originalState; // original / marked state
	private Element headElement; // the current head element
	private Element contextElement; // fragment parse context -- could be null even if fragment parsing
	private ArrayList<Element> formattingElements = new ArrayList<Element>(); // active (open) formatting elements
	private List<String> pendingTableCharacters = new ArrayList<String>(); // chars in table to be shifted out
	private Token.EndTag emptyEnd = new Token.EndTag(); // reused empty end tag
	private boolean framesetOk = true; // if ok to go into frameset
	private boolean fosterInserts = false; // if next inserts should be fostered
	private boolean fragmentParsing = false; // if parsing a fragment of html

	public TreeBuilderHtml()
	{
	}

	boolean isTransitionLowerCase()
	{
		return this.transitionLowerCase;
	}

	@Override
	public Document parse(String input, boolean transitionLowerCase)
	{
		state = TreeBuilderHtmlState.Initial;
		return super.parse(input, transitionLowerCase);
	}

	public List<Node> parseFragment(String inputFragment, Element context, boolean transitionLowerCase)
	{
		// context may be null
		state = TreeBuilderHtmlState.Initial;
		initialiseParse(inputFragment, transitionLowerCase);
		contextElement = context;
		fragmentParsing = true;
		Element root = null;
		if(context != null)
		{
			String contextTag = context.tagName();
			if(StringUtil.in(contextTag, "title", "textarea"))
				tokeniser.transition(TokeniserState.Rcdata);
			else if(StringUtil.in(contextTag, "iframe", "noembed", "noframes", "style", "xmp"))
				tokeniser.transition(TokeniserState.Rawtext);
			else if(contextTag.equals("script"))
				tokeniser.transition(TokeniserState.ScriptData);
			else if(contextTag.equals(("noscript")))
				tokeniser.transition(TokeniserState.Data); // if scripting enabled, rawtext
			else if(contextTag.equals("plaintext"))
				tokeniser.transition(TokeniserState.Data);
			else
				tokeniser.transition(TokeniserState.Data); // default
			root = new Element(Tag.valueOf("html", transitionLowerCase));
			doc.appendChild(root);
			stack.add(root);
			resetInsertionMode();
			// setup form element to nearest form on context (up ancestor chain). ensures form controls are associated
			// with form correctly
			List<Element> contextChain = context.parents();
			contextChain.add(0, context);
		}
		runParser();
		if(context != null && root != null)
			return root.childNodes();
		else
			return doc.childNodes();
	}

	@Override
	protected boolean process(Token token)
	{
		currentToken = token;
		return this.state.process(token, this);
	}

	boolean process(Token token, TreeBuilderHtmlState state)
	{
		currentToken = token;
		return state.process(token, this);
	}

	void transition(TreeBuilderHtmlState state)
	{
		this.state = state;
	}

	TreeBuilderHtmlState state()
	{
		return state;
	}

	void markInsertionMode()
	{
		originalState = state;
	}

	TreeBuilderHtmlState originalState()
	{
		return originalState;
	}

	void framesetOk(boolean framesetOk)
	{
		this.framesetOk = framesetOk;
	}

	boolean framesetOk()
	{
		return framesetOk;
	}

	Document getDocument()
	{
		return doc;
	}

	boolean isFragmentParsing()
	{
		return fragmentParsing;
	}

	void error(TreeBuilderHtmlState state)
	{
	}

	Element insert(Token.StartTag startTag)
	{
		// handle empty unknown tags
		// when the spec expects an empty tag, will directly hit insertEmpty, so won't generate this fake end tag.
		if(startTag.isSelfClosing())
		{
			Element el = insertEmpty(startTag);
			stack.add(el);
			tokeniser.transition(TokeniserState.Data); // handles <script />, otherwise needs breakout steps from script
														// data
			tokeniser.emit(emptyEnd.reset().name(el.tagName())); // ensure we get out of whatever state we are in.
																	// emitted for yielded processing
			return el;
		}
		Element el = new Element(Tag.valueOf(startTag.name(), transitionLowerCase), TreeBuilderHtml.normalizeAttributes(startTag.attributes, transitionLowerCase));
		insert(el);
		return el;
	}

	Element insertStartTag(String startTagName)
	{
		Element el = new Element(Tag.valueOf(startTagName, transitionLowerCase));
		insert(el);
		return el;
	}

	void insert(Element el)
	{
		insertNode(el);
		stack.add(el);
	}

	Element insertEmpty(Token.StartTag startTag)
	{
		Tag tag = Tag.valueOf(startTag.name(), transitionLowerCase);
		Element el = new Element(tag, startTag.attributes);
		insertNode(el);
		if(startTag.isSelfClosing())
		{
			if(tag.isKnownTag())
			{
				if(tag.isSelfClosing())
					tokeniser.acknowledgeSelfClosingFlag(); // if not acked, promulagates error
			}
			else
			{
				tag.setSelfClosing();
				tokeniser.acknowledgeSelfClosingFlag(); // not an distinct error
			}
		}
		return el;
	}

	void insert(Token.Comment commentToken)
	{
		NodeComment comment = new NodeComment(commentToken.getData());
		insertNode(comment);
	}

	void insert(Token.Character characterToken)
	{
		Node node;
		// characters in script and style go in as datanodes, not text nodes
		String tagName = currentElement().tagName();
		if(tagName.equals("script") || tagName.equals("style"))
			node = new NodeData(characterToken.getData());
		else
			node = new NodeText(characterToken.getData());
		currentElement().appendChild(node); // doesn't use insertNode, because we don't foster these; and will always have a stack.
	}

	private void insertNode(Node node)
	{
		// if the stack hasn't been set up yet, elements (doctype, comments) go into the doc
		if(stack.size() == 0)
			doc.appendChild(node);
		else if(isFosterInserts())
			insertInFosterParent(node);
		else
			currentElement().appendChild(node);
	}

	Element pop()
	{
		int size = stack.size();
		return stack.remove(size - 1);
	}

	void push(Element element)
	{
		stack.add(element);
	}

	ArrayList<Element> getStack()
	{
		return stack;
	}

	boolean onStack(Element el)
	{
		return isElementInQueue(stack, el);
	}

	private boolean isElementInQueue(ArrayList<Element> queue, Element element)
	{
		for(int pos = queue.size() - 1; pos >= 0; pos--)
		{
			Element next = queue.get(pos);
			if(next == element)
			{
				return true;
			}
		}
		return false;
	}

	Element getFromStack(String elName)
	{
		for(int pos = stack.size() - 1; pos >= 0; pos--)
		{
			Element next = stack.get(pos);
			if(next.nodeName().equals(elName))
			{
				return next;
			}
		}
		return null;
	}

	boolean removeFromStack(Element el)
	{
		for(int pos = stack.size() - 1; pos >= 0; pos--)
		{
			Element next = stack.get(pos);
			if(next == el)
			{
				stack.remove(pos);
				return true;
			}
		}
		return false;
	}

	void popStackToClose(String elName)
	{
		for(int pos = stack.size() - 1; pos >= 0; pos--)
		{
			Element next = stack.get(pos);
			stack.remove(pos);
			if(next.nodeName().equals(elName))
				break;
		}
	}

	void popStackToClose(String... elNames)
	{
		for(int pos = stack.size() - 1; pos >= 0; pos--)
		{
			Element next = stack.get(pos);
			stack.remove(pos);
			if(StringUtil.in(next.nodeName(), elNames))
				break;
		}
	}

	void popStackToBefore(String elName)
	{
		for(int pos = stack.size() - 1; pos >= 0; pos--)
		{
			Element next = stack.get(pos);
			if(next.nodeName().equals(elName))
			{
				break;
			}
			else
			{
				stack.remove(pos);
			}
		}
	}

	void clearStackToTableContext()
	{
		clearStackToContext("table");
	}

	void clearStackToTableBodyContext()
	{
		clearStackToContext("tbody", "tfoot", "thead");
	}

	void clearStackToTableRowContext()
	{
		clearStackToContext("tr");
	}

	private void clearStackToContext(String... nodeNames)
	{
		for(int pos = stack.size() - 1; pos >= 0; pos--)
		{
			Element next = stack.get(pos);
			if(StringUtil.in(next.nodeName(), nodeNames) || next.nodeName().equals("html"))
				break;
			else
				stack.remove(pos);
		}
	}

	Element aboveOnStack(Element el)
	{
		assert onStack(el);
		for(int pos = stack.size() - 1; pos >= 0; pos--)
		{
			Element next = stack.get(pos);
			if(next == el)
			{
				return stack.get(pos - 1);
			}
		}
		return null;
	}

	void insertOnStackAfter(Element after, Element in)
	{
		int i = stack.lastIndexOf(after);
		stack.add(i + 1, in);
	}

	void replaceOnStack(Element out, Element in)
	{
		replaceInQueue(stack, out, in);
	}

	private void replaceInQueue(ArrayList<Element> queue, Element out, Element in)
	{
		int i = queue.lastIndexOf(out);
		queue.set(i, in);
	}

	void resetInsertionMode()
	{
		boolean last = false;
		for(int pos = stack.size() - 1; pos >= 0; pos--)
		{
			Element node = stack.get(pos);
			if(pos == 0)
			{
				last = true;
				node = contextElement;
			}
			String name = node.nodeName();
			if("select".equals(name))
			{
				transition(TreeBuilderHtmlState.InSelect);
				break; // frag
			}
			else if(("td".equals(name) || "th".equals(name) && !last))
			{
				transition(TreeBuilderHtmlState.InCell);
				break;
			}
			else if("tr".equals(name))
			{
				transition(TreeBuilderHtmlState.InRow);
				break;
			}
			else if("tbody".equals(name) || "thead".equals(name) || "tfoot".equals(name))
			{
				transition(TreeBuilderHtmlState.InTableBody);
				break;
			}
			else if("caption".equals(name))
			{
				transition(TreeBuilderHtmlState.InCaption);
				break;
			}
			else if("colgroup".equals(name))
			{
				transition(TreeBuilderHtmlState.InColumnGroup);
				break; // frag
			}
			else if("table".equals(name))
			{
				transition(TreeBuilderHtmlState.InTable);
				break;
			}
			else if("head".equals(name))
			{
				transition(TreeBuilderHtmlState.InBody);
				break; // frag
			}
			else if("body".equals(name))
			{
				transition(TreeBuilderHtmlState.InBody);
				break;
			}
			else if("frameset".equals(name))
			{
				transition(TreeBuilderHtmlState.InFrameset);
				break; // frag
			}
			else if("html".equals(name))
			{
				transition(TreeBuilderHtmlState.BeforeHead);
				break; // frag
			}
			else if(last)
			{
				transition(TreeBuilderHtmlState.InBody);
				break; // frag
			}
		}
	}
	// todo: tidy up in specific scope methods
	private String[] specificScopeTarget =
	{
			null
	};

	private boolean inSpecificScope(String targetName, String[] baseTypes, String[] extraTypes)
	{
		specificScopeTarget[0] = targetName;
		return inSpecificScope(specificScopeTarget, baseTypes, extraTypes);
	}

	private boolean inSpecificScope(String[] targetNames, String[] baseTypes, String[] extraTypes)
	{
		for(int pos = stack.size() - 1; pos >= 0; pos--)
		{
			Element el = stack.get(pos);
			String elName = el.nodeName();
			if(StringUtil.in(elName, targetNames))
				return true;
			if(StringUtil.in(elName, baseTypes))
				return false;
			if(extraTypes != null && StringUtil.in(elName, extraTypes))
				return false;
		}
		return false;
	}

	boolean inScope(String[] targetNames)
	{
		return inSpecificScope(targetNames, TagsSearchInScope, null);
	}

	boolean inScope(String targetName)
	{
		return inScope(targetName, null);
	}

	boolean inScope(String targetName, String[] extras)
	{
		return inSpecificScope(targetName, TagsSearchInScope, extras);
	}

	boolean inListItemScope(String targetName)
	{
		return inScope(targetName, TagSearchList);
	}

	boolean inButtonScope(String targetName)
	{
		return inScope(targetName, TagSearchButton);
	}

	boolean inTableScope(String targetName)
	{
		return inSpecificScope(targetName, TagSearchTableScope, null);
	}

	boolean inSelectScope(String targetName)
	{
		for(int pos = stack.size() - 1; pos >= 0; pos--)
		{
			Element el = stack.get(pos);
			String elName = el.nodeName();
			if(elName.equals(targetName))
				return true;
			if(!StringUtil.in(elName, TagSearchSelectScope)) // all elements except
				return false;
		}
		return false;
	}

	void setHeadElement(Element headElement)
	{
		this.headElement = headElement;
	}

	Element getHeadElement()
	{
		return headElement;
	}

	boolean isFosterInserts()
	{
		return fosterInserts;
	}

	void setFosterInserts(boolean fosterInserts)
	{
		this.fosterInserts = fosterInserts;
	}

	void newPendingTableCharacters()
	{
		pendingTableCharacters = new ArrayList<String>();
	}

	List<String> getPendingTableCharacters()
	{
		return pendingTableCharacters;
	}

	void setPendingTableCharacters(List<String> pendingTableCharacters)
	{
		this.pendingTableCharacters = pendingTableCharacters;
	}

	void generateImpliedEndTags(String excludeTag)
	{
		while((excludeTag != null && !currentElement().nodeName().equals(excludeTag)) && StringUtil.in(currentElement().nodeName(), TagSearchEndTags))
			pop();
	}

	void generateImpliedEndTags()
	{
		generateImpliedEndTags(null);
	}

	boolean isSpecial(Element el)
	{
		// todo: mathml's mi, mo, mn
		// todo: svg's foreigObject, desc, title
		String name = el.nodeName();
		return StringUtil.in(name, TagSearchSpecial);
	}

	Element lastFormattingElement()
	{
		return formattingElements.size() > 0 ? formattingElements.get(formattingElements.size() - 1) : null;
	}

	Element removeLastFormattingElement()
	{
		int size = formattingElements.size();
		if(size > 0)
			return formattingElements.remove(size - 1);
		else
			return null;
	}

	// active formatting elements
	void pushActiveFormattingElements(Element in)
	{
		int numSeen = 0;
		for(int pos = formattingElements.size() - 1; pos >= 0; pos--)
		{
			Element el = formattingElements.get(pos);
			if(el == null) // marker
				break;
			if(isSameFormattingElement(in, el))
				numSeen++;
			if(numSeen == 3)
			{
				formattingElements.remove(pos);
				break;
			}
		}
		formattingElements.add(in);
	}

	private boolean isSameFormattingElement(Element a, Element b)
	{
		// same if: same namespace, tag, and attributes. Element.equals only checks tag, might in future check children
		return a.nodeName().equals(b.nodeName()) &&
		// a.namespace().equals(b.namespace()) &&
				a.attributes().equals(b.attributes());
	}

	void reconstructFormattingElements()
	{
		Element last = lastFormattingElement();
		if(last == null || onStack(last))
			return;
		Element entry = last;
		int size = formattingElements.size();
		int pos = size - 1;
		boolean skip = false;
		while(true)
		{
			if(pos == 0)
			{ // step 4. if none before, skip to 8
				skip = true;
				break;
			}
			entry = formattingElements.get(--pos); // step 5. one earlier than entry
			if(entry == null || onStack(entry)) // step 6 - neither marker nor on stack
				break; // jump to 8, else continue back to 4
		}
		while(true)
		{
			if(!skip) // step 7: on later than entry
				entry = formattingElements.get(++pos);
			// 8. create new element from element, 9 insert into current node, onto stack
			skip = false; // can only skip increment from 4.
			Element newEl = insertStartTag(entry.nodeName()); // todo: avoid fostering here?
			// newEl.namespace(entry.namespace()); // todo: namespaces
			newEl.attributes().addAll(entry.attributes());
			// 10. replace entry with new entry
			formattingElements.set(pos, newEl);
			// 11
			if(pos == size - 1) // if not last entry in list, jump to 7
				break;
		}
	}

	void clearFormattingElementsToLastMarker()
	{
		while(!formattingElements.isEmpty())
		{
			Element el = removeLastFormattingElement();
			if(el == null)
				break;
		}
	}

	void removeFromActiveFormattingElements(Element el)
	{
		for(int pos = formattingElements.size() - 1; pos >= 0; pos--)
		{
			Element next = formattingElements.get(pos);
			if(next == el)
			{
				formattingElements.remove(pos);
				break;
			}
		}
	}

	boolean isInActiveFormattingElements(Element el)
	{
		return isElementInQueue(formattingElements, el);
	}

	Element getActiveFormattingElement(String nodeName)
	{
		for(int pos = formattingElements.size() - 1; pos >= 0; pos--)
		{
			Element next = formattingElements.get(pos);
			if(next == null) // scope marker
				break;
			else if(next.nodeName().equals(nodeName))
				return next;
		}
		return null;
	}

	void replaceActiveFormattingElement(Element out, Element in)
	{
		replaceInQueue(formattingElements, out, in);
	}

	void insertMarkerToFormattingElements()
	{
		formattingElements.add(null);
	}

	void insertInFosterParent(Node in)
	{
		Element fosterParent;
		Element lastTable = getFromStack("table");
		boolean isLastTableParent = false;
		if(lastTable != null)
		{
			if(lastTable.parent() != null)
			{
				fosterParent = lastTable.parent();
				isLastTableParent = true;
			}
			else
				fosterParent = aboveOnStack(lastTable);
		}
		else
		{ // no table == frag
			fosterParent = stack.get(0);
		}
		if(isLastTableParent)
		{
			lastTable.before(in);
		}
		else
			fosterParent.appendChild(in);
	}

	@Override
	public String toString()
	{
		return "TreeBuilder{" + "currentToken=" + currentToken + ", state=" + state + ", currentElement=" + currentElement() + '}';
	}
	
	static Attributes normalizeAttributes(Attributes attributes, boolean transitionLowerCase)
	{
		if(transitionLowerCase)
		{
			for(Attribute attr : attributes)
			{
				attr.setKey(attr.getKey().toLowerCase());
			}
		}
		return attributes;
	}
}
