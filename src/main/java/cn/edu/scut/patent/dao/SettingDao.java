package cn.edu.scut.patent.dao;

import org.hibernate.Session;
import cn.edu.scut.patent.model.Setting;

public class SettingDao extends TotalDao {

	/**
	 * 检查表是否为空
	 * 
	 * @param session
	 */
	public static Boolean isEmpty(Session session) {
		return isEmpty(session, "SETTING");
	}

	/**
	 * 清空表
	 * 
	 * @param session
	 */
	public static void cleanTable(Session session) {
		cleanTable(session, "SETTING");
	}

	/**
	 * 保存
	 * 
	 * @param session
	 * @param setting
	 */
	public void save(Session session, Setting setting) {
		save(session, (Object) setting);
	}

	/**
	 * 查找
	 * 
	 * @param session
	 * @param function
	 * @return
	 */
	public Setting find(Session session, String function) {
		session.beginTransaction();
		Setting setting = (Setting) session.get(Setting.class, function);
		session.getTransaction().commit();
		return setting;
	}

	/**
	 * 更新
	 * 
	 * @param session
	 * @param setting
	 */
	public void update(Session session, Setting setting) {
		update(session, (Object) setting);
	}
}
