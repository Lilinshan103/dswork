package dswork.html.parser;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public final class StringUtil
{
	public static String join(Collection<?> strings, String sep)
	{
		return join(strings.iterator(), sep);
	}

	public static String join(Iterator<?> strings, String sep)
	{
		if(!strings.hasNext())
			return "";
		String start = strings.next().toString();
		if(!strings.hasNext()) // only one, avoid builder
			return start;
		StringBuilder sb = new StringBuilder(64).append(start);
		while(strings.hasNext())
		{
			sb.append(sep);
			sb.append(strings.next());
		}
		return sb.toString();
	}

	public static boolean isBlank(String string)
	{
		if(string == null || string.length() == 0)
			return true;
		int l = string.length();
		for(int i = 0; i < l; i++)
		{
			if(!StringUtil.isWhitespace(string.codePointAt(i)))
				return false;
		}
		return true;
	}

	public static boolean isWhitespace(int c)
	{
		return c == ' ' || c == '\t' || c == '\n' || c == '\f' || c == '\r';
	}

	public static String normaliseWhitespace(String string)
	{
		StringBuilder sb = new StringBuilder(string.length());
		appendNormalisedWhitespace(sb, string, false);
		return sb.toString();
	}

	public static void appendNormalisedWhitespace(StringBuilder accum, String string, boolean stripLeading)
	{
		boolean lastWasWhite = false;
		boolean reachedNonWhite = false;
		int len = string.length();
		int c;
		for(int i = 0; i < len; i += Character.charCount(c))
		{
			c = string.codePointAt(i);
			if(isWhitespace(c))
			{
				if((stripLeading && !reachedNonWhite) || lastWasWhite)
					continue;
				accum.append(' ');
				lastWasWhite = true;
			}
			else
			{
				accum.appendCodePoint(c);
				lastWasWhite = false;
				reachedNonWhite = true;
			}
		}
	}

	public static boolean in(String needle, String... haystack)
	{
		for(String hay : haystack)
		{
			if(hay.equals(needle))
				return true;
		}
		return false;
	}

	public static boolean inSorted(String needle, String[] haystack)
	{
		return Arrays.binarySearch(haystack, needle) >= 0;
	}
}
