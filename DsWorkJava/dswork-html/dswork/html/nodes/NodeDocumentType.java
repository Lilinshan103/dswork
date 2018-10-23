package dswork.html.nodes;

import java.io.IOException;

import dswork.html.parser.StringUtil;

/**
 * DOCTYPE标签
 */
public class NodeDocumentType extends Node
{
	public static final String PUBLIC_KEY = "PUBLIC";
	public static final String SYSTEM_KEY = "SYSTEM";
	private static final String NAME = "name";
	private static final String PUB_SYS_KEY = "pubSysKey"; // PUBLIC or SYSTEM
	private static final String PUBLIC_ID = "publicId";
	private static final String SYSTEM_ID = "systemId";

	public NodeDocumentType(String name, String publicId, String systemId)
	{
		super(new Attributes());
		attr(NAME, name);
		attr(PUBLIC_ID, publicId);
		if(has(PUBLIC_ID))
		{
			attr(PUB_SYS_KEY, PUBLIC_KEY);
		}
		attr(SYSTEM_ID, systemId);
	}

	public NodeDocumentType(String name, String pubSysKey, String publicId, String systemId)
	{
		super(new Attributes());
		attr(NAME, name);
		if(pubSysKey != null)
		{
			attr(PUB_SYS_KEY, pubSysKey);
		}
		attr(PUBLIC_ID, publicId);
		attr(SYSTEM_ID, systemId);
	}

	@Override
	public String nodeName()
	{
		return "#doctype";
	}

	@Override
	void outerHtmlHead(Appendable accum, int depth, Document out) throws IOException
	{
		if(!has(PUBLIC_ID) && !has(SYSTEM_ID))
		{
			accum.append("<!doctype");
		}
		else
		{
			accum.append("<!DOCTYPE");
		}
		if(has(NAME))
			accum.append(" ").append(attr(NAME));
		if(has(PUB_SYS_KEY))
			accum.append(" ").append(attr(PUB_SYS_KEY));
		if(has(PUBLIC_ID))
			accum.append(" \"").append(attr(PUBLIC_ID)).append('"');
		if(has(SYSTEM_ID))
			accum.append(" \"").append(attr(SYSTEM_ID)).append('"');
		accum.append('>');
	}

	@Override
	void outerHtmlTail(Appendable accum, int depth, Document out)
	{
	}

	private boolean has(final String attribute)
	{
		return !StringUtil.isBlank(attr(attribute));
	}
}
