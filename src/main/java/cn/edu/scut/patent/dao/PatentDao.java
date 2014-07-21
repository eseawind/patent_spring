package cn.edu.scut.patent.dao;

import org.hibernate.Session;
import cn.edu.scut.patent.model.Patent;

public class PatentDao extends TotalDao {

	/**
	 * 检查表是否为空
	 * 
	 * @param session
	 */
	public static Boolean isEmpty(Session session) {
		return isEmpty(session, "PATENTS");
	}

	/**
	 * 清空表
	 * 
	 * @param session
	 */
	public static void cleanTable(Session session) {
		cleanTable(session, "PATENTS");
	}

	/**
	 * 保存
	 * 
	 * @param session
	 * @param patent
	 */
	public void save(Session session, Patent patent) {
		save(session, (Object) patent);
	}

	/**
	 * 查找
	 * 
	 * @param session
	 * @param PTT_NUM
	 * @return
	 */
	public Patent find(Session session, String pttNum) {
		session.beginTransaction();
		Patent patent = (Patent) session.get(Patent.class, pttNum);
		session.getTransaction().commit();
		return patent;
	}
}
