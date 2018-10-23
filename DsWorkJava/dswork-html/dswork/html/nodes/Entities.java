package dswork.html.nodes;

import static dswork.html.nodes.Entities.EscapeMode.mycharacters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;

import dswork.html.parser.StringUtil;

/**
 * 只放了几个常用的转码，可自行扩展
 * http://www.w3.org/TR/html5/named-character-references.html#named-character-references
 */
public class Entities
{
	private static final int empty = -1;
	private static final String emptyName = "";

	public enum EscapeMode
	{
		mycharacters();
		private int[] codeVals;
		private String[] nameVals;

		EscapeMode()
		{
			String[] arr = {
					 "AMP=38"
					,"COPY=169"
					,"GT=62"
					,"LT=60"
					,"QUOT=34"
					,"REG=174"
					,"acute=180"
					,"amp=38"
					,"copy=169"
					,"curren=164"
					,"deg=176"
					,"divide=247"
					,"frac12=189"
					,"frac14=188"
					,"frac34=190"
					,"gt=62"
					,"laquo=171"
					,"lt=60"
					,"middot=183"
					,"nbsp=160"
					,"para=182"
					,"plusmn=177"
					,"pound=163"
					,"quot=34"
					,"raquo=187"
					,"reg=174"
					,"sect=167"
					,"shy=173"
					,"sup1=185"
					,"sup2=178"
					,"sup3=179"
					,"szlig=223"
					,"thorn=254"
					,"times=215"
					,"uml=168"
					,"yen=165"
			};
			this.nameVals = new String[arr.length];
			this.codeVals = new int[arr.length];
			for(int j = 0; j < arr.length; j++)
			{
				String[] ss = arr[j].split("=", -1);
				this.nameVals[j] = ss[0];
				this.codeVals[j] = Integer.parseInt(ss[1], 10);
			}
		}

		int codepointForName(final String name)
		{
			int index = Arrays.binarySearch(nameVals, name);
			return index >= 0 ? codeVals[index] : empty;
		}

		String nameForCodepoint(final int codepoint)
		{
			final int index = Arrays.binarySearch(codeVals, codepoint);
			if(index >= 0)
			{
				return (index < nameVals.length - 1 && codeVals[index + 1] == codepoint) ? nameVals[index + 1] : nameVals[index];
			}
			return emptyName;
		}
	}
	
	private Entities()
	{
	}

	public static boolean isNamedEntity(final String name)
	{
		return mycharacters.codepointForName(name) != empty;
	}

	public static String getByName(String name)
	{
		int codepoint = mycharacters.codepointForName(name);
		if(codepoint != empty)
			return new String(new int[]{codepoint}, 0, 1);
		return emptyName;
	}

	public static int codepointsForName(final String name, final int[] codepoints)
	{
		int codepoint = mycharacters.codepointForName(name);
		if(codepoint != empty)
		{
			codepoints[0] = codepoint;
			return 1;
		}
		return 0;
	}

	static void escape(Appendable accum, String string, Document out, boolean inAttribute, boolean normaliseWhite, boolean stripLeadingWhite) throws IOException
	{
		boolean lastWasWhite = false;
		boolean reachedNonWhite = false;
		final CharsetEncoder encoder = out.encoder();
		final CoreCharset coreCharset = CoreCharset.byName(encoder.charset().name());
		final int length = string.length();
		int codePoint;
		for(int offset = 0; offset < length; offset += Character.charCount(codePoint))
		{
			codePoint = string.codePointAt(offset);
			if(normaliseWhite)
			{
				if(StringUtil.isWhitespace(codePoint))
				{
					if((stripLeadingWhite && !reachedNonWhite) || lastWasWhite)
						continue;
					accum.append(' ');
					lastWasWhite = true;
					continue;
				}
				else
				{
					lastWasWhite = false;
					reachedNonWhite = true;
				}
			}
			if(codePoint < Character.MIN_SUPPLEMENTARY_CODE_POINT)
			{
				final char c = (char) codePoint;
				switch(c)
				{
					case '&':
						accum.append("&amp;");
						break;
					case 0xA0:
						accum.append("&nbsp;");
						break;
					case '<':
						accum.append("&lt;");
						break;
					case '>':
						if(!inAttribute)
							accum.append("&gt;");
						else
							accum.append(c);
						break;
					case '"':
						if(inAttribute)
							accum.append("&quot;");
						else
							accum.append(c);
						break;
					default:
						if(canEncode(coreCharset, c, encoder))
							accum.append(c);
						else
							appendEncoded(accum, codePoint);
				}
			}
			else
			{
				final String c = new String(Character.toChars(codePoint));
				if(encoder.canEncode(c)) // uses fallback encoder for simplicity
					accum.append(c);
				else
					appendEncoded(accum, codePoint);
			}
		}
	}

	private static void appendEncoded(Appendable accum, int codePoint) throws IOException
	{
		final String name = mycharacters.nameForCodepoint(codePoint);
		if(name != emptyName)
			accum.append('&').append(name).append(';');
	}

	private static boolean canEncode(final CoreCharset charset, final char c, final CharsetEncoder fallback)
	{
		switch(charset)
		{
			case ascii:
				return c < 0x80;
			case utf:
				return true;
			default:
				return fallback.canEncode(c);
		}
	}

	private enum CoreCharset
	{
		ascii, utf, fallback;
		private static CoreCharset byName(String name)
		{
			if(name.equals("US-ASCII"))
				return ascii;
			if(name.startsWith("UTF-"))
				return utf;
			return fallback;
		}
	}
	public static ByteBuffer readToByteBuffer(InputStream inStream, int maxSize) throws IOException
	{
		final int bufferSize = 60000;
		final boolean capped = maxSize > 0;
		byte[] buffer = new byte[capped && maxSize < bufferSize ? maxSize : bufferSize];
		ByteArrayOutputStream outStream = new ByteArrayOutputStream(capped ? maxSize : bufferSize);
		int read;
		int remaining = maxSize;
		while(!Thread.interrupted())
		{
			read = inStream.read(buffer);
			if(read == -1)
				break;
			if(capped)
			{
				if(read > remaining)
				{
					outStream.write(buffer, 0, remaining);
					break;
				}
				remaining -= read;
			}
			outStream.write(buffer, 0, read);
		}
		return ByteBuffer.wrap(outStream.toByteArray());
	}
}
