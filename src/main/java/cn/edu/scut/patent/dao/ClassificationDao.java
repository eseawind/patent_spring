package cn.edu.scut.patent.dao;

import java.math.BigInteger;
import org.hibernate.Query;
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
		// save(session, (Object) classification);
		session.beginTransaction();
		Query query = session
				.createSQLQuery("INSERT INTO patentdb.CLASSIFICATION (PTT_NUM, TRIZ_NUM) VALUES "
						+ "('"
						+ classification.getPttNum()
						+ "','"
						+ classification.getTrizNum() + "')");
		query.executeUpdate();
		session.getTransaction().commit();
	}

	/**
	 * 查找Classification
	 * 
	 * @param session
	 * @param queryClassification
	 * @return
	 */
	public Classification find(Session session,
			Classification queryClassification) {
		session.beginTransaction();
		Classification classification = (Classification) session.get(
				Classification.class, queryClassification);
		session.getTransaction().commit();
		return classification;
	}

	/**
	 * 删除
	 * 
	 * @param session
	 * @param classification
	 */
	public void delete(Session session, Classification classification) {
		// delete(session, (Object) classification);
		session.beginTransaction();
		Query query = session
				.createSQLQuery("DELETE FROM patentdb.CLASSIFICATION WHERE PTT_NUM = '"
						+ classification.getPttNum() + "'");
		query.executeUpdate();
		session.getTransaction().commit();
	}

	/**
	 * CLASSIFICATION统计单个TRIZ的个数
	 * 
	 * @return
	 */
	public int getCount(Session session, int trizNum) {
		session.beginTransaction();
		Query query = session
				.createSQLQuery("select count(*) from CLASSIFICATION where TRIZ_NUM = "
						+ trizNum + " group by TRIZ_NUM");
		session.getTransaction().commit();
		if (query.uniqueResult() == null) {
			return 0;
		} else {
			return ((BigInteger) query.uniqueResult()).intValue();
		}
	}
}
