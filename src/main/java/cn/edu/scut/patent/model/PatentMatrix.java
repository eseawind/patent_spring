package cn.edu.scut.patent.model;

/**
 * 用于记录每一个专利的特征词的权重
 * 
 * @author CJX
 * 
 */
public class PatentMatrix {

	public String pttNum;
	// 记录20个特征词的权重
	public double[] value = new double[20];
}
