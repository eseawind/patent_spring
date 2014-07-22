package cn.edu.scut.patent.service;

import java.util.ArrayList;
import java.util.List;
import cn.edu.scut.patent.dao.ClassificationDao;
import cn.edu.scut.patent.model.Classification;
import cn.edu.scut.patent.util.StringHelper;

public class ClassificationService extends TotalService {

	/**
	 * 检查表是否为空
	 * 
	 * @return
	 */
	public static Boolean isEmpty() {
		return ClassificationDao.isEmpty(session);
	}

	/**
	 * 清空表
	 */
	public static void cleanTable() {
		ClassificationDao.cleanTable(session);
	}

	/**
	 * 保存
	 * 
	 * @param classification
	 */
	public void save(Classification classification) {
		new ClassificationDao().save(session, classification);
	}

	/**
	 * 删除
	 * 
	 * @param classification
	 */
	public void delete(Classification classification) {
		new ClassificationDao().delete(session, classification);
	}

	/**
	 * 更新Classification号码
	 * 
	 * @param PTT_NUM
	 * @param TRIZ_NUM
	 * @return
	 */
	public Boolean updateClassificationNumber(String PTT_NUM, String[] TRIZ_NUM) {
		if (TRIZ_NUM.length < 1) {
			return false;
		}
		Classification classification = new Classification();
		classification.setPttNum(PTT_NUM);
		delete(classification);
		for (int i = 0; i < TRIZ_NUM.length; i++) {
			save(new Classification(
					StringHelper.replaceSpecialCharacters(PTT_NUM),
					Integer.parseInt(TRIZ_NUM[i])));
		}
		return true;
	}

	/**
	 * CLASSIFICATION统计所有TRIZ的个数
	 * 
	 * @return
	 */
	public List<String> getCount() {
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= 40; i++) {
			list.add(String.valueOf(getCount(i)));
		}
		return list;
	}

	/**
	 * CLASSIFICATION统计单个TRIZ的个数
	 * 
	 * @return
	 */
	public int getCount(int trizNum) {
		return new ClassificationDao().getCount(session, trizNum);
	}
}
