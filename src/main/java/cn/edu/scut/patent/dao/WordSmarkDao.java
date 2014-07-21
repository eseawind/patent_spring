package cn.edu.scut.patent.dao;

import org.hibernate.Session;
import cn.edu.scut.patent.model.WordSmark;

public class WordSmarkDao extends TotalDao {

	/**
	 * 检查表是否为空
	 * 
	 * @param session
	 */
	public static Boolean isEmpty(Session session) {
		// return isEmpty(session, "T_WORD_SMARK");
		return false;
	}
	
	/**
	 * 清空表
	 * 
	 * @param session
	 */
	public static void cleanTable(Session session) {
		cleanTable(session, "T_WORD_SMARK");
	}

	/**
	 * 保存
	 * 
	 * @param session
	 * @param wordSmark
	 */
	public void save(Session session, WordSmark wordSmark) {
		save(session, (Object) wordSmark);
	}
}
