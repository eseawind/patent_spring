package hibernate.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static Configuration configuration;

	static {
		/*
		 * Hibernate4新增了一个接口ServiceRegistry，
		 * 所有基于Hibernate的配置或者服务都必须统一向这个ServiceRegistry注册后才能生效
		 * 。所以不难看出，Hibernate4的配置入口不再是Configuration对象
		 * ，而是ServiceRegistry对象，Configuration对象将通过ServiceRegistry对象获取hibernate
		 * .cfg.xml的配置信息。
		 */
		configuration = new Configuration().configure();
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	}

	/**
	 * 获取SessionFactory
	 * 
	 * @return
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * 关闭SessionFactory
	 */
	public static void close() {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}
}
