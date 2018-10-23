package dswork.ee;

import java.io.File;
import java.util.Map;

import org.apache.catalina.startup.Tomcat;

public class MyTomcat
{
	private Tomcat tomcat = new Tomcat();
	private int port = 8080;
	private String hostname = "localhost";
	private String baseDir = "/WorkServer/TomcatEmbed";
	private java.io.File contextDir = null;
	private java.util.Map<String, String> map = new java.util.LinkedHashMap<String, String>();
	public MyTomcat()
	{
	}
	private static String loaderPath = "";
	private static java.io.File loaderFile = null;
	static
	{
		java.net.URL url = Thread.currentThread().getContextClassLoader().getResource("");
		String loadpath = url == null ? System.getProperty("user.dir") + "/" : url.getPath();
		loadpath = String.valueOf(loadpath).replace('\\', '/').replace("//", "/");
		if(loadpath.endsWith("/"))
		{
			loadpath = loadpath.substring(0, loadpath.length() - 1);
		}
		loaderPath = loadpath;
		loaderFile = new java.io.File(loaderPath);
		System.out.println(loaderPath);
	}
	
	public static String getLoaderPath()
	{
		return loaderPath;
	}
	
	public static String getProjectPath()
	{
		String path;
		if(loaderPath.endsWith("/WEB-INF/classes"))
		{
			path = loaderFile.getParentFile().getParentFile().getParentFile().getPath().replace('\\', '/');
		}
		else if(loaderPath.endsWith("/bin"))
		{
			path = loaderFile.getParentFile().getPath().replace('\\', '/');
		}
		else
		{
			path = loaderFile.getPath().replace('\\', '/');
		}
		return path;
	}
	
	public static String getProjectName()
	{
		File f = new File(getProjectPath());
		return f.getName();
	}
	
	public static String getWebPath()
	{
		if(!loaderPath.endsWith("/WEB-INF/classes"))
		{
			System.err.println("请确保你的项目编译输出目录为\"/WEB-INF/classes\"");
		}
		File f = new File(getProjectPath());
		String path = f.getPath().replace('\\', '/') + "/web";
		if(!(new java.io.File(path).isDirectory()))
		{
			path = f.getPath().replace('\\', '/') + "/WebContent";
			if(!(new java.io.File(path).isDirectory()))
			{
				path = f.getPath().replace('\\', '/') + "/WebRoot";
				if(!(new java.io.File(path).isDirectory()))
				{
					path = f.getPath().replace('\\', '/');
				}
			}
		}
		return path;
	}
	
	public MyTomcat addWebapp() throws Exception
	{
		map.put("/" + getProjectName(), getWebPath());
		return this;
	}
	
	public MyTomcat addWebapp(String projectName) throws Exception
	{
		map.put(projectName, getWebPath());
		return this;
	}
	
	public MyTomcat addWebapp(String projectName, String projectPath) throws Exception
	{
		map.put(projectName, (new java.io.File(String.valueOf(projectPath).replace('\\', '/').replace("//", "/"))).getPath().replace('\\', '/'));
		return this;
	}
	
	public MyTomcat setPort(int port)
	{
		this.port = port;
		return this;
	}
	
	public MyTomcat setHostname(String hostname)
	{
		this.hostname = hostname;
		return this;
	}
	
	public MyTomcat setBaseDir(String baseDir)
	{
		this.baseDir = baseDir;
		return this;
	}
	
	public MyTomcat setContextDir(String contextDir)
	{
		File f = new File(loaderPath + "/" + contextDir);
		if(f.isFile())
		{
			this.contextDir = f;
		}
		else
		{
			this.contextDir = new File(contextDir);
		}
		return this;
	}
	
	public MyTomcat start() throws Exception
	{
		tomcat.setPort(port);
		tomcat.setBaseDir(baseDir);
		tomcat.setHostname(hostname);
		for(Map.Entry<String, String> x : map.entrySet())
		{
			System.out.println(x.getKey() + "=" + x.getValue());
			org.apache.catalina.Context c = tomcat.addWebapp(x.getKey(), x.getValue());
			if(contextDir != null && contextDir.isFile())
			{
				c.setConfigFile(contextDir.toURI().toURL());
			}
		}
		tomcat.start();
		tomcat.getServer().await();
		return this;
	}
	// String jarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile() + "!";
}
