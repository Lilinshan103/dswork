public class Tomcat
{
	public static void main(String[] args) throws Exception
	{
		dswork.ee.MyTomcat.class.newInstance().setPort(8080).setContextDir("context.xml").setBaseDir("/WorkServer/TomcatEmbed").addWebapp().addWebapp("web", "E:\\WorkServer\\web").start();
	}
}
