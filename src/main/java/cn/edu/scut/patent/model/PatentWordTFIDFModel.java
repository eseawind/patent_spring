package cn.edu.scut.patent.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 存储每一个专利的词汇及其TF和DF数据
 * 
 * @author CJX
 * 
 */
public class PatentWordTFIDFModel {

	private int id;
	private String pttNum = "";
	private String word = "";
	private int tf = 1;
	private int df = 1;
	private int flag = 0; // 1标题 0摘要

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPttNum() {
		return pttNum;
	}

	public void setPttNum(String pttNum) {
		this.pttNum = pttNum;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getTf() {
		return tf;
	}

	public void setTf(int tf) {
		this.tf = tf;
	}

	public int getDf() {
		return df;
	}

	public void setDf(int df) {
		this.df = df;
	}

	/**
	 * 根据结果集生成PatentsWordTFIDFModel并返回
	 * 
	 * @param rs
	 * @return
	 */
	public PatentWordTFIDFModel read(ResultSet rs) {
		try {
			setId(rs.getInt("ID"));
			setPttNum(rs.getString("PTT_NUM"));
			setWord(rs.getString("WORD"));
			setTf(rs.getInt("TF"));
			setDf(rs.getInt("DF"));
			setFlag(rs.getInt("FLAG"));

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
			String sql = "INSERT INTO patent_word_tf_df (PTT_NUM,WORD,TF,DF,FLAG) VALUES ("
					+ "'"
					+ getPttNum()
					+ "','"
					+ getWord()
					+ "',"
					+ getTf()
					+ "," + getDf() + "," + getFlag() + ")";
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
}
