package dswork.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dswork.common.dao.DsCommonDaoCommonIOrg;
import dswork.common.model.IOrg;
import dswork.common.model.IOrgTree;
import dswork.spring.BeanFactory;

public class DsCommonFactoryOrg
{
	private static DsCommonDaoCommonIOrg dao;

	private DsCommonFactoryOrg()
	{
		if(dao == null)
		{
			dao = (DsCommonDaoCommonIOrg) BeanFactory.getBean("dsCommonDaoCommonIOrg");
		}
	}
	private static final DsCommonFactoryOrg instace = new DsCommonFactoryOrg();

	public static DsCommonFactoryOrg getInstance()
	{
		return instace;
	}

	/**
	 * 取得列表数据
	 * @param pid 上级组织机构主键，为null时获取所有组织机构数据
	 * @param status 0-2为指定分类（2单位，1部门，0岗位），超出0-2范围则不过滤，pid为null时也不过滤
	 * @return String
	 */
	public String getOrgJson(Long pid, Integer status)
	{
		return dao.queryListOrg(pid, status).toString();
	}

	// 取得所有树型对象集合
	private List<IOrgTree> getOrgTreeList()
	{
		List<IOrg> list = dao.queryListOrg(null, null);
		List<IOrgTree> treeList = new ArrayList<IOrgTree>();
		for(IOrg o : list)
		{
			IOrgTree m = new IOrgTree(o);
			treeList.add(m);
		}
		return treeList;
	}

	/**
	 * 取得树型对象集合json，包括所有下级节点
	 * @param pid 上级组织机构主键，为null时获取所有组织机构数据
	 * @return String
	 */
	public String getJsonOrgTree(Long pid)
	{
		List<IOrgTree> treeList = getOrgTreeList();
		Map<String, IOrgTree> map = new HashMap<String, IOrgTree>();
		List<IOrgTree> rootList = new ArrayList<IOrgTree>();
		for(IOrgTree m : treeList)
		{
			if("0".equals(m.getPid()))
			{
				rootList.add(m);
			}
			map.put(m.getId(), m);
		}
		for(IOrgTree m : treeList)
		{
			if(!"0".equals(m.getPid()))
			{
				map.get(m.getPid()).addListItem(m);// 把所有子节点放入父节点
			}
		}
		if(pid != null)
		{
			rootList.clear();
			IOrgTree once = map.get(String.valueOf(pid));
			if(once != null)
			{
				rootList.add(once);
			}
		}
		return rootList.toString();
	}

	/**
	 * 取得列表对象集合json，包括所有下级节点
	 * @param pid 上级组织机构主键，为null时获取所有组织机构数据
	 * @param status 0-2为指定分类（2单位，1部门，0岗位），超出0-2范围则不过滤，pid为null时也不过滤
	 * @return String
	 */
	public String getJsonOrgList(Long pid)
	{
		List<IOrgTree> treeList = getOrgTreeList();
		Map<String, IOrgTree> map = new HashMap<String, IOrgTree>();
		List<IOrgTree> rootList = new ArrayList<IOrgTree>();
		for(IOrgTree m : treeList)
		{
			if("0".equals(m.getPid()))
			{
				rootList.add(m);
			}
			map.put(m.getId(), m);
		}
		for(IOrgTree m : treeList)
		{
			if(!"0".equals(m.getPid()))
			{
				map.get(m.getPid()).addListItem(m);// 把所有子节点放入父节点，这里主用要于找到所有上下级
			}
		}
		List<IOrgTree> resultList = new ArrayList<IOrgTree>();
		// 这里输出的list需要顺序
		if(pid != null)
		{
			IOrgTree once = map.get(String.valueOf(pid));
			if(once != null)
			{
				initListSort(once, resultList);
			}
		}
		else
		{
			for(IOrgTree o : rootList)
			{
				initListSort(o, resultList);
			}
		}
		for(IOrgTree o : resultList)
		{
			o.clearList();// 清除所有的节点级联，只剩下排好序的列表集合
		}
		return resultList.toString();
	}
	
	private void initListSort(IOrgTree m, List<IOrgTree> list)
	{
		list.add(m);
		if(m.getList().size() > 0)
		{
			for(IOrgTree o : m.getList())
			{
				initListSort(o, list);
			}
		}
	}
}
