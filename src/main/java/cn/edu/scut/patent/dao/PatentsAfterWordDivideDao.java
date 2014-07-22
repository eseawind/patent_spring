package cn.edu.scut.patent.dao;

import java.util.List;
import org.hibernate.Query;
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
	 * 获取表格长度
	 * 
	 * @param session
	 * @return
	 */
	public int getTableSize(Session session) {
		return getTableSize(session, "PATENTS_AFTER_WORD_DIVIDE");
	}

	/**
	 * 保存
	 * 
	 * @param session
	 * @param patentsAfterWordDivide
	 */
	public void save(Session session,
			PatentsAfterWordDivide patentsAfterWordDivide) {
		save(session, (Object) patentsAfterWordDivide);
	}

	/**
	 * 获取所有的PatentsAfterWordDivide数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PatentsAfterWordDivide> getAllPatentsAfterWordDivide(
			Session session) {
		session.beginTransaction();
		Query query = session.createQuery("from PatentsAfterWordDivide");
		session.getTransaction().commit();
		List<PatentsAfterWordDivide> list = query.list();
		return list;
	}
}
