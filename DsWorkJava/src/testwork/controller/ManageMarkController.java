/**
 * 功能:MyBatis样例Controller
 * 开发人员:skey
 * 创建时间:2014-07-01 12:01:01
 */
package testwork.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.mvc.BaseController;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.page.PageRequest;
import dswork.core.util.TimeUtil;
import testwork.model.Demo;
import testwork.model.Mark;
import testwork.service.ManageMarkService;

@Scope("prototype")
@Controller
@RequestMapping("/manage/mark")// 控制器类名对应url的目录部分(除应用名contextPath)
public class ManageMarkController extends BaseController
{
	@Autowired
	private ManageMarkService service;// 一个service对应一个控制器Controller

	@RequestMapping("/addMark2")
	public void addMark2()
	{
		try
		{
			Long demoid = req.getLong("demoid");
			List<Mark> list = new ArrayList<Mark>();
			String[] contentArr = req.getStringArray("content", false);
			for(int i = 0; i < contentArr.length; i++)
			{
				Mark m = new Mark();
				m.setDemoid(demoid);
				m.setContent(contentArr[i]);
				m.setFoundtime(TimeUtil.getCurrentDate());
				list.add(m);
			}
			if(list.size() == 3)
			{
				service.test(list);
			}
			else
			{
				service.save(list);
			}
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
		return "/manage/mark/getDemo.jsp";
	}

	@RequestMapping("/getMark")
	public String getMark()
	{
		Long id = req.getLong("keyIndex");
		put("po", service.get(id));
		put("list", service.queryListMark(id));
		return "/manage/mark/getMark.jsp";
	}
}
