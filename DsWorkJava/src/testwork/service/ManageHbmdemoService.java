/**
 * Hibernate样例Service
 */
package testwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dswork.core.db.EntityDao;
import dswork.core.db.BaseService;
import testwork.model.Demo;
import testwork.dao.HbmDemoDao;

@Service
@SuppressWarnings("all")
// 一个service对应一个控制器Controller
public class ManageHbmdemoService extends BaseService<Demo, Long>
{
	@Autowired
	private HbmDemoDao dao;

	@Override
	protected EntityDao getEntityDao()
	{
		return dao;
	}
}
