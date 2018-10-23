package dswork.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.common.model.DsCommonFlow;
import dswork.common.model.DsCommonFlowCategory;
import dswork.common.model.DsCommonFlowDataRow;
import dswork.common.model.DsCommonFlowTask;
import dswork.common.service.DsCommonFlowService;
import dswork.core.page.PageRequest;
import dswork.core.util.CollectionUtil;
import dswork.flow.MyFlow;
import dswork.flow.dom.MyNode;
import dswork.mvc.BaseController;

@Scope("prototype")
@Controller
@RequestMapping("/ds/common/flow")
@SuppressWarnings("all")
public class DsCommonFlowController extends BaseController
{
	static com.google.gson.GsonBuilder builder = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String toJson(Object object)
	{
		return builder.create().toJson(object);
	}

	public static <T> T toBean(String json, Class<T> classOfT)
	{
		return builder.create().fromJson(json, classOfT);
	}
	
	@Autowired
	private DsCommonFlowService service;

	@RequestMapping("/addFlow1")
	public String addFlow1()
	{
		return "/ds/common/flow/addFlowXML.jsp";
	}

	// 授权
	@RequestMapping("/getFlowDataTableRwx")
	public String getFlowDataTableRwx()
	{
		return "/ds/common/flow/getFlowDataTableRwx.jsp";
	}
	
	@RequestMapping("/getFlowDataTable")
	public String getFlowDataTable()
	{
		return "/ds/common/flow/getFlowDataTable.jsp";
	}

