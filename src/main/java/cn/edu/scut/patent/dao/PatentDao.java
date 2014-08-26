package cn.edu.scut.patent.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import cn.edu.scut.patent.model.Patent;
import cn.edu.scut.patent.model.PatentClassification;

public class PatentDao extends TotalDao {

	/**
	 * 检查表是否为空
	 * 
	 * @param session
	 */
	public static Boolean isEmpty(Session session) {
		return isEmpty(session, "PATENTS");
	}

	/**
	 * 清空表
	 * 
	 * @param session
	 */
	public static void cleanTable(Session session) {
		cleanTable(session, "PATENTS");
	}

	/**
	 * 保存
	 * 
	 * @param session
	 * @param patent
	 */
	public void save(Session session, Patent patent) {
		save(session, (Object) patent);
	}

	/**
	 * 获取所有的pttNum
	 * 
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllPttNum(Session session) {
		session.beginTransaction();
		Query query = session.createQuery("select pttNum from Patent");
		session.getTransaction().commit();
		List<String> list = query.list();
		return list;
	}

	/**
	 * 查找
	 * 
	 * @param session
	 * @param PTT_NUM
	 * @return
	 */
	public Patent find(Session session, String pttNum) {
		session.beginTransaction();
		Patent patent = (Patent) session.get(Patent.class, pttNum);
		session.getTransaction().commit();
		return patent;
	}

	/**
	 * 获取所有的专利数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Patent> getAllPatents(Session session) {
		session.beginTransaction();
		Query query = session.createQuery("from Patent");
		session.getTransaction().commit();
		List<Patent> list = query.list();
		return list;
	}

	/**
	 * 获取所有的专利数据,包含分类的数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PatentClassification> getAllPatentsWithClassification(
			Session session) {
		List<PatentClassification> list = new ArrayList<PatentClassification>();
		session.beginTransaction();
		Query query = session
				.createSQLQuery("select patent.PTT_NUM, patent.PTT_NAME, patent.APPLY_NUM, patent.PTT_TYPE, patent.PTT_DATE, patent.CLASS_NUM_G06Q, classification.TRIZ_NUM from PATENTS as patent left join CLASSIFICATION as classification on patent.PTT_NUM = classification.PTT_NUM");
		session.getTransaction().commit();
		List<Object[]> templist = query.list();

		for (int i = 0; i < templist.size(); i++) {
			Object[] object = templist.get(i);
			PatentClassification patentClassification = new PatentClassification();
			if (object[6] != null) {
				String trizNum = String.valueOf(object[6]);
				while (true) {
					if (((String) templist.get(i + 1)[0])
							.equals((String) object[0])) {
						trizNum += ", "
								+ String.valueOf(templist.get(i + 1)[6]);
						i++;
					} else {
						break;
					}
				}
				patentClassification.setTrizNum(trizNum);
			}
			patentClassification.setPttNum((String) object[0]);
			patentClassification.setPttName((String) object[1]);
			patentClassification.setApplyNum((String) object[2]);
			patentClassification.setPttType((String) object[3]);
			patentClassification.setPttDate((Date) object[4]);
			patentClassification.setClassNumG06Q((String) object[5]);
			list.add(patentClassification);
		}
		return list;
	}

	/**
	 * 获取PATENTS所有专利关键属性的数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Patent> getPatentsKey(Session session) {
		List<Patent> list = new ArrayList<Patent>();
		session.beginTransaction();
		Query query = session
				.createQuery("select pttNum, pttName, pttDate, classNumG06Q, pttAbstract from Patent");
		session.getTransaction().commit();
		List<Object[]> templist = query.list();
		for (Object[] object : templist) {
			Patent patent = new Patent();
			patent.setPttNum((String) object[0]);
			patent.setPttName((String) object[1]);
			patent.setPttDate((Date) object[2]);
			patent.setClassNumG06Q((String) object[3]);
			patent.setPttAbstract((String) object[4]);
			list.add(patent);
		}
		return list;
	}
}
