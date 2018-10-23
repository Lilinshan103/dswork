package dswork.html.parser;

import dswork.html.nodes.Attribute;
import dswork.html.nodes.Attributes;

abstract class Token
{
	TokenType type;

	private Token()
	{
	}

	String tokenType()
	{
		return this.getClass().getSimpleName();
	}

	abstract Token reset();

	static void reset(StringBuilder sb)
	{
		if(sb != null)
		{
			sb.delete(0, sb.length());
		}
	}

	static final class Doctype extends Token
	{
		final StringBuilder name = new StringBuilder();
		String pubSysKey = null;
		final StringBuilder publicIdentifier = new StringBuilder();
		final StringBuilder systemIdentifier = new StringBuilder();

		Doctype()
		{
			type = TokenType.Doctype;
		}

		@Override
		Token reset()
		{
			reset(name);
			pubSysKey = null;
			reset(publicIdentifier);
			reset(systemIdentifier);
			return this;
		}

		String getName()
		{
			return name.toString();
		}

		String getPubSysKey()
		{
			return pubSysKey;
		}

		String getPublicIdentifier()
		{
			return publicIdentifier.toString();
		}

		public String getSystemIdentifier()
		{
			return systemIdentifier.toString();
		}
	}

	static abstract class Tag extends Token
	{
		protected String tagName;
		protected String normalName; // lc version of tag name, for case insensitive tree build
		private String pendingAttributeName; // attribute names are generally caught in one hop, not accumulated
		private StringBuilder pendingAttributeValue = new StringBuilder(); // but values are accumulated, from e.g. & in
																			// hrefs
		private String pendingAttributeValueS; // try to get attr vals in one shot, vs Builder
		private boolean hasEmptyAttributeValue = false; // distinguish boolean attribute from empty string value
		private boolean hasPendingAttributeValue = false;
		boolean selfClosing = false;
		Attributes attributes; // start tags get attributes on construction. End tags get attributes on first new attribute (but only for parser convenience, not used).

		@Override
		Tag reset()
		{
			tagName = null;
			normalName = null;
			pendingAttributeName = null;
			reset(pendingAttributeValue);
			pendingAttributeValueS = null;
			hasEmptyAttributeValue = false;
			hasPendingAttributeValue = false;
			selfClosing = false;
			attributes = null;
			return this;
		}

		final void newAttribute()
		{
			if(attributes == null)
				attributes = new Attributes();
			if(pendingAttributeName != null)
			{
				pendingAttributeName = pendingAttributeName.trim();
				if(pendingAttributeName.length() > 0)
				{
					Attribute attribute;
					if(hasPendingAttributeValue)
						attribute = new Attribute(pendingAttributeName, pendingAttributeValue.length() > 0 ? pendingAttributeValue.toString() : pendingAttributeValueS);
					else if(hasEmptyAttributeValue)
						attribute = new Attribute(pendingAttributeName, "");
					else
						attribute = new Attribute(pendingAttributeName, pendingAttributeName);
					attributes.put(attribute);
				}
			}
			pendingAttributeName = null;
			hasEmptyAttributeValue = false;
			hasPendingAttributeValue = false;
			reset(pendingAttributeValue);
			pendingAttributeValueS = null;
		}

		final void finaliseTag()
		{
			if(pendingAttributeName != null)
			{
				newAttribute();
			}
		}

		final String name()
		{
			return tagName;
		}

		final String normalName()
		{
			return normalName;
		}

		final Tag name(String name)
		{
			tagName = name;
			normalName = name.toLowerCase();
			return this;
		}

		final boolean isSelfClosing()
		{
			return selfClosing;
		}

		final Attributes getAttributes()
		{
			return attributes;
		}

		final void appendTagName(String append)
		{
			tagName = tagName == null ? append : tagName.concat(append);
			normalName = tagName.toLowerCase();
		}

		final void appendTagName(char append)
		{
			appendTagName(String.valueOf(append));
		}

		final void appendAttributeName(String append)
		{
			pendingAttributeName = pendingAttributeName == null ? append : pendingAttributeName.concat(append);
		}

