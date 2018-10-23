package dswork.html.select;

import dswork.html.nodes.Element;

/**
 * Base structural evaluator.
 */
abstract class EvaluatorStructural extends Evaluator
{
	Evaluator evaluator;

	static class Root extends Evaluator
	{
		public boolean matches(Element root, Element element)
		{
			return root == element;
		}
	}

	static class Has extends EvaluatorStructural
	{
		public Has(Evaluator evaluator)
		{
			this.evaluator = evaluator;
		}

		public boolean matches(Element root, Element element)
		{
			for(Element e : element.getAllElements())
			{
				if(e != element && evaluator.matches(root, e))
					return true;
			}
			return false;
		}

		@Override
		public String toString()
		{
			return String.format(":has(%s)", evaluator);
		}
	}

	static class Not extends EvaluatorStructural
	{
		public Not(Evaluator evaluator)
		{
			this.evaluator = evaluator;
		}

		public boolean matches(Element root, Element node)
		{
			return !evaluator.matches(root, node);
		}

		@Override
		public String toString()
		{
			return String.format(":not%s", evaluator);
		}
	}

	static class Parent extends EvaluatorStructural
	{
		public Parent(Evaluator evaluator)
		{
			this.evaluator = evaluator;
		}

		public boolean matches(Element root, Element element)
		{
			if(root == element)
				return false;
			Element parent = element.parent();
			while(true)
			{
				if(evaluator.matches(root, parent))
					return true;
				if(parent == root)
					break;
				parent = parent.parent();
			}
			return false;
		}

		@Override
		public String toString()
		{
			return String.format(":parent%s", evaluator);
		}
	}

	static class ImmediateParent extends EvaluatorStructural
	{
		public ImmediateParent(Evaluator evaluator)
		{
			this.evaluator = evaluator;
		}

		public boolean matches(Element root, Element element)
		{
			if(root == element)
				return false;
			Element parent = element.parent();
			return parent != null && evaluator.matches(root, parent);
		}

		@Override
		public String toString()
		{
			return String.format(":ImmediateParent%s", evaluator);
		}
	}

	static class PreviousSibling extends EvaluatorStructural
	{
		public PreviousSibling(Evaluator evaluator)
		{
			this.evaluator = evaluator;
		}

		public boolean matches(Element root, Element element)
		{
			if(root == element)
				return false;
			Element prev = element.previousElementSibling();
			while(prev != null)
			{
				if(evaluator.matches(root, prev))
					return true;
				prev = prev.previousElementSibling();
			}
			return false;
		}

		@Override
		public String toString()
		{
			return String.format(":prev*%s", evaluator);
		}
	}

	static class ImmediatePreviousSibling extends EvaluatorStructural
	{
		public ImmediatePreviousSibling(Evaluator evaluator)
		{
			this.evaluator = evaluator;
		}

		public boolean matches(Element root, Element element)
		{
			if(root == element)
				return false;
			Element prev = element.previousElementSibling();
			return prev != null && evaluator.matches(root, prev);
		}

		@Override
		public String toString()
		{
			return String.format(":prev%s", evaluator);
		}
	}
}
