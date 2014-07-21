package cn.edu.scut.patent.dao;

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
}
