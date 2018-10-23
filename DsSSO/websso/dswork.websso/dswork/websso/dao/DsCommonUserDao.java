/**
 * 用户Dao
 */
package dswork.websso.dao;

import org.springframework.stereotype.Repository;

import dswork.core.db.MyBatisDao;
import dswork.websso.model.DsCommonUser;

@Repository
@SuppressWarnings("all")
public class DsCommonUserDao extends MyBatisDao
{
	@Override
	public Class getEntityClass()
	{
		return DsCommonUserDao.class;
	}

	public int save(DsCommonUser po)
	{
		return executeInsert("insert", po);
	}

	public DsCommonUser get(Long id)
	{
		return (DsCommonUser) executeSelect("select", id);
	}

	public DsCommonUser getByAccount(String account)
	{
		if(account == null || account.length() == 0)
		{
			return null;
		}
		return (DsCommonUser) executeSelect("selectByAccount", account);
	}

	public DsCommonUser getByIdcard(String idcard)
	{
		if(idcard == null || idcard.length() == 0)
		{
			return null;
		}
		return (DsCommonUser) executeSelect("selectByIdcard", idcard);
	}

	public DsCommonUser getByMobile(String mobile)
	{
		if(mobile == null || mobile.length() == 0)
		{
			return null;
		}
		return (DsCommonUser) executeSelect("selectByMobile", mobile);
	}

	public DsCommonUser getByEmail(String email)
	{
		if(email == null || email.length() == 0)
		{
			return null;
		}
		return (DsCommonUser) executeSelect("selectByEmail", email);
	}
}
