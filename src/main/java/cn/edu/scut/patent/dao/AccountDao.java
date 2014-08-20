package cn.edu.scut.patent.dao;

import org.hibernate.Session;
import cn.edu.scut.patent.model.Account;

public class AccountDao extends TotalDao {

	/**
	 * 检查表是否为空
	 * 
	 * @param session
	 */
	public static Boolean isEmpty(Session session) {
		return isEmpty(session, "ACCOUNT");
	}

	/**
	 * 清空表
	 * 
	 * @param session
	 */
	public static void cleanTable(Session session) {
		cleanTable(session, "ACCOUNT");
	}

	/**
	 * 保存
	 * 
	 * @param session
	 * @param account
	 */
	public void save(Session session, Account account) {
		save(session, (Object) account);
	}

	/**
	 * 查找
	 * 
	 * @param session
	 * @param email
	 * @return
	 */
	public Account find(Session session, String email) {
		session.beginTransaction();
		Account account = (Account) session.get(Account.class, email);
		session.getTransaction().commit();
		return account;
	}
}
