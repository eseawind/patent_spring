package cn.edu.scut.patent.service;

import java.util.List;
import cn.edu.scut.patent.dao.PatentWordTfDfDao;
import cn.edu.scut.patent.model.PatentWordTfDf;

public class PatentWordTfDfService extends TotalService {

	/**
	 * 检查表是否为空
	 * 
	 * @return
	 */
	public static Boolean isEmpty() {
		return PatentWordTfDfDao.isEmpty(session);
	}

	/**
	 * 清空表
	 */
	public static void cleanTable() {
		PatentWordTfDfDao.cleanTable(session);
	}

	/**
	 * 保存
	 * 
	 * @param patentWordTfDf
	 */
	public void save(PatentWordTfDf patentWordTfDf) {
		new PatentWordTfDfDao().save(session, patentWordTfDf);
	}

	/**
	 * 更新
	 * 
	 * @param patentWordTfDf
	 */
	public void update(PatentWordTfDf patentWordTfDf) {
		new PatentWordTfDfDao().update(session, patentWordTfDf);
	}

	/**
	 * 获取表格中所有的word
	 * 
	 * @return
	 */
	public List<String> getAllWord() {
		return new PatentWordTfDfDao().getAllWord(session);
	}

	/**
	 * 获取表格中所有的pttNum
	 * 
	 * @return
	 */
	public List<String> getAllPttNum() {
		return new PatentWordTfDfDao().getAllPttNum(session);
	}

	/**
	 * 根据word获取表格符合要求的pttNum
	 * 
	 * @param word
	 * @return
	 */
	public List<String> getAllPttNumFromWord(String word) {
		return new PatentWordTfDfDao().getAllPttNumFromWord(session, word);
	}

	/**
	 * 根据word获取符合要求的数据
	 * 
	 * @param word
	 * @return
	 */
	public List<PatentWordTfDf> getAllFromWord(String word) {
		return new PatentWordTfDfDao().getAllFromWord(session, word);
	}

	/**
	 * 根据pttNum获取符合要求的数据
	 * 
	 * @param pttNum
	 * @return
	 */
	public List<PatentWordTfDf> getAllFromPttNum(String pttNum) {
		return new PatentWordTfDfDao().getAllFromPttNum(session, pttNum);
	}
}
