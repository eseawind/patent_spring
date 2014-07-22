package cn.edu.scut.patent.dao;

import org.hibernate.Session;
import cn.edu.scut.patent.model.PatentCluster;

public class PatentClusterDao extends TotalDao {

	/**
	 * 检查表是否为空
	 * 
	 * @param session
	 */
	public static Boolean isEmpty(Session session) {
		return isEmpty(session, "PATENT_CLUSTER");
	}

	/**
	 * 清空表
	 * 
	 * @param session
	 */
	public static void cleanTable(Session session) {
		cleanTable(session, "PATENT_CLUSTER");
	}

	/**
	 * 保存
	 * 
	 * @param session
	 * @param patentCluster
	 */
	public void save(Session session, PatentCluster patentCluster) {
		save(session, (Object) patentCluster);
	}

	/**
	 * 获取具体专利文献的聚类数据，返回该专利的聚类号；如果不存在则返回-1
	 * 
	 * @param session
	 * @param pttNum
	 * @return
	 */
	public int find(Session session, String pttNum) {
		session.beginTransaction();
		PatentCluster patentCluster = (PatentCluster) session.get(
				PatentCluster.class, pttNum);
		session.getTransaction().commit();
		if (patentCluster == null) {
			return -1;
		} else {
			return patentCluster.getCluster();
		}
	}
}
