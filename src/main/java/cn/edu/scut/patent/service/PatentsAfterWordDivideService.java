package cn.edu.scut.patent.service;

import java.sql.ResultSet;
import java.sql.SQLException;
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
	 * 保存
	 * 
	 * @param patentsAfterWordDivide
	 */
	public void save(PatentsAfterWordDivide patentsAfterWordDivide) {
		new PatentsAfterWordDivideDao().save(session, patentsAfterWordDivide);
	}
	
	/**
	 * 根据结果集生成PatentsAfterWordDivide并返回
	 * 
	 * @param rs
	 * @return
	 */
	public PatentsAfterWordDivide read(ResultSet rs) {
		PatentsAfterWordDivide patentsAfterWordDivide = new PatentsAfterWordDivide();
		try {
			patentsAfterWordDivide.setPttNum(rs.getString("PTT_NUM"));
			patentsAfterWordDivide.setPttDate(rs.getDate("PTT_DATE"));
			patentsAfterWordDivide.setClassNumG06Q(rs
					.getString("CLASS_NUM_G06Q"));
			patentsAfterWordDivide.setPttNameDivided(rs
					.getString("PTT_NAME_DIVIDED"));
			patentsAfterWordDivide.setPttAbstractDivided(rs
					.getString("PTT_ABSTRACT_DIVIDED"));
			patentsAfterWordDivide.setPttContentDivided(rs
					.getString("PTT_CONTENT_DIVIDED"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return patentsAfterWordDivide;
	}
}
