/**
 * 获取认证信息
 */
package dswork.sso.controller;

import java.io.PrintWriter;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.sso.model.IFunc;
import dswork.sso.model.IOrg;
import dswork.sso.model.ISystem;
import dswork.sso.model.IUser;
import dswork.sso.service.AuthFactoryService;
import dswork.sso.service.TicketService;
import dswork.web.MyRequest;

@Controller
//@RequestMapping("/api")
public class APIController
{
	private static com.google.gson.GsonBuilder builder = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");
	private static com.google.gson.Gson gson = builder.create();
	//static com.alibaba.fastjson.serializer.SerializeConfig mapping = new com.alibaba.fastjson.serializer.SerializeConfig();
	//static{mapping.put(java.util.Date.class, new com.alibaba.fastjson.serializer.SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));}
	private static String toJson(Object object)
	{
		System.out.println(gson.toJson(object));
		return gson.toJson(object);
		//return com.alibaba.fastjson.JSON.toJSONString(object, mapping);
	}
	//private static <T> T toBean(String json, Class<T> classOfT)
	//{
	//	return gson.fromJson(json, classOfT);
	//	//return com.alibaba.fastjson.JSON.parseObject(json, classOfT);
	//}
	
	@Autowired
	private AuthFactoryService service;


	//////////////////////////////////////////////////////////////////////////////
	// 登录及校验相关的方法
	//////////////////////////////////////////////////////////////////////////////
	/**
	 * @note 获取指定用户的基本信息
	 * @param ticket 一次性用户ticket
	 * @return Map
	 */
	@RequestMapping("/getLoginer")
	public void getLoginer(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String onceTicket = req.getString("ticket");
		String account = null;
		if(onceTicket.length() > 0)
		{
			account = TicketService.getValueByOnceTicket(onceTicket);
		}
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			if(account != null)
			{
				IUser m = service.getUserByAccount(account);
				out.print(toJson(m));
			}
			else
			{
				out.print("{}");
			}
			//out.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	private static String pwdMd5(String str)
	{
		if(str != null)
		{
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
			String v = sdf.format(cal.getTime()) + str;
			StringBuilder sb = new StringBuilder();
			try
			{
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] digest = md.digest(v.getBytes("UTF-8"));
				String stmp = "";
				for(int n = 0; n < digest.length; n++)
				{
					stmp = (Integer.toHexString(digest[n] & 0XFF));
					sb.append((stmp.length() == 1) ? "0" : "").append(stmp);
				}
				return sb.toString().toUpperCase(Locale.ENGLISH);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				sb = null;
			}
		}
		return null;
	}
	private boolean isSystemCheck(String systemAlias, String pwd)
	{
		ISystem sys = service.getSystem(systemAlias);
		return (sys != null && String.valueOf(pwd).equals(pwdMd5(sys.getPassword())));
	}

