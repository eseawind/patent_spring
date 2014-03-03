package cn.edu.scut.patent.util;

import java.io.IOException;
import java.io.StringReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class CheckHelper {

	/**
	 * 打印分词结果
	 * 
	 * @throws IOException
	 */
	public static void printKeyWords(Analyzer analyzer, String text)
			throws IOException {
		System.out.println("当前使用的分词器：" + analyzer.getClass().getSimpleName());
		// TokenStream tokenStream = analyzer.tokenStream("contents",
		// new StringReader(text));
		// tokenStream.reset(); // 必须要reset，不然会抛出NullPointerException
		// tokenStream.addAttribute(CharTermAttribute.class);
		//
		// // 打印方法一
		// System.out.println("打印方法一");
		// while (tokenStream.incrementToken()) {
		// CharTermAttribute charTermAttribute = tokenStream
		// .getAttribute(CharTermAttribute.class);
		// System.out.println(new String(charTermAttribute.buffer()));
		// }
		// tokenStream.close();// 必须要close，不然会报错
		//
		// // 打印方法二
		// System.out.println("打印方法二");
		// // 将一个字符串创建成Token流
		TokenStream tokenStream2 = analyzer.tokenStream("contents",
				new StringReader(text));
		tokenStream2.reset(); // 必须要reset，不然会抛出NullPointerException
		// 保存相应词汇
		CharTermAttribute charTermAttribute2 = tokenStream2
				.addAttribute(CharTermAttribute.class);
		while (tokenStream2.incrementToken()) {
			System.out.print("[" + charTermAttribute2 + "]");
		}
		tokenStream2.close();// 必须要close，不然会报错
		System.out.println();
	}
}
