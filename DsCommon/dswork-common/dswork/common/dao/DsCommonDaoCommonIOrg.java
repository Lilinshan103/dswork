/**
 * 公共Dao
 */
package dswork.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import dswork.common.model.IOrg;
import dswork.core.db.MyBatisDao;

@Repository
@SuppressWarnings("all")
public class DsCommonDaoCommonIOrg extends MyBatisDao
{
	private SqlSessionTemplate sqlSessionTemplateCommon;
	private static boolean hasCommon = false;

	@Override
	protected SqlSessionTemplate getSqlSessionTemplate()
	{
		if(hasCommon)
		{
			return sqlSessionTemplateCommon;
		}
		return super.getSqlSessionTemplate();
	}
	
	public void setSqlSessionTemplateCommon(SqlSessionTemplate sqlSessionTemplate)
	{
		System.out.println("======== DsCommonDaoCommonIOrg call setSqlSessionTemplateCommon ========");
		hasCommon = true;
		this.sqlSessionTemplateCommon = sqlSessionTemplate;
	}
	
	@Override
	protected Class getEntityClass()
	{
		return DsCommonDaoCommonIOrg.class;
	}
	
	// /////////////////////////////////////////////////////////////////////////
	// 组织机构
	// /////////////////////////////////////////////////////////////////////////
	/**
	 * 根据上级组织机构主键取得列表数据
	 * @param pid 上级组织机构主键，为null时获取所有组织机构数据
	 * @param status 0-2为指定分类（2单位，1部门，0岗位），超出0-2范围则不过滤，pid为null时也不过滤
	 * @return List&lt;IOrg&gt;
	 */
	public List<IOrg> queryListOrg(Long pid, Integer status)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if(pid != null)
		{
			map.put("pid", pid);
			if(status != null && status.intValue() > -1 && status.intValue() < 3)
			{
				map.put("status", status);
			}
		}
		List<IOrg> list = executeSelectList("queryOrg", map);
		return list;
	}
}
