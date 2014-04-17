package cn.edu.scut.patent.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 提取专利表中部分的分词数据
 * 
 * @author CJX
 * 
 */
public class PatentsAfterWordDivideModel {

	private String ptt_num;
	private String ptt_name;
	private Date ptt_date;
	private String class_num_g06q;
	private String ptt_abstract;
	private String ptt_content;

	public String getPtt_num() {
		return ptt_num;
	}

	public void setPtt_num(String ptt_num) {
		this.ptt_num = ptt_num;
	}

	public String getPtt_name() {
		return ptt_name;
	}

	public void setPtt_name(String ptt_name) {
		this.ptt_name = ptt_name;
	}

	public Date getPtt_date() {
		return ptt_date;
	}

	public void setPtt_date(Date ptt_date) {
		this.ptt_date = ptt_date;
	}

	public String getClass_num_g06q() {
		return class_num_g06q;
	}

	public void setClass_num_g06q(String class_num_g06q) {
		this.class_num_g06q = class_num_g06q;
	}

	public String getPtt_abstract() {
		return ptt_abstract;
	}

	public void setPtt_abstract(String ptt_abstract) {
		this.ptt_abstract = ptt_abstract;
	}

	public String getPtt_content() {
		return ptt_content;
	}

	public void setPtt_content(String content) {
		this.ptt_content = content;
	}

	/**
	 * 根据结果集生成PatentsAfterWordDivideModel并返回
	 * 
	 * @param rs
	 * @return
	 */
	public PatentsAfterWordDivideModel read(ResultSet rs) {
		try {
			setPtt_num(rs.getString("PTT_NUM"));
			setPtt_date(rs.getDate("PTT_DATE"));
			setClass_num_g06q(rs.getString("CLASS_NUM_G06Q"));
			setPtt_name(rs.getString("PTT_NAME_DIVIDED"));
			setPtt_abstract(rs.getString("PTT_ABSTRACT_DIVIDED"));
			setPtt_content(rs.getString("PTT_CONTENT_DIVIDED"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}
}
