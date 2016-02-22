package com.bio.main.pojo;

public class Sequence {

	private String header;
	private String str;

	public Sequence(String header, String str) {
		this.header = header;
		this.str = str;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}
}
