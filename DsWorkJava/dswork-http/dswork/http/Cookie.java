package dswork.http;

import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
import java.util.Locale;

/**
 * 自定义Cookie
 * @author skey
 * @version 1.0
 */
public class Cookie implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
	private final String name;
	private String value;
	private String domain;
	private Date expiryDate;
	private String path;
	private boolean secure = false;//表示cookie通过https连接传送，假如是http，那么就不会被传送
	private boolean httpOnly = false;//表示是否不允许javascript进行读取
	//private Map<String, String> attribs;
	//private String cookieComment;
	//private int cookieVersion;
	//private Date creationDate;

	/**
	 * 构造方法
	 * @param name String
	 * @param value String
	 */
	public Cookie(final String name, final String value)
	{
		this.name = String.valueOf(name);
		//this.attribs = new HashMap<String, String>();
		this.value = value;
	}

	public String getName()
	{
		return this.name;
	}

	public String getValue()
	{
		return this.value;
	}

	public void setValue(final String value)
	{
		this.value = value;
	}

	public String getCommentURL()
	{
		return null;
	}

	public Date getExpiryDate()
	{
		return expiryDate;
	}

	public void setExpiryDate(final Date expiryDate)
	{
		this.expiryDate = expiryDate;
	}

	public boolean isExpired(Date date)
	{
		return (this.expiryDate != null && this.expiryDate.getTime() <= date.getTime());
	}

//	public boolean isPersistent()
//	{
//		return (null != cookieExpiryDate);
//	}

	public String getDomain()
	{
		return domain;
	}

	public void setDomain(final String domain)
	{
		if(domain != null)
		{
			this.domain = domain.toLowerCase(Locale.ROOT);
		}
		else
		{
			this.domain = null;
		}
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(final String path)
	{
		this.path = path;
	}

	public boolean isSecure()
	{
		return secure;
	}

	public void setSecure(final boolean secure)
	{
		this.secure = secure;
	}
	
	public boolean isHttpOnly()
	{
		return httpOnly;
	}

	public void setHttpOnly(boolean httpOnly)
	{
		this.httpOnly = httpOnly;
	}

//	public String getComment()
//	{
//		return cookieComment;
//	}
//
//	public void setComment(final String comment)
//	{
//		cookieComment = comment;
//	}
//
//	public int getVersion()
//	{
//		return cookieVersion;
//	}
//
//	public void setVersion(int version)
//	{
//		cookieVersion = version;
//	}
//
//	public Date getCreationDate()
//	{
//		return creationDate;
//	}
//
//	public void setCreationDate(final Date creationDate)
//	{
//		this.creationDate = creationDate;
//	}
//
//	public void setAttribute(final String name, final String value)
//	{
//		this.attribs.put(name, value);
//	}
//
//	public String getAttribute(final String name)
//	{
//		return this.attribs.get(name);
//	}
//
//	public boolean containsAttribute(final String name)
//	{
//		return this.attribs.containsKey(name);
//	}
//
//	public boolean removeAttribute(final String name)
//	{
//		return this.attribs.remove(name) != null;
//	}

	@Override
	public String toString()
	{
		final StringBuilder buffer = new StringBuilder();
//		buffer.append("[version: ");
//		buffer.append(Integer.toString(this.cookieVersion));
//		buffer.append("]");
		buffer.append("[name: ");
		buffer.append(this.name);
		buffer.append("]");
		buffer.append("[value: ");
		buffer.append(this.value);
		buffer.append("]");
		buffer.append("[domain: ");
		buffer.append(this.domain);
		buffer.append("]");
		buffer.append("[path: ");
		buffer.append(this.path);
		buffer.append("]");
		buffer.append("[expiry: ");
		buffer.append(this.expiryDate);
		buffer.append("]");
		return buffer.toString();
	}

	@Override
	public Cookie clone()
	{
		Cookie c = new Cookie(name, value);
		c.setPath(getPath());
		c.setSecure(isSecure());
		c.setHttpOnly(isHttpOnly());
		c.setDomain(getDomain());
		c.setExpiryDate(getExpiryDate());
		return c;
	}
}