		final void appendAttributeName(char append)
		{
			appendAttributeName(String.valueOf(append));
		}

		final void appendAttributeValue(String append)
		{
			ensureAttributeValue();
			if(pendingAttributeValue.length() == 0)
			{
				pendingAttributeValueS = append;
			}
			else
			{
				pendingAttributeValue.append(append);
			}
		}

		final void appendAttributeValue(char append)
		{
			ensureAttributeValue();
			pendingAttributeValue.append(append);
		}

		final void appendAttributeValue(char[] append)
		{
			ensureAttributeValue();
			pendingAttributeValue.append(append);
		}

		final void appendAttributeValue(int[] appendCodepoints)
		{
			ensureAttributeValue();
			for(int codepoint : appendCodepoints)
			{
				pendingAttributeValue.appendCodePoint(codepoint);
			}
		}

		final void setEmptyAttributeValue()
		{
			hasEmptyAttributeValue = true;
		}

		private void ensureAttributeValue()
		{
			hasPendingAttributeValue = true;
			if(pendingAttributeValueS != null)
			{
				pendingAttributeValue.append(pendingAttributeValueS);
				pendingAttributeValueS = null;
			}
		}
	}

	final static class StartTag extends Tag
	{
		StartTag()
		{
			super();
			attributes = new Attributes();
			type = TokenType.StartTag;
		}

		@Override
		Tag reset()
		{
			super.reset();
			attributes = new Attributes();
			return this;
		}

		StartTag nameAttr(String name, Attributes attributes)
		{
			this.tagName = name;
			this.attributes = attributes;
			normalName = tagName.toLowerCase();
			return this;
		}

		@Override
		public String toString()
		{
			if(attributes != null && attributes.size() > 0)
				return "<" + name() + " " + attributes.toString() + ">";
			else
				return "<" + name() + ">";
		}
	}

	final static class EndTag extends Tag
	{
		EndTag()
		{
			super();
			type = TokenType.EndTag;
		}

		@Override
		public String toString()
		{
			return "</" + name() + ">";
		}
	}

	final static class Comment extends Token
	{
		final StringBuilder data = new StringBuilder();
		boolean bogus = false;

		@Override
		Token reset()
		{
			reset(data);
			bogus = false;
			return this;
		}

		Comment()
		{
			type = TokenType.Comment;
		}

		String getData()
		{
			return data.toString();
		}

		@Override
		public String toString()
		{
			return "<!--" + getData() + "-->";
		}
	}

	final static class Character extends Token
	{
		private String data;

		Character()
		{
			super();
			type = TokenType.Character;
		}

		@Override
		Token reset()
		{
			data = null;
			return this;
		}

		Character data(String data)
		{
			this.data = data;
			return this;
		}

		String getData()
		{
			return data;
		}

		@Override
		public String toString()
		{
			return getData();
		}
	}

	final static class EOF extends Token
	{
		EOF()
		{
			type = Token.TokenType.EOF;
		}

		@Override
		Token reset()
		{
			return this;
		}
	}

	final boolean isDoctype()
	{
		return type == TokenType.Doctype;
	}

	final Doctype asDoctype()
	{
		return (Doctype) this;
	}

	final boolean isStartTag()
	{
		return type == TokenType.StartTag;
	}

	final StartTag asStartTag()
	{
		return (StartTag) this;
	}

	final boolean isEndTag()
	{
		return type == TokenType.EndTag;
	}

	final EndTag asEndTag()
	{
		return (EndTag) this;
	}

	final boolean isComment()
	{
		return type == TokenType.Comment;
	}

	final Comment asComment()
	{
		return (Comment) this;
	}

	final boolean isCharacter()
	{
		return type == TokenType.Character;
	}

	final Character asCharacter()
	{
		return (Character) this;
	}

	final boolean isEOF()
	{
		return type == TokenType.EOF;
	}

	enum TokenType
	{
		Doctype, StartTag, EndTag, Comment, Character, EOF
	}
}
