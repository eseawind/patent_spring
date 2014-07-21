package cn.edu.scut.patent.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import cn.edu.scut.patent.dao.WordInfoDao;
import cn.edu.scut.patent.model.WordInfo;

public class WordInfoService extends TotalService {

	/**
	 * 检查表是否为空
	 * 
	 * @return
	 */
	public static Boolean isEmpty() {
		return WordInfoDao.isEmpty(session);
	}

	/**
	 * 清空表
	 */
	public static void cleanTable() {
		WordInfoDao.cleanTable(session);
	}
	
	/**
	 * 保存
	 * 
	 * @param wordInfo
	 */
	public void save(WordInfo wordInfo) {
		new WordInfoDao().save(session, wordInfo);
	}
	
	/**
	 * 根据结果集读取并返回
	 * 
	 * @param rs
	 * @return
	 */
	public WordInfo read(ResultSet rs) {
		WordInfo wordInfo = new WordInfo();
		try {
			wordInfo.setWord(rs.getString("WORD"));
			wordInfo.setMaxTf(rs.getInt("MAX_TF"));
			wordInfo.setDf(rs.getInt("DF"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wordInfo;
	}
}
