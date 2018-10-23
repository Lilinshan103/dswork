/**
 * 调用webapi的工厂类
 */
package dswork.sso;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.reflect.TypeToken;

import dswork.sso.http.HttpUtil;
import dswork.sso.model.IFunc;
import dswork.sso.model.IOrg;
import dswork.sso.model.ISystem;
import dswork.sso.model.IUser;

public class AuthFactory
{
	static com.google.gson.Gson gson = AuthGlobal.getGson();
	static String url = "";
	static Logger log = LoggerFactory.getLogger("dswork.sso");
	
	public static String toJson(Object object)
	{
		return gson.toJson(object);
	}
	
	private static StringBuilder getPath(String path)
	{
		StringBuilder sb = new StringBuilder(30);
		sb.append(AuthGlobal.getURL()).append("/").append(path).append("?name=").append(AuthGlobal.getName()).append("&pwd=").append(AuthGlobal.getPwd());
		return sb;
	}
	
	//////////////////////////////////////////////////////////////////////////////
	// 权限相关的方法
	//////////////////////////////////////////////////////////////////////////////
	/**
	 * 获取子系统信息
	 * @param systemPassword 系统访问密码
	 * @return ISystem
	 */
	public static ISystem getSystem()
	{
		String u = getPath("getSystem").toString();
		String v = new HttpUtil().create(u, u.startsWith("https:")).connect().trim();
		if(log.isDebugEnabled())
		{
			log.debug("AuthFactory:url=" + u + ", json:" + v);
		}
		ISystem m = gson.fromJson(v, ISystem.class);
		return m;
	}
	/**
	 * 获取用户有权限访问的子系统
	 * @param userAccount 用户帐号
	 * @return ISystem[]
	 */
	public static ISystem[] getSystemByUser(String userAccount)
	{
		String u = getPath("getSystemByUser").append("&userAccount=").append(userAccount).toString();
		String v = new HttpUtil().create(u, u.startsWith("https:")).connect().trim();
		if(log.isDebugEnabled())
		{
			log.debug("AuthFactory:url=" + u + ", json:" + v);
		}
		List<ISystem> list = gson.fromJson(v, new TypeToken<List<ISystem>>(){}.getType());
		return list.toArray(new ISystem[list.size()]);
	}
	
	/**
	 * 获取系统的功能结构
	 * @param systemAlias 系统标识
	 * @param systemPassword 系统访问密码
	 * @return IFunc[]
	 */
	public static IFunc[] getFunctionBySystem()
	{
		String u = getPath("getFunctionBySystem").toString();
		String v = new HttpUtil().create(u, u.startsWith("https:")).connect().trim();
		if(log.isDebugEnabled())
		{
			log.debug("AuthFactory:url=" + u + ", json:" + v);
		}
		List<IFunc> list = gson.fromJson(v, new TypeToken<List<IFunc>>(){}.getType());
		return list.toArray(new IFunc[list.size()]);
	}
	
	/**
	 * 获取用户权限范围内的系统功能结构
	 * @param systemAlias 系统标识
	 * @param systemPassword 系统访问密码
	 * @param userAccount 用户帐号
	 * @return IFunc[]
	 */
	public static IFunc[] getFunctionByUser(String userAccount)
	{
		String u = getPath("getFunctionByUser").append("&userAccount=").append(userAccount).toString();
		String v = new HttpUtil().create(u, u.startsWith("https:")).connect().trim();
		if(log.isDebugEnabled())
		{
			log.debug("AuthFactory:url=" + u + ", json:" + v);
		}
		List<IFunc> list = gson.fromJson(v, new TypeToken<List<IFunc>>(){}.getType());
		return list.toArray(new IFunc[list.size()]);
	}
	
	/**
	 * 获取岗位权限范围内的系统功能结构
	 * @param systemAlias 系统标识
	 * @param systemPassword 系统访问密码
	 * @param orgId 单位ID、部门ID、岗位ID
	 * @return IFunc[]
	 */
	public static IFunc[] getFunctionByOrg(String orgId)
	{
		String u = getPath("getFunctionByOrg").append("&orgId=").append(orgId).toString();
		String v = new HttpUtil().create(u, u.startsWith("https:")).connect().trim();
		if(log.isDebugEnabled())
		{
			log.debug("AuthFactory:url=" + u + ", json:" + v);
		}
		List<IFunc> list = gson.fromJson(v, new TypeToken<List<IFunc>>(){}.getType());
		return list.toArray(new IFunc[list.size()]);
	}

	//////////////////////////////////////////////////////////////////////////////
	// 用户相关的方法
	//////////////////////////////////////////////////////////////////////////////
	/**
	 * @note 获取组织机构
	 * @param orgId 组织机构ID
	 * @return IOrg
	 */
	public static IOrg getOrg(String orgId)
	{
		String u = getPath("getOrg").append("&orgId=").append(orgId).toString();
		String v = new HttpUtil().create(u, u.startsWith("https:")).connect().trim();
		if(log.isDebugEnabled())
		{
			log.debug("AuthFactory:url=" + u + ", json:" + v);
		}
		IOrg m = gson.fromJson(v, IOrg.class);
		return m;
	}
	
