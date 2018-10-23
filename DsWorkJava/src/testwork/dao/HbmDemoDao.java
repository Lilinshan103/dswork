/**
 * Hibernate样例Dao
 */
package testwork.dao;

import java.util.Map;

import dswork.core.db.HibernateBaseDao;
import testwork.model.Demo;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("all")
public class HbmDemoDao extends HibernateBaseDao<Demo, Long>
{
	@Override
	public Class getEntityClass()
	{
		return Demo.class;
	}
	
	@Override
	protected void queryParam(Map map, Criteria criteria)
	{
		Object tmp;
			tmp = map.get("id");
		if(tmp != null)
		{
			criteria.add(Restrictions.eq("id", String.valueOf(tmp).trim()));
		}
		tmp = map.get("title");
		if(tmp != null)
		{
			criteria.add(Restrictions.like("title", String.valueOf(tmp).trim(), MatchMode.ANYWHERE));
		}
		tmp = map.get("content");
		if(tmp != null)
		{
			criteria.add(Restrictions.like("content", String.valueOf(tmp).trim(), MatchMode.ANYWHERE));
		}
		tmp = map.get("foundtime");
		if(tmp != null)
		{
			criteria.add(Restrictions.like("foundtime", String.valueOf(tmp).trim(), MatchMode.ANYWHERE));
		}
	}

}