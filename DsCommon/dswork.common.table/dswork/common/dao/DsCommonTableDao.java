/**
 * 数据表定义Dao
 */
package dswork.common.dao;

import org.springframework.stereotype.Repository;

import dswork.common.model.DsCommonTable;
import dswork.core.db.BaseDao;

@Repository
@SuppressWarnings("all")
public class DsCommonTableDao extends BaseDao<DsCommonTable, Long>
{
	@Override
	public Class getEntityClass()
	{
		return DsCommonTableDao.class;
	}
}