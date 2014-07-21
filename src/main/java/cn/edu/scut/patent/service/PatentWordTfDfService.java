package cn.edu.scut.patent.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import cn.edu.scut.patent.dao.PatentWordTfDfDao;
import cn.edu.scut.patent.model.PatentWordTfDf;

public class PatentWordTfDfService extends TotalService {

	/**
	 * 检查表是否为空
	 * 
	 * @return
	 */
	public static Boolean isEmpty() {
		return PatentWordTfDfDao.isEmpty(session);
	}

	/**
	 * 清空表
	 */
	public static void cleanTable() {
		PatentWordTfDfDao.cleanTable(session);
	}
	
	/**
	 * 保存
	 * 
	 * @param patentWordTfDf
	 */
	public void save(PatentWordTfDf patentWordTfDf) {
		new PatentWordTfDfDao().save(session, patentWordTfDf);
	}
	
	/**
	 * 根据结果集生成PatentsWordTFIDF并返回
	 * 
	 * @param rs
	 * @return
	 */
	public PatentWordTfDf read(ResultSet rs) {
		PatentWordTfDf patentWordTfDf = new PatentWordTfDf();
		try {
			patentWordTfDf.setId(rs.getInt("ID"));
			patentWordTfDf.setPttNum(rs.getString("PTT_NUM"));
			patentWordTfDf.setWord(rs.getString("WORD"));
			patentWordTfDf.setTf(rs.getInt("TF"));
			patentWordTfDf.setDf(rs.getInt("DF"));
			patentWordTfDf.setFlag(rs.getInt("FLAG"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return patentWordTfDf;
	}
}
