package dswork.html.nodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import dswork.html.select.NodeTraversor;
import dswork.html.select.NodeVisitor;

public abstract class Node implements Cloneable
{
	private static final List<Node> EMPTY_NODES = Collections.emptyList();
	Node parentNode;
	List<Node> childNodes;
	Attributes attributes;
	int siblingIndex;

	protected Node(Attributes attributes)
	{
		childNodes = EMPTY_NODES;
		this.attributes = attributes;
	}

	protected Node()
	{
		childNodes = EMPTY_NODES;
		attributes = null;
	}

	public abstract String nodeName();

	public String attr(String attributeKey)
	{
		String val = attributes.getIgnoreCase(attributeKey);
		if(val.length() > 0)
			return val;
		else
			return "";
	}

	public Attributes attributes()
	{
		return attributes;
	}

	public Node attr(String attributeKey, String attributeValue)
	{
		attributes.put(attributeKey, attributeValue);
		return this;
	}

	public boolean hasAttr(String attributeKey)
	{
		return attributes.hasKeyIgnoreCase(attributeKey);
	}

	public Node removeAttr(String attributeKey)
	{
		attributes.removeIgnoreCase(attributeKey);
		return this;
	}

	public Node childNode(int index)
	{
		return childNodes.get(index);
	}

	public List<Node> childNodes()
	{
		return Collections.unmodifiableList(childNodes);
	}

	public List<Node> childNodesCopy()
	{
		List<Node> children = new ArrayList<Node>(childNodes.size());
		for(Node node : childNodes)
		{
			children.add(node.clone());
		}
		return children;
	}

	public final int childNodeSize()
	{
		return childNodes.size();
	}

	protected Node[] childNodesAsArray()
	{
		return childNodes.toArray(new Node[childNodeSize()]);
	}

	public Node parent()
	{
		return parentNode;
	}

	public final Node parentNode()
	{
		return parentNode;
	}

	public Node root()
	{
		Node node = this;
		while(node.parentNode != null)
			node = node.parentNode;
		return node;
	}

	public Document ownerDocument()
	{
		Node root = root();
		return (root instanceof Document) ? (Document) root : null;
	}

	public Document rootDocument()
	{
		Document owner = ownerDocument();
		return owner != null ? owner: (new Document(""));
	}

	public void remove()
	{
		parentNode.removeChild(this);
	}

	public Node before(String html)
	{
		addSiblingHtml(siblingIndex, html);
		return this;
	}

	public Node before(Node node)
	{
		parentNode.addChildren(siblingIndex, node);
		return this;
	}

	public Node after(String html)
	{
		addSiblingHtml(siblingIndex + 1, html);
		return this;
	}

	public Node after(Node node)
	{
		parentNode.addChildren(siblingIndex + 1, node);
		return this;
	}

	private void addSiblingHtml(int index, String html)
	{
		Element context = parent() instanceof Element ? (Element) parent() : null;
		List<Node> nodes = Node.parseFragment(html, context);
		parentNode.addChildren(index, nodes.toArray(new Node[nodes.size()]));
	}

	public Node wrap(String html)
	{
		Element context = parent() instanceof Element ? (Element) parent() : null;
		List<Node> wrapChildren = Node.parseFragment(html, context);
		Node wrapNode = wrapChildren.get(0);
		if(wrapNode == null || !(wrapNode instanceof Element)) // nothing to wrap with; noop
			return null;
		Element wrap = (Element) wrapNode;
		Element deepest = getDeepChild(wrap);
		parentNode.replaceChild(this, wrap);
		deepest.addChildren(this);
		// remainder (unbalanced wrap, like <div></div><p></p> -- The <p> is remainder
		if(wrapChildren.size() > 0)
		{
			for(int i = 0; i < wrapChildren.size(); i++)
			{
				Node remainder = wrapChildren.get(i);
				remainder.parentNode.removeChild(remainder);
				wrap.appendChild(remainder);
			}
		}
		return this;
	}

	public Node unwrap()
	{
		Node firstChild = childNodes.size() > 0 ? childNodes.get(0) : null;
		parentNode.addChildren(siblingIndex, this.childNodesAsArray());
		this.remove();
		return firstChild;
	}

	private Element getDeepChild(Element el)
	{
		List<Element> children = el.children();
		if(children.size() > 0)
			return getDeepChild(children.get(0));
		else
			return el;
	}

	public void replaceWith(Node in)
	{
		parentNode.replaceChild(this, in);
	}

	protected void setParentNode(Node parentNode)
	{
		if(this.parentNode != null)
			this.parentNode.removeChild(this);
		this.parentNode = parentNode;
	}

	protected void replaceChild(Node out, Node in)
	{
		if(in.parentNode != null)
			in.parentNode.removeChild(in);
		final int index = out.siblingIndex;
		childNodes.set(index, in);
		in.parentNode = this;
		in.setSiblingIndex(index);
		out.parentNode = null;
	}

	protected void removeChild(Node out)
	{
		final int index = out.siblingIndex;
		childNodes.remove(index);
		reindexChildren(index);
		out.parentNode = null;
	}

	protected void addChildren(Node... children)
	{
		// most used. short circuit addChildren(int), which hits reindex children and array copy
		for(Node child : children)
		{
			reparentChild(child);
			ensureChildNodes();
			childNodes.add(child);
			child.setSiblingIndex(childNodes.size() - 1);
		}
	}

