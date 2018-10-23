package dswork.html.nodes;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Attributes implements Iterable<Attribute>, Cloneable
{
	protected static final String dataPrefix = "data-";
	private LinkedHashMap<String, Attribute> attributes = null;

	public String get(String key)
	{
		if(attributes == null)
			return "";
		Attribute attr = attributes.get(key);
		return attr != null ? attr.getValue() : "";
	}

	public String getIgnoreCase(String key)
	{
		if(attributes == null)
			return "";
		Attribute attr = attributes.get(key);
		if (attr != null)
			return attr.getValue();
		for(String attrKey : attributes.keySet())
		{
			if(attrKey.equalsIgnoreCase(key))
				return attributes.get(attrKey).getValue();
		}
		return "";
	}

	public void put(String key, String value)
	{
		Attribute attr = new Attribute(key, value);
		put(attr);
	}

	public void put(String key, boolean value)
	{
		Attribute attr = new Attribute(key, (value ? key : ""));// checked="checked"
		put(attr);
	}

	public void put(Attribute attribute)
	{
		if(attributes == null)
			attributes = new LinkedHashMap<String, Attribute>(2);
		attributes.put(attribute.getKey(), attribute);
	}
	
	public void remove(String key)
	{
		if(attributes == null)
			return;
		attributes.remove(key);
	}

	public void removeIgnoreCase(String key)
	{
		if(attributes == null)
			return;
		for(Iterator<String> it = attributes.keySet().iterator(); it.hasNext();)
		{
			String attrKey = it.next();
			if(attrKey.equalsIgnoreCase(key))
				it.remove();
		}
	}

	public boolean hasKey(String key)
	{
		return attributes != null && attributes.containsKey(key);
	}

	public boolean hasKeyIgnoreCase(String key)
	{
		if(attributes == null)
			return false;
		for(String attrKey : attributes.keySet())
		{
			if(attrKey.equalsIgnoreCase(key))
				return true;
		}
		return false;
	}

	public int size()
	{
		if(attributes == null)
			return 0;
		return attributes.size();
	}

	public void addAll(Attributes incoming)
	{
		if(incoming.size() == 0)
			return;
		if(attributes == null)
			attributes = new LinkedHashMap<String, Attribute>(incoming.size());
		attributes.putAll(incoming.attributes);
	}

	public Iterator<Attribute> iterator()
	{
		if(attributes == null || attributes.isEmpty())
		{
			return Collections.<Attribute> emptyList().iterator();
		}
		return attributes.values().iterator();
	}

	public List<Attribute> asList()
	{
		if(attributes == null)
			return Collections.emptyList();
		List<Attribute> list = new ArrayList<Attribute>(attributes.size());
		for(Map.Entry<String, Attribute> entry : attributes.entrySet())
		{
			list.add(entry.getValue());
		}
		return Collections.unmodifiableList(list);
	}

	public Map<String, String> dataset()
	{
		return new Dataset();
	}

	public String html()
	{
		StringBuilder accum = new StringBuilder();
		try
		{
			html(accum, new Document(""));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return accum.toString();
	}

	void html(Appendable accum, Document out) throws IOException
	{
		if(attributes == null)
		{
			return;
		}
		for(Map.Entry<String, Attribute> entry : attributes.entrySet())
		{
			Attribute attribute = entry.getValue();
			accum.append(" ");
			attribute.html(accum, out);
		}
	}

	@Override
	public String toString()
	{
		return html();
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if(!(o instanceof Attributes))
			return false;
		Attributes that = (Attributes) o;
		return !(attributes != null ? !attributes.equals(that.attributes) : that.attributes != null);
	}

	@Override
	public int hashCode()
	{
		return attributes != null ? attributes.hashCode() : 0;
	}

	@Override
	public Attributes clone()
	{
		if(attributes == null)
			return new Attributes();
		Attributes clone;
		try
		{
			clone = (Attributes) super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			throw new RuntimeException(e);
		}
		clone.attributes = new LinkedHashMap<String, Attribute>(attributes.size());
		for(Attribute attribute : this)
			clone.attributes.put(attribute.getKey(), attribute.clone());
		return clone;
	}

	private class Dataset extends AbstractMap<String, String>
	{
		private Dataset()
		{
			if(attributes == null)
				attributes = new LinkedHashMap<String, Attribute>(2);
		}

		@Override
		public Set<Entry<String, String>> entrySet()
		{
			return new EntrySet();
		}

		@Override
		public String put(String key, String value)
		{
			String dataKey = dataKey(key);
			String oldValue = hasKey(dataKey) ? attributes.get(dataKey).getValue() : null;
			Attribute attr = new Attribute(dataKey, value);
			attributes.put(dataKey, attr);
			return oldValue;
		}

		private class EntrySet extends AbstractSet<Map.Entry<String, String>>
		{
			@Override
			public Iterator<Map.Entry<String, String>> iterator()
			{
				return new DatasetIterator();
			}

			@Override
			public int size()
			{
				int count = 0;
				Iterator<?> iter = new DatasetIterator();
				while(iter.hasNext())
					count++;
				return count;
			}
		}

		private class DatasetIterator implements Iterator<Map.Entry<String, String>>
		{
			private Iterator<Attribute> attrIter = attributes.values().iterator();
			private Attribute attr;

			public boolean hasNext()
			{
				while(attrIter.hasNext())
				{
					attr = attrIter.next();
					if(attr.isDataAttribute())
						return true;
				}
				return false;
			}

			public Entry<String, String> next()
			{
				return new Attribute(attr.getKey().substring(dataPrefix.length()), attr.getValue());
			}

			public void remove()
			{
				attributes.remove(attr.getKey());
			}
		}
	}

	private static String dataKey(String key)
	{
		return dataPrefix + key;
	}
}
