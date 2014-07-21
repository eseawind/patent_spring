package cn.edu.scut.patent.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Triz implements Serializable {

	// TRIZ编号
	private int trizNum;
	// TRIZ名称
	private String trizText;

	public int getTrizNum() {
		return trizNum;
	}

	public void setTrizNum(int trizNum) {
		this.trizNum = trizNum;
	}

	public String getTrizText() {
		return trizText;
	}

	public void setTrizText(String trizText) {
		this.trizText = trizText;
	}
}
