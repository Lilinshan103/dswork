/**
 * 功能:MyBatis样例Controller
 * 开发人员:skey
 * 创建时间:2018-09-04 18:03:53
 */
package testwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.mvc.BaseController;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.util.CollectionUtil;
import testwork.model.Demo;
import testwork.service.ManageDemoService;

@Scope("prototype")
@Controller
@RequestMapping("/manage/demo")// 控制器类名对应url的目录部分(除应用名contextPath)
public class ManageDemoController extends BaseController
{
	@Autowired
	private ManageDemoService service;// 一个service对应一个控制器Controller

	//添加
	@RequestMapping
	public String addDemo1()
	{
		return "/manage/demo/addDemo.jsp";
	}

	@RequestMapping
	public void addDemo2(Demo po)
	{
		try
		{
			service.save(po);
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	//删除
	@RequestMapping
	public void delDemo()
	{
		try
		{
			service.deleteBatch(CollectionUtil.toLongArray(req.getLongArray("keyIndex", 0)));
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	//修改
	@RequestMapping
	public String updDemo1()
	{
		Long id = req.getLong("keyIndex");
		put("po", service.get(id));
		put("page", req.getInt("page", 1));
		return "/manage/demo/updDemo.jsp";
	}

	@RequestMapping
	public void updDemo2(Demo po)
	{
		try
		{
			service.update(po);
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	//获得分页
	@RequestMapping
	public String getDemo()
	{
		//PageRequest pr = getPageRequest();
		//pr.setFilters(req.getParameterValueMap(false, false));
		Page<Demo> pageModel = service.queryPage(getPageRequest());
		put("pageModel", pageModel);
		put("pageNav", new PageNav<Demo>(request, pageModel));
		return "/manage/demo/getDemo.jsp";
	}

	//明细
	@RequestMapping
	public String getDemoById()
	{
		Long id = req.getLong("keyIndex");
		put("po", service.get(id));
		return "/manage/demo/getDemoById.jsp";
	}
}
