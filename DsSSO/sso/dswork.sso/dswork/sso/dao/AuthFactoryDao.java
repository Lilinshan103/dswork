package dswork.sso.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Repository;

import dswork.core.db.MyBatisDao;
import dswork.core.util.TimeUtil;
import dswork.core.util.UniqueId;
import dswork.sso.model.IFunc;
import dswork.sso.model.IOrg;
import dswork.sso.model.ISystem;
import dswork.sso.model.IUser;
import dswork.sso.model.LoginUser;

@Repository
@SuppressWarnings("all")
public class AuthFactoryDao extends MyBatisDao
{
	@Override
	public Class getEntityClass()
	{
		return AuthFactoryDao.class;
	}

	// login //////////////////////////////////////////////////////////////////
	public void saveLogLogin(String ticket, String ip, String account, String name, boolean isSuccess)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", UniqueId.genUniqueId());
		map.put("logintime", TimeUtil.getCurrentTime());
		map.put("logouttime", isSuccess ? "0" : TimeUtil.getCurrentTime());// 退出前标识为0
		map.put("ticket", ticket);
		map.put("ip", ip);
		map.put("account", account);
		map.put("name", name);
		map.put("status", isSuccess ? "1" : "0");
		executeInsert("insert", map);
	}

	public void saveLogLogout(String ticket, boolean isTimeout, boolean isUpdatePassword)
	{
		if(ticket.length() > 0 && !ticket.equals("null"))
		{
			Map<String, Object> map = new HashMap<String, Object>();
			String time = TimeUtil.getCurrentTime();
			map.put("logouttime", time);
			map.put("timeouttime", isTimeout ? time : "");
			map.put("pwdtime", isUpdatePassword ? time : "");
			map.put("ticket", ticket);
			executeUpdate("update", map);
		}
	}

	/**
	 * 用户密码重置。
	 * @param userid - 要修改密码的用户的编号。
	 * @param pwd - 新的密码信息。
	 */
	public void updatePassword(String account, String password)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account", account);
		map.put("password", password);
		executeUpdate("updatePassword", map);
	}

	/**
	 * 根据用户输入的账号来获得用户对象，有则返回用户对象，无则返回null。
	 * @param account - 用户输入的账号字符串。
	 * @return User - 用户对象。
	 */
	public LoginUser getLoginUserByAccount(String account)
	{
		return (LoginUser) executeSelect("getLoginUserByAccount", account);
	}

	/**
	 * 根据用户输入的账号来获得用户对象，有则返回用户对象，无则返回null。
	 * @param account - 用户输入的账号字符串。
	 * @return User - 用户对象。
	 */
	public LoginUser getLoginUserByCAKEY(String cakey)
	{
		return (LoginUser) executeSelect("getLoginUserByCAKEY", cakey);
	}
	
	
	
	// system//////////////////////////////////////////////////////////////////
	private static ConcurrentMap<String, ISystem> map = new ConcurrentHashMap<String, ISystem>();
	private static long refreshTime = 0L;

	private ISystem getISystem(String systemAlias)
	{
		ISystem sys = map.get(systemAlias);
		if(sys==null || System.currentTimeMillis() > refreshTime)// 初始时refreshPermissionTime为0，肯定会执行
		{
			// 刷新全部系统配置信息
			List<ISystem> list = this.executeSelectList("querySystem", null);// 获取全部系统配置信息
			map.clear();
			for(ISystem i : list)
			{
				map.put(i.getAlias(), i);
			}
			refreshTime += System.currentTimeMillis() + 60000;// 刷新间隔1000(1秒)|60000(1分钟)|3600000(1小时)|86400000(1天)
			sys = null;
		}
		if(sys == null)
		{
			sys = map.get(systemAlias);
		}
		return sys;// 没有则返回null
	}
	private String getSystemId(String systemAlias)
	{
		ISystem sys = getISystem(systemAlias);
		if(sys != null)
		{
			return String.valueOf(sys.getId());
		}
		return "0";// 该id主要作为sql查询条件，没有值时返回0，DS_COMMON_SYSTEM表没有0的记录
	}

	public ISystem getSystem(String systemAlias)
	{
		return getISystem(systemAlias);
	}
	public List<ISystem> querySystemByAccount(String account)
	{
		return this.executeSelectList("querySystemByAccount", account);
	}

	// org //////////////////////////////////////////////////////////////////
	public IOrg getOrgByOrgid(String orgid)
	{
		return (IOrg) this.executeSelect("getOrgByOrgid", orgid);
	}
	public List<IOrg> queryOrgByOrgPid(long orgpid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgpid", orgpid);
		return this.executeSelectList("queryOrgByOrgpid", map);
	}

	// post //////////////////////////////////////////////////////////////////
	public List<IOrg> queryPostByUserId(Long userid)
	{
		return this.executeSelectList("queryPostByUserId", userid);
	}

	// user //////////////////////////////////////////////////////////////////
	public IUser getUserByAccount(String account)
	{
		return (IUser) this.executeSelect("getUserByAccount", account);
	}

	public List<IUser> queryUserByPostid(String postid)
	{
		return this.executeSelectList("queryUserByPostid", postid);
	}

	public List<IUser> queryUserByOrgPid(String orgpid)
	{
		return this.executeSelectList("queryUserByOrgPid", orgpid);
	}

	public List<IUser> queryUserByOrgId(String orgid)
	{
		return this.executeSelectList("queryUserByOrgId", orgid);
	}
	
	// func //////////////////////////////////////////////////////////////////
	public List<IFunc> queryFuncBySystemAlias(String systemAlias)
	{
		return this.executeSelectList("queryFuncBySystemid", getSystemId(systemAlias));
	}

	public List<IFunc> getFuncBySystemAliasAndAccount(String systemAlias, String account)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("systemid", getSystemId(systemAlias));
		map.put("account", account);
		return this.executeSelectList("getFuncBySystemidAndAccount", map);
	}

	public List<IFunc> getFuncBySystemAliasAndOrgid(String systemAlias, String orgid)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("systemid", getSystemId(systemAlias));
		map.put("orgid", orgid);
		return this.executeSelectList("getFuncBySystemidAndOrgid", map);
	}
}