	@RequestMapping("/addFlow2")
	public void addFlow2(DsCommonFlow po)
	{
		try
		{
			if (po.getAlias().length() <= 0)
			{
				print("0:添加失败，标识不能为空");
			}
			else
			{
				DsCommonFlowCategory fc = service.get(po.getCategoryid());
				Map<String, String> map = new LinkedHashMap<String, String>();
				String[] tkeyArr = req.getStringArray("tkey");
				if(tkeyArr.length > 0)
				{
					String[] tjsonArr = req.getStringArray("tjson");
					for (int i = 0; i < tkeyArr.length; i++)
					{
						if(tjsonArr[i].indexOf("\\") < 0)
						{
							tjsonArr[i] = tjsonArr[i].replaceAll("\"", "\\\\\"");
						}
						map.put(tkeyArr[i], tjsonArr[i]);
					}
				}
				
				if (fc != null)
				{
					if (!service.isExistsByAlias(po.getAlias()))
					{
						List<DsCommonFlowTask> taskList = new ArrayList<DsCommonFlowTask>();
						if (po.getFlowxml().length() < 50)
						{
							String[] taliasArr = req.getStringArray("talias");
							String[] tnameArr = req.getStringArray("tname");
							int[] tcountArr = req.getIntArray("tcount", 0);
							String[] tnextArr = req.getStringArray("tnext");
							String[] tusersArr = req.getStringArray("tusers");
							String[] tmemoArr = req.getStringArray("tmemo");
							int[] subcountArr = req.getIntArray("subcount", -1);
							String[] subusersArr = req.getStringArray("subusers");
							for (int i = 0; i < taliasArr.length; i++)
							{
								DsCommonFlowTask m = new DsCommonFlowTask();
								m.setTname(tnameArr[i]);
								m.setTalias(taliasArr[i]);
								m.setTcount(tcountArr[i]);
								m.setTnext(tnextArr[i]);
								m.setTusers(tusersArr[i]);
								m.setTmemo(tmemoArr[i]);
								m.setDatatable(map.get(taliasArr[i]));
								m.setSubcount(subcountArr[i]);
								m.setSubusers(subusersArr[i]);
								taskList.add(m);
							}
						}
						else
						{
							MyFlow flow = new MyFlow(po.getFlowxml());
							List<MyNode> list = flow.getTasks();
							for (int i = 0; i < list.size(); i++)
							{
								MyNode node = list.get(i);
								DsCommonFlowTask m = new DsCommonFlowTask();
								m.setTname(node.getName());
								m.setTalias(node.getAlias());
								m.setTcount(node.getCount());
								m.setTnext(flow.getNextTask(node.getAlias()));
								m.setTusers(node.getUsers());
								m.setTmemo("");
								m.setDatatable(map.get(node.getAlias()));
								m.setSubcount(node.getSubcount());
								m.setSubusers(node.getSubusers());
								taskList.add(m);
							}
						}
						print(service.saveFlow(po, taskList));
					}
					else
					{
						print("0:添加失败，该标识已存在");
					}
				}
				else
				{
					print("0:添加失败，该分类不存在");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 删除
	@RequestMapping("/delFlow")
	public void delFlow()
	{
		try
		{
			long[] ids = req.getLongArray("keyIndex", 0);
			for (long id : ids)
			{
				service.deleteFlow(id);
			}
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 修改
	@RequestMapping("/updFlow1")
	public String updFlow1()
	{
		Long id = req.getLong("keyIndex");
		DsCommonFlow po = service.getFlow(id);
		Map<String, String> taskMap = new HashMap<String, String>();
		List<DsCommonFlowTask> taskList = po.getTaskList();
		for (int i = 0; i < taskList.size(); i++)
		{
			DsCommonFlowTask task = taskList.get(i);
			taskMap.put(task.getTalias(), task.getDatatable());
		}
		String datatable = po.getDatatable();
		if (!"".equals(datatable))
		{
			taskMap.put("default", toJson(toBean(datatable, List.class)).replaceAll("\"", "\\\\\""));
		}
		put("po", po);
		put("taskMap", taskMap);
		return "/ds/common/flow/updFlowXML.jsp";
	}

	@RequestMapping("/updFlow2")
	public void updFlow2(DsCommonFlow po)
	{
		try
		{

			Map<String, String> map = new LinkedHashMap<String, String>();
			String[] tkeyArr = req.getStringArray("tkey");
			if(tkeyArr.length > 0)
			{
				String[] tjsonArr = req.getStringArray("tjson");
				for (int i = 0; i < tkeyArr.length; i++)
				{
					if(tjsonArr[i].indexOf("\\") < 0)
					{
						tjsonArr[i] = tjsonArr[i].replaceAll("\"", "\\\\\"");
					}
					map.put(tkeyArr[i], tjsonArr[i]);
				}
			}
			
			List<DsCommonFlowTask> taskList = new ArrayList<DsCommonFlowTask>();
			if (po.getFlowxml().length() < 50)
			{
				String[] taliasArr = req.getStringArray("talias");
				String[] tnameArr = req.getStringArray("tname");
				int[] tcountArr = req.getIntArray("tcount", 0);
				String[] tnextArr = req.getStringArray("tnext");
				String[] tusersArr = req.getStringArray("tusers");
				String[] tmemoArr = req.getStringArray("tmemo");
				int[] subcountArr = req.getIntArray("subcount", -1);
				String[] subusersArr = req.getStringArray("subusers");
				for (int i = 0; i < taliasArr.length; i++)
				{
					DsCommonFlowTask m = new DsCommonFlowTask();
					m.setTname(tnameArr[i]);
					m.setTalias(taliasArr[i]);
					m.setTcount(tcountArr[i]);
					m.setTnext(tnextArr[i]);
					m.setTusers(tusersArr[i]);
					m.setTmemo(tmemoArr[i]);
					m.setDatatable(map.get(taliasArr[i]));
					m.setSubcount(subcountArr[i]);
					m.setSubusers(subusersArr[i]);
					taskList.add(m);
				}
			}
			else
			{
				MyFlow flow = new MyFlow(po.getFlowxml());
				List<MyNode> list = flow.getTasks();
				for (int i = 0; i < list.size(); i++)
				{
					MyNode node = list.get(i);
					DsCommonFlowTask m = new DsCommonFlowTask();
					m.setTname(node.getName());
					m.setTalias(node.getAlias());
					m.setTcount(node.getCount());
					m.setTnext(flow.getNextTask(node.getAlias()));
					m.setTusers(node.getUsers());
					m.setTmemo("");
					m.setDatatable(map.get(node.getAlias()));
					m.setSubcount(node.getSubcount());
					m.setSubusers(node.getSubusers());
					taskList.add(m);
				}
			}
			service.updateFlow(po, taskList);
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 获得列表
	@RequestMapping("/getFlow")
	public String getFlow()
	{
		Long categoryid = req.getLong("categoryid");
		if (categoryid > 0)
		{
			DsCommonFlowCategory po = service.get(categoryid);
			if (po != null)
			{
				PageRequest rq = getPageRequest();
				rq.getFilters().put("vnum", 0);
				put("po", po);
				put("list", service.queryListFlow(rq.getFilters()));
				return "/ds/common/flow/getFlow.jsp";
			}
		}
		return null;
	}

	// 明细
	@RequestMapping("/getFlowById")
	public String getFlowById()
	{
		Long id = req.getLong("keyIndex");
		put("po", service.getFlow(id));
		return "/ds/common/flow/getFlowById.jsp";
	}

	// 更改状态
	@RequestMapping("/updStatus")
	public void updStatus()
	{
		long id = req.getLong("keyIndex");
		int status = req.getInt("status", -1);
		try
		{
			if (status == 0 || status == 1)
			{
				service.updateStatus(id, status);
				print(1);
			}
			else
			{
				print("0:参数错误");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 发布
	@RequestMapping("deployFlow")
	public void deployFlow()
	{
		try
		{
			Long id = req.getLong("keyIndex");
			int i = service.deployFlow(id);
			print(i == 1 ? 1 : "0:此流程版本不能发布");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 添加
	@RequestMapping("/addFlowCategory1")
	public String addFlowCategory1()
	{
		long pid = req.getLong("pid");
		DsCommonFlowCategory parent = null;
		if (pid > 0)
		{
			parent = service.get(pid);
		}
		else
		{
			parent = new DsCommonFlowCategory();
		}
		put("parent", parent);
		put("pid", pid);
		return "/ds/common/flow/addFlowCategory.jsp";
	}

	@RequestMapping("/addFlowCategory2")
	public void addFlowCategory2(DsCommonFlowCategory po)
	{
		try
		{
			if (po.getName().length() == 0)
			{
				print("0:名称不能为空");
				return;
			}
			if (0 < po.getPid().longValue())// 存在上级节点时
			{
				DsCommonFlowCategory parent = service.get(po.getPid());
				if (null == parent)
				{
					print("0:参数错误，请刷新重试");
					return;
				}
			}
			service.save(po);
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 删除
	@RequestMapping("/delFlowCategory")
	public void delFlowCategory()
	{
		try
		{
			int v = 0;
			long[] ids = req.getLongArray("keyIndex", 0);
			for (long id : ids)
			{
				if (id <= 0)
				{
					continue;
				}
				int count = service.getCountByPid(id);
				if (0 >= count)
				{
					service.delete(id);
				}
				else
				{
					v++;// print("0:下级节点不为空，不能删除");
				}
			}
			print("1" + (v > 0 ? ":" + v + "个不能删除，下级节点不为空" : ""));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:下级节点或用户不为空");
		}
	}

	// 修改
	@RequestMapping("/updFlowCategory1")
	public String updFlowCategory1()
	{
		Long id = req.getLong("keyIndex");
		DsCommonFlowCategory po = service.get(id);
		if (null == po)
		{
			return null;// 非法访问，否则肯定存在id
		}
		DsCommonFlowCategory parent = null;
		if (0 < po.getPid())
		{
			parent = service.get(po.getPid());
			if (null == parent)
			{
				return null;// 非法数据，否则肯定存在parent
			}
		}
		else
		{
			parent = new DsCommonFlowCategory();
		}
		put("po", po);
		put("parent", parent);
		return "/ds/common/flow/updFlowCategory.jsp";
	}

	@RequestMapping("/updFlowCategory2")
	public void updFlowCategory2(DsCommonFlowCategory po)
	{
		try
		{
			if (0 >= po.getName().length())
			{
				print("0:名称不能为空");
				return;
			}
			DsCommonFlowCategory old = service.get(po.getId());
			if (null == old)
			{
				print("0:操作失败，请刷新重试");
				return;
			}
			if (old.getPid() > 0)// 存在上级节点时
			{
				DsCommonFlowCategory parent = service.get(old.getPid());
				if (null == parent)
				{
					print("0:参数错误，请刷新重试");
					return;
				}
			}
			service.update(po);
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 排序
	@RequestMapping("/updFlowCategorySeq1")
	public String updFlowCategorySeq1()
	{
		long pid = req.getLong("pid");
		List<DsCommonFlowCategory> list = service.queryList(pid);
		put("list", list);
		return "/ds/common/flow/updFlowCategorySeq.jsp";
	}

	@RequestMapping("/updFlowCategorySeq2")
	public void updFlowCategorySeq2()
	{
		Long[] ids = CollectionUtil.toLongArray(req.getLongArray("keyIndex", 0));
		try
		{
			if (ids.length > 0)
			{
				service.updateSeq(ids);
				print(1);
			}
			else
			{
				print("0:没有需要排序的节点");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 移动
	@RequestMapping("/updFlowCategoryMove1")
	public String updFlowCategoryMove1()
	{
		Long rootid = req.getLong("rootid");// 作为限制根节点显示
		put("po", (rootid > 0) ? service.get(rootid) : null);
		put("rootid", rootid);
		return "/ds/common/flow/updFlowCategoryMove.jsp";
	}

	@RequestMapping("/updFlowCategoryMove2")
	public void updFlowCategoryMove2()
	{
		long pid = req.getLong("pid");
		if (pid <= 0)
		{
			pid = 0;
		}
		else
		{
			DsCommonFlowCategory m = service.get(pid);
			if (m == null)
			{
				print("0:不能进行移动");// 移动的节点不存在，或者不在相同的系统
				return;
			}
		}
		Long[] ids = getLongArray(req.getString("ids"));
		try
		{
			if (ids.length > 0)
			{
				service.updatePid(ids, pid);
				print(1);
			}
			else
			{
				print("0:没有需要移动的节点");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 树形管理
	@RequestMapping("/getFlowCategoryTree")
	public String getFlowCategoryTree()
	{
		Long rootid = req.getLong("rootid");// 作为限制根节点显示
		DsCommonFlowCategory po = null;
		if (rootid > 0)
		{
			po = service.get(rootid);
			if (null == po)
			{
				return null;// 没有此根节点
			}
		}
		else
		{
			po = new DsCommonFlowCategory();
		}
		put("po", po);
		return "/ds/common/flow/getFlowCategoryTree.jsp";
	}

	// 获得列表
	@RequestMapping("/getFlowCategory")
	public String getFlowCategory()
	{
		Long rootid = req.getLong("rootid");// 作为限制根节点显示
		Long pid = req.getLong("pid");
		List<DsCommonFlowCategory> list = service.queryList(pid);
		put("list", list);
		put("rootid", rootid);
		put("pid", pid);
		return "/ds/common/flow/getFlowCategory.jsp";
	}

	// 获得树形管理时的json数据
	@RequestMapping("/getFlowCategoryJson")
	// ByPid
	public void getFlowCategoryJson()
	{
		long pid = req.getLong("pid");
		print(service.queryList(pid));
	}

	// 明细
	@RequestMapping("/getFlowCategoryById")
	public String getFlowCategoryById()
	{
		Long id = req.getLong("keyIndex");
		DsCommonFlowCategory po = service.get(id);
		put("po", po);
		return "/ds/common/flow/getFlowCategoryById.jsp";
	}

	private Long[] getLongArray(String value)
	{
		try
		{
			String[] _v = value.split(",");
			if (_v != null && _v.length > 0)
			{
				Long[] _numArr = new Long[_v.length];
				for (int i = 0; i < _v.length; i++)
				{
					try
					{
						_numArr[i] = Long.parseLong(_v[i].trim());
					}
					catch (NumberFormatException e)
					{
						_numArr[i] = 0L;
					}
				}
				return _numArr;
			}
		}
		catch (Exception e)
		{
		}
		return new Long[0];
	}

}
