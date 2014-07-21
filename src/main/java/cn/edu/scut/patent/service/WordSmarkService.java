package cn.edu.scut.patent.service;

import cn.edu.scut.patent.dao.WordSmarkDao;
import cn.edu.scut.patent.model.WordSmark;

public class WordSmarkService extends TotalService {

	/**
	 * 检查表是否为空
	 * 
	 * @return
	 */
	public static Boolean isEmpty() {
		return WordSmarkDao.isEmpty(session);
	}

	/**
	 * 清空表
	 */
	public static void cleanTable() {
		WordSmarkDao.cleanTable(session);
	}
	
	/**
	 * 保存
	 * 
	 * @param wordSmark
	 */
	public void save(WordSmark wordSmark) {
		new WordSmarkDao().save(session, wordSmark);
	}
}
