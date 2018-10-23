/**
 * MyBatis样例Service
 */
package testwork.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.core.db.EntityDao;
import dswork.core.db.BaseService;
import dswork.core.page.Page;
import dswork.core.page.PageRequest;
import dswork.core.util.UniqueId;
import testwork.model.Demo;
import testwork.model.Mark;
import testwork.dao.DemoDao;
import testwork.dao.MarkDao;

@Service
@SuppressWarnings("all")
// 一个service对应一个控制器Controller
public class ManageMarkService
{
	@Autowired
	private DemoDao demoDao;
	@Autowired
	private MarkDao markDao;

	public Page<Demo> queryPage(PageRequest pageRequest)
	{
		return demoDao.queryPage(pageRequest);
	}

	public Demo get(Long primaryKey)
	{
		return (Demo) demoDao.get(primaryKey);
	}
	
	public List<Mark> queryListMark(Long demoid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("demoid", demoid);
		return markDao.queryList(map);
	}
	public int save(List<Mark> list)
	{
		System.out.println("进入save方法，该方法在接口中定义");
		if(list.size() == 2)// 用来测试数据回滚的
		{
			long id = UniqueId.genId();
			for(Mark m : list)
			{
				m.setId(id);
			}
		}
		else
		{
			for(Mark m : list)
			{
				m.setId(UniqueId.genId());
			}
		}
		for(Mark m : list)
		{
			markDao.save(m);
		}
		return 1;
	}
	
	public int test(List<Mark> list)
	{
		System.out.println("进入test方法，该方法不在接口中定义");
		if(list.size() == 3)// 用来测试数据回滚的
		{
			long id = UniqueId.genId();
			for(Mark m : list)
			{
				m.setId(id);
			}
		}
		for(Mark m : list)
		{
			markDao.save(m);
		}
		return 1;
	}
}
