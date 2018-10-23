/**
 * 数据表分类Dao
 */
package dswork.common.dao;

import org.springframework.stereotype.Repository;

import dswork.common.model.DsCommonTableCategory;
import dswork.core.db.BaseDao;

@Repository
@SuppressWarnings("all")
public class DsCommonTableCategoryDao extends BaseDao<DsCommonTableCategory, Long>
{
	@Override
	public Class getEntityClass()
	{
		return DsCommonTableCategoryDao.class;
	}
}