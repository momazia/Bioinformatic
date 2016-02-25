package com.bio.main.pojo;

public class Motif {

	private String str;
	private int location;

	public Motif(String str, int location) {
		super();
		this.str = str;
		this.location = location;
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
}
