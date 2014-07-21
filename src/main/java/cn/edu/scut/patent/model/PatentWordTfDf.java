package cn.edu.scut.patent.model;

import java.io.Serializable;

/**
 * 专利特征词表，存储每一个专利的词汇及其TF和DF数据
 * 
 * @author CJX
 * 
 */
@SuppressWarnings("serial")
public class PatentWordTfDf implements Serializable {

	// 唯一标识ID
	private int id;
	// 专利编号
	private String pttNum = "";
	// 词汇
	private String word = "";
	// 词频
	private int tf = 1;
	// 文档频数
	private int df = 1;
	// 标识，1标题，0摘要
	private int flag = 0;

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

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
