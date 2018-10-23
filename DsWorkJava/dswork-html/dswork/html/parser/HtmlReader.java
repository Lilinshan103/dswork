package dswork.html.parser;

import java.util.Arrays;
import java.util.Locale;

public final class HtmlReader
{
	static final char EOF = (char) -1;
	private static final int maxCacheLen = 12;
	private final char[] input;
	private final int length;
	private int pos = 0;
	private int mark = 0;
	private final String[] stringCache = new String[512];

	public HtmlReader(String input)
	{
		this.input = input.toCharArray();
		this.length = this.input.length;
	}

	public int pos()
	{
		return pos;
	}

	public boolean isEmpty()
	{
		return pos >= length;
	}

	public char current()
	{
		return pos >= length ? EOF : input[pos];
	}

	char consume()
	{
		char val = pos >= length ? EOF : input[pos];
		pos++;
		return val;
	}

	void unconsume()
	{
		pos--;
	}

	public void advance()
	{
		pos++;
	}

	void mark()
	{
		mark = pos;
	}

	void rewindToMark()
	{
		pos = mark;
	}

	String consumeAsString()
	{
		return new String(input, pos++, 1);
	}

	int nextIndexOf(char c)
	{
		for(int i = pos; i < length; i++)
		{
			if(c == input[i])
			{
				return i - pos;
			}
		}
		return -1;
	}

	int nextIndexOf(CharSequence seq)
	{
		char startChar = seq.charAt(0);
		for(int offset = pos; offset < length; offset++)
		{
			if(startChar != input[offset])
			{
				while(++offset < length && startChar != input[offset]){}
			}
			int i = offset + 1;
			int last = i + seq.length() - 1;
			if(offset < length && last <= length)
			{
				for(int j = 1; i < last && seq.charAt(j) == input[i]; i++, j++){}
				if(i == last)
				{
					return offset - pos;
				}
			}
		}
		return -1;
	}

	public String consumeTo(char c)
	{
		int offset = nextIndexOf(c);
		if(offset != -1)
		{
			String consumed = cacheString(pos, offset);
			pos += offset;
			return consumed;
		}
		else
		{
			return consumeToEnd();
		}
	}

	String consumeTo(String seq)
	{
		int offset = nextIndexOf(seq);
		if(offset != -1)
		{
			String consumed = cacheString(pos, offset);
			pos += offset;
			return consumed;
		}
		else
		{
			return consumeToEnd();
		}
	}

	public String consumeToAny(final char... chars)
	{
		final int start = pos;
		final int remaining = length;
		final char[] val = input;
		OUTER:while(pos < remaining)
		{
			for(char c : chars)
			{
				if(val[pos] == c)
				{
					break OUTER;
				}
			}
			pos++;
		}
		return pos > start ? cacheString(start, pos - start) : "";
	}

	String consumeToAnySorted(final char... chars)
	{
		final int start = pos;
		final int remaining = length;
		final char[] val = input;
		while(pos < remaining)
		{
			if(Arrays.binarySearch(chars, val[pos]) >= 0)
			{
				break;
			}
			pos++;
		}
		return pos > start ? cacheString(start, pos - start) : "";
	}

	String consumeData()
	{
		final int start = pos;
		final int remaining = length;
		final char[] val = input;
		while(pos < remaining)
		{
			final char c = val[pos];
			if(c == '&' || c == '<' || c == TokeniserState.nullChar)
			{
				break;
			}
			pos++;
		}
		return pos > start ? cacheString(start, pos - start) : "";
	}

	String consumeTagName()
	{
		// '\t', '\n', '\r', '\f', ' ', '/', '>', nullChar
		final int start = pos;
		final int remaining = length;
		final char[] val = input;
		while(pos < remaining)
		{
			final char c = val[pos];
			if(c == '\t' || c == '\n' || c == '\r' || c == '\f' || c == ' ' || c == '/' || c == '>' || c == TokeniserState.nullChar)
			{
				break;
			}
			pos++;
		}
		return pos > start ? cacheString(start, pos - start) : "";
	}

	String consumeToEnd()
	{
		String data = cacheString(pos, length - pos);
		pos = length;
		return data;
	}

