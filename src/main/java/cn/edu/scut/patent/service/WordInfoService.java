package cn.edu.scut.patent.service;

import java.util.List;
import cn.edu.scut.patent.dao.WordInfoDao;
import cn.edu.scut.patent.model.WordInfo;

public class WordInfoService extends TotalService {

	/**
	 * 检查表是否为空
	 * 
	 * @return
	 */
	public static Boolean isEmpty() {
		return WordInfoDao.isEmpty(session);
	}

	/**
	 * 清空表
	 */
	public static void cleanTable() {
		WordInfoDao.cleanTable(session);
	}

	/**
	 * 保存
	 * 
	 * @param wordInfo
	 */
	public void save(WordInfo wordInfo) {
		new WordInfoDao().save(session, wordInfo);
	}

	/**
	 * 获取表格中所有的数据
	 * 
	 * @return
	 */
	public List<WordInfo> getAll() {
		return new WordInfoDao().getAll(session);
	}
}
