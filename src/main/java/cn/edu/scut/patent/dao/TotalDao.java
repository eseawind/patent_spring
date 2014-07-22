package cn.edu.scut.patent.dao;

import java.math.BigInteger;
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
	 * 获取表格长度
	 * 
	 * @param session
	 * @param tableName
	 * @return
	 */
	public int getTableSize(Session session, String tableName) {
		session.beginTransaction();
		Query query = session.createSQLQuery("select count(*) from "
				+ tableName);
		session.getTransaction().commit();
		if (query.uniqueResult() == null) {
			return 0;
		} else {
			return ((BigInteger) query.uniqueResult()).intValue();
		}
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
		session.clear();
		session.beginTransaction();
		session.update(object);
		session.getTransaction().commit();
	}

	/**
	 * 更新或另存为
	 * 
	 * @param session
	 * @param object
	 */
	public void saveOrUpdate(Session session, Object object) {
		session.beginTransaction();
		session.saveOrUpdate(object);
		session.getTransaction().commit();
	}

	/**
	 * 删除
	 * 
	 * @param session
	 * @param object
	 */
	public void delete(Session session, Object object) {
		session.beginTransaction();
		session.delete(object);
		session.getTransaction().commit();
	}
}