	protected void addChildren(int index, Node... children)
	{
		ensureChildNodes();
		for(int i = children.length - 1; i >= 0; i--)
		{
			Node in = children[i];
			reparentChild(in);
			childNodes.add(index, in);
			reindexChildren(index);
		}
	}

	protected void ensureChildNodes()
	{
		if(childNodes == EMPTY_NODES)
		{
			childNodes = new ArrayList<Node>(4);
		}
	}

	protected void reparentChild(Node child)
	{
		if(child.parentNode != null)
			child.parentNode.removeChild(child);
		child.setParentNode(this);
	}

	private void reindexChildren(int start)
	{
		for(int i = start; i < childNodes.size(); i++)
		{
			childNodes.get(i).setSiblingIndex(i);
		}
	}

	public List<Node> siblingNodes()
	{
		if(parentNode == null)
			return Collections.emptyList();
		List<Node> nodes = parentNode.childNodes;
		List<Node> siblings = new ArrayList<Node>(nodes.size() - 1);
		for(Node node : nodes)
			if(node != this)
				siblings.add(node);
		return siblings;
	}

	public Node nextSibling()
	{
		if(parentNode == null)
			return null; // root
		final List<Node> siblings = parentNode.childNodes;
		final int index = siblingIndex + 1;
		if(siblings.size() > index)
			return siblings.get(index);
		else
			return null;
	}

	public Node previousSibling()
	{
		if(parentNode == null)
			return null; // root
		if(siblingIndex > 0)
			return parentNode.childNodes.get(siblingIndex - 1);
		else
			return null;
	}

	public int siblingIndex()
	{
		return siblingIndex;
	}

	protected void setSiblingIndex(int siblingIndex)
	{
		this.siblingIndex = siblingIndex;
	}

	public Node traverse(NodeVisitor nodeVisitor)
	{
		NodeTraversor traversor = new NodeTraversor(nodeVisitor);
		traversor.traverse(this);
		return this;
	}

	public String outerHtml()
	{
		StringBuilder accum = new StringBuilder(128);
		outerHtml(accum);
		return accum.toString();
	}

	protected void outerHtml(Appendable accum)
	{
		new NodeTraversor(new OuterHtmlVisitor(accum, rootDocument())).traverse(this);
	}

	abstract void outerHtmlHead(Appendable accum, int depth, Document out) throws IOException;

	abstract void outerHtmlTail(Appendable accum, int depth, Document out) throws IOException;

	public <T extends Appendable> T html(T appendable)
	{
		outerHtml(appendable);
		return appendable;
	}

	public String toString()
	{
		return outerHtml();
	}

	private static final String[] padding =
	{
			"", "\t", "\t\t", "\t\t\t", "\t\t\t\t", "\t\t\t\t\t", "\t\t\t\t\t\t", "\t\t\t\t\t\t\t", "\t\t\t\t\t\t\t\t", "\t\t\t\t\t\t\t\t\t", "\t\t\t\t\t\t\t\t\t\t"
	};
	private static String padding(int width)
	{
		if(width < padding.length)
			return padding[width];
		char[] out = new char[width];
		for(int i = 0; i < width; i++)
			out[i] = '\t';
		return String.valueOf(out);
	}
	protected void indent(Appendable accum, int depth) throws IOException
	{
		accum.append("\n").append(Node.padding(depth));
	}

	@Override
	public boolean equals(Object o)
	{
		// implemented just so that javadoc is clear this is an identity test
		return this == o;
	}

	public boolean hasSameValue(Object o)
	{
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		return this.outerHtml().equals(((Node) o).outerHtml());
	}

	@Override
	public Node clone()
	{
		Node thisClone = doClone(null); // splits for orphan
		// Queue up nodes that need their children cloned (BFS).
		LinkedList<Node> nodesToProcess = new LinkedList<Node>();
		nodesToProcess.add(thisClone);
		while(!nodesToProcess.isEmpty())
		{
			Node currParent = nodesToProcess.remove();
			for(int i = 0; i < currParent.childNodes.size(); i++)
			{
				Node childClone = currParent.childNodes.get(i).doClone(currParent);
				currParent.childNodes.set(i, childClone);
				nodesToProcess.add(childClone);
			}
		}
		return thisClone;
	}

	protected Node doClone(Node parent)
	{
		Node clone;
		try
		{
			clone = (Node) super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			throw new RuntimeException(e);
		}
		clone.parentNode = parent; // can be null, to create an orphan split
		clone.siblingIndex = parent == null ? 0 : siblingIndex;
		clone.attributes = attributes != null ? attributes.clone() : null;
		clone.childNodes = new ArrayList<Node>(childNodes.size());
		for(Node child : childNodes)
			clone.childNodes.add(child);
		return clone;
	}

	private static class OuterHtmlVisitor implements NodeVisitor
	{
		private Appendable accum;
		private Document out;

		OuterHtmlVisitor(Appendable accum, Document out)
		{
			this.accum = accum;
			this.out = out;
		}

		public void head(Node node, int depth)
		{
			try
			{
				node.outerHtmlHead(accum, depth, out);
			}
			catch(IOException exception)
			{
				exception.printStackTrace();
			}
		}

		public void tail(Node node, int depth)
		{
			if(!node.nodeName().equals("#text"))
			{
				try
				{
					node.outerHtmlTail(accum, depth, out);
				}
				catch(IOException exception)
				{
					exception.printStackTrace();
				}
			}
		}
	}
	
	protected static List<Node> parseFragment(String fragmentHtml, Element context)
	{
		return new dswork.html.parser.TreeBuilderHtml().parseFragment(fragmentHtml, context, true);
	}
}
