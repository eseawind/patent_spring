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

	/**
	 * 获取具体专利文献的聚类数据，返回该专利的聚类号；如果不存在就返回-1
	 * 
	 * @return
	 */
	public int find(String pttNum) {
		if (isEmpty()) {
			return -1;
		} else {
			return new PatentClusterDao().find(session, pttNum);
		}
	}
}
