/**
 * ${table.comment}Service
 */
package ${namespace}.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ${frame}.core.db.EntityDao;
import ${frame}.core.db.BaseService;
import ${namespace}.model.${model};
import ${namespace}.dao.${model}Dao;

@Service
@SuppressWarnings("all")
public class ${model}Service extends BaseService<${model}, Long>
{
	@Autowired
	private ${model}Dao dao;

	@Override
	protected EntityDao getEntityDao()
	{
		return dao;
	}
}
