package cn.edu.scut.patent.dao;

import org.hibernate.Session;
import cn.edu.scut.patent.model.Data;

public class DataDao extends TotalDao {

	/**
	 * 检查表是否为空
	 * 
	 * @param session
	 */
	public static Boolean isEmpty(Session session) {
		return isEmpty(session, "DATA");
	}

	/**
	 * 清空表
	 * 
	 * @param session
	 */
	public static void cleanTable(Session session) {
		cleanTable(session, "DATA");
	}

	/**
	 * 保存
	 * 
	 * @param session
	 * @param data
	 */
	public void save(Session session, Data data) {
		save(session, (Object) data);
	}
}