	/**
	 * @note 获取下级组织机构(status:2单位,1部门,0岗位)
	 * @param orgPid 组织机构ID，为0则取顶级
	 * @return IOrg[]
	 */
	public static IOrg[] queryOrgByOrgParent(String orgPid)
	{
		String u = getPath("queryOrgByOrgParent").append("&orgPid=").append(orgPid).toString();
		String v = new HttpUtil().create(u, u.startsWith("https:")).connect().trim();
		if(log.isDebugEnabled())
		{
			log.debug("AuthFactory:url=" + u + ", json:" + v);
		}
		List<IOrg> list = gson.fromJson(v, new TypeToken<List<IOrg>>(){}.getType());
		return list.toArray(new IOrg[list.size()]);
	}

	/**
	 * @note 获取组织机构下的岗位
	 * @param orgId 组织机构ID
	 * @return IOrg[]
	 */
	public static IOrg[] queryPostByOrg(String orgId)
	{
		String u = getPath("queryPostByOrg").append("&orgId=").append(orgId).toString();
		String v = new HttpUtil().create(u, u.startsWith("https:")).connect().trim();
		if(log.isDebugEnabled())
		{
			log.debug("AuthFactory:url=" + u + ", json:" + v);
		}
		List<IOrg> list = gson.fromJson(v, new TypeToken<List<IOrg>>(){}.getType());
		return list.toArray(new IOrg[list.size()]);
	}
	
	/**
	 * @note 获取指定用户的基本信息
	 * @param userAccount 用户帐号
	 * @return IUser
	 */
	public static IUser getUser(String userAccount)
	{
		String u = getPath("getUser").append("&userAccount=").append(userAccount).toString();
		String v = new HttpUtil().create(u, u.startsWith("https:")).connect().trim();
		if(log.isDebugEnabled())
		{
			log.debug("AuthFactory:url=" + u + ", json:" + v);
		}
		IUser m = gson.fromJson(v, IUser.class);
		return m;
	}
	
	/**
	 * @note 获取岗位下的所有用户
	 * @param postId 岗位ID
	 * @return IUser[]
	 */
	public static IUser[] queryUserByPost(String postId)
	{
		String u = getPath("queryUserByPost").append("&postId=").append(postId).toString();
		String v = new HttpUtil().create(u, u.startsWith("https:")).connect().trim();
		if(log.isDebugEnabled())
		{
			log.debug("AuthFactory:url=" + u + ", json:" + v);
		}
		List<IUser> list = gson.fromJson(v, new TypeToken<List<IUser>>(){}.getType());
		return list.toArray(new IUser[list.size()]);
	}
	
	/**
	 * @note 获取指定单位下的用户，不含子单位
	 * @param orgPid 单位ID
	 * @return IUser[]
	 */
	public static IUser[] queryUserByOrgParent(String orgPid)
	{
		String u = getPath("queryUserByOrgParent").append("&orgPid=").append(orgPid).toString();
		String v = new HttpUtil().create(u, u.startsWith("https:")).connect().trim();
		if(log.isDebugEnabled())
		{
			log.debug("AuthFactory:url=" + u + ", json:" + v);
		}
		List<IUser> list = gson.fromJson(v, new TypeToken<List<IUser>>(){}.getType());
		return list.toArray(new IUser[list.size()]);
	}
	
	/**
	 * @note 获取指定部门下的用户，不含子部门
	 * @param orgId 部门 ID
	 * @return IUser[]
	 */
	public static IUser[] queryUserByOrg(String orgId)
	{
		String u = getPath("queryUserByOrg").append("&orgId=").append(orgId).toString();
		String v = new HttpUtil().create(u, u.startsWith("https:")).connect().trim();
		if(log.isDebugEnabled())
		{
			log.debug("AuthFactory:url=" + u + ", json:" + v);
		}
		List<IUser> list = gson.fromJson(v, new TypeToken<List<IUser>>(){}.getType());
		return list.toArray(new IUser[list.size()]);
	}
	
	/**
	 * @note 获取指定用户拥有的岗位
	 * @param userAccount 用户帐号
	 * @return IOrg[]
	 */
	public static IOrg[] queryPostByUser(String userAccount)
	{
		String u = getPath("queryPostByUser").append("&userAccount=").append(userAccount).toString();
		String v = new HttpUtil().create(u, u.startsWith("https:")).connect().trim();
		if(log.isDebugEnabled())
		{
			log.debug("AuthFactory:url=" + u + ", json:" + v);
		}
		List<IOrg> list = gson.fromJson(v, new TypeToken<List<IOrg>>(){}.getType());
		return list.toArray(new IOrg[list.size()]);
	}
}
