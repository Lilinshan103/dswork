/**
 * 功能:公共Controller
 */
package dswork.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.web.MyRequest;
import dswork.common.DsFactory;

@Controller
@RequestMapping("/common/share")
public class CommonShareDictController
{
	@RequestMapping("/getJsonDict")
	public void getJsonDict(HttpServletRequest request, HttpServletResponse response)
	{
		response.setCharacterEncoding("UTF-8");
		MyRequest req = new MyRequest(request);
		String name = req.getString("name", "0");
		String parentAlias = req.getString("value", null);// null时全部节点数据，""时根节点数据
		try
		{
			response.getWriter().print(DsFactory.getDict().getDictJson(name, parentAlias));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
