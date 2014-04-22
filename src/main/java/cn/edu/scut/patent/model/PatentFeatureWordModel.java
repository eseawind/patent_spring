package cn.edu.scut.patent.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import cn.edu.scut.patent.util.DatabaseHelper;

/**
 * 经过特征提取之后的专利特征词及其权重
 * 
 * @author CJX
 * 
 */
public class PatentFeatureWordModel {

	private int id;
	private String pttnum;
	private String featureWord;
	private double tfidfValue;
	private double tfidfValueStandard;

	public double getTfidfValueStandard() {
		return tfidfValueStandard;
	}

	public void setTfidfValueStandard(double tfidfValueStandard) {
		this.tfidfValueStandard = tfidfValueStandard;
	}

	public PatentFeatureWordModel() {
	}

	public PatentFeatureWordModel(String pttnum, String featureWord,
			double tfidfValue) {
		setPttnum(pttnum);
		setFeatureWord(featureWord);
		setTfidfValue(tfidfValue);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPttnum() {
		return pttnum;
	}

	public void setPttnum(String pttnum) {
		this.pttnum = pttnum;
	}

	public String getFeatureWord() {
		return featureWord;
	}

	public void setFeatureWord(String featureWord) {
		this.featureWord = featureWord;
	}

	public double getTfidfValue() {
		return tfidfValue;
	}

	public void setTfidfValue(double tfidfValue) {
		this.tfidfValue = tfidfValue;
	}

	/**
	 * 根据结果集生成PatentsWordTFIDFModel并返回
	 * 
	 * @param rs
	 * @return
	 */
	public PatentFeatureWordModel read(ResultSet rs) {
		try {

			setId(rs.getInt("ID"));
			setPttnum(rs.getString("PTT_NUM"));
			setFeatureWord(rs.getString("FEATURE_WORD"));
			setTfidfValue(rs.getDouble("TFIDF_VALUE"));
			setTfidfValueStandard(rs.getDouble("TFIDF_VALUE_STANDARD"));

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
			String sql = "INSERT INTO patent_feature_word (PTT_NUM,FEATURE_WORD,TFIDF_VALUE,TFIDF_VALUE_STANDARD) VALUES ("
					+ "'"
					+ getPttnum()
					+ "','"
					+ getFeatureWord()
					+ "',"
					+ getTfidfValue() + "," + getTfidfValueStandard() + ")";
			System.out.println(sql);
			sta.execute(sql);
		} catch (Exception e) {
			// System.out.println(getPttnum() + getFeatureWord() +
			// getTfidfValue());
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

	/**
	 * 修改规范化后的TF-IDF值
	 * 
	 */
	public void updateTfidfValueStandard() {
		Connection con = null;
		Statement sta = null;
		try {
			con = DatabaseHelper.getConnection();
			sta = con.createStatement();
			sta.execute("update patent_feature_word SET TFIDF_VALUE_STANDARD = "
					+ getTfidfValueStandard() + " WHERE ID=" + getId());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (sta != null) {
					sta.close();
				}
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
