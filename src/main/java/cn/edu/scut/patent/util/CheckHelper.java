package cn.edu.scut.patent.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import cn.edu.scut.patent.model.Patent;
import cn.edu.scut.patent.service.PatentService;

public class CheckHelper {

	/**
	 * 打印分词结果
	 * 
	 * @throws IOException
	 */
	public static void printKeyWords(Analyzer analyzer, Patent patent)
			throws IOException {
		Map<String, String> map = new PatentService().getAllPatentProperties(patent);
		System.out.println("当前使用的分词器：" + analyzer.getClass().getSimpleName());
		if (map != null) {
			Set<String> keySet = map.keySet();
			Iterator<String> it = keySet.iterator();
			while (it.hasNext()) {
				String key = it.next();
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
