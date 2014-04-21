package cn.edu.scut.patent.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import cn.edu.scut.patent.util.DatabaseHelper;

/**
 * 保存所有词的最大词频MAX_TF及文档频数DF
 * 
 * @author CJX
 * 
 */
public class WordInfoModel {

	private String word;
	private int maxTf;
	private int df;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getMaxTf() {
		return maxTf;
	}

	public void setMaxTf(int maxTf) {
		this.maxTf = maxTf;
	}

	public int getDf() {
		return df;
	}

	public void setDf(int df) {
		this.df = df;
	}

	/**
	 * 根据结果集读取this并返回
	 * 
	 * @param rs
	 * @return
	 */
	public WordInfoModel read(ResultSet rs) {
		try {

			setWord(rs.getString("WORD"));
			setMaxTf(rs.getInt("MAX_TF"));
			setDf(rs.getInt("DF"));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return this;
	}

	/**
	 * 将此记录写入数据库
	 * 
	 * @param con
	 */
	public void write(Connection con) {
		Statement sta = null;
		try {
			sta = con.createStatement();
			String sql = "INSERT INTO t_word_info (WORD,MAX_TF,DF) VALUES ("
					+ "'" + getWord() + "'," + getMaxTf() + "," + getDf() + ")";
			sta.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (sta != null) {
					sta.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void write() {
		Connection con = null;
		try {
			con = DatabaseHelper.getConnection();
			write(con);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
