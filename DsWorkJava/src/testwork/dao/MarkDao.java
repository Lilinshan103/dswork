/**
 * MyBatis样例Dao
 */
package testwork.dao;

import org.springframework.stereotype.Repository;

import dswork.core.db.BaseDao;
import testwork.model.Mark;

@Repository
@SuppressWarnings("all")
public class MarkDao extends BaseDao<Mark, Long>
{
	@Override
	public Class getEntityClass()
	{
		return Mark.class;
	}
}