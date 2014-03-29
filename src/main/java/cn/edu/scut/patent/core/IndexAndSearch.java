package cn.edu.scut.patent.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
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
		Analyzer analyzer = new ICTCLASAnalyzer(Version.LUCENE_46);
		// 存放索引文件的位置
		Directory directory = FSDirectory.open(new File(
				Constants.INDEX_DIR_STRING));
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46,
				analyzer);
		// 设置打开索引模式为创建或追加
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		// config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		// 索引开始的时间
		long startTime = new Date().getTime();
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
		// 索引结束的时间
		long endTime = new Date().getTime();
		System.out.println("一共花费了" + (endTime - startTime) + "毫秒完成索引！");
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
	 * 查询程序的执行
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
					Query tempQuery = new PrefixQuery(new Term(key, value));
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
					Query tempQuery = new PrefixQuery(new Term(key, value));
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "PROPOSER_ADDRESS") {
					Query tempQuery = new PrefixQuery(new Term(key, value));
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "INVENTOR") {
					Query tempQuery = new PrefixQuery(new Term(key, value));
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "INTERNATIONAL_APPLY") {
					Query tempQuery = new PrefixQuery(new Term(key, value));
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "INTERNATIONAL_PUBLICATION") {
					Query tempQuery = new PrefixQuery(new Term(key, value));
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "INTO_DATE") {
					Query tempQuery = new TermQuery(new Term(key, value));
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "PTT_AGENCY_ORG") {
					Query tempQuery = new PrefixQuery(new Term(key, value));
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "PTT_AGENCY_PERSON") {
					Query tempQuery = new PrefixQuery(new Term(key, value));
					booleanQuery.add(tempQuery, Occur.MUST);
				} else if (key == "PTT_ABSTRACT") {
					Query tempQuery = new PrefixQuery(new Term(key, value));
					booleanQuery.add(tempQuery, Occur.MUST);
				}
				System.out.println("key:" + key + "&value:" + value);
			}

			BooleanQuery pttTypeBooleanQuery = new BooleanQuery();
			for (int i = 0; i < pttTypeList.size(); i++) {
				Query tempQuery = new TermQuery(new Term("PTT_TYPE",
						pttTypeList.get(i)));
				pttTypeBooleanQuery.add(tempQuery, Occur.SHOULD);
			}
			booleanQuery.add(pttTypeBooleanQuery, Occur.MUST);

			query = booleanQuery;
		}
		// String[] fields;
		// String[] stringQuery;
		// Occur[] flags;
		// if (map.size() > 0) {
		// System.out.println("size:" + map.size());
		// fields = new String[map.size()];
		// stringQuery = new String[map.size()];
		// flags = new Occur[map.size()];
		// int i = 0;
		// Set<String> keySet = map.keySet();
		// Iterator it = keySet.iterator();
		// while (it.hasNext()) {
		// String key = (String) it.next();
		// fields[i] = key;
		// String value = map.get(key);
		// stringQuery[i] = value;
		// flags[i++] = Occur.SHOULD;
		// System.out.println("key:" + key + "&value:" + value);
		// }
		// try {
		// query = MultiFieldQueryParser.parse(Version.LUCENE_46,
		// stringQuery, fields, flags, analyzer);
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		if (query != null) {
			td = searcher.search(query, 1000);
			if (td.totalHits > 0) {
				System.out.println("找到：" + td.totalHits + "个结果！");
				List<PatentDao> patentList = new ArrayList<PatentDao>();
				ScoreDoc[] sds = td.scoreDocs;
				for (int i = 0; i < sds.length; i++) {
					System.out.println("******第" + (i + 1) + "个结果******");
					PatentDao pttDao = new PatentDao();
					Document d = searcher.doc(sds[i].doc);
					System.out.println("APPLY_NUM" + ":[" + d.get("APPLY_NUM")
							+ "]");
					pttDao.setApplyNum(d.get("APPLY_NUM"));
					System.out.println("APPLY_DATE" + ":["
							+ d.get("APPLY_DATE") + "]");
					pttDao.setApplyDate(StringHelper.stringToDate(d
							.get("APPLY_DATE")));
					System.out.println("PTT_NAME" + ":[" + d.get("PTT_NAME")
							+ "]");
					pttDao.setPttName(d.get("PTT_NAME"));
					System.out.println("PTT_NUM" + ":[" + d.get("PTT_NUM")
							+ "]");
					pttDao.setPttNum(d.get("PTT_NUM"));
					System.out.println("PTT_DATE" + ":[" + d.get("PTT_DATE")
							+ "]");
					pttDao.setPttDate(StringHelper.stringToDate(d
							.get("PTT_DATE")));
					System.out.println("PTT_MAIN_CLASS_NUM" + ":["
							+ d.get("PTT_MAIN_CLASS_NUM") + "]");
					pttDao.setPttMainClassNum(d.get("PTT_MAIN_CLASS_NUM"));
					System.out.println("PTT_CLASS_NUM" + ":["
							+ d.get("PTT_CLASS_NUM") + "]");
					pttDao.setPttClassNum(d.get("PTT_CLASS_NUM"));
					System.out.println("PROPOSER" + ":[" + d.get("PROPOSER")
							+ "]");
					pttDao.setProposer(d.get("PROPOSER"));
					System.out.println("PROPOSER_ADDRESS" + ":["
							+ d.get("PROPOSER_ADDRESS") + "]");
					pttDao.setProposerAddress(d.get("PROPOSER_ADDRESS"));
					System.out.println("INVENTOR" + ":[" + d.get("INVENTOR")
							+ "]");
					pttDao.setInventor(d.get("INVENTOR"));
					System.out.println("INTERNATIONAL_APPLY" + ":["
							+ d.get("INTERNATIONAL_APPLY") + "]");
					pttDao.setInternationalApply(d.get("INTERNATIONAL_APPLY"));
					System.out.println("INTERNATIONAL_PUBLICATION" + ":["
							+ d.get("INTERNATIONAL_PUBLICATION") + "]");
					pttDao.setInternationalPublication(d
							.get("INTERNATIONAL_PUBLICATION"));
					System.out.println("INTO_DATE" + ":[" + d.get("INTO_DATE")
							+ "]");
					pttDao.setIntoDate(StringHelper.stringToDate(d
							.get("INTO_DATE")));
					System.out.println("PTT_AGENCY_ORG" + ":["
							+ d.get("PTT_AGENCY_ORG") + "]");
					pttDao.setPttAgencyOrg(d.get("PTT_AGENCY_ORG"));
					System.out.println("PTT_AGENCY_PERSON" + ":["
							+ d.get("PTT_AGENCY_PERSON") + "]");
					pttDao.setPttAgencyPerson(d.get("PTT_AGENCY_PERSON"));
					System.out.println("PTT_ABSTRACT" + ":["
							+ d.get("PTT_ABSTRACT") + "]");
					pttDao.setPttAbstract(d.get("PTT_ABSTRACT"));
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
