package dswork.common;

import java.util.List;

import dswork.common.dao.DsCommonDaoCommonIDict;
import dswork.common.model.IDict;
import dswork.spring.BeanFactory;

public class DsCommonFactoryDict
{
	private static DsCommonDaoCommonIDict dao;
	private DsCommonFactoryDict()
	{
		if(dao == null)
		{
			dao = (DsCommonDaoCommonIDict) BeanFactory.getBean("dsCommonDaoCommonIDict");
		}
	}
	
	private static final DsCommonFactoryDict instace = new DsCommonFactoryDict();
	
	public static DsCommonFactoryDict getInstance()
	{
		return instace;
	}

	public String getSelect(String name, String selectName)
	{
		return getSelect(name, selectName, "");
	}
	public String getSelect(String name, String selectName, String parentAlias)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<select id=\"").append(selectName).append("\" name=\"").append(selectName).append("\">");
		sb.append(getOption(name, parentAlias));
		sb.append("</select>");
		return sb.toString();
	}
	public String getOption(String name, String parentAlias)
	{
		List<IDict> list = dao.queryListDict(name, parentAlias);
		StringBuilder sb = new StringBuilder();
		for(IDict dict : list)
		{
			sb.append("<option value=\"").append(dict.getAlias()).append("\"").append(">").append(dict.getLabel()).append("</option>");
		}
		return sb.toString();
	}
	public String getCheckbox(String name, String checkboxName)
	{
		List<IDict> list = dao.queryListDict(name, "");
		StringBuilder sb = new StringBuilder();
		IDict dict;
		for(int i = 0; i < list.size(); i++)
		{
			dict = list.get(i);
			sb.append("<label><input name=\"").append(checkboxName).append("\" type=\"checkbox\" value=\"").append(dict.getAlias()).append("\"/>").append(dict.getLabel()).append("</label>");
		}
		sb.append("<input name=\"").append(checkboxName).append("\" type=\"checkbox\" datatype=\"Group\" msg=\"必选\" style=\"display:none;\" />");
		return sb.toString();
	}
	public String getRadio(String name, String radioName)
	{
		List<IDict> list = dao.queryListDict(name, "");
		StringBuilder sb = new StringBuilder();
		IDict dict;
		for(int i = 0; i < list.size(); i++)
		{
			dict = list.get(i);
			sb.append("<label><input name=\"").append(radioName).append("\" type=\"radio\" value=\"").append(dict.getAlias()).append("\"/>").append(dict.getLabel()).append("</label>");
		}
		sb.append("<input name=\"").append(radioName).append("\" type=\"radio\" datatype=\"Group\" msg=\"必选\" style=\"display:none;\" />");
		return sb.toString();
	}
	
	/**
	 * 获取指定节点的列表数据
	 * @param name 字典分类名
	 * @param parentAlias 上级标识，当alias为null时获取全部节点数据，当alias为""时获取根节点数据
	 * @return String
	 */
	public String getDictJson(String name, String parentAlias)
	{
		return dao.queryListDict(name, parentAlias).toString();
	}
	
	/**
	 * 获取指定节点的列表数据
	 * @param name 字典分类名
	 * @param parentAlias 上级标识，当alias为null时获取全部节点数据，当alias为""时获取根节点数据
	 * @return List&lt;IDict&gt;
	 */
	public List<IDict> getDict(String name, String parentAlias)
	{
		return dao.queryListDict(name, parentAlias);
	}

	public String getLabel(String name, String alias)
	{
		IDict po = dao.getDict(name, alias);
		if(po == null)
		{
			return "";
		}
		return po.getLabel();
	}
}
