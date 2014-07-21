package cn.edu.scut.patent.model;

import java.io.Serializable;

/**
 * 词性标注表
 * 
 * @author Vincent_Melancholy
 * 
 */
@SuppressWarnings("serial")
public class WordSmark implements Serializable {

	// 标记
	private String wordSmark;
	// 备注
	private String remark;
	// 是否过滤
	private int flag;

	public String getWordSmark() {
		return wordSmark;
	}

	public void setWordSmark(String wordSmark) {
		this.wordSmark = wordSmark;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
