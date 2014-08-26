package cn.edu.scut.patent.model;

import java.io.Serializable;
import java.sql.Date;

@SuppressWarnings("serial")
public class PatentClassification implements Serializable {

	// 专利名称
	private String pttName;
	// 专利公开号（主键）
	private String pttNum;
	// 申请号
	private String applyNum;
	// 专利类型
	private String pttType;
	// 专利公开日
	private Date pttDate;
	// 商业方法类下的分类号
	private String classNumG06Q;
	// TRIZ编号
	private String trizNum;

	public String getPttName() {
		return pttName;
	}

	public void setPttName(String pttName) {
		this.pttName = pttName;
	}

	public String getPttNum() {
		return pttNum;
	}

	public void setPttNum(String pttNum) {
		this.pttNum = pttNum;
	}

	public String getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(String applyNum) {
		this.applyNum = applyNum;
	}

	public String getPttType() {
		return pttType;
	}

	public void setPttType(String pttType) {
		this.pttType = pttType;
	}

	public Date getPttDate() {
		return pttDate;
	}

	public void setPttDate(Date pttDate) {
		this.pttDate = pttDate;
	}

	public String getClassNumG06Q() {
		return classNumG06Q;
	}

	public void setClassNumG06Q(String classNumG06Q) {
		this.classNumG06Q = classNumG06Q;
	}

	public String getTrizNum() {
		return trizNum;
	}

	public void setTrizNum(String trizNum) {
		this.trizNum = trizNum;
	}
}
