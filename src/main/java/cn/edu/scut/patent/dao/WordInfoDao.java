package cn.edu.scut.patent.dao;

import org.hibernate.Session;
import cn.edu.scut.patent.model.WordInfo;

public class WordInfoDao extends TotalDao {

	/**
	 * 检查表是否为空
	 * 
	 * @param session
	 */
	public static Boolean isEmpty(Session session) {
		return isEmpty(session, "T_WORD_INFO");
	}
	
	/**
	 * 清空表
	 * 
	 * @param session
	 */
	public static void cleanTable(Session session) {
		cleanTable(session, "T_WORD_INFO");
	}
	
	/**
	 * 保存
	 * 
	 * @param session
	 * @param wordInfo
	 */
	public void save(Session session, WordInfo wordInfo) {
		save(session, (Object) wordInfo);
	}
}
