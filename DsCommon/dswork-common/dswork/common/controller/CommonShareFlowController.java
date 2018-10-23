/**
 * 功能:公共Controller
 */
package dswork.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.common.model.IFlow;
import dswork.common.model.IFlowPi;
import dswork.common.model.IFlowPiData;
import dswork.common.model.IFlowWaiting;
import dswork.common.service.DsCommonIFlowService;
import dswork.web.MyRequest;

@Controller
@RequestMapping("/common/share")
public class CommonShareFlowController
{
	@Autowired
	private DsCommonIFlowService service;
	
	@RequestMapping("/showFlow")
	public void showFlow(HttpServletRequest request, HttpServletResponse response)
	{
		response.setCharacterEncoding("UTF-8");
		MyRequest req = new MyRequest(request);
		long piid = req.getLong("piid");
		try
		{
			IFlowPi pi = service.getFlowPiByPiid(piid+"");
			if(pi != null)
			{
				IFlow f = service.getFlowById(pi.getFlowid());
				if(f.getFlowxml() != null && f.getFlowxml().length() > 50)
				{
					dswork.flow.MyFlow flow = new dswork.flow.MyFlow(f.getFlowxml());
					//flow.prettyPrint(false);
					response.getWriter().print("<html><head><meta charset=\"UTF-8\"/></head><body>"+flow.toSvg()+"</body></html>");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/showFlowRunning")
	public void showFlowRunning(HttpServletRequest request, HttpServletResponse response)
	{
		response.setCharacterEncoding("UTF-8");
		MyRequest req = new MyRequest(request);
		long piid = req.getLong("piid");
		try
		{
			IFlowPi pi = service.getFlowPiByPiid(piid+"");
			if(pi != null)
			{
				IFlow f = service.getFlowById(pi.getFlowid());
				if(f.getFlowxml() != null && f.getFlowxml().length() > 50)
				{
					dswork.flow.MyFlow flow = new dswork.flow.MyFlow(f.getFlowxml());
					java.util.List<dswork.flow.dom.MyLine> lineList = flow.getLines();
					java.util.List<IFlowPiData> doneList = service.queryFlowPiData(piid+"");
					for(IFlowPiData m : doneList)
					{
						for(dswork.flow.dom.MyLine n : lineList)
						{
							String[] prevArray = m.getTprev().split(",", -1);
							for(String prev : prevArray)
							{
								if(n.getFrom().getAlias().equals(prev) && n.getTo().getAlias().equals(m.getTalias()))
								{
									n.setColor("0055AA");
									if(!n.getFrom().getAlias().equals("start"))
									{
										n.getFrom().setColor("0055AA");
									}
									if(!n.getTo().getAlias().equals("end"))
									{
										n.getTo().setColor("0055AA");
									}
								}
							}
						}
					}
					if(pi.getStatus() != 0)
					{
						java.util.List<IFlowWaiting> doingList = service.queryFlowWaitingByPiid(piid+"");
						for(IFlowWaiting m : doingList)
						{
							for(dswork.flow.dom.MyLine n : lineList)
							{
								String[] prevArray = m.getTprev().split(",", -1);
								for(String prev : prevArray)
								{
									if(n.getFrom().getAlias().equals(prev) && n.getTo().getAlias().equals(m.getTalias()))
									{
										n.setColor("FF0000");// 当前的，变亮
										n.getTo().setColor("FF0000");// 当前的，变亮
									}
								}
							}
						}
					}
					response.getWriter().print("<html><head></head><meta charset=\"UTF-8\"/><body>"+flow.toSvg()+"</body></html>");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}