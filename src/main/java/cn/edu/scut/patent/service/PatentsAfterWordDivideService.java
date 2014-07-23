package cn.edu.scut.patent.service;

import java.util.List;
import cn.edu.scut.patent.dao.PatentsAfterWordDivideDao;
import cn.edu.scut.patent.model.PatentsAfterWordDivide;

public class PatentsAfterWordDivideService extends TotalService {

	/**
	 * 检查表是否为空
	 * 
	 * @return
	 */
	public static Boolean isEmpty() {
		return PatentsAfterWordDivideDao.isEmpty(session);
	}

	/**
	 * 清空表
	 */
	public static void cleanTable() {
		PatentsAfterWordDivideDao.cleanTable(session);
	}

	/**
	 * 获取表格长度
	 * 
	 * @return
	 */
	public int getTableSize() {
		return new PatentsAfterWordDivideDao().getTableSize(session);
	}

	/**
	 * 保存
	 * 
	 * @param patentsAfterWordDivide
	 */
	public void save(PatentsAfterWordDivide patentsAfterWordDivide) {
		new PatentsAfterWordDivideDao().save(session, patentsAfterWordDivide);
	}

	/**
	 * 获取所有的PatentsAfterWordDivide数据
	 * 
	 * @return
	 */
	public List<PatentsAfterWordDivide> getAllPatentsAfterWordDivide() {
		return new PatentsAfterWordDivideDao()
				.getAllPatentsAfterWordDivide(session);
	}
}
