package cn.edu.scut.patent.dao;

import java.util.List;
import org.hibernate.Query;
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
		session.clear();
		save(session, (Object) data);
	}

	/**
	 * 根据i获取与之相关的所有距离
	 * 
	 * @param session
	 * @param i
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Double> getAllDistanceFromI(Session session, int i) {
		session.beginTransaction();
		Query query = session
				.createQuery("select distance from Data where i = " + i);
		session.getTransaction().commit();
		List<Double> list = query.list();
		return list;
	}
}
