package cn.edu.scut.patent.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * 提取专利表中部分的分词数据
 * 
 * @author CJX
 * 
 */
@SuppressWarnings("serial")
public class PatentsAfterWordDivide implements Serializable {

	// 专利编号
	private String pttNum;
	// 已分词的专利名称
	private String pttNameDivided;
	// 专利公开日
	private Date pttDate;
	// 商业方法类下的分类号
	private String classNumG06Q;
	// 已分词的专利概述
	private String pttAbstractDivided;
	// 已分词的专利内容
	private String pttContentDivided = "testing";

	public String getPttNum() {
		return pttNum;
	}

	public void setPttNum(String pttNum) {
		this.pttNum = pttNum;
	}

	public String getPttNameDivided() {
		return pttNameDivided;
	}

	public void setPttNameDivided(String pttNameDivided) {
		this.pttNameDivided = pttNameDivided;
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

	public String getPttAbstractDivided() {
		return pttAbstractDivided;
	}

	public void setPttAbstractDivided(String pttAbstractDivided) {
		this.pttAbstractDivided = pttAbstractDivided;
	}

	public String getPttContentDivided() {
		return pttContentDivided;
	}

	public void setPttContentDivided(String pttContentDivided) {
		this.pttContentDivided = pttContentDivided;
	}
}
