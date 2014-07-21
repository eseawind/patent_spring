package cn.edu.scut.patent.service;

import cn.edu.scut.patent.dao.StopwordDao;
import cn.edu.scut.patent.model.Stopword;

public class StopwordService extends TotalService {

	/**
	 * 检查表是否为空
	 * 
	 * @return
	 */
	public static Boolean isEmpty() {
		return StopwordDao.isEmpty(session);
	}

	/**
	 * 清空表
	 */
	public static void cleanTable() {
		StopwordDao.cleanTable(session);
	}
	
	/**
	 * 保存
	 * 
	 * @param stopword
	 */
	public void save(Stopword stopword) {
		new StopwordDao().save(session, stopword);
	}
}
