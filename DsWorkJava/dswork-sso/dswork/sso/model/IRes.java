/**
 * @描述：系统资源
 */
package dswork.sso.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class IRes implements Serializable
{
	private static final long serialVersionUID = 1L;
	// 资源对应的URL
	private String url = "";
	// 参数(格式:name1=value2,name2=value2...)
	private String param = "";
	// 对param进行key=value切分后的集合
	private Map<String, String> paramMap = new HashMap<String, String>();

	public void setUrl(String url)
	{
		this.url = String.valueOf(url).trim();
	}

	public String getUrl()
	{
		return this.url;
	}

	public String getParam()
	{
		return this.param;
	}

	public String getParamByKey(String key)
	{
		return paramMap != null ? paramMap.get(key) : null;
	}

	public String[] getParamKeys()
	{
		return paramMap.keySet().toArray(new String[paramMap.size()]);
	}

	/**
	 * @param param k=v,k2=v2
	 */
	public void setParam(String param)
	{
		this.param = String.valueOf(param).trim();
		paramMap.clear();
		if(this.param.length() > 0)
		{
			String[] params = this.param.split(",", 0);
			for(String str : params)
			{
				if(str.indexOf("=") > 0)
				{
					String[] keyValue = str.split("=");
					paramMap.put(keyValue[0].trim(), keyValue[1].trim());
				}
			}
		}
	}

	public Map<String, String> getParamMap()
	{
		return paramMap;
	}
}
