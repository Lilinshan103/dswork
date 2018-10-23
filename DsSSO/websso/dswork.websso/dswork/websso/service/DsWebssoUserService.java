/**
 * DS_WEBSSO_USERService
 */
package dswork.websso.service;

import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dswork.core.util.TimeUtil;
import dswork.core.util.UniqueId;
import dswork.websso.model.DsCommonUser;
import dswork.websso.model.DsWebssoUser;
import dswork.websso.dao.DsCommonUserDao;
import dswork.websso.dao.DsWebssoUserDao;

@Service
public class DsWebssoUserService
{
	@Autowired
	private DsWebssoUserDao dao;
	@Autowired
	private DsCommonUserDao commonUserDao;

	/**
	 * 这是注册功能
	 * @param po
	 */
	public int saveForRebind(DsWebssoUser po)
	{
		int result = 0;
		if(po.getSsoaccount().length() > 0)
		{
			DsWebssoUser u = dao.getBySsoaccount(po.getSsoaccount());
			if(u == null)
			{
				po.setId(UniqueId.genUniqueId());
				result = dao.save(po);
			}
		}
		return result;
	}

	/**
	 * 这是注册功能
	 * @param po
	 */
	public int saveForRegister(DsWebssoUser po)
	{
		int result = 0;
		boolean saveUser = false;
		DsCommonUser c = new DsCommonUser();
		if(po.getSsoaccount().length() > 0)// 直接注册
		{
			DsCommonUser o = commonUserDao.getByAccount(po.getSsoaccount());
			if(o == null)
			{
				saveUser = true;
				po.setId(UniqueId.genUniqueId());
			}
		}
		else// 第三方注册
		{
			DsWebssoUser u = dao.getByOpenid(po);// 一定是跟sso用户绑定的
			if(u == null)
			{
				po.setId(UniqueId.genUniqueId());
				po.setSsoaccount(getAccount(po.getId()));// 全小写
				po.setPassword("");
				dao.save(po);
				saveUser = true;
			}
		}
		if(saveUser)
		{
			c.setId(po.getId());
			c.setAccount(po.getSsoaccount());
			c.setPassword(po.getPassword());
			c.setName(po.getName());
			c.setIdcard(po.getIdcard());
			//c.setCakey(cakey);
			//c.setWorkcard(workcard);
			c.setEmail(po.getEmail());
			c.setMobile(po.getMobile());
			c.setPhone(po.getPhone());
			c.setStatus(po.getStatus());
			c.setCreatetime(TimeUtil.getCurrentTime());
			
			//c.setOrgpid(orgpid);
			//c.setOrgid(orgid);
			//c.setOrgpname(orgpname);
			//c.setOrgname(orgname);
			
			c.setType(po.getType());
			c.setTypename(po.getTypename());
			c.setExalias(po.getExalias());
			c.setExname(po.getExname());
			
			result = commonUserDao.save(c);
		}
		return result;
	}

	public void updateForRebind(DsWebssoUser po)
	{
		dao.update(po);
	}

	public DsWebssoUser getByOpendid(DsWebssoUser po)
	{
		return dao.getByOpenid(po);
	}

	public DsWebssoUser getBySsoaccount(String ssoaccount)
	{
		return dao.getBySsoaccount(ssoaccount);
	}

	public boolean getByAccount(String account)
	{
		DsCommonUser user = commonUserDao.getByAccount(account);
		return user != null;
	}

	private String getAccount(long id)
	{
		String account = "u" + longToString(id);
		DsCommonUser u = commonUserDao.getByAccount(account);
		for(int i = 1; u != null; i++)
		{
			account = "u" + longToString(id + i);
			u = commonUserDao.getByAccount(account);
		}
		return account;
	}

	private String longToString(long id)
	{
		Stack<Integer> s = new Stack<Integer>();
		String str = "abcdefghijklmnopqrstuvwxyz0123456789";
		long a = id;
		while(a != 0)
		{
			Long b = a % str.length();
			a = a / str.length();
			s.push(b.intValue());
		}
		StringBuilder sb = new StringBuilder();
		while(!s.isEmpty())
		{
			sb.append(str.charAt(s.pop()));
		}
		return sb.toString();
	}
}
