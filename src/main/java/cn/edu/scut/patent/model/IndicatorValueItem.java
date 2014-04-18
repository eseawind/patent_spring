package cn.edu.scut.patent.model;

/**
 * 用于记录指标的每个单位的数据
 * 
 * @author CJX
 * 
 */
public class IndicatorValueItem {

	public String year;
	public double value;

	public IndicatorValueItem(String year, double value) {
		this.year = year;
		this.value = value;
	}
}
