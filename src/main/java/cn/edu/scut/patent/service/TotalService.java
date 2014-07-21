package cn.edu.scut.patent.service;

import hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class TotalService {

	public static Session session;

	static {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}

	/**
	 * 关闭session
	 */
	public static void close() {
		session.close();
	}
}
