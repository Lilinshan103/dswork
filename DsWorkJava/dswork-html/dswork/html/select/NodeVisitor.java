package dswork.html.select;

import dswork.html.nodes.Node;

public interface NodeVisitor
{
	void head(Node node, int depth);

	void tail(Node node, int depth);
}
