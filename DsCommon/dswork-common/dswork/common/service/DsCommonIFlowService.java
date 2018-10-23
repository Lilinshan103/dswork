package dswork.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.common.dao.DsCommonDaoIFlow;
import dswork.common.model.IFlow;
import dswork.common.model.IFlowPi;
import dswork.common.model.IFlowPiData;
import dswork.common.model.IFlowTask;
import dswork.common.model.IFlowWaiting;

@Service
public class DsCommonIFlowService
{
	@Autowired
	private DsCommonDaoIFlow dao;

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
	public String saveStart(String alias, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay)
	{
		try
		{
			return dao.saveStart(alias, null, ywlsh, sblsh, caccount, cname, piDay, isWorkDay);
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
	public String saveStart(String alias, String users, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay)
	{
		try
		{
			return dao.saveStart(alias, users, ywlsh, sblsh, caccount, cname, piDay, isWorkDay);
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
	public IFlowWaiting saveFlowStart(String alias, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay)
	{
		return dao.saveFlowStart(alias, null, ywlsh, sblsh, caccount, cname, piDay, isWorkDay);
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
	public IFlowWaiting saveFlowStart(String alias, String users, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay)
	{
		return dao.saveFlowStart(alias, users, ywlsh, sblsh, caccount, cname, piDay, isWorkDay);
	}
	
	public void saveStop(String piid)
	{
		dao.saveStop(Long.parseLong(piid));
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
	public boolean saveProcess(long waitid, String[] nextTalias, String paccount, String pname, String resultType, String resultMsg, String datatable)
	{
		return dao.saveProcess(waitid, nextTalias, null, paccount, pname, resultType, resultMsg, datatable);
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
	public boolean saveProcess(long waitid, String[] nextTalias, String[] nextTusers, String paccount, String pname, String resultType, String resultMsg, String datatable)
	{
		return dao.saveProcess(waitid, nextTalias, nextTusers, paccount, pname, resultType, resultMsg, datatable);
	}

	public List<IFlowWaiting> queryFlowWaiting(String account)
	{
		return dao.queryFlowWaiting(account);
	}

	public boolean updateFlowWaitingUser(long waitid, String user)
	{
		if(user != null && user.trim().length() > 0)
		{
			dao.updateFlowWaitingUser(waitid, user);
			return true;
		}
		return false;
	}

	public IFlowWaiting getFlowWaiting(long waitid)
	{
		return dao.getFlowWaiting(waitid);
	}

	public Map<String, String> queryFlowTask(long flowid)
	{
		Map<String, String> map = new HashMap<String, String>();
		List<IFlowTask> list = dao.queryFlowTask(flowid);
		if(list != null)
		{
			for(IFlowTask m : list)
			{
				map.put(m.getTalias(), m.getTname());
			}
		}
		return map;
	}

	public IFlow getFlowById(long flowid)
	{
		return dao.getFlowById(flowid);
	}

	public IFlowPi getFlowPiByPiid(String piid)
	{
		return dao.getFlowPiByPiid(piid);
	}

	public List<IFlowPi> queryFlowPi(String ywlsh)
	{
		return dao.queryFlowPi(ywlsh);
	}

	public List<IFlowPi> queryFlowPiBySblsh(String sblsh)
	{
		return dao.queryFlowPiBySblsh(sblsh);
	}

	public IFlowPi getFlowPi(String ywlsh)
	{
		return dao.getFlowPi(ywlsh);
	}

	public IFlowPi getFlowPiBySblsh(String sblsh)
	{
		return dao.getFlowPiBySblsh(sblsh);
	}

	public List<IFlowPiData> queryFlowPiData(String piid)
	{
		return dao.queryFlowPiData(Long.parseLong(piid));
	}
	public List<IFlowWaiting> queryFlowWaitingByPiid(String piid)
	{
		return dao.queryFlowWaitingByPiid(Long.parseLong(piid));
	}
	public void deleteFlowPi(String id)
	{
		dao.deleteFlowPi(Long.parseLong(id));
	}
}
