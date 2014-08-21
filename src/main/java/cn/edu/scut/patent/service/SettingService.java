package cn.edu.scut.patent.service;

import cn.edu.scut.patent.dao.SettingDao;
import cn.edu.scut.patent.model.Setting;

public class SettingService extends TotalService {

	/**
	 * 检查表是否为空
	 * 
	 * @return
	 */
	public static Boolean isEmpty() {
		return SettingDao.isEmpty(session);
	}

	/**
	 * 清空表
	 */
	public static void cleanTable() {
		SettingDao.cleanTable(session);
	}

	/**
	 * 保存
	 * 
	 * @param setting
	 */
	public void save(Setting setting) {
		new SettingDao().save(session, setting);
	}

	/**
	 * 查找
	 * 
	 * @param function
	 * @return
	 */
	public Setting find(String function) {
		return new SettingDao().find(session, function);
	}

	/**
	 * 更新
	 * 
	 * @param setting
	 */
	public void update(Setting setting) {
		new SettingDao().update(session, setting);
	}
}
