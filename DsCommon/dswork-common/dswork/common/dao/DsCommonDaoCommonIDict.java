/**
 * 公共Dao
 */
package dswork.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import dswork.common.model.IDict;
import dswork.core.db.MyBatisDao;
import dswork.core.util.TimeUtil;

@Repository
@SuppressWarnings("all")
public class DsCommonDaoCommonIDict extends MyBatisDao
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
		System.out.println("======== DsCommonDaoCommonIDict call setSqlSessionTemplateCommon ========");
		hasCommon = true;
		this.sqlSessionTemplateCommon = sqlSessionTemplate;
	}
	
	
	@Override
	protected Class getEntityClass()
	{
		return DsCommonDaoCommonIDict.class;
	}

	// /////////////////////////////////////////////////////////////////////////
	// 字典
	// /////////////////////////////////////////////////////////////////////////
	/**
	 * 获取指定节点
	 * @param name 字典分类名
	 * @param alias 上级标识，当alias为null时获取全部节点数据，当alias为""时获取根节点数据
	 * @return IDict
	 */
	public IDict getDict(String name, String alias)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", name);
		map.put("alias", alias);
		return (IDict) executeSelect("getDict", map);
	}
	/**
	 * 获取指定节点的列表数据
	 * @param name 字典分类名
	 * @param alias 上级标识，当alias为null时获取全部节点数据，当alias为""时获取根节点数据
	 * @return List&lt;IDict&gt;
	 */
	public List<IDict> queryListDict(String name, String parentAlias)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", String.valueOf(name));
		map.put("alias", parentAlias);
		List<IDict> list = executeSelectList("queryDict", map);
		return list;
	}
}
