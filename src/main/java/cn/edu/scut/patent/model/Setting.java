package cn.edu.scut.patent.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Setting implements Serializable {

	// 功能
	private String function;
	// 标识
	private String flag;

	public Setting() {

	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}
