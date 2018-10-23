/**
 * 数据表定义Service
 */
package dswork.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dswork.common.dao.DsCommonTableCategoryDao;
import dswork.common.dao.DsCommonTableDao;
import dswork.common.model.DsCommonTable;
import dswork.common.model.DsCommonTableCategory;
import dswork.core.db.BaseService;
import dswork.core.db.EntityDao;
import dswork.core.page.Page;
import dswork.core.page.PageRequest;

/**
 * @author whr
 *
 */
@Service
@SuppressWarnings("all")
public class DsCommonTableService extends BaseService<DsCommonTable, Long>
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
	private DsCommonTableDao tableDao;

	@Autowired
	private DsCommonTableCategoryDao categoryDao;
	
	@Override
	protected EntityDao getEntityDao()
	{
		return tableDao;
	}
	
	/**
	 * 获得所有类别
	 * @param map
	 * @return
	 */
	public List<DsCommonTableCategory> getCategoryList(Map map)
	{
		return categoryDao.queryList(map);
	}
	
	/**
	 * 查询类别下表单数目
	 * @param id
	 * @return
	 */
	public int queryFromCountByFormTypeId(Long id) 
	{
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("typeId", id);//通过类型ID查找表单数量
		PageRequest pageRequest = new PageRequest(map);
		return tableDao.queryCount(pageRequest);
	}
	
	

	/**
	 * 新增
	 * @param model 分类对象
	 * @return int
	 */
	public int saveCategory(DsCommonTableCategory model)
	{
		return categoryDao.save(model);
	}

	/**
	 * 删除分类
	 * @param primaryKey 分类主键
	 * @return int
	 */
	public int deleteCategory(long primaryKey)
	{
		return categoryDao.delete(primaryKey);
	}

	/**
	 * 批量删除分类
	 * @param primaryKeys 分类主键数组
	 */
	public void deleteBatchCategory(Long[] primaryKeys)
	{
		if(primaryKeys != null && primaryKeys.length > 0)
		{
			for(long p : primaryKeys)
			{
				deleteCategory(p);
			}
		}
	}

	/**
	 * 更新对象
	 * @param model 分类对象
	 * @return int
	 */
	public int updateCategory(DsCommonTableCategory model)
	{
		return categoryDao.update(model);
	}

	/**
	 * 查询分类对象
	 * @param primaryKey 分类主键
	 * @return DsCommonTableCategory
	 */
	public DsCommonTableCategory getCategory(long primaryKey)
	{
		return (DsCommonTableCategory) categoryDao.get(primaryKey);
	}
	
	/**
	 * 获取分类列表数据
	 * @param map 查询条件
	 * @return List&lt;DsCommonTableCategory&gt;
	 */
	public List<DsCommonTableCategory> queryListCategory(Map<String, Object> map)
	{
		return categoryDao.queryList(map);
	}
	
	/**
	 * 获取分类列表分页数据
	 * @param map 查询条件
	 * @return Page&lt;DsCommonTableCategory&gt;
	 */
	public Page<DsCommonTableCategory> queryPageCategory(PageRequest pr)
	{
		return categoryDao.queryPage(pr);
	}
}
