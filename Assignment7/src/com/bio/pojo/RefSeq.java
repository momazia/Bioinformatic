package com.bio.pojo;

public class RefSeq {

	private int startIndex;
	private int endIndex;
	private String id;

	public RefSeq(int startIndex, int endIndex, String id) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.id = id;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
