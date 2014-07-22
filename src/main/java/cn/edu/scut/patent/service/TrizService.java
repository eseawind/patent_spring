package cn.edu.scut.patent.service;

import java.util.ArrayList;
import java.util.List;
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

	/**
	 * 获取所有的TRIZ
	 * 
	 * @return
	 */
	public List<Triz> getAllTriz() {
		return new TrizDao().getAllTriz(session);
	}

	/**
	 * 获取所有的TRIZ
	 * 
	 * @return
	 */
	public List<String> getAllTrizString() {
		List<Triz> list = getAllTriz();
		List<String> result = new ArrayList<String>();
		for (Triz triz : list) {
			result.add(triz.getTrizNum() + triz.getTrizText());
		}
		return result;
	}
}
