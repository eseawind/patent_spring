package cn.edu.scut.patent.model;

import java.io.Serializable;

/**
 * 停用词表
 * 
 * @author Vincent_Melancholy
 * 
 */
@SuppressWarnings("serial")
public class Stopword implements Serializable {

	// 停用词
	private String word;
	// 标记
	private int flag;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
