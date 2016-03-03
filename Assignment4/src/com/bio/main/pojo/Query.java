package com.bio.main.pojo;

import java.util.List;

public class Query {

	private StringBuffer str = new StringBuffer();
	private Integer length;
	private List<Integer> alignmentLengths;

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public StringBuffer getStr() {
		return str;
	}

	public void setStr(StringBuffer str) {
		this.str = str;
	}

	public List<Integer> getAlignmentLengths() {
		return alignmentLengths;
	}

	public void setAlignmentLengths(List<Integer> alignmentLengths) {
		this.alignmentLengths = alignmentLengths;
	}

}
