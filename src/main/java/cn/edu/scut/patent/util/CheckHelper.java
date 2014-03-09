package cn.edu.scut.patent.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import cn.edu.scut.patent.model.PatentDao;

public class CheckHelper {

	/**
	 * 打印分词结果
	 * 
	 * @throws IOException
	 */
	public static void printKeyWords(Analyzer analyzer, PatentDao patentdao)
			throws IOException {
		Map<String, String> map = patentdao.getAll();
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
		if (map != null) {
			Set<String> keySet = map.keySet();
			Iterator it = keySet.iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				String value = map.get(key);
				System.out
						.println("打印分词field=" + key + "\n" + "value=" + value);
				// 将一个字符串创建成Token流
				TokenStream tokenStream2 = analyzer.tokenStream(key,
						new StringReader(value));
				// 必须要reset，不然会抛出NullPointerException
				tokenStream2.reset();
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
	}
}
