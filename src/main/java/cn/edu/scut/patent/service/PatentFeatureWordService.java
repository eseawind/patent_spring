package cn.edu.scut.patent.service;

import java.util.List;
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
	 * 获取所有的pttNum
	 * 
	 * @return
	 */
	public List<String> getAllPttNum() {
		return new PatentFeatureWordDao().getAllPttNum(session);
	}

	/**
	 * 根据pttNum获取符合要求的数据
	 * 
	 * @param pttNum
	 * @return
	 */
	public List<PatentFeatureWord> getAllFromPttNum(String pttNum) {
		return new PatentFeatureWordDao().getAllFromPttNum(session, pttNum);
	}
}
