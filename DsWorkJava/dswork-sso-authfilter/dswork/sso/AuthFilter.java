package dswork.sso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dswork.sso.model.IFunc;
import dswork.sso.model.IRes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class AuthFilter implements Filter
{
	static Logger log = LoggerFactory.getLogger("dswork.sso");
	//private static Long SYSTEM_REFRESH = AuthFactory.getToLong(KEY_SYSTEM_REFRESH, 60000);// 当前系统的全部权限缓存更新间隔1000(1秒)|60000(1分钟)|3600000(1小时)|86400000(1天)
	private static Set<String> PAGE_IGNORE = new HashSet<String>();// 无需验证页面
	private static String CHECK_FIX;// 需要过滤的后缀名
	private static boolean isCheckAllFix;// 是否需要过滤所有后缀名
	private static boolean isCheckParam;// 是否需要验证参数
	private static Map<String, List<IRes>> resMap = new HashMap<String, List<IRes>>();// 只存放url作为key值
	private static long refreshTime = 0L;
	private static long  SYSTEM_REFRESH = 0L;
	private static String PAGE_NOACCESS;// 跳转到无权限提示页面

	private static void splitToSet(String str, Set<String> set)
	{
		if(str != null && str.length() > 0)
		{
			String[] values = str.trim().split(",");
			for(String value : values)
			{
				if(value.length() > 0)
				{
					set.add(value.trim());
				}
			}
		}
	}

	private static Timer _timer = null;
	private static TimerTask _timerTask = new TimerTask()
	{
		public void run()
		{
			log.info("--AuthFilter定时任务开始执行--");
			try
			{
				refreshSystem();
			}
			catch(Exception ex)
			{
				log.error(ex.getMessage());
			}
		}
	};
	private static void refreshSystem()
	{
		if(System.currentTimeMillis() > refreshTime)// 初始时refreshPermissionTime为0，肯定会执行
		{
			boolean isFailure = true;
			try
			{
				Map<String, List<IRes>> _resMap = new HashMap<String, List<IRes>>();
				
				IFunc[] funcArray = AuthFactory.getFunctionBySystem();// 初始化该系统已配置的全部功能
				if(funcArray != null)// 读得到数据，结果集也可以为0
				{
					for(IFunc o : funcArray)// 将系统里的所有资源初始化到map中
					{
						if(o == null || o.getResList() == null || o.getResList().size() == 0)
						{
							continue;
						}
						for(IRes r : o.getResList())
						{
							if(_resMap.get(r.getUrl()) == null)
							{
								_resMap.put(r.getUrl(), new ArrayList<IRes>());
							}
							if(isCheckParam)// 需要参数判断时才放入参数信息，否则只需要有url的key值即可
							{
								_resMap.get(r.getUrl()).add(r);// 也可只放入参数值，但放入r的话，方便取map信息
							}
						}
					}
					resMap.clear();
					resMap.putAll(_resMap);
					refreshTime += System.currentTimeMillis() + SYSTEM_REFRESH;
					isFailure = false;
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			if(isFailure)// funcArray为null或异常（cxf为null时抛出异常）
			{
				resMap.clear();
				resMap.put("dswork_null_key", new ArrayList<IRes>());// 读取出错，随便放一个nullkey，让它不为空，避免读不到信息时权限验证被越
			}
		}
	}

	public void init(FilterConfig config) throws ServletException
	{
		SYSTEM_REFRESH = getToLong(config.getInitParameter("refreshTime"), 3600000L);
		PAGE_NOACCESS = getToString(config.getInitParameter("forbiddenPage"), "");
		splitToSet(getToString(config.getInitParameter("ignorePage"), ""), PAGE_IGNORE);
		splitToSet(PAGE_NOACCESS, PAGE_IGNORE);
		CHECK_FIX = getToString(config.getInitParameter("fixSuffix"), "");
		isCheckParam = getToBoolean(config.getInitParameter("checkParam"), false);
		if(CHECK_FIX.length() > 0)
		{
			CHECK_FIX = "," + CHECK_FIX + ",";
			isCheckAllFix = false;
		}
		else
		{
			isCheckAllFix = true;
		}
		_timer = new Timer(true);
		// Timer.schedule(TimerTask task, Date date, long period)// 从date开始,每period毫秒执行task.
		_timer.schedule(_timerTask, 0, SYSTEM_REFRESH);// 从服务器启动开始运行,每period毫秒执行
	}

	public void destroy()
	{
	}

	/**
	 * 判断是否为不处理的页面
	 * @param uri 本地相对地址
	 * @return true 不用进行权限判断，false 要进行权限判断
	 */
	private static boolean isIgnoreURI(String uri)
	{
		// System.out.println("当前访问的URI是：" + uri);
		if(PAGE_IGNORE.contains(uri))// 判断是否为不处理页面
		{
			return true;// 是不处理的页面，不用判断
		}
		if(!isCheckAllFix)// 判断是否要判断后缀名
		{
			int i = uri.lastIndexOf(".");
			if(i == -1)
			{
				return true;// 找不到后缀名，不用判断
			}
			String fix = "," + uri.substring(uri.lastIndexOf(".") + 1, uri.length()) + ",";
			if(CHECK_FIX.indexOf(fix) == -1)// 判断后缀名是否是要处理
			{
				return true;// 不在配置的后缀名里，不用判断
			}
		}
		return false;
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String relativeURI = request.getRequestURI().trim();// 相对地址
		if(request.getContextPath().length() > 0)
		{
			relativeURI = relativeURI.replaceFirst(request.getContextPath(), "");
		}
		if(isIgnoreURI(relativeURI))// 是否不用判断
		{
			chain.doFilter(request, response);
			return;
		}
		// 取得当前用户账号
		String userAccount = WebFilter.getAccount(request.getSession());
		if(isAccess(request, userAccount, relativeURI))// 判断是否能访问该页面
		{
			chain.doFilter(request, response);// 有权限访问
			return;
		}
		// 没权限
		response.sendRedirect(request.getContextPath() + PAGE_NOACCESS); // 无权限访问，跳转页面
		return;
	}

	private static boolean isAccess(HttpServletRequest request, String userAccount, String uri)
	{
		if(userAccount.length() == 0)
		{
			return false;
		}
		//refreshSystem();// 刷新数据
		if(resMap.get("dswork_null_key") != null)
		{
			return false;// 读不到权限数据
		}
		if(resMap.get(uri) == null)// 不需要验证
		{
			return true;// 可以访问
		}
		// 获取用户有权限访问的功能结构
		Map<String, List<IRes>> userResMap = getUserRes(userAccount, request.getSession());
		if(userResMap.get(uri) == null)// 没权限
		{
			return false;// 不可以访问
		}
		if(isCheckParam)// 是否需要验证参数
		{
			try
			{
				Map<String, String[]> reqParamMap = request.getParameterMap();// 锁定的只读map
				int mapLen = reqParamMap.size();
				List<IRes> resList = userResMap.get(uri);
				boolean flag = true;
				for(IRes r : resList)
				{
					if(r == null)
					{
						return false;// 应该是出错的数据，正常情况下不会出现
					}
					if(r.getParamMap().size() == 0)// 没参数，不用判断了
					{
						return true;// 没参数，直接就算过了，相当于不用判断参数
					}
					if(mapLen == 0)// 有参数，但request没参数
					{
						continue;// 没法匹配，看其余的res有没有无参数的
					}
					flag = true;// 重新初始化
					for(Entry<String, String> entry : r.getParamMap().entrySet())
					{
						// 此处没有处理这种情况：有多个同名参数
						String key = entry.getKey();
						String value = entry.getValue();
						// reqParamMap.get(key)是String[]类型，转为list再跟value判断
						if(reqParamMap.containsKey(key) && Arrays.asList(reqParamMap.get(key)).contains(value))
						{
						}
						else
						{
							flag = false;
							break;// 任意一个找不到则跳出内层for循环
						}
					}
					if(flag)
					{
						return true;// 有权限
					}
				}
			}
			catch(Exception e)
			{
			}
			return false;// 出错或没有匹配到参数，当没权限
		}
		return true;
	}

	private final static String KEY_SESSIONFUNC = "dswork.cas.func";// 存放在session中用户岗位信息的key
	private final static String KEY_SESSIONFUNC_ACCOUNT = "dswork.cas.func.account";// 存放在session中用户岗位信息的key
	// 获取用户有权限访问的资源
	private static Map<String, List<IRes>> getUserRes(String userAccount, HttpSession session)
	{
		Map<String, List<IRes>> userResMap = new HashMap<String, List<IRes>>();
		String oldAccount = String.valueOf(session.getAttribute(KEY_SESSIONFUNC_ACCOUNT));
		if(!userAccount.equals(oldAccount))
		{
			session.setAttribute(KEY_SESSIONFUNC_ACCOUNT, userAccount);// 更新新的用户
			session.setAttribute(KEY_SESSIONFUNC, null);// 清空旧的权限信息
		}
		// 用户未改变，则session有信息
		if(session.getAttribute(KEY_SESSIONFUNC) != null)
		{
			userResMap = (Map<String, List<IRes>>) session.getAttribute(KEY_SESSIONFUNC);
		}
		else
		{
			IFunc[] myFuncArray = AuthFactory.getFunctionByUser(userAccount);
			if(myFuncArray != null)
			{
				for(IFunc o : myFuncArray)
				{
					if(o == null || o.getResList() == null || o.getResList().size() == 0)
					{
						continue;
					}
					for(IRes r : o.getResList())
					{
						if(userResMap.get(r.getUrl()) == null)
						{
							userResMap.put(r.getUrl(), new ArrayList<IRes>());
						}
						if(isCheckParam)
						{
							userResMap.get(r.getUrl()).add(r);
						}
					}
				}
			}
			session.setAttribute(KEY_SESSIONFUNC, userResMap);
		}
		return userResMap;
	}

	/**
	 * 获取用户菜单功能，过滤掉不显示在菜单上的功能项
	 * @param account
	 * @return IFunc[]
	 */
	public static IFunc[] getFuncByUser(String account)
	{
		List<IFunc> list = new ArrayList<IFunc>();
		if(account != null && !account.equals(""))
		{
			IFunc[] arr = AuthFactory.getFunctionByUser(account);
			if(arr != null)
			{
				for(IFunc m : arr)
				{
					if(m.getStatus() == 1)
					{
						list.add(m);
					}
				}
			}
			return list.toArray(new IFunc[list.size()]);
		}
		return null;
	}
	
	
	
	
	
	
	
	

	private static final String getString(Object v)
	{
		if(v != null)
		{
			return String.valueOf(v).trim();
		}
		return null;
	}
	private static final long getToLong(String value, long defaultValue)
	{
		try
		{
			return Long.parseLong(getString(value));
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}
	private static final String getToString(String value, String defaultValue)
	{
		try
		{
			String str = getString(value);
			if (str != null)
			{
				return str;
			}
		}
		catch (Exception e)
		{
		}
		return defaultValue;
	}
	private static final boolean getToBoolean(String value, boolean defaultValue)
	{
		try
		{
			String str = getString(value);
			if(str.equals("true"))
			{
				return Boolean.TRUE;
			}
			else if(str.equals("false"))
			{
				return Boolean.FALSE;
			}
		}
		catch (Exception e)
		{
		}
		return defaultValue;
	}
}
