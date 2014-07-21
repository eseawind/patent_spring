package cn.edu.scut.patent.service;

import cn.edu.scut.patent.dao.PatentClusterDao;
import cn.edu.scut.patent.model.PatentCluster;

public class PatentClusterService extends TotalService {

	/**
	 * 检查表是否为空
	 * 
	 * @return
	 */
	public static Boolean isEmpty() {
		return PatentClusterDao.isEmpty(session);
	}

	/**
	 * 清空表
	 */
	public static void cleanTable() {
		PatentClusterDao.cleanTable(session);
	}
	
	/**
	 * 保存
	 * 
	 * @param patentCluster
	 */
	public void save(PatentCluster patentCluster) {
		new PatentClusterDao().save(session, patentCluster);
	}
}
