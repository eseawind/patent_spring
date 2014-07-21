package cn.edu.scut.patent.dao;

import org.hibernate.Session;
import cn.edu.scut.patent.model.Classification;

public class ClassificationDao extends TotalDao {

	/**
	 * 检查表是否为空
	 * 
	 * @param session
	 */
	public static Boolean isEmpty(Session session) {
		return isEmpty(session, "CLASSIFICATION");
	}

	/**
	 * 清空表
	 * 
	 * @param session
	 */
	public static void cleanTable(Session session) {
		cleanTable(session, "CLASSIFICATION");
	}

	/**
	 * 保存
	 * 
	 * @param session
	 * @param classification
	 */
	public void save(Session session, Classification classification) {
		save(session, (Object) classification);
	}

	/**
	 * 查找Classification
	 * 
	 * @param session
	 * @param pttNum
	 * @param trizNum
	 * @return
	 */
	public Classification find(Session session, String pttNum, int trizNum) {
		session.beginTransaction();
		Classification classification = (Classification) session.get(
				Classification.class, pttNum);
		session.getTransaction().commit();
		return classification;
	}
}
