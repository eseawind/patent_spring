package cn.edu.scut.patent.dao;

import java.util.List;
import org.hibernate.Query;
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

	/**
	 * 获取表格中所有的数据
	 * 
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WordInfo> getAll(Session session) {
		session.beginTransaction();
		Query query = session.createQuery("from WordInfo");
		session.getTransaction().commit();
		List<WordInfo> list = query.list();
		return list;
	}
}
