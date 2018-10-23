package dswork.analyzer.core;

/**
 * 源代码由林良益(linliangyi2005@gmail.com)提供
 * 版权声明 2012，乌龙茶工作室
 * provided by Linliangyi and copyright 2012 by Oolong studio
 * 子分词器接口
 */
public interface Segmenter
{
	/**
	 * 从分析器读取下一个可能分解的词元对象
	 * @param context 分词算法上下文
	 */
	void analyze(AnalyzeContext context);

	/**
	 * 重置子分析器状态
	 */
	void reset();
}
