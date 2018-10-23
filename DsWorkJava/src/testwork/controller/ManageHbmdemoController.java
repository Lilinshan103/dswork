/**
 * 功能:Hibernate样例Controller
 * 开发人员:skey
 * 创建时间:2014-07-01 12:01:01
 */
package testwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.mvc.BaseController;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.page.PageRequest;
import dswork.core.util.CollectionUtil;
import testwork.model.Demo;
import testwork.service.ManageHbmdemoService;

@Scope("prototype")
@Controller
@RequestMapping("/manage/hbmdemo")// 控制器类名对应url的目录部分(除应用名contextPath)
public class ManageHbmdemoController extends BaseController
{
	@Autowired
	private ManageHbmdemoService service;// 一个service对应一个控制器Controller

	@RequestMapping("/addDemo1")
	public String addDemo1()
	{
		return "/manage/demo/addDemo.jsp";
	}

	@RequestMapping("/addDemo2")
	public void addDemo2(Demo po)
	{
		try
		{
			service.save(po);
			print(1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	@RequestMapping("/delDemo")
	public void delDemo()
	{
		try
		{
			service.deleteBatch(CollectionUtil.toLongArray(req.getLongArray("keyIndex", 0)));
			print("1");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	@RequestMapping("/updDemo1")
	public String upd1()
	{
		Long id = req.getLong("keyIndex");
		put("po", service.get(id));
		put("page", req.getInt("page", 1));
		return "/manage/demo/updDemo.jsp";
	}

	@RequestMapping("/updDemo2")
	public void updDemo2(Demo po)
	{
		try
		{
			service.update(po);
			print(1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	@RequestMapping("/getDemo")
	public String getDemo()
	{
		PageRequest pr = getPageRequest();
		pr.setFilters(req.getParameterValueMap(false, false));
		Page<Demo> pageModel = service.queryPage(pr);
		put("pageModel", pageModel);
		put("pageNav", new PageNav<Demo>(request, pageModel));
		return "/manage/demo/getDemo.jsp";
	}

	@RequestMapping("/getDemoById")
	public String getDemoById()
	{
		Long id = req.getLong("keyIndex");
		put("po", service.get(id));
		return "/manage/demo/getDemoById.jsp";
	}
}
