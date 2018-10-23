package dswork.common.util;

import java.util.List;
import java.util.Map;

import dswork.common.model.IFlow;
import dswork.common.model.IFlowPi;
import dswork.common.model.IFlowPiData;
import dswork.common.model.IFlowWaiting;
import dswork.common.service.DsCommonIFlowService;

public class DsCommonIFlowServiceUtil
{
	private  DsCommonIFlowService service;
	
	public DsCommonIFlowServiceUtil(DsCommonIFlowService service) 
	{
		this.service = service;
	}
	
	/**
	 * 流程启动
	 * @param alias 启动流程的标识
	 * @param ywlsh 业务流水号
	 * @param caccount 提交人账号
	 * @param cname 提交人姓名
	 * @param piDay 时限天数
	 * @param isWorkDay 时限天数类型(false日历日,true工作日)
	 * 
	 * @param taskInterface 接口类（暂时无用）
	 * @return 流程实例ID
	 */
	public String start(String alias, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay)
	{
		try
		{
			return service.saveStart(alias, null, ywlsh, sblsh, caccount, cname, piDay, isWorkDay);
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
	 * @param users 启动节点任务处理人，如果为null，则使用流程配置中的处理人信息
	 * @param ywlsh 业务流水号
	 * @param caccount 提交人账号
	 * @param cname 提交人姓名
	 * @param piDay 时限天数
	 * @param isWorkDay 时限天数类型(false日历日,true工作日)
	 * 
	 * @param taskInterface 接口类（暂时无用）
	 * @return 流程实例ID
	 */
	public String start(String alias, String users, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay)
	{
		try
		{
			return service.saveStart(alias, users, ywlsh, sblsh, caccount, cname, piDay, isWorkDay);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 流程启动并返回开始节点待办信息
	 * @param alias 启动流程的标识
	 * @param ywlsh 业务流水号
	 * @param caccount 提交人账号
	 * @param cname 提交人姓名
	 * @param piDay 时限天数
	 * @param isWorkDay 时限天数类型(false日历日,true工作日)
	 * @param taskInterface 接口类（暂时无用）
	 * @return 流程实例的start待办信息或null
	 */
	public IFlowWaiting startFlow(String alias, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay)
	{
		return service.saveFlowStart(alias, null, ywlsh, sblsh, caccount, cname, piDay, isWorkDay);
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
	 * @param taskInterface 接口类（暂时无用）
	 * @return 流程实例的start待办信息或null
	 */
	public IFlowWaiting startFlow(String alias, String users, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay)
	{
		return service.saveFlowStart(alias, users, ywlsh, sblsh, caccount, cname, piDay, isWorkDay);
	}
	
	public void stop(String piid)
	{
		service.saveStop(piid);
	}

	/**
	 * 流程处理
	 * @param waitid 待办事项ID
	 * @param nextTalias 下级任务列表，如果为null，处理当前任务后，会结束流程
	 * @param paccount 当前处理人账号
	 * @param pname 当前处理人姓名
	 * @param resultType 处理类型
	 * @param resultMsg 处理意见
	 * @return true|false
	 */
	public boolean process(long waitid, String[] nextTalias, String paccount, String pname, String resultType, String resultMsg, String datatable)
	{
		return service.saveProcess(waitid, nextTalias, null, paccount, pname, resultType, resultMsg, datatable);
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
	public boolean process(long waitid, String[] nextTalias, String[] nextTusers, String paccount, String pname, String resultType, String resultMsg, String datatable)
	{
		return service.saveProcess(waitid, nextTalias, nextTusers, paccount, pname, resultType, resultMsg, datatable);
	}

	public List<IFlowWaiting> queryWaiting(String account)
	{
		return service.queryFlowWaiting(account);
	}

	public boolean takeWaiting(long waitid, String user)
	{
		if(user != null && user.trim().length() > 0)
		{
			service.updateFlowWaitingUser(waitid, user);
			return true;
		}
		return false;
	}

	public IFlowWaiting getWaiting(long waitid)
	{
		return service.getFlowWaiting(waitid);
	}

	public Map<String, String> getTaskList(long flowid)
	{
		return service.queryFlowTask(flowid);
	}

	public IFlow getFlowById(long flowid)
	{
		return service.getFlowById(flowid);
	}

	public IFlowPi getFlowPiByPiid(String piid)
	{
		return service.getFlowPiByPiid(piid);
	}

	public List<IFlowPi> queryFlowPi(String ywlsh)
	{
		return service.queryFlowPi(ywlsh);
	}

	public List<IFlowPi> queryFlowPiBySblsh(String sblsh)
	{
		return service.queryFlowPiBySblsh(sblsh);
	}

	public IFlowPi getFlowPi(String ywlsh)
	{
		return service.getFlowPi(ywlsh);
	}

	public IFlowPi getFlowPiBySblsh(String sblsh)
	{
		return service.getFlowPiBySblsh(sblsh);
	}

	public List<IFlowPiData> queryFlowPiData(String piid)
	{
		return service.queryFlowPiData(piid);
	}
	public List<IFlowWaiting> queryFlowWaitingByPiid(String piid)
	{
		return service.queryFlowWaitingByPiid(piid);
	}
	public void deleteFlowPi(String id)
	{
		service.deleteFlowPi(id);
	}
}
