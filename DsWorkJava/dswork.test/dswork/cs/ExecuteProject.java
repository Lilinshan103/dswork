package dswork.cs;

import java.util.HashMap;
import java.util.List;

import dswork.spring.BeanFactory;
import testwork.model.Demo;
import testwork.service.ManageDemoService;

/**
 * 执行需要调用的任务
 */
public class ExecuteProject
{
	public static void execute(String[] args)
	{
		System.out.println("从这行开始是测试样例");
		ManageDemoService service = (ManageDemoService)BeanFactory.getBean("manageDemoService");
		Demo demo = new Demo();
		demo.setContent("Content");
		demo.setTitle("Title");
		demo.setFoundtime("2010-01-01");
		service.save(demo);
		List<Demo> csList = service.queryList(new HashMap<Object, Object>());
		System.out.println("Total:"+csList.size());
	}
}
