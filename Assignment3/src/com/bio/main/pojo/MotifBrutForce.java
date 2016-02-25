package com.bio.main.pojo;

public class MotifBrutForce {

	private String str;
	private int location;
	private int distance;

	public MotifBrutForce(String motif, int location, int distance) {
		str = motif;
		this.location = location;
		this.distance = distance;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
}
