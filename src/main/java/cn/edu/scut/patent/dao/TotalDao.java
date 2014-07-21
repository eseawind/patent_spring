package cn.edu.scut.patent.dao;

import org.hibernate.Query;
import org.hibernate.Session;

public class TotalDao {

	/**
	 * 检查表是否为空
	 * 
	 * @param session
	 * @param tableName
	 * @return
	 */
	public static Boolean isEmpty(Session session, String tableName) {
		session.beginTransaction();
		Query query = session.createSQLQuery("select * from " + tableName);
		session.getTransaction().commit();
		if (query.list().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 清空表格
	 * 
	 * @param session
	 * @param tableName
	 * @return
	 */
	public static void cleanTable(Session session, String tableName) {
		session.beginTransaction();
		Query query = session.createSQLQuery("delete from " + tableName);
		query.executeUpdate();
		session.getTransaction().commit();
	}

	/**
	 * 保存
	 * 
	 * @param session
	 * @param object
	 */
	public void save(Session session, Object object) {
		session.beginTransaction();
		session.save(object);
		session.getTransaction().commit();
	}
	
	/**
	 * 更新
	 * 
	 * @param session
	 * @param object
	 */
	public void update(Session session, Object object) {
		session.beginTransaction();
		session.update(object);
		session.getTransaction().commit();
	}
}
