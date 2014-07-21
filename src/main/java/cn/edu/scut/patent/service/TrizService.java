package cn.edu.scut.patent.service;

import cn.edu.scut.patent.dao.TrizDao;
import cn.edu.scut.patent.model.Triz;

public class TrizService extends TotalService {

	/**
	 * 检查表是否为空
	 * 
	 * @return
	 */
	public static Boolean isEmpty() {
		return TrizDao.isEmpty(session);
	}

	/**
	 * 清空表
	 */
	public static void cleanTable() {
		TrizDao.cleanTable(session);
	}

	/**
	 * 保存
	 * 
	 * @param triz
	 */
	public void save(Triz triz) {
		new TrizDao().save(session, triz);
	}

	/**
	 * 填充所有TRIZ
	 */
	public void fillTriz() {
		new TrizDao().fillTriz(session);
	}
}
