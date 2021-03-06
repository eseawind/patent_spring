package cn.edu.scut.patent.service;

import java.util.List;
import cn.edu.scut.patent.dao.AccountDao;
import cn.edu.scut.patent.model.Account;

public class AccountService extends TotalService {

	/**
	 * 检查表是否为空
	 * 
	 * @return
	 */
	public static Boolean isEmpty() {
		return AccountDao.isEmpty(session);
	}

	/**
	 * 清空表
	 */
	public static void cleanTable() {
		AccountDao.cleanTable(session);
	}

	/**
	 * 保存
	 * 
	 * @param account
	 */
	public void save(Account account) {
		new AccountDao().save(session, account);
	}

	/**
	 * 查找
	 * 
	 * @param email
	 * @return
	 */
	public Account find(String email) {
		return new AccountDao().find(session, email);
	}

	/**
	 * 检查是否已经通过审核
	 * 
	 * @param email
	 * @return
	 */
	public Boolean isPass(String email) {
		Account account = new AccountDao().find(session, email);
		if (account != null) {
			String pass = account.getPass();
			if (pass.equals("0")) {
				return false;
			} else if (pass.equals("2")) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 更新
	 * 
	 * @param account
	 */
	public void update(Account account) {
		new AccountDao().update(session, account);
	}

	/**
	 * 根据pass获取没有通过审核的账户
	 * 
	 * @return
	 */
	public List<Account> getUncheckAccount() {
		return new AccountDao().getUncheckAccount(session);
	}
}
