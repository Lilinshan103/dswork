/**
 * ${table.comment}Dao
 */
package ${namespace}.dao;

import org.springframework.stereotype.Repository;
import ${frame}.core.db.BaseDao;
import ${namespace}.model.${model};

@Repository
@SuppressWarnings("all")
public class ${model}Dao extends BaseDao<${model}, Long>
{
	@Override
	public Class getEntityClass()
	{
		return ${model}Dao.class;
	}
}