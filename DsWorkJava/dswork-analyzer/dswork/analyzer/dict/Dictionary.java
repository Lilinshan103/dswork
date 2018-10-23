package dswork.analyzer.dict;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

/**
 * 词典管理类,单子模式
 */
public class Dictionary
{
	private static final String FILE_MAIN = "config/analyzer/main.dic";
	private static final String FILE_QUANTIFIER = "config/analyzer/quantifier.dic";
	private static final String FILE_EXTEND = "config/analyzer/extend.dic";
	private static final String FILE_STOPWORD = "config/analyzer/stopword.dic";
	
	/*
	 * 词典单子实例
	 */
	private static Dictionary singleton = new Dictionary();

	/*
	 * 主词典对象
	 */
	private DictSegment _MainDict = new DictSegment((char) 0);
	/*
	 * 停止词词典
	 */
	private DictSegment _StopWordDict = new DictSegment((char) 0);
	/*
	 * 量词词典
	 */
	private DictSegment _QuantifierDict = new DictSegment((char) 0);

	private void loadDict(DictSegment dict, String filePath)
	{
		// 读取主词典文件
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(filePath);
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"), 512);
			String theWord = null;
			do
			{
				theWord = br.readLine();
				if(theWord != null)
				{
					theWord = theWord.trim();
					if(theWord.length() > 0)
					{
						dict.fillSegment(theWord.toLowerCase().toCharArray());
					}
				}
			}
			while(theWord != null);
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			try
			{
				if(is != null)
				{
					is.close();
					is = null;
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private Dictionary()
	{
		System.out.println("开始加载词库");
		this.loadDict(_MainDict, Dictionary.FILE_MAIN);
		this.loadDict(_MainDict, Dictionary.FILE_EXTEND);
		this.loadDict(_StopWordDict, Dictionary.FILE_STOPWORD);
		this.loadDict(_QuantifierDict, Dictionary.FILE_QUANTIFIER);
		System.out.println("词库加载结束");
	}
	
	/**
	 * 获取词典单子实例
	 * @return Dictionary 单例对象
	 */
	public static Dictionary getSingleton()
	{
		return singleton;
	}

	/**
	 * 批量加载新词条
	 * @param words Collection<String>词条列表
	 */
	public void addWords(Collection<String> words)
	{
		if(words != null)
		{
			for(String word : words)
			{
				if(word != null)
				{
					// 批量加载词条到主内存词典中
					singleton._MainDict.fillSegment(word.trim().toLowerCase().toCharArray());
				}
			}
		}
	}

	/**
	 * 批量移除（屏蔽）词条
	 * @param words
	 */
	public void disableWords(Collection<String> words)
	{
		if(words != null)
		{
			for(String word : words)
			{
				if(word != null)
				{
					// 批量屏蔽词条
					singleton._MainDict.disableSegment(word.trim().toLowerCase().toCharArray());
				}
			}
		}
	}

	/**
	 * 检索匹配主词典
	 * @param charArray
	 * @return Hit 匹配结果描述
	 */
	public Hit matchInMainDict(char[] charArray)
	{
		return singleton._MainDict.match(charArray);
	}

	/**
	 * 检索匹配主词典
	 * @param charArray
	 * @param begin
	 * @param length
	 * @return Hit 匹配结果描述
	 */
	public Hit matchInMainDict(char[] charArray, int begin, int length)
	{
		return singleton._MainDict.match(charArray, begin, length);
	}

	/**
	 * 检索匹配量词词典
	 * @param charArray
	 * @param begin
	 * @param length
	 * @return Hit 匹配结果描述
	 */
	public Hit matchInQuantifierDict(char[] charArray, int begin, int length)
	{
		return singleton._QuantifierDict.match(charArray, begin, length);
	}

	/**
	 * 从已匹配的Hit中直接取出DictSegment，继续向下匹配
	 * @param charArray
	 * @param currentIndex
	 * @param matchedHit
	 * @return Hit
	 */
	public Hit matchWithHit(char[] charArray, int currentIndex, Hit matchedHit)
	{
		DictSegment ds = matchedHit.getMatchedDictSegment();
		return ds.match(charArray, currentIndex, 1, matchedHit);
	}

	/**
	 * 判断是否是停止词
	 * @param charArray
	 * @param begin
	 * @param length
	 * @return boolean
	 */
	public boolean isStopWord(char[] charArray, int begin, int length)
	{
		return singleton._StopWordDict.match(charArray, begin, length).isMatch();
	}
}
