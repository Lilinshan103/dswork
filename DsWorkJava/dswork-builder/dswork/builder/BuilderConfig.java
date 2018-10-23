package dswork.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuilderConfig
{
	public class Template
	{
		public String name;
		public String viewpath;
		public String path;
		public String comment;
	}
	public class Templates
	{
		public int max = 0;
		public List<Template> template = new ArrayList<Template>();
	}
	public class Build
	{
		public String namespace;
		public String module;
		public String table;
		public String model;
		public String comment;
		public String moduleUpperCamel;
	}
	public class Builds
	{
		public String rootpath;
		public String templatename;
		public String src;
		public String web;
		public String url;
		public List<Build> build = new ArrayList<Build>();
	}
	public class Charset
	{
		public String outputEncoding = "";
		public String text = "";
	}
	public Charset charset = new Charset();
	public Templates templates = new Templates();
	public Template addTemplate()
	{
		Template t = new Template();
		templates.template.add(t);
		return t;
	}
	public Map<String, String> params = new HashMap<String, String>();
	public void addParam(String name, String value)
	{
		params.put(name, value);
	}
	public Builds builds = new Builds();
	public Build addBuild()
	{
		Build b = new Build();
		builds.build.add(b);
		return b;
	}
}
