package cn.edu.scut.patent.service;

import java.util.List;
import cn.edu.scut.patent.dao.DataDao;
import cn.edu.scut.patent.model.Data;

public class DataService extends TotalService {

	/**
	 * 检查表是否为空
	 * 
	 * @return
	 */
	public static Boolean isEmpty() {
		return DataDao.isEmpty(session);
	}

	/**
	 * 清空表
	 */
	public static void cleanTable() {
		DataDao.cleanTable(session);
	}

	/**
	 * 保存
	 * 
	 * @param data
	 */
	public void save(Data data) {
		new DataDao().save(session, data);
	}

	/**
	 * 根据i获取与之相关的所有距离
	 * 
	 * @param i
	 * @return
	 */
	public List<Double> getAllDistanceFromI(int i) {
		return new DataDao().getAllDistanceFromI(session, i);
	}
}
