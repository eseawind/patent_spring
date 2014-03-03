package cn.edu.scut.patent.testing;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import net.paoding.analysis.analyzer.PaodingAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;
import ICTCLAS2014.Nlpir;
import cn.edu.scut.patent.util.CheckHelper;
import cn.edu.scut.patent.util.Constants;
import cn.edu.scut.patent.util.PDFHelper;
import cn.edu.scut.patent.ICTCLASAnalyzer.ICTCLASAnalyzer;

/**
 * @author qyj
 * @version February 6, 2014
 */
public class IndexAndSearch {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			index();
			// search("孙皓", "contents");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * 索引
	 * 
	 * @throws Exception
	 */
	private static void index() throws Exception {
		// 索引文件(夹)的位置
		File fileDir = new File(Constants.FILE_DIR_STRING);
		Analyzer analyzer = new ICTCLASAnalyzer(Version.LUCENE_46);
		// Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
		// 存放索引文件的位置
		Directory directory = FSDirectory.open(new File(
				Constants.INDEX_DIR_STRING));
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_46,
				analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, iwc);
		// 索引开始的时间
		long startTime = new Date().getTime();
		Document document = PDFHelper.getDocumentFromPDF(fileDir);

		// 打印分词结果
		TextField textfield = (TextField) document.getField("contents");
		String result = textfield.stringValue();
		CheckHelper.printKeyWords(analyzer, result);
		// testing analyzer
		// testAllAnalyzer();

		indexWriter.addDocument(document);
		// 提交事务
		indexWriter.commit();
		indexWriter.close();
		// 索引结束的时间
		long endTime = new Date().getTime();
		System.out.println("一共花费了" + (endTime - startTime) + "毫秒完成索引！");
	}

	/**
	 * 查询
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private static void search(String queryString, String field)
			throws IOException {
		TopDocs td = null;
		Query query = null;
		Directory directory = FSDirectory.open(new File(
				Constants.INDEX_DIR_STRING));
		IndexReader indexreader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(indexreader);
		Analyzer analyzer = new ICTCLASAnalyzer(Version.LUCENE_46);
		// Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
		try {
			QueryParser queryParser = new QueryParser(Version.LUCENE_46, field,
					analyzer);
			query = queryParser.parse(queryString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (searcher != null) {
			td = searcher.search(query, 1000);
			if (td.totalHits > 0) {
				System.out.println("找到：" + td.totalHits + "个结果！");
				ScoreDoc[] sds = td.scoreDocs;
				for (ScoreDoc sd : sds) {
					Document d = searcher.doc(sd.doc);
					System.out.println(d.get("path") + ":[" + d.get("path")
							+ "]");
				}
			} else {
				System.out.println("没有找到任何结果！");
			}
		}
	}

	/**
	 * 测试所有的analyzer
	 * 
	 * @throws IOException
	 */
	private static void testAllAnalyzer() throws IOException {
		String text = "据悉，质检总局已将       最新 good news  有关情况再次通报美方，要求美方加强对输华玉米的产地来源、运输及仓储等环节的管控措施，有效避免输华玉米被未经我国农业部安全评估并批准的转基因品系污染。";

		Analyzer standardAnalyzer = new StandardAnalyzer(Version.LUCENE_46);
		CheckHelper.printKeyWords(standardAnalyzer, text);

		Analyzer stopAnalyzer = new StopAnalyzer(Version.LUCENE_46);
		CheckHelper.printKeyWords(stopAnalyzer, text);

		Analyzer cjkAnalyzer = new CJKAnalyzer(Version.LUCENE_46);
		CheckHelper.printKeyWords(cjkAnalyzer, text);

		Analyzer chineseAnalyzer = new ChineseAnalyzer();
		CheckHelper.printKeyWords(chineseAnalyzer, text);

		Analyzer paodingAnalyzer = new PaodingAnalyzer();
		CheckHelper.printKeyWords(paodingAnalyzer, text);

		Analyzer ikAnalyzer = new IKAnalyzer();
		CheckHelper.printKeyWords(ikAnalyzer, text);

		Analyzer smartChineseAnalyzer = new SmartChineseAnalyzer(
				Version.LUCENE_46);
		CheckHelper.printKeyWords(smartChineseAnalyzer, text);

		Analyzer ictclasAnalyzer = new ICTCLASAnalyzer(Version.LUCENE_46);
		CheckHelper.printKeyWords(ictclasAnalyzer,
				Nlpir.doNlpirString(text, null, null));
	}
}
