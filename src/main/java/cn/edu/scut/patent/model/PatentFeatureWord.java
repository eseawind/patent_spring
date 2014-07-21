package cn.edu.scut.patent.model;

import java.io.Serializable;

/**
 * 特征词权重表，经过特征提取之后的专利特征词及其权重
 * 
 * @author CJX
 * 
 */
@SuppressWarnings("serial")
public class PatentFeatureWord implements Serializable {

	// 自增长ID
	private int id;
	// 专利编号
	private String pttNum;
	// 特征词
	private String featureWord;
	// TF-IDF词频统计算法求出的权重
	private double tfIdfValue;
	// TF-IDF词频统计算法求出的权重后的标准化数值
	private double tfIdfValueStandard;

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

	public String getFeatureWord() {
		return featureWord;
	}

	public void setFeatureWord(String featureWord) {
		this.featureWord = featureWord;
	}

	public double getTfIdfValue() {
		return tfIdfValue;
	}

	public void setTfIdfValue(double tfIdfValue) {
		this.tfIdfValue = tfIdfValue;
	}

	public double getTfIdfValueStandard() {
		return tfIdfValueStandard;
	}

	public void setTfIdfValueStandard(double tfIdfValueStandard) {
		this.tfIdfValueStandard = tfIdfValueStandard;
	}

	public PatentFeatureWord() {

	}

	public PatentFeatureWord(String pttNum, String featureWord,
			double tfIdfValue) {
		setPttNum(pttNum);
		setFeatureWord(featureWord);
		setTfIdfValue(tfIdfValue);
	}
}
