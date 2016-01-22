package testwork.service.demo;

import dswork.core.db.BaseService;
import dswork.core.db.EntityDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import testwork.model.Demo;
import testwork.model.*;
import testwork.dao.HbmDemoDao;

@Service
@SuppressWarnings(value={"all"})
public class HbmDemoService extends BaseService<Demo, java.lang.Long>
{
	@Autowired
	private HbmDemoDao dao;

	public EntityDao getEntityDao()
	{
		return this.dao;
	}
}