	//////////////////////////////////////////////////////////////////////////////
	// 权限相关的方法
	//////////////////////////////////////////////////////////////////////////////
	/**
	 * 获取子系统信息
	 * @param systemAlias 系统标识
	 * @param systemPassword 系统访问密码
	 * @return ISystem
	 */
	@RequestMapping("/getSystem")
	public void getSystem(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String systemAlias = req.getString("name");
		String pwd = req.getString("pwd");
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			if(isSystemCheck(systemAlias, pwd))
			{
				ISystem m = service.getSystem(systemAlias);
				out.print(toJson(m));
			}
			out.print("");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * 获取用户有权限访问的子系统
	 * @param userAccount 用户帐号
	 * @return ISystem[]
	 */
	@RequestMapping("/getSystemByUser")
	public void getSystemByUser(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String systemAlias = req.getString("name");
		String pwd = req.getString("pwd");
		String userAccount = req.getString("userAccount");
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			if(isSystemCheck(systemAlias, pwd))
			{
				ISystem[] m = service.getSystemByUser(userAccount);
				out.print(toJson(m));
			}
			out.print("");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * 获取系统的功能结构
	 * @param systemAlias 系统标识
	 * @param systemPassword 系统访问密码
	 * @return IFunc[]
	 */
	@RequestMapping("/getFunctionBySystem")
	public void getFunctionBySystem(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String systemAlias = req.getString("name");
		String pwd = req.getString("pwd");
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			if(isSystemCheck(systemAlias, pwd))
			{
				IFunc[] m = service.getFuncBySystemAlias(systemAlias);
				out.print(toJson(m));
			}
			out.print("");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * 获取用户权限范围内的系统功能结构
	 * @param systemAlias 系统标识
	 * @param systemPassword 系统访问密码
	 * @param userAccount 用户帐号
	 * @return IFunc[]
	 */
	@RequestMapping("/getFunctionByUser")
	public void getFunctionByUser(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String systemAlias = req.getString("name");
		String pwd = req.getString("pwd");
		String userAccount = req.getString("userAccount");
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			if(isSystemCheck(systemAlias, pwd))
			{
				IFunc[] m = service.getFuncBySystemAliasAndAccount(systemAlias, userAccount);
				out.print(toJson(m));
			}
			out.print("");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * 获取岗位权限范围内的系统功能结构
	 * @param systemAlias 系统标识
	 * @param systemPassword 系统访问密码
	 * @param postId 岗位ID
	 * @return IFunc[]
	 */
	@RequestMapping("/getFunctionByOrg")
	public void getFunctionByOrg(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String systemAlias = req.getString("name");
		String pwd = req.getString("pwd");
		String orgId = req.getString("orgId");
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			if(isSystemCheck(systemAlias, pwd))
			{
				IFunc[] m = service.getFuncBySystemAliasAndOrgid(systemAlias, orgId);
				out.print(toJson(m));
			}
			out.print("");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	//////////////////////////////////////////////////////////////////////////////
	// 用户相关的方法
	//////////////////////////////////////////////////////////////////////////////
	/**
	 * @note 获取组织机构
	 * @param orgId 组织机构ID
	 * @return IOrg
	 */
	@RequestMapping("/getOrg")
	public void getOrg(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String systemAlias = req.getString("name");
		String pwd = req.getString("pwd");
		String orgId = req.getString("orgId");
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			if(isSystemCheck(systemAlias, pwd))
			{
				IOrg m = service.getOrgByOrgId(orgId);
				out.print(toJson(m));
			}
			out.print("");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * @note 获取下级组织机构(status:2单位,1部门,0岗位)
	 * @param orgPid 组织机构ID，为0则取顶级
	 * @return IOrg[]
	 */
	@RequestMapping("/queryOrgByOrgParent")
	public void queryOrgByOrgParent(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String systemAlias = req.getString("name");
		String pwd = req.getString("pwd");
		String orgPid = req.getString("orgPid");
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			if(isSystemCheck(systemAlias, pwd))
			{
				IOrg[] m = service.queryOrgByOrgPid(orgPid);
				out.print(toJson(m));
			}
			out.print("");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * @note 获取组织机构下的岗位
	 * @param orgId 组织机构ID
	 * @return IOrg[]
	 */
	@RequestMapping("/queryPostByOrg")
	public void queryPostByOrg(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String systemAlias = req.getString("name");
		String pwd = req.getString("pwd");
		String orgId = req.getString("orgId");
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			if(isSystemCheck(systemAlias, pwd))
			{
				IOrg[] arr = service.queryOrgByOrgPid(orgId);
				if (arr != null)
				{
					List<IOrg> newList = new ArrayList<IOrg>();
					for(IOrg m : arr)
					{
						if(m.getStatus() == 0)//岗位
						{
							newList.add(m);
						}
					}
					IOrg[] m = newList.toArray(new IOrg[newList.size()]);
					out.print(toJson(m));
				}
			}
			out.print("");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * @note 获取指定用户的基本信息
	 * @param userAccount 用户帐号
	 * @return IUser
	 */
	@RequestMapping("/getUser")
	public void getUser(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String systemAlias = req.getString("name");
		String pwd = req.getString("pwd");
		String userAccount = req.getString("userAccount");
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			if(isSystemCheck(systemAlias, pwd))
			{
				IUser m = service.getUserByAccount(userAccount);
				out.print(toJson(m));
			}
			out.print("");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * @note 获取岗位下的所有用户
	 * @param postId 岗位ID
	 * @return IUser[]
	 */
	@RequestMapping("/queryUserByPost")
	public void queryUserByPost(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String systemAlias = req.getString("name");
		String pwd = req.getString("pwd");
		String postId = req.getString("postId");
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			if(isSystemCheck(systemAlias, pwd))
			{
				IUser[] m = service.queryUserByPostid(postId);
				out.print(toJson(m));
			}
			out.print("");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * @note 获取指定单位下的用户，不含子单位
	 * @param orgPid 单位ID
	 * @return IUser[]
	 */
	@RequestMapping("/queryUserByOrgParent")
	public void queryUserByOrgParent(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String systemAlias = req.getString("name");
		String pwd = req.getString("pwd");
		String orgPid = req.getString("orgPid");
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			if(isSystemCheck(systemAlias, pwd))
			{
				IUser[] m = service.queryUserByOrgPid(orgPid);
				out.print(toJson(m));
			}
			out.print("");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * @note 获取指定部门下的用户，不含子部门
	 * @param orgId 部门 ID
	 * @return IUser[]
	 */
	@RequestMapping("/queryUserByOrg")
	public void queryUserByOrg(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String systemAlias = req.getString("name");
		String pwd = req.getString("pwd");
		String orgId = req.getString("orgId");
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			if(isSystemCheck(systemAlias, pwd))
			{
				IUser[] m = service.queryUserByOrgId(orgId);
				out.print(toJson(m));
			}
			out.print("");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * @note 获取指定用户拥有的岗位
	 * @param userAccount 用户帐号
	 * @return IOrg[]
	 */
	@RequestMapping("/queryPostByUser")
	public void queryPostByUser(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String systemAlias = req.getString("name");
		String pwd = req.getString("pwd");
		String userAccount = req.getString("userAccount");
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			if(isSystemCheck(systemAlias, pwd))
			{
				IUser user = service.getUserByAccount(userAccount);
				if(user != null)
				{
					IOrg[] m = service.getPostByUserId(user.getId());
					out.print(toJson(m));
				}
			}
			out.print("");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
