package cn.edu.scut.patent.model;

import java.io.Serializable;

/**
 * 保存所有词的最大词频MAX_TF及文档频数DF
 * 
 * @author CJX
 * 
 */
@SuppressWarnings("serial")
public class WordInfo implements Serializable {

	// 词汇
	private String word;
	// 最大词频
	private int maxTf;
	// 文档频数
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
}
