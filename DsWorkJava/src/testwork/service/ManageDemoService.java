/**
 * 样例Service
 */
package testwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dswork.core.db.EntityDao;
import dswork.core.db.BaseService;
import testwork.model.Demo;
import testwork.dao.DemoDao;

@Service
@SuppressWarnings("all")
//一个service对应一个控制器Controller
public class ManageDemoService extends BaseService<Demo, Long>
{
	@Autowired
	private DemoDao dao;

	@Override
	protected EntityDao getEntityDao()
	{
		return dao;
	}
}
