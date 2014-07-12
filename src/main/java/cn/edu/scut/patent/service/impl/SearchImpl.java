package cn.edu.scut.patent.service.impl;

import java.io.IOException;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import cn.edu.scut.patent.model.PatentDao;

public interface SearchImpl {

	/**
	 * 查询程序的执行
	 * 
	 * @param patentdao
	 * @param pttTypeList
	 * @return
	 * @throws IOException
	 */
	List<PatentDao> doSearch(PatentDao patentdao, List<String> pttTypeList)
			throws IOException;

	/**
	 * 把网页上的查询条件插入到Query类当中
	 * 
	 * @param patentdao
	 * @param pttTypeList
	 * @return
	 */
	Query htmlConditionsToQuery(PatentDao patentdao, List<String> pttTypeList);

	/**
	 * 提取检索的结果并以List<PatentDao>的形式返回
	 * 
	 * @param td
	 * @param searcher
	 * @param query
	 * @param analyzer
	 * @return
	 * @throws IOException
	 */
	List<PatentDao> searchResultToPatentdao(TopDocs td, IndexSearcher searcher,
			Query query, Analyzer analyzer) throws IOException;
}
