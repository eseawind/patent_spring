package cn.edu.scut.patent.dao;

import org.hibernate.Session;
import cn.edu.scut.patent.model.Stopword;

public class StopwordDao extends TotalDao {

	/**
	 * 检查表是否为空
	 * 
	 * @param session
	 */
	public static Boolean isEmpty(Session session) {
		// return isEmpty(session, "T_STOPWORD");
		return false;
	}
	
	/**
	 * 清空表
	 * 
	 * @param session
	 */
	public static void cleanTable(Session session) {
		cleanTable(session, "T_STOPWORD");
	}

	/**
	 * 保存
	 * 
	 * @param session
	 * @param stopword
	 */
	public void save(Session session, Stopword stopword) {
		save(session, (Object) stopword);
	}
}
