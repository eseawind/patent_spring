package cn.edu.scut.patent.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Data implements Serializable {

	private int i;
	private int j;
	private double distance;

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}
