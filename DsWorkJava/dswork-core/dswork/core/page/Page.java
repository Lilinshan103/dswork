package dswork.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页信息, 第一页从1开始
 * @author skey
 */
public class Page<T> implements Serializable
{
	private static final long serialVersionUID = 1L;
	private List<T> result = new ArrayList<T>();
	private int pageSize;
	private int currentPage;
	private int lastPage;
	private int totalCount = 0;
	private String pageName = "page";
	private String pageSizeName = "pageSize";

	/**
	 * 构造函数
	 * @param currentPage 当前页码
	 * @param pageSize 一页显示的条数
	 * @param totalCount 数据总条数
	 */
	public Page(int currentPage, int pageSize, int totalCount)
	{
		this(currentPage, pageSize, totalCount, new ArrayList<T>(0));
	}

	/**
	 * 构造函数
	 * @param currentPage 当前页码
	 * @param pageSize 一页显示的条数
	 * @param totalCount 数据总条数
	 * @param result 结果集List&lt;T&gt;，其长度不影响计数
	 */
	public Page(int currentPage, int pageSize, int totalCount, List<T> result)
	{
		if(pageSize <= 0)
		{
			throw new IllegalArgumentException("[pageSize] must great than zero");
		}
		this.lastPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
		if(this.lastPage < 1)
		{
			this.lastPage = 1;
		}
		this.pageSize = pageSize;
		if(currentPage <= 1)
		{
			currentPage = 1;
		}
		else if(Integer.MAX_VALUE == currentPage || currentPage > this.lastPage)
		{
			currentPage = this.lastPage;
		}
//		// 下面这种的话，如果pageSize=0的话，就相当于不分页
//		if(pageSize == 0)
//		{
//			pageNumber = 1;
//			pageSize = (totalCount <= 0) ? 1 : totalCount;
//		}
		this.currentPage = currentPage;
		this.totalCount = totalCount;
		setResult(result);
	}

	/**
	 * 取得当前页页码
	 * @return int
	 */
	public int getCurrentPage()
	{
		return currentPage;
	}

	/**
	 * 得到结果集第一条数据的行编码
	 * @return int
	 */
	public int getFirstResultIndex()
	{
		if (pageSize <= 0)
		{
			throw new IllegalArgumentException("[pageSize] must great than zero");
		}
		return (currentPage - 1) * pageSize;
	}
	
	/**
	 * 取得数据总页数，也就是最后一页页码
	 * @return int
	 */
	public int getLastPage()
	{
		return lastPage;
	}

	/**
	 * 取得下一页页码
	 * @return int
	 */
	public int getNextPage()
	{
		return currentPage + 1;
	}

	/**
	 * 取得当前页码的参数名
	 * @return String，默认值是page
	 */
	public String getPageName()
	{
		return pageName;
	}

	/**
	 * 设置当前页码的参数名，默认值是page
	 * @param pageName String
	 */
	public void setPageName(String pageName)
	{
		if (pageName == null || pageName.trim().length() == 0)
		{
			throw new IllegalArgumentException("[pageName] must be not null");
		}
		this.pageName = pageName.trim().replace("\"", "&quot;").replace("\r", "").replace("\n", "");
	}

	/**
	 * 取得一页显示的条数
	 * @return int
	 */
	public int getPageSize()
	{
		return pageSize;
	}

	/**
	 * 取得一页显示的条数的参数名
	 * @return String，默认值是pageSize
	 */
	public String getPageSizeName()
	{
		return pageSizeName;
	}

	/**
	 * 设置当前页码的参数名，默认值是pageSize
	 * @param pageSizeName String
	 */
	public void setPageSizeName(String pageSizeName)
	{
		if (pageSizeName == null || pageSizeName.trim().length() == 0)
		{
			throw new IllegalArgumentException("[pageSizeName] must be not null");
		}
		this.pageSizeName = pageSizeName.trim().replace("\"", "&quot;").replace("\r", "").replace("\n", "");
	}

	/**
	 * 取得上一页页码
	 * @return int
	 */
	public int getPreviousPage()
	{
		return currentPage - 1;
	}

	/**
	 * 取得结果集
	 * @return 结果集List&lt;T&gt;
	 */
	public List<T> getResult()
	{
		return result;
	}

	/**
	 * 设置结果集，其值与长度不影响计数
	 * @param result 结果集List&lt;T&gt;
	 */
	public void setResult(List<T> result)
	{
		if(result == null)
		{
			throw new IllegalArgumentException("'result' must be not null");
		}
		this.result = result;
	}

	/**
	 * 取得数据总条数，0表示没有数据
	 * @return int
	 */
	public int getTotalCount()
	{
		return totalCount;
	}

	/**
	 * 取得数据总页数
	 * @return int
	 */
	public int getTotalPage()
	{
		return lastPage;
	}

	/**
	 * 是否是首页（第一页），第一页页码为1
	 * @return boolean
	 */
	public boolean isFirstPage()
	{
		return currentPage == 1;
	}

	/**
	 * 是否有下一页
	 * @return boolean
	 */
	public boolean isHasNextPage()
	{
		return lastPage > currentPage;
	}

	/**
	 * 是否有上一页
	 * @return boolean
	 */
	public boolean isHasPreviousPage()
	{
		return currentPage > 1;
	}

	/**
	 * 是否是最后一页
	 * @return boolean
	 */
	public boolean isLastPage()
	{
		return currentPage >= lastPage;
	}

//	/**
//	 * 取得当前页的首条数据的行编码
//	 * @return int
//	 */
//	public int getThisPageFirstElementNumber()
//	{
//		return (currentPage - 1) * pageSize + 1;
//	}
//
//	/**
//	 * 取得当前页的末条数据的行编码
//	 * @return int
//	 */
//	public int getThisPageLastElementNumber()
//	{
//		int fullPage = getThisPageFirstElementNumber() + pageSize - 1;
//		return totalCount < fullPage ? totalCount : fullPage;
//	}

//	/**
//	 * 得到用于多页跳转的页码
//	 * @return
//	 */
//	public List<Integer> getLinkPageNumbers()
//	{
//		return generateLinkPageNumbers(getCurrentPage(), getLastPage(), 10);
//	}

//	private static List<Integer> generateLinkPageNumbers(int currentPageNumber, int lastPageNumber, int count)
//	{
//		int avg = count / 2;
//		int startPageNumber = currentPageNumber - avg;
//		if (startPageNumber <= 0)
//		{
//			startPageNumber = 1;
//		}
//		int endPageNumber = startPageNumber + count - 1;
//		if (endPageNumber > lastPageNumber)
//		{
//			endPageNumber = lastPageNumber;
//		}
//		if (endPageNumber - startPageNumber < count)
//		{
//			startPageNumber = endPageNumber - count;
//			if (startPageNumber <= 0)
//			{
//				startPageNumber = 1;
//			}
//		}
//		List<Integer> result = new ArrayList<Integer>();
//		for (int i = startPageNumber; i <= endPageNumber; i++)
//		{
//			result.add(new Integer(i));
//		}
//		return result;
//	}
}
