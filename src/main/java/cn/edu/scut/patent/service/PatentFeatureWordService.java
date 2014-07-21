package cn.edu.scut.patent.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import cn.edu.scut.patent.dao.PatentFeatureWordDao;
import cn.edu.scut.patent.model.PatentFeatureWord;

public class PatentFeatureWordService extends TotalService {

	/**
	 * 检查表是否为空
	 * 
	 * @return
	 */
	public static Boolean isEmpty() {
		return PatentFeatureWordDao.isEmpty(session);
	}

	/**
	 * 清空表
	 */
	public static void cleanTable() {
		PatentFeatureWordDao.cleanTable(session);
	}

	/**
	 * 保存
	 * 
	 * @param patentFeatureWord
	 */
	public void save(PatentFeatureWord patentFeatureWord) {
		new PatentFeatureWordDao().save(session, patentFeatureWord);
	}

	/**
	 * 更新
	 * 
	 * @param patentFeatureWord
	 */
	public void update(PatentFeatureWord patentFeatureWord) {
		new PatentFeatureWordDao().update(session, patentFeatureWord);
	}
	
	/**
	 * 根据结果集生成PatentFeatureWord并返回
	 * 
	 * @param rs
	 * @return
	 */
	public PatentFeatureWord read(ResultSet rs) {
		PatentFeatureWord patentFeatureWord = new PatentFeatureWord();
		try {
			patentFeatureWord.setId(rs.getInt("ID"));
			patentFeatureWord.setPttNum(rs.getString("PTT_NUM"));
			patentFeatureWord.setFeatureWord(rs.getString("FEATURE_WORD"));
			patentFeatureWord.setTfIdfValue(rs.getDouble("TFIDF_VALUE"));
			patentFeatureWord.setTfIdfValueStandard(rs
					.getDouble("TFIDF_VALUE_STANDARD"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return patentFeatureWord;
	}
}
