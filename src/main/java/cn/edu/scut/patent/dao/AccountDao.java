package cn.edu.scut.patent.dao;

import java.util.List;
import org.hibernate.Query;
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

	/**
	 * 更新
	 * 
	 * @param session
	 * @param account
	 */
	public void update(Session session, Account account) {
		update(session, (Object) account);
	}

	/**
	 * 根据pass获取没有通过审核的账户
	 * 
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Account> getUncheckAccount(Session session) {
		session.beginTransaction();
		Query query = session.createQuery("from Account where pass = '0'");
		session.getTransaction().commit();
		List<Account> list = query.list();
		return list;
	}
}
