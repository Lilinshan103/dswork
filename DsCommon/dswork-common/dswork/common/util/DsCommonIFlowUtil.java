package dswork.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dswork.common.dao.DsCommonDaoIFlow;
import dswork.common.model.IFlow;
import dswork.common.model.IFlowPi;
import dswork.common.model.IFlowPiData;
import dswork.common.model.IFlowTask;
import dswork.common.model.IFlowWaiting;
import dswork.spring.BeanFactory;

public class DsCommonIFlowUtil
{
	private static DsCommonDaoIFlow dao = null;

	private static void init()
	{
		if(dao == null)
		{
			dao = (DsCommonDaoIFlow) BeanFactory.getBean("dsCommonDaoIFlow");
		}
	}

	/**
	 * 流程启动
	 * @param alias 启动流程的标识
	 * @param users 启动节点任务处理人，如果为null，则使用流程配置中的处理人信息
	 * @param ywlsh 业务流水号
	 * @param caccount 提交人账号
	 * @param cname 提交人姓名
	 * @param piDay 时限天数
	 * @param isWorkDay 时限天数类型(false日历日,true工作日)
	 * @return 流程实例ID
	 */
	public String start(String alias, String users, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay)
	{
		try
		{
			init();
			return dao.saveStart(alias, users, ywlsh, sblsh, caccount, cname, piDay, isWorkDay);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 流程启动
	 * @param alias 启动流程的标识
	 * @param ywlsh 业务流水号
	 * @param caccount 提交人账号
	 * @param cname 提交人姓名
	 * @param piDay 时限天数
	 * @param isWorkDay 时限天数类型(false日历日,true工作日)
	 * @return 流程实例ID
	 */
	public String start(String alias, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay)
	{
		return start(alias, null, ywlsh, sblsh, caccount, cname, piDay, isWorkDay);
	}

	/**
	 * 流程启动并返回开始节点待办信息
	 * @param alias 启动流程的标识
	 * @param users 启动节点任务处理人，如果为null，则使用流程配置中的处理人信息
	 * @param ywlsh 业务流水号
	 * @param caccount 提交人账号
	 * @param cname 提交人姓名
	 * @param piDay 时限天数
	 * @param isWorkDay 时限天数类型(false日历日,true工作日)
	 * @return 流程实例的start待办信息或null
	 */
	public IFlowWaiting startFlow(String alias, String users, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay)
	{
		try
		{
			init();
			return dao.saveFlowStart(alias, users, ywlsh, sblsh, caccount, cname, piDay, isWorkDay);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 流程启动并返回开始节点待办信息
	 * @param alias 启动流程的标识
	 * @param ywlsh 业务流水号
	 * @param caccount 提交人账号
	 * @param cname 提交人姓名
	 * @param piDay 时限天数
	 * @param isWorkDay 时限天数类型(false日历日,true工作日)
	 * @return 流程实例的start待办信息或null
	 */
	public IFlowWaiting startFlow(String alias, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay)
	{
		return startFlow(alias, null, ywlsh, sblsh, caccount, cname, piDay, isWorkDay);
	}
	
	public void stop(String piid)
	{
		try
		{
			init();
			dao.saveStop(Long.parseLong(piid));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 流程处理
	 * @param waitid 待办事项ID
	 * @param nextTalias 下级任务列表，如果为null，处理当前任务后，会结束流程
	 * @param nextTusers 下级任务处理人列表，如果为null，则使用流程配置中的处理人信息
	 * @param paccount 当前处理人账号
	 * @param pname 当前处理人姓名
	 * @param resultType 处理类型
	 * @param resultMsg 处理意见
	 * @return true|false
	 */
	public boolean process(long waitid, String[] nextTalias, String[] nextTusers, String paccount, String pname, String resultType, String resultMsg, String formdata)
	{
		try
		{
			init();
			return dao.saveProcess(waitid, nextTalias, nextTusers, paccount, pname, resultType, resultMsg, formdata);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 流程处理
	 * @param waitid 待办事项ID
	 * @param nextTalias 下级任务列表，如果为null，处理当前任务后，会结束流程
	 * @param nextTusers 下级任务处理人列表，如果为null，则使用流程配置中的处理人信息
	 * @param paccount 当前处理人账号
	 * @param pname 当前处理人姓名
	 * @param resultType 处理类型
	 * @param resultMsg 处理意见
	 * @return true|false
	 */
	public boolean process(long waitid, String[] nextTalias, String paccount, String pname, String resultType, String resultMsg, String formdata)
	{
		return process(waitid, nextTalias, null, paccount, pname, resultType, resultMsg, formdata);
	}

	public List<IFlowWaiting> queryWaiting(String account)
	{
		try
		{
			init();
			return dao.queryFlowWaiting(account);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public boolean takeWaiting(long waitid, String user)
	{
		try
		{
			if(user != null && user.trim().length() > 0)
			{
				init();
				dao.updateFlowWaitingUser(waitid, user);
				return true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public IFlowWaiting getWaiting(long waitid)
	{
		try
		{
			init();
			return dao.getFlowWaiting(waitid);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, String> getTaskList(long flowid)
	{
		Map<String, String> map = new HashMap<String, String>();
		try
		{
			init();
			List<IFlowTask> list = dao.queryFlowTask(flowid);
			if(list != null)
			{
				for(IFlowTask m : list)
				{
					map.put(m.getTalias(), m.getTname());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}

	public IFlow getFlowById(long flowid)
	{
		try
		{
			init();
			return dao.getFlowById(flowid);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public IFlowPi getFlowPiByPiid(String piid)
	{
		try
		{
			init();
			return dao.getFlowPiByPiid(piid);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public List<IFlowPi> queryFlowPi(String ywlsh)
	{
		try
		{
			init();
			return dao.queryFlowPi(ywlsh);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public List<IFlowPi> queryFlowPiBySblsh(String sblsh)
	{
		try
		{
			init();
			return dao.queryFlowPiBySblsh(sblsh);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public IFlowPi getFlowPi(String ywlsh)
	{
		try
		{
			init();
			return dao.getFlowPi(ywlsh);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public IFlowPi getFlowPiBySblsh(String sblsh)
	{
		try
		{
			init();
			return dao.getFlowPiBySblsh(sblsh);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public List<IFlowPiData> queryFlowPiData(String piid)
	{
		try
		{
			init();
			return dao.queryFlowPiData(Long.parseLong(piid));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public List<IFlowWaiting> queryFlowWaitingByPiid(String piid)
	{
		try
		{
			init();
			return dao.queryFlowWaitingByPiid(Long.parseLong(piid));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public void deleteFlowPi(String id)
	{
		try
		{
			init();
			dao.deleteFlowPi(Long.parseLong(id));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
