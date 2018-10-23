/**
 * DS_WEBSSO_USERDao
 */
package dswork.websso.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import dswork.core.db.BaseDao;
import dswork.websso.model.DsWebssoUser;

@Repository
@SuppressWarnings("all")
public class DsWebssoUserDao extends BaseDao<DsWebssoUser, Long>
{
	@Override
	public Class getEntityClass()
	{
		return DsWebssoUserDao.class;
	}

	public DsWebssoUser getBySsoaccount(String ssoaccount)
	{
		return (DsWebssoUser) executeSelect("selectBySsoaccount", ssoaccount);
	}

	public DsWebssoUser getByOpenid(DsWebssoUser po)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("openidqq", po.getOpenidqq());
		map.put("openidalipay", po.getOpenidalipay());
		map.put("openidwechat", po.getOpenidwechat());
		map.put("openidweibo", po.getOpenidweibo());
		return (DsWebssoUser) executeSelect("selectByOpenid", map);
	}
}