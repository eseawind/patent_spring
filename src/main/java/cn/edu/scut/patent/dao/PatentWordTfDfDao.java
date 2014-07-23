package cn.edu.scut.patent.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import cn.edu.scut.patent.model.PatentWordTfDf;

public class PatentWordTfDfDao extends TotalDao {

	/**
	 * 检查表是否为空
	 * 
	 * @param session
	 */
	public static Boolean isEmpty(Session session) {
		return isEmpty(session, "PATENT_WORD_TF_DF");
	}

	/**
	 * 清空表
	 * 
	 * @param session
	 */
	public static void cleanTable(Session session) {
		cleanTable(session, "PATENT_WORD_TF_DF");
	}

	/**
	 * 保存
	 * 
	 * @param session
	 * @param patentWordTfDf
	 */
	public void save(Session session, PatentWordTfDf patentWordTfDf) {
		save(session, (Object) patentWordTfDf);
	}

	/**
	 * 更新
	 * 
	 * @param session
	 * @param patentWordTfDf
	 */
	public void update(Session session, PatentWordTfDf patentWordTfDf) {
		update(session, (Object) patentWordTfDf);
	}

	/**
	 * 获取表格中所有的word
	 * 
	 * @param session
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllWord(Session session) {
		session.beginTransaction();
		Query query = session
				.createQuery("select distinct word from PatentWordTfDf");
		session.getTransaction().commit();
		List<String> list = query.list();
		return list;
	}

	/**
	 * 获取表格中所有的pttNum
	 * 
	 * @param session
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllPttNum(Session session) {
		session.beginTransaction();
		Query query = session
				.createQuery("select distinct pttNum from PatentWordTfDf");
		session.getTransaction().commit();
		List<String> list = query.list();
		return list;
	}

	/**
	 * 根据word获取表格符合要求的pttNum
	 * 
	 * @param session
	 * @param word
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllPttNumFromWord(Session session, String word) {
		session.beginTransaction();
		Query query = session
				.createQuery("select distinct pttNum from PatentWordTfDf where word = '"
						+ word + "'");
		session.getTransaction().commit();
		List<String> list = query.list();
		return list;
	}

	/**
	 * 根据word获取符合要求的数据
	 * 
	 * @param session
	 * @param word
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PatentWordTfDf> getAllFromWord(Session session, String word) {
		session.beginTransaction();
		Query query = session.createQuery("from PatentWordTfDf where word = '"
				+ word + "'");
		session.getTransaction().commit();
		List<PatentWordTfDf> list = query.list();
		return list;
	}

	/**
	 * 根据pttNum获取符合要求的数据
	 * 
	 * @param session
	 * @param pttNum
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PatentWordTfDf> getAllFromPttNum(Session session, String pttNum) {
		session.beginTransaction();
		Query query = session
				.createQuery("from PatentWordTfDf where pttNum = '" + pttNum
						+ "'");
		session.getTransaction().commit();
		List<PatentWordTfDf> list = query.list();
		return list;
	}
}
