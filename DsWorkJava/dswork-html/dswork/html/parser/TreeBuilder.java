package dswork.html.parser;

import java.util.ArrayList;

import dswork.html.nodes.Attributes;
import dswork.html.nodes.Document;
import dswork.html.nodes.Element;

abstract class TreeBuilder
{
	HtmlReader reader;
	Tokeniser tokeniser;
	protected Document doc; // current doc we are building into
	protected ArrayList<Element> stack; // the stack of open elements
	protected Token currentToken; // currentToken is used only for error tracking.
	private Token.StartTag start = new Token.StartTag(); // start tag to process
	private Token.EndTag end = new Token.EndTag();
	protected boolean transitionLowerCase = true;

	protected void initialiseParse(String input, boolean transitionLowerCase)
	{
		doc = new Document("");
		this.transitionLowerCase = transitionLowerCase;
		reader = new HtmlReader(input);
		tokeniser = new Tokeniser(reader);
		stack = new ArrayList<Element>(32);
	}

	Document parse(String input, boolean transitionLowerCase)
	{
		initialiseParse(input, transitionLowerCase);
		runParser();
		return doc;
	}

	protected void runParser()
	{
		while(true)
		{
			Token token = tokeniser.read();
			process(token);
			token.reset();
			if(token.type == Token.TokenType.EOF)
				break;
		}
	}

	protected abstract boolean process(Token token);

	protected boolean processStartTag(String name)
	{
		if(currentToken == start)
		{ // don't recycle an in-use token
			return process(new Token.StartTag().name(name));
		}
		return process(start.reset().name(name));
	}

	public boolean processStartTag(String name, Attributes attrs)
	{
		if(currentToken == start)
		{ // don't recycle an in-use token
			return process(new Token.StartTag().nameAttr(name, attrs));
		}
		start.reset();
		start.nameAttr(name, attrs);
		return process(start);
	}

	protected boolean processEndTag(String name)
	{
		if(currentToken == end)
		{ // don't recycle an in-use token
			return process(new Token.EndTag().name(name));
		}
		return process(end.reset().name(name));
	}

	protected Element currentElement()
	{
		int size = stack.size();
		return size > 0 ? stack.get(size - 1) : null;
	}
}
