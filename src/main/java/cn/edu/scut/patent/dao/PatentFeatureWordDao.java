package cn.edu.scut.patent.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import cn.edu.scut.patent.model.PatentFeatureWord;

public class PatentFeatureWordDao extends TotalDao {

	/**
	 * 检查表是否为空
	 * 
	 * @param session
	 */
	public static Boolean isEmpty(Session session) {
		return isEmpty(session, "PATENT_FEATURE_WORD");
	}

	/**
	 * 清空表
	 * 
	 * @param session
	 */
	public static void cleanTable(Session session) {
		cleanTable(session, "PATENT_FEATURE_WORD");
	}

	/**
	 * 保存
	 * 
	 * @param session
	 * @param patentFeatureWord
	 */
	public void save(Session session, PatentFeatureWord patentFeatureWord) {
		save(session, (Object) patentFeatureWord);
	}

	/**
	 * 更新
	 * 
	 * @param session
	 * @param patentFeatureWord
	 */
	public void update(Session session, PatentFeatureWord patentFeatureWord) {
		update(session, (Object) patentFeatureWord);
	}

	/**
	 * 获取所有的pttNum
	 * 
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllPttNum(Session session) {
		session.beginTransaction();
		Query query = session
				.createQuery("select distinct pttNum from PatentFeatureWord");
		session.getTransaction().commit();
		List<String> list = query.list();
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
	public List<PatentFeatureWord> getAllFromPttNum(Session session,
			String pttNum) {
		session.beginTransaction();
		Query query = session
				.createQuery("from PatentFeatureWord where pttNum = '" + pttNum
						+ "'");
		session.getTransaction().commit();
		List<PatentFeatureWord> list = query.list();
		return list;
	}
}
