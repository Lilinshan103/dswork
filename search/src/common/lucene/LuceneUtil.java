package common.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.FieldType.NumericType;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;



import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

import dswork.analyzer.BaseAnalyzer;
import dswork.core.util.FileUtil;
import dswork.html.HtmlUtil;

public class LuceneUtil
{
	private static final String SEARCH_PATH = dswork.core.util.EnvironmentUtil.getToString("dswork.lucene.search", "");
	private static final String INDEX_PATH = dswork.core.util.EnvironmentUtil.getToString("dswork.lucene.index", "");
	private static final String Domain = dswork.core.util.EnvironmentUtil.getToString("dswork.lucene.domain", "");
	private static final int Size = new Long(dswork.core.util.EnvironmentUtil.getToLong("dswork.lucene.size", 500)).intValue();
	public static final long Refreshtime = dswork.core.util.EnvironmentUtil.getToLong("dswork.lucene.refreshtime", 86400000);

	private static final String CmsSeq = dswork.core.util.EnvironmentUtil.getToString("dswork.lucene.cms.seq", ".searchseq");
	private static final String CmsType = dswork.core.util.EnvironmentUtil.getToString("dswork.lucene.cms.type", ".searchtype");
	private static final String CmsTitle = dswork.core.util.EnvironmentUtil.getToString("dswork.lucene.cms.title", ".searchtitle");
	private static final String CmsContent = dswork.core.util.EnvironmentUtil.getToString("dswork.lucene.cms.content", ".searchcontent");

	private static String SearchType = "type";
	private static String SearchSeq = "seq";
	private static String SearchKey = "search";
	private static String SearchName = "name";
	private static String SearchMsg = "msg";
	private static String SearchUri = "uri";
	
	private static Formatter formatter = new SimpleHTMLFormatter("<span class='keyvalue'>", "</span>");// 关键字增加前后缀
	private static Analyzer analyzerIndex = new BaseAnalyzer(false);
	private static Analyzer analyzerKeyword = new org.apache.lucene.analysis.core.UnicodeWhitespaceAnalyzer();
	private static Analyzer analyzerHighlighter = new BaseAnalyzer(true);
	

	private static QueryParser queryParserName = new QueryParser(SearchName, analyzerKeyword);
	private static QueryParser queryParser = new QueryParser(SearchKey, analyzerKeyword);
	private static QueryParser queryParserOther = new QueryParser(SearchKey, analyzerHighlighter);
	static
	{
		queryParserName.setDefaultOperator(QueryParser.OR_OPERATOR);
		queryParser.setDefaultOperator(QueryParser.OR_OPERATOR);
		queryParserOther.setDefaultOperator(QueryParser.AND_OPERATOR);
	}
	
