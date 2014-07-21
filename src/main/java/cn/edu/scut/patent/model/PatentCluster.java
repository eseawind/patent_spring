package cn.edu.scut.patent.model;

import java.io.Serializable;

/**
 * 专利聚类表
 * 
 * @author Vincent_Melancholy
 * 
 */
@SuppressWarnings("serial")
public class PatentCluster implements Serializable {

	// 专利编号
	private String pttNum;
	// 专利聚类号
	private int cluster;

	public String getPttNum() {
		return pttNum;
	}

	public void setPttNum(String pttNum) {
		this.pttNum = pttNum;
	}

	public int getCluster() {
		return cluster;
	}

	public void setCluster(int cluster) {
		this.cluster = cluster;
	}
}
