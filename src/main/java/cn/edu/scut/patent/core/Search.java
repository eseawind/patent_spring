package cn.edu.scut.patent.core;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
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
import cn.edu.scut.patent.core.impl.SearchImpl;
import cn.edu.scut.patent.model.Patent;
import cn.edu.scut.patent.service.PatentService;
import cn.edu.scut.patent.util.Constants;
import cn.edu.scut.patent.util.StringHelper;
import cn.edu.scut.patent.ICTCLASAnalyzer.ICTCLASAnalyzer;

/**
 * @author qyj
 * @version February 6, 2014
 */
public class Search implements SearchImpl {

	@SuppressWarnings("resource")
	public List<Patent> doSearch(Patent patentdao, List<String> pttTypeList)
			throws IOException {
		TopDocs td = null;
		Query query = null;
		Directory directory = FSDirectory.open(new File(
				Constants.INDEX_DIR_STRING));
		IndexReader indexreader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(indexreader);
		Analyzer analyzer = new ICTCLASAnalyzer(Version.LUCENE_46);
		query = htmlConditionsToQuery(patentdao, pttTypeList);
		if (query != null) {
			td = searcher.search(query, Constants.MAX_SEARCH_RESULT);
			if (td.totalHits > 0) {
				System.out.println("找到：" + td.totalHits + "个结果！");
				return searchResultToPatentdao(td, searcher, query, analyzer);
			} else {
				System.out.println("没有找到任何结果！");
				return null;
			}
		} else {
			System.out.println("没有输入任何有效的查询条件！");
			return null;
		}
	}

	public Query htmlConditionsToQuery(Patent patent, List<String> pttTypeList) {
		Map<String, String> map = new PatentService()
				.getAllPatentProperties(patent);
		if (map.size() > 0) {
			System.out.println("size:" + map.size());
			BooleanQuery booleanQuery = new BooleanQuery();
			Set<String> keySet = map.keySet();
			Iterator<String> it = keySet.iterator();
			while (it.hasNext()) {
				String key = it.next();
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
			return booleanQuery;
		}
		return null;
	}

	public List<Patent> searchResultToPatentdao(TopDocs td,
			IndexSearcher searcher, Query query, Analyzer analyzer)
			throws IOException {
		List<Patent> patentList = new ArrayList<Patent>();
		ScoreDoc[] sds = td.scoreDocs;
		for (int i = 0; i < sds.length; i++) {
			System.out.println("******第" + (i + 1) + "个结果******");
			Patent pttDao = new Patent();
			Document d = searcher.doc(sds[i].doc);
			String highlighterStr;
			System.out.println("PTT_TYPE" + ":[" + d.get("PTT_TYPE") + "]");
			pttDao.setPttType(d.get("PTT_TYPE"));
			System.out.println("APPLY_NUM" + ":[" + d.get("APPLY_NUM") + "]");
			pttDao.setApplyNum(d.get("APPLY_NUM"));
			System.out.println("APPLY_DATE" + ":[" + d.get("APPLY_DATE") + "]");
			pttDao.setApplyDate(StringHelper.stringToDate(d.get("APPLY_DATE")));
			highlighterStr = toHighlighter(query, analyzer, d.get("PTT_NAME"));
			System.out.println("PTT_NAME" + ":[" + highlighterStr + "]");
			pttDao.setPttName(highlighterStr);
			System.out.println("PTT_NUM" + ":[" + d.get("PTT_NUM") + "]");
			pttDao.setPttNum(d.get("PTT_NUM"));
			System.out.println("PTT_DATE" + ":[" + d.get("PTT_DATE") + "]");
			pttDao.setPttDate(StringHelper.stringToDate(d.get("PTT_DATE")));
			highlighterStr = toHighlighter(query, analyzer,
					d.get("PTT_MAIN_CLASS_NUM"));
			System.out.println("PTT_MAIN_CLASS_NUM" + ":[" + highlighterStr
					+ "]");
			pttDao.setPttMainClassNum(highlighterStr);
			highlighterStr = toHighlighter(query, analyzer,
					d.get("PTT_CLASS_NUM"));
			System.out.println("PTT_CLASS_NUM" + ":[" + highlighterStr + "]");
			pttDao.setPttClassNum(highlighterStr);
			highlighterStr = toHighlighter(query, analyzer, d.get("PROPOSER"));
			System.out.println("PROPOSER" + ":[" + highlighterStr + "]");
			pttDao.setProposer(highlighterStr);
			highlighterStr = toHighlighter(query, analyzer,
					d.get("PROPOSER_ADDRESS"));
			System.out
					.println("PROPOSER_ADDRESS" + ":[" + highlighterStr + "]");
			pttDao.setProposerAddress(highlighterStr);
			highlighterStr = toHighlighter(query, analyzer, d.get("INVENTOR"));
			System.out.println("INVENTOR" + ":[" + highlighterStr + "]");
			pttDao.setInventor(highlighterStr);
			highlighterStr = toHighlighter(query, analyzer,
					d.get("INTERNATIONAL_APPLY"));
			System.out.println("INTERNATIONAL_APPLY" + ":[" + highlighterStr
					+ "]");
			pttDao.setInternationalApply(highlighterStr);
			highlighterStr = toHighlighter(query, analyzer,
					d.get("INTERNATIONAL_PUBLICATION"));
			System.out.println("INTERNATIONAL_PUBLICATION" + ":["
					+ highlighterStr + "]");
			pttDao.setInternationalPublication(highlighterStr);
			System.out.println("INTO_DATE" + ":[" + d.get("INTO_DATE") + "]");
			pttDao.setIntoDate(StringHelper.stringToDate(d.get("INTO_DATE")));
			highlighterStr = toHighlighter(query, analyzer,
					d.get("PTT_AGENCY_ORG"));
			System.out.println("PTT_AGENCY_ORG" + ":[" + highlighterStr + "]");
			pttDao.setPttAgencyOrg(highlighterStr);
			highlighterStr = toHighlighter(query, analyzer,
					d.get("PTT_AGENCY_PERSON"));
			System.out.println("PTT_AGENCY_PERSON" + ":[" + highlighterStr
					+ "]");
			pttDao.setPttAgencyPerson(highlighterStr);
			highlighterStr = toHighlighter(query, analyzer,
					d.get("PTT_ABSTRACT"));
			System.out.println("PTT_ABSTRACT" + ":[" + highlighterStr + "]");
			pttDao.setPttAbstract(highlighterStr);
			patentList.add(pttDao);
		}
		return patentList;
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
}
