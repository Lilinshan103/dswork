package dswork.html.select;

import java.util.ArrayList;
import java.util.List;

import dswork.html.nodes.Element;
import dswork.html.nodes.Node;

/**
 * 查询Util
 */
public class SelectUtil
{
	private SelectUtil()
	{
	}

	public static List<Element> select(Evaluator eval, Element root)
	{
		List<Element> elements = new ArrayList<Element>();
		new NodeTraversor(new Accumulator(root, elements, eval)).traverse(root);
		return elements;
	}

	private static class Accumulator implements NodeVisitor
	{
		private final Element root;
		private final List<Element> elements;
		private final Evaluator eval;

		Accumulator(Element root, List<Element> elements, Evaluator eval)
		{
			this.root = root;
			this.elements = elements;
			this.eval = eval;
		}

		public void head(Node node, int depth)
		{
			if(node instanceof Element)
			{
				Element el = (Element) node;
				if(eval.matches(root, el))
					elements.add(el);
			}
		}

		public void tail(Node node, int depth)
		{
			// void
		}
	}
}
