package dswork.builder;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BuilderParser
{
	private static DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	private BuilderConfig config;

	public BuilderConfig parse(String xmlString)
	{
		try
		{
			this.config = new BuilderConfig();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			parseConfig(builder.parse(new ByteArrayInputStream(xmlString.getBytes("UTF-8"))).getDocumentElement());
		}
		catch(SAXException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		return config;
	}

	private void parseConfig(Element e)
	{
		parseCharset(getElement(e, "charset"));
		parseTemplates(getElement(e, "templates"));
		parseParams(getElement(e, "params"));
		parseBuilder(getElement(e, "builds"));
	}

	private Element getElement(Element e, String name)
	{
		return (Element)e.getElementsByTagName(name).item(0);
	}
	private NodeList getElements(Element e, String name)
	{
		return e.getElementsByTagName(name);
	}
	private String getAttr(Element e, String name)
	{
		try
		{
			String attr = e.getAttribute(name);
			if(attr == null)
			{
				attr = "";
			}
			return attr;
		}
		catch(Exception ee)
		{
			return "";
		}
	}

	private void parseCharset(Element e)
	{
		config.charset.outputEncoding = getAttr(e, "outputEncoding");
		config.charset.text = e.getTextContent();
	}

	private void parseTemplates(Element e)
	{
		NodeList ns = getElements(e, "template");
		for(int i = 0; i < ns.getLength(); i++)
		{
			Element n = (Element)ns.item(i);
			BuilderConfig.Template t = config.addTemplate();
			t.name = getAttr(n, "name");
			t.viewpath = getAttr(n, "viewpath");
			t.path = getAttr(n, "path");
			t.comment = getAttr(n, "comment");
			if(config.templates.max < t.viewpath.length())
			{
				config.templates.max = t.viewpath.length();
			}
		}
	}

	private void parseParams(Element e)
	{
		NodeList ns = getElements(e, "param");
		for(int i = 0; i < ns.getLength(); i++)
		{
			Element n = (Element)ns.item(i);
			config.addParam(getAttr(n, "name"), getAttr(n, "value"));
		}
	}

	private void parseBuilder(Element e)
	{
		config.builds.rootpath = getAttr(e, "rootpath");
		config.builds.templatename = getAttr(e, "templatename");
		config.builds.src = getAttr(e, "src");
		config.builds.web = getAttr(e, "web");
		config.builds.url = getAttr(e, "url");
		NodeList ns = getElements(e, "build");
		for(int i = 0; i < ns.getLength(); i++)
		{
			Element n = (Element)ns.item(i);
			BuilderConfig.Build m = config.addBuild();
			m.namespace = Builder.getToPath(getAttr(n, "namespace"));
			m.module = Builder.getToPath(getAttr(n, "module"));
			m.moduleUpperCamel = Builder.upperCamel(m.module.replace('/', '_'));
			m.table = getAttr(n, "table");
			m.model = getAttr(n, "model");
			if(m.model.length() == 0)
			{
				m.model = Builder.upperCamel(m.table);
			}
			m.comment = getAttr(n, "comment");
		}
	}
}
