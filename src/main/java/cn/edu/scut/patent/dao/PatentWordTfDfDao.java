package cn.edu.scut.patent.dao;

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
}