	String consumeLetterSequence()
	{
		int start = pos;
		while(pos < length)
		{
			char c = input[pos];
			if((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || Character.isLetter(c))
			{
				pos++;
			}
			else
			{
				break;
			}
		}
		return cacheString(start, pos - start);
	}

	String consumeLetterThenDigitSequence()
	{
		int start = pos;
		while(pos < length)
		{
			char c = input[pos];
			if((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || Character.isLetter(c))
			{
				pos++;
			}
			else
			{
				break;
			}
		}
		while(!isEmpty())
		{
			char c = input[pos];
			if(c >= '0' && c <= '9')
			{
				pos++;
			}
			else
			{
				break;
			}
		}
		return cacheString(start, pos - start);
	}

	String consumeHexSequence()
	{
		int start = pos;
		while(pos < length)
		{
			char c = input[pos];
			if((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f'))
			{
				pos++;
			}
			else
			{
				break;
			}
		}
		return cacheString(start, pos - start);
	}

	String consumeDigitSequence()
	{
		int start = pos;
		while(pos < length)
		{
			char c = input[pos];
			if(c >= '0' && c <= '9')
			{
				pos++;
			}
			else
			{
				break;
			}
		}
		return cacheString(start, pos - start);
	}

	boolean matches(char c)
	{
		return !isEmpty() && input[pos] == c;
	}

	boolean matches(String seq)
	{
		int scanLength = seq.length();
		if(scanLength > length - pos)
		{
			return false;
		}
		for(int offset = 0; offset < scanLength; offset++)
		{
			if(seq.charAt(offset) != input[pos + offset])
			{
				return false;
			}
		}
		return true;
	}

	boolean matchesIgnoreCase(String seq)
	{
		int scanLength = seq.length();
		if(scanLength > length - pos)
		{
			return false;
		}
		for(int offset = 0; offset < scanLength; offset++)
		{
			char upScan = Character.toUpperCase(seq.charAt(offset));
			char upTarget = Character.toUpperCase(input[pos + offset]);
			if(upScan != upTarget)
			{
				return false;
			}
		}
		return true;
	}

	boolean matchesAny(char... seq)
	{
		if(isEmpty())
		{
			return false;
		}
		char c = input[pos];
		for(char seek : seq)
		{
			if(seek == c)
			{
				return true;
			}
		}
		return false;
	}

	boolean matchesAnySorted(char[] seq)
	{
		return !isEmpty() && Arrays.binarySearch(seq, input[pos]) >= 0;
	}

	boolean matchesLetter()
	{
		if(isEmpty())
		{
			return false;
		}
		char c = input[pos];
		return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || Character.isLetter(c);
	}

	boolean matchesDigit()
	{
		if(isEmpty())
		{
			return false;
		}
		char c = input[pos];
		return (c >= '0' && c <= '9');
	}

	boolean matchConsume(String seq)
	{
		if(matches(seq))
		{
			pos += seq.length();
			return true;
		}
		else
		{
			return false;
		}
	}

	boolean matchConsumeIgnoreCase(String seq)
	{
		if(matchesIgnoreCase(seq))
		{
			pos += seq.length();
			return true;
		}
		else
		{
			return false;
		}
	}

	boolean containsIgnoreCase(String seq)
	{
		String loScan = seq.toLowerCase(Locale.ENGLISH);
		String hiScan = seq.toUpperCase(Locale.ENGLISH);
		return (nextIndexOf(loScan) > -1) || (nextIndexOf(hiScan) > -1);
	}

	@Override
	public String toString()
	{
		return new String(input, pos, length - pos);
	}

	private String cacheString(final int start, final int count)
	{
		final char[] val = input;
		final String[] cache = stringCache;
		if(count > maxCacheLen)
		{
			return new String(val, start, count);
		}
		int hash = 0;
		int offset = start;
		for(int i = 0; i < count; i++)
		{
			hash = 31 * hash + val[offset++];
		}
		final int index = hash & cache.length - 1;
		String cached = cache[index];
		if(cached == null)
		{ // miss, add
			cached = new String(val, start, count);
			cache[index] = cached;
		}
		else
		{
			if(rangeEquals(start, count, cached))
			{
				return cached;
			}
			else
			{
				cached = new String(val, start, count);
				cache[index] = cached;
			}
		}
		return cached;
	}

	boolean rangeEquals(final int start, int count, final String cached)
	{
		if(count == cached.length())
		{
			char one[] = input;
			int i = start;
			int j = 0;
			while(count-- != 0)
			{
				if(one[i++] != cached.charAt(j++))
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
