package cn.edu.scut.patent.core;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import cn.edu.scut.patent.model.PatentDao;
import cn.edu.scut.patent.prework.GetPatents;
import cn.edu.scut.patent.prework.SaveToMysql;
import cn.edu.scut.patent.util.Constants;
import cn.edu.scut.patent.util.DatabaseHelper;
import cn.edu.scut.patent.util.FileHelper;
import cn.edu.scut.patent.util.PDFHelper;
import cn.edu.scut.patent.util.StringHelper;
import cn.edu.scut.patent.ICTCLASAnalyzer.ICTCLASAnalyzer;

/**
 * @author qyj
 * @version February 6, 2014
 */
public class IndexAndSearch {

	/**
	 * 数据库索引程序的执行
	 * 
	 * @param dir_string
	 * @throws Exception
	 */
	public static void doIndexFromDatabase() throws Exception {
		doIndexPrework();
		// 索引开始的时间
		long startTime = new Date().getTime();
		Analyzer analyzer = new ICTCLASAnalyzer(Version.LUCENE_46);
		// 存放索引文件的位置
		Directory directory = FSDirectory.open(new File(
				Constants.INDEX_DIR_STRING));
		if (IndexWriter.isLocked(directory)) {
			IndexWriter.unlock(directory);
		}
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46,
				analyzer);
		// 设置打开索引模式为创建或追加
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		// config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		List<Document> listDocument = DatabaseHelper
				.getDocumentFromDatabase(analyzer);
		if (listDocument == null || listDocument.size() == 0) {
			return;
		}
		for (int i = 0; i < listDocument.size(); i++) {
			indexWriter.addDocument(listDocument.get(i));
		}
		// 提交事务
		indexWriter.commit();
		indexWriter.close();
		System.out.println("一共花费了" + StringHelper.timer(startTime) + "完成索引！");
	}

	/**
	 * 逐个PDF索引程序的执行
	 * 
	 * @throws Exception
	 */
	public static void doIndexFromPDF() throws Exception {
		Map<String, String> map = FileHelper
				.readfile(Constants.FILE_DIR_STRING);
		int i = 1;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println(i++ + "#######################################");
			// 索引文件(夹)的位置
			File fileDir = new File(entry.getValue());
			Analyzer analyzer = new ICTCLASAnalyzer(Version.LUCENE_46);
			// 存放索引文件的位置
			Directory directory = FSDirectory.open(new File(
					Constants.INDEX_DIR_STRING));
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46,
					analyzer);
			// 设置打开索引模式为创建或追加
			config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			IndexWriter indexWriter = new IndexWriter(directory, config);
			// 索引开始的时间
			long startTime = new Date().getTime();
			Document document = PDFHelper.getDocumentFromPDF(fileDir, analyzer);
			indexWriter.addDocument(document);
			// 提交事务
			indexWriter.commit();
			indexWriter.close();
			// 索引结束的时间
			long endTime = new Date().getTime();
			System.out.println("一共花费了" + (endTime - startTime) + "毫秒完成索引！");
		}
	}

	/**
	 * 数据库索引程序的执行前所需要的准备工作
	 */
	private static void doIndexPrework() {
		if (!FileHelper.hasFiles(Constants.WEBSITE_PATH)) {
			System.out.println("开始进行GetPatents专利网页下载工作。");
			GetPatents.doGetPatents();
			System.out.println("GetPatents专利网页下载工作结束！");
		} else {
			System.out.println("专利网页已经存在，不需要进行GetPatents网页下载，跳过。");
		}
		if (!DatabaseHelper.isTableExisted("PATENTS")) {
			System.out.println("开始把保存下来专利网页的专利信息保存到数据库。");
			SaveToMysql.doSaveToMysql();
			System.out.println("专利信息保存到数据库完成！");
		} else {
			System.out.println("PATENTS数据库已经存在，不需要进行专利信息保存到数据库工作，跳过。");
		}
	}

	/**
	 * 高亮显示设置
	 * 
	 * @param query
	 * @param analyzer
	 * @param result
	 * @return
	 */
	private static String toHighlighter(Query query, Analyzer analyzer,
			String result) {
		try {
			SimpleHTMLFormatter simpleHtmlFormatter = new SimpleHTMLFormatter(
					"<font color=\"red\">", "</font>");
			Highlighter highlighter = new Highlighter(simpleHtmlFormatter,
					new QueryScorer(query));
			TokenStream tokenStream1 = analyzer.tokenStream("text",
					new StringReader(result));
			String highlighterStr = highlighter.getBestFragment(tokenStream1,
					result);
			if (highlighterStr != null) {
				return highlighterStr;
			} else {
				return result;
			}
		} catch (InvalidTokenOffsetsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 查询程序的执行
	 * 
	 * @param patentdao
	 * @param pttTypeList
	 * @return
	 * @throws IOException
	 */
	public static List<PatentDao> doSearch(PatentDao patentdao,
			List<String> pttTypeList) throws IOException {
		TopDocs td = null;
		Query query = null;
		Directory directory = FSDirectory.open(new File(
				Constants.INDEX_DIR_STRING));
		IndexReader indexreader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(indexreader);
		Analyzer analyzer = new ICTCLASAnalyzer(Version.LUCENE_46);
		Map<String, String> map = patentdao.getAll();
		if (map.size() > 0) {
			System.out.println("size:" + map.size());
			BooleanQuery booleanQuery = new BooleanQuery();
			Set<String> keySet = map.keySet();
			Iterator it = keySet.iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				String value = map.get(key);
				if (key == "APPLY_NUM") {
					Query tempQuery = new TermQuery(new Term(key, value));
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "APPLY_DATE") {
					Query tempQuery = new TermQuery(new Term(key, value));
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "PTT_NAME") {
					PhraseQuery tempQuery = new PhraseQuery();
					for (String word : value.split(" ")) {
						tempQuery.add(new Term(key, word));
					}
					tempQuery.setSlop(10);
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "PTT_NUM") {
					Query tempQuery = new TermQuery(new Term(key, value));
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "PTT_DATE") {
					Query tempQuery = new TermQuery(new Term(key, value));
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "PTT_MAIN_CLASS_NUM") {
					Query tempQuery = new TermQuery(new Term(key, value));
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "PTT_CLASS_NUM") {
					Query tempQuery = new TermQuery(new Term(key, value));
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "PROPOSER") {
					PhraseQuery tempQuery = new PhraseQuery();
					for (String word : value.split(" ")) {
						tempQuery.add(new Term(key, word));
					}
					tempQuery.setSlop(20);
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "PROPOSER_ADDRESS") {
					PhraseQuery tempQuery = new PhraseQuery();
					for (String word : value.split(" ")) {
						tempQuery.add(new Term(key, word));
					}
					tempQuery.setSlop(20);
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "INVENTOR") {
					PhraseQuery tempQuery = new PhraseQuery();
					for (String word : value.split(" ")) {
						tempQuery.add(new Term(key, word));
					}
					tempQuery.setSlop(10);
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "INTERNATIONAL_APPLY") {
					PhraseQuery tempQuery = new PhraseQuery();
					for (String word : value.split(" ")) {
						tempQuery.add(new Term(key, word));
					}
					tempQuery.setSlop(20);
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "INTERNATIONAL_PUBLICATION") {
					PhraseQuery tempQuery = new PhraseQuery();
					for (String word : value.split(" ")) {
						tempQuery.add(new Term(key, word));
					}
					tempQuery.setSlop(20);
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "INTO_DATE") {
					Query tempQuery = new TermQuery(new Term(key, value));
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "PTT_AGENCY_ORG") {
					PhraseQuery tempQuery = new PhraseQuery();
					for (String word : value.split(" ")) {
						tempQuery.add(new Term(key, word));
					}
					tempQuery.setSlop(20);
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "PTT_AGENCY_PERSON") {
					PhraseQuery tempQuery = new PhraseQuery();
					for (String word : value.split(" ")) {
						tempQuery.add(new Term(key, word));
					}
					tempQuery.setSlop(10);
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "PTT_ABSTRACT") {
					PhraseQuery tempQuery = new PhraseQuery();
					for (String word : value.split(" ")) {
						tempQuery.add(new Term(key, word));
					}
					tempQuery.setSlop(50);
					booleanQuery.add(tempQuery, Occur.MUST);
				}
				System.out.println("key:" + key + "&value:" + value);
			}

			BooleanQuery pttTypeBooleanQuery = new BooleanQuery();
			for (String item : pttTypeList) {
				Query tempQuery = new TermQuery(new Term("PTT_TYPE", item));
				pttTypeBooleanQuery.add(tempQuery, Occur.SHOULD);
			}
			booleanQuery.add(pttTypeBooleanQuery, Occur.MUST);
			query = booleanQuery;
		}
		if (query != null) {
			td = searcher.search(query, 1000000);// 这里填写最大输出结果数量
			if (td.totalHits > 0) {
				System.out.println("找到：" + td.totalHits + "个结果！");
				List<PatentDao> patentList = new ArrayList<PatentDao>();
				ScoreDoc[] sds = td.scoreDocs;
				for (int i = 0; i < sds.length; i++) {
					System.out.println("******第" + (i + 1) + "个结果******");
					PatentDao pttDao = new PatentDao();
					Document d = searcher.doc(sds[i].doc);
					String highlighterStr = toHighlighter(query, analyzer,
							d.get("PTT_TYPE"));
					System.out
							.println("PTT_TYPE" + ":[" + highlighterStr + "]");
					pttDao.setPttType(highlighterStr);
					highlighterStr = toHighlighter(query, analyzer,
							d.get("APPLY_NUM"));
					System.out.println("APPLY_NUM" + ":[" + highlighterStr
							+ "]");
					pttDao.setApplyNum(highlighterStr);
					System.out.println("APPLY_DATE" + ":["
							+ d.get("APPLY_DATE") + "]");
					pttDao.setApplyDate(StringHelper.stringToDate(d
							.get("APPLY_DATE")));
					highlighterStr = toHighlighter(query, analyzer,
							d.get("PTT_NAME"));
					System.out
							.println("PTT_NAME" + ":[" + highlighterStr + "]");
					pttDao.setPttName(highlighterStr);
					highlighterStr = toHighlighter(query, analyzer,
							d.get("PTT_NUM"));
					System.out.println("PTT_NUM" + ":[" + highlighterStr + "]");
					pttDao.setPttNum(highlighterStr);
					System.out.println("PTT_DATE" + ":[" + d.get("PTT_DATE")
							+ "]");
					pttDao.setPttDate(StringHelper.stringToDate(d
							.get("PTT_DATE")));
					highlighterStr = toHighlighter(query, analyzer,
							d.get("PTT_MAIN_CLASS_NUM"));
					System.out.println("PTT_MAIN_CLASS_NUM" + ":["
							+ highlighterStr + "]");
					pttDao.setPttMainClassNum(highlighterStr);
					highlighterStr = toHighlighter(query, analyzer,
							d.get("PTT_CLASS_NUM"));
					System.out.println("PTT_CLASS_NUM" + ":[" + highlighterStr
							+ "]");
					pttDao.setPttClassNum(highlighterStr);
					highlighterStr = toHighlighter(query, analyzer,
							d.get("PROPOSER"));
					System.out
							.println("PROPOSER" + ":[" + highlighterStr + "]");
					pttDao.setProposer(highlighterStr);
					highlighterStr = toHighlighter(query, analyzer,
							d.get("PROPOSER_ADDRESS"));
					System.out.println("PROPOSER_ADDRESS" + ":["
							+ highlighterStr + "]");
					pttDao.setProposerAddress(highlighterStr);
					highlighterStr = toHighlighter(query, analyzer,
							d.get("INVENTOR"));
					System.out
							.println("INVENTOR" + ":[" + highlighterStr + "]");
					pttDao.setInventor(highlighterStr);
					highlighterStr = toHighlighter(query, analyzer,
							d.get("INTERNATIONAL_APPLY"));
					System.out.println("INTERNATIONAL_APPLY" + ":["
							+ highlighterStr + "]");
					pttDao.setInternationalApply(highlighterStr);
					highlighterStr = toHighlighter(query, analyzer,
							d.get("INTERNATIONAL_PUBLICATION"));
					System.out.println("INTERNATIONAL_PUBLICATION" + ":["
							+ highlighterStr + "]");
					pttDao.setInternationalPublication(highlighterStr);
					System.out.println("INTO_DATE" + ":[" + d.get("INTO_DATE")
							+ "]");
					pttDao.setIntoDate(StringHelper.stringToDate(d
							.get("INTO_DATE")));
					highlighterStr = toHighlighter(query, analyzer,
							d.get("PTT_AGENCY_ORG"));
					System.out.println("PTT_AGENCY_ORG" + ":[" + highlighterStr
							+ "]");
					pttDao.setPttAgencyOrg(highlighterStr);
					highlighterStr = toHighlighter(query, analyzer,
							d.get("PTT_AGENCY_PERSON"));
					System.out.println("PTT_AGENCY_PERSON" + ":["
							+ highlighterStr + "]");
					pttDao.setPttAgencyPerson(highlighterStr);
					highlighterStr = toHighlighter(query, analyzer,
							d.get("PTT_ABSTRACT"));
					System.out.println("PTT_ABSTRACT" + ":[" + highlighterStr
							+ "]");
					pttDao.setPttAbstract(highlighterStr);
					patentList.add(pttDao);
				}
				return patentList;
			} else {
				System.out.println("没有找到任何结果！");
				return null;
			}
		} else {
			System.out.println("没有输入任何有效的查询条件！");
			return null;
		}
	}
}