	private static Directory directory = null;
	private static Document getLuceneDocument(String type, long seq, String uri, String name, String msg)
	{
		if(type.length() > 0)
		{
			tempMapList.put(type, type);
		}
		Document doc = new Document();
		FieldType fieldType = new FieldType();
		fieldType.setIndexOptions(IndexOptions.NONE);
		fieldType.setStored(true);
		fieldType.setTokenized(false);
		fieldType.setNumericType(NumericType.LONG);
		doc.add(new LongField(SearchSeq, seq, fieldType));
		doc.add(new NumericDocValuesField(SearchSeq, seq));
		
		fieldType.setIndexOptions(IndexOptions.NONE);
		fieldType.setStored(true);
		fieldType.setTokenized(false);
		doc.add(new Field(SearchUri, uri, fieldType));
		fieldType = new FieldType();
		fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
		fieldType.setStored(true);
		fieldType.setTokenized(true);
		doc.add(new Field(SearchName, name, fieldType));
		fieldType = new FieldType();
		fieldType.setIndexOptions(IndexOptions.NONE);
		fieldType.setStored(true);
		fieldType.setTokenized(false);
		doc.add(new Field(SearchMsg, msg, fieldType));
		
		// 用于分类检查和存储
		fieldType = new FieldType();
		fieldType.setIndexOptions(IndexOptions.DOCS);
		fieldType.setStored(true);
		fieldType.setTokenized(false);// 不分词
		doc.add(new Field(SearchType, type, fieldType));
		
		// 空间换时间，用于搜索的field
		fieldType = new FieldType();
		fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
		fieldType.setStored(true);
		fieldType.setTokenized(true);;// 分词
		doc.add(new Field(SearchKey, name+msg, fieldType));
		return doc;
	}
	private static void initFile(IndexWriter iwriter, String rootpath, File files) throws IOException
	{
		if(files.isFile())
		{
			if(files.getName().endsWith(".html") && !files.getName().startsWith("index"))//
			{
				String url = files.getPath().replaceAll("\\\\", "/").replaceFirst(rootpath, "");
				dswork.html.nodes.Document document = HtmlUtil.parse(FileUtil.readFile(files.getPath(), "UTF-8").replaceAll("&nbsp;", ""));
				if(document == null)
				{
					System.out.println("---------------------\nerror:");
					return;
				}
				// ----------------------------------------
				// 此处读取的信息应该根据不同的项目，截取不同的文档信息
				// ----------------------------------------
				long id = 1L;
				try
				{
					id = Long.parseLong(document.selectOwnText(CmsSeq).trim());// 越大越在前
				}
				catch(NumberFormatException e)
				{
				}
				String type = document.selectOwnText(CmsType).trim();
				String title = document.selectOwnText(CmsTitle).trim();
				String content = document.selectText(CmsContent).trim();
				if(title.length() > 0 && content.length() > 0)
				{
					count++;
					iwriter.addDocument(LuceneUtil.getLuceneDocument(type, id, url, title, content));
				}
			}
		}
		else if(files.isDirectory())
		{
			for(File file : files.listFiles())
			{
				initFile(iwriter, rootpath, file);
			}
		}
	}
	private static int count = 0;
	private static void initIndex()
	{
		IndexWriter iwriter = null;
		try
		{
			if(SEARCH_PATH.length() == 0)
			{
				directory = new org.apache.lucene.store.RAMDirectory();
			}
			else
			{
				directory = org.apache.lucene.store.FSDirectory.open(java.nio.file.FileSystems.getDefault().getPath(INDEX_PATH));
			}
			IndexWriterConfig iwConfig = new IndexWriterConfig(analyzerIndex);// new BaseAnalyzer(false)
			iwConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
			iwriter = new IndexWriter(directory, iwConfig);
			
			File files = new File(SEARCH_PATH);
			String path = files.getPath().replaceAll("\\\\", "/");
			if(files.isDirectory())
			{
				iwriter.deleteAll();
				initFile(iwriter, path, files);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(iwriter != null)
				{
					iwriter.close();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private static int MaxLength = 300;
	
	public static MyPage search(String[] types, String keyword, int page, int pagesize)
	{
		// 太多了，处理一下
		if(pagesize > 100)
		{
			pagesize = 10;
		}
		List<String> tlist = new ArrayList<String>();
		if(types.length > 0)
		{
			for(String x : types)
			{
				if(x.trim().length() > 0)
				{
					tlist.add(x.trim());
				}
			}
		}
		System.out.println("-------------------------");
		System.out.print("搜索《" + keyword);
		IndexReader ireader = null;
		MyPage pageModel = null;
		int searchSize = 0;
		try
		{
			if(directory == null)
			{
				directory = org.apache.lucene.store.FSDirectory.open(java.nio.file.FileSystems.getDefault().getPath(INDEX_PATH));
			}
			ireader = DirectoryReader.open(directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);
			BooleanQuery.Builder builder;
			Query queryBuilder = null;
			Query query = null;
			org.apache.lucene.search.TopFieldDocs topDocs;
			
			if(tlist.size() > 0)
			{
				System.out.print("，分类：");
				if(tlist.size() > 0)
				{
					if(tlist.size() == 1)
					{
						queryBuilder = new TermQuery(new Term(SearchType, tlist.get(0)));
					}
					else
					{
						BooleanQuery.Builder builderTemp = new BooleanQuery.Builder();//构造booleanQuery
						for(String x : tlist)
						{
							builderTemp.add(new TermQuery(new Term(SearchType, x)), BooleanClause.Occur.SHOULD);
						}
						queryBuilder = builderTemp.build();
					}
				}
			}
			
			builder = new BooleanQuery.Builder();//构造booleanQuery
			if(queryBuilder != null)
			{
				builder.add(queryBuilder, BooleanClause.Occur.MUST);
			}
			query = queryParserName.parse(keyword);
			builder.add(query, BooleanClause.Occur.MUST);
			topDocs = isearcher.search(builder.build(), Size, new Sort());
			searchSize = topDocs.totalHits;
			
			if(searchSize == 0)
			{
				SortField[] sortField = new SortField[1];
				sortField[0] = new SortField(SearchSeq, SortField.Type.LONG, true);// 按时间排序
				
				builder = new BooleanQuery.Builder();//构造booleanQuery
				if(queryBuilder != null)
				{
					builder.add(queryBuilder, BooleanClause.Occur.MUST);
				}
				query = queryParser.parse(keyword);
				builder.add(query, BooleanClause.Occur.MUST);
				topDocs = isearcher.search(builder.build(), Size, new Sort());
				searchSize = topDocs.totalHits;
				
				if(searchSize == 0)
				{
					builder = new BooleanQuery.Builder();//构造booleanQuery
					if(queryBuilder != null)
					{
						builder.add(queryBuilder, BooleanClause.Occur.MUST);
					}
					query = queryParserOther.parse(keyword);
					builder.add(query, BooleanClause.Occur.MUST);
					topDocs = isearcher.search(builder.build(), Size, new Sort(sortField));
					searchSize = topDocs.totalHits;
				}
			}
			// org.apache.lucene.search.TopDocs topDocs = isearcher.search(query, Size);
			System.out.print("》，命中");
			System.out.print(searchSize);
			System.out.print("条(");
			System.out.print(query);
			System.out.print(")");
			System.out.println();
			
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;

			pageModel = new MyPage(page, pagesize, scoreDocs.length, searchSize);

			Scorer scorer = new QueryScorer(query);// 封装关键字
			Highlighter highlighter = new Highlighter(formatter, scorer);
			// 文档切片
			Fragmenter fragmenter = new SimpleFragmenter(MaxLength);
			highlighter.setTextFragmenter(fragmenter);
			
			List<MyDocument> ls = new ArrayList<MyDocument>();
			int end = pageModel.getFirstResultIndex() + pageModel.getPageSize();
			for(int i = pageModel.getFirstResultIndex(); i < scoreDocs.length && i < end; i++)
			{
				ScoreDoc scoreDoc = scoreDocs[i];
				Document targetDoc = isearcher.doc(scoreDoc.doc);
				String name = targetDoc.get(SearchName);
				String content = targetDoc.get(SearchMsg);
				String uri = targetDoc.get(SearchUri);
				String title = highlighter.getBestFragment(analyzerHighlighter, SearchName, name);
				String summary = highlighter.getBestFragment(analyzerHighlighter, SearchMsg, content);
				if(title == null)
				{
					title = name;
				}
				if(summary == null)
				{
					summary = (content.length() > MaxLength) ? content.substring(0, 300): content;
				}
				MyDocument doc = new MyDocument();
				doc.setTitle(title).setSummary(summary).setUrl(Domain + uri);
				ls.add(doc);
			}
			pageModel.setResult(ls);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(ireader != null)
			{
				try
				{
					ireader.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		if(pageModel == null || pageModel.getResult() == null)
		{
			pageModel = new MyPage(1, pagesize, 0, searchSize);
		}
		return pageModel;
	}

	private static List<String> typeList = new ArrayList<String>();
	private static Map<String, String> tempMapList = new LinkedHashMap<String, String>();
	
	public static synchronized void reload()
	{
		System.out.println("------------------");
		try
		{
			System.out.println("索引初始化开始");
			System.out.println("------------------");
			count = 0;
			tempMapList.clear();
			initIndex();
			typeList.clear();
			typeList.addAll(tempMapList.values());
			System.out.println("索引初始化文件个数为：" + count);
			System.out.println("索引初始化结束");
		}
		catch(Exception e)
		{
			System.out.println("***索引初始化失败***");
			e.printStackTrace();
		}
		System.out.println("------------------");
	}
	
	public static String[] getTypeArray()
	{
		return typeList.toArray(new String[0]);
	}
}
