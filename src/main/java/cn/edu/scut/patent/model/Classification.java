package cn.edu.scut.patent.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Classification implements Serializable {

	// 专利编号
	private String pttNum;
	// TRIZ编号
	private int trizNum;

	public Classification(){
		
	}
	
	public Classification(String pttNum, int trizNum){
		this.pttNum = pttNum;
		this.trizNum = trizNum;
	}
	
	public String getPttNum() {
		return pttNum;
	}

	public void setPttNum(String pttNum) {
		this.pttNum = pttNum;
	}

	public int getTrizNum() {
		return trizNum;
	}

	public void setTrizNum(int trizNum) {
		this.trizNum = trizNum;
	}
}
