package cn.edu.scut.patent.dao;

import org.hibernate.Session;
import cn.edu.scut.patent.model.PatentsAfterWordDivide;

public class PatentsAfterWordDivideDao extends TotalDao {

	/**
	 * 检查表是否为空
	 * 
	 * @param session
	 */
	public static Boolean isEmpty(Session session) {
		return isEmpty(session, "PATENTS_AFTER_WORD_DIVIDE");
	}
	
	/**
	 * 清空表
	 * 
	 * @param session
	 */
	public static void cleanTable(Session session) {
		cleanTable(session, "PATENTS_AFTER_WORD_DIVIDE");
	}
	
	/**
	 * 保存
	 * 
	 * @param session
	 * @param patentsAfterWordDivide
	 */
	public void save(Session session, PatentsAfterWordDivide patentsAfterWordDivide) {
		save(session, (Object) patentsAfterWordDivide);
	}
}
