/**
 * 功能:数据表定义Controller
 * 开发人员:
 * 创建时间:2018-10-10 18:05:45
 */
package dswork.common.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.common.model.DsCommonTable;
import dswork.common.model.DsCommonTableCategory;
import dswork.common.service.DsCommonTableService;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.util.CollectionUtil;
import dswork.core.util.UniqueId;
import dswork.mvc.BaseController;
import dswork.web.MyRequest;

@Scope("prototype")
@Controller
@RequestMapping("/ds/common/table")
public class DsCommonTableController extends BaseController
{
	@Autowired
	private DsCommonTableService service;
	
	//添加
	@RequestMapping("/addTable1")
	public String addTable1()
	{
		put("categoryList",service.getCategoryList(null));
		return "/ds/common/table/addTable.jsp";
	}

	@RequestMapping("/addTable2")
	public void addTable2(DsCommonTable po)
	{
		try
		{
			//判断该名称是否已经被使用
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", po.getName());
			map.put("categoryid",po.getCategoryid());
			List<DsCommonTable> list = service.queryList(map);
			if(list.size()>0) 
			{
				throw new Exception("该类型下的\""+po.getName()+"\"表单已经存在！");
			}
			po.setId(UniqueId.genId());
			po.setDatatable(resolve(req));//解析成json字符串
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
	@RequestMapping("/delTable")
	public void delTable()
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
	@RequestMapping("/updTable1")
	public String updTable1()
	{
		Long id = req.getLong("keyIndex");
		DsCommonTable po = service.get(id);
		put("po", po);
		put("typeList", service.getCategoryList(null));//所有表单类别
		put("map",DsCommonTableService.toBean(po.getDatatable(), Map.class));
		put("page", req.getInt("page", 1));
		return "/ds/common/table/updTable.jsp";
	}

	@RequestMapping("/updTable2")
	public void updTable2(DsCommonTable po)
	{
		try
		{
			po.setDatatable(resolve(req));//解析表单json
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
	@RequestMapping("/getTable")
	public String getTable()
	{
		put("categoryList",service.getCategoryList(null));
		Page<DsCommonTable> pageModel = service.queryPage(getPageRequest());
		put("pageModel", pageModel);
		put("pageNav", new PageNav<DsCommonTable>(request, pageModel));
		return "/ds/common/table/getTable.jsp";
	}

	//明细
	@RequestMapping("/getTableById")
	public String getTableById()
	{
		Long id = req.getLong("keyIndex");
		DsCommonTable po = service.get(id);
		String string = po.getDatatable();
		Map<?, ?> map = DsCommonTableService.toBean(string, Map.class);
		put("po", po);
		put("map",map);
		return "/ds/common/table/getTableById.jsp";
	}

	//解析表单自定义字段成json字符串
	private String resolve(MyRequest req) {
		String[] name = req.getStringArray("tname");//自定义字段名称数组
		String[] info = req.getStringArray("tinfo");//自定义字段说明数组
		String[] datatype = req.getStringArray("ttype");//自定义字段类型数组
		Map<String,Map<String, String>> map = new LinkedHashMap<String,Map<String, String>>();
		if(name!=null&&name.length==info.length&&name.length==datatype.length) {
			for (int i=0;i<name.length;i++) 
			{
				Map<String, String> map2 = new HashMap<String,String>();
				map2.put("name", name[i]);
				map2.put("info", info[i]);
				map2.put("datatype", datatype[i]);
				map.put(name[i], map2);
			}
		}else {
			throw new RuntimeException("json 数据解析错误");
		}
		return DsCommonTableService.toJson(map);
	}
	
	
	//######
	//添加
	@RequestMapping("/addTableCategory1")
	public String addTableCategory1()
	{
		return "/ds/common/table/addTableCategory.jsp";
	}

	@RequestMapping("/addTableCategory2")
	public void addTableCategory2(DsCommonTableCategory po)
	{
		try
		{
			//判断该名称是否已经被使用
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", po.getName());
			List<DsCommonTableCategory> list = service.queryListCategory(map);
			if(list.size()>0) 
			{
				throw new Exception("该类型\""+po.getName()+"\"已经存在！");
			}
			po.setId(UniqueId.genId());
			service.saveCategory(po);
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	//删除
	@RequestMapping("/delTableCategory")
	public void delTableCategory()
	{
		try
		{
			//判断此类别下是否由表单,有，不允许删除
			Long[] array = CollectionUtil.toLongArray(req.getLongArray("keyIndex", 0));
			for (Long id: array) 
			{
				int count = service.queryFromCountByFormTypeId(id);
				if(count>0) 
				{
					DsCommonTableCategory formType = service.getCategory(id);
					throw new Exception(formType.getName()+" 类别正在被使用");
				}
			}
			service.deleteBatchCategory(array);
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	//修改
	@RequestMapping("/updTableCategory1")
	public String updTableCategory1()
	{
		Long id = req.getLong("keyIndex");
		put("po", service.getCategory(id));
		put("page", req.getInt("page", 1));
		return "/ds/common/table/updTableCategory.jsp";
	}

	@RequestMapping("/updTableCategory2")
	public void updTableCategory2(DsCommonTableCategory po)
	{
		try
		{
			//判断该名称是否已经被使用
			HashMap<String, Object> map = new HashMap<String, Object>();
			DsCommonTableCategory category = service.getCategory(po.getId());

			if(!category.getName().equals(po.getName())) {//如果新名字和当前名字不一样，
				map.put("name", po.getName());
				List<DsCommonTableCategory> list = service.queryListCategory(map);
				if(list.size()>0) 
				{
					throw new Exception("该类型\""+po.getName()+"\"已经存在！");
				}
			}
			service.updateCategory(po);
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	//获得分页
	@RequestMapping("/getTableCategory")
	public String getTableCategory()
	{
		Page<DsCommonTableCategory> pageModel = service.queryPageCategory(getPageRequest());
		put("pageModel", pageModel);
		put("pageNav", new PageNav<DsCommonTableCategory>(request, pageModel));
		return "/ds/common/table/getTableCategory.jsp";
	}

	//明细
	@RequestMapping("/getTableCategoryById")
	public String getTableCategoryById()
	{
		Long id = req.getLong("keyIndex");
		put("po", service.getCategory(id));
		return "/ds/common/table/getTableCategoryById.jsp";
	}
}
