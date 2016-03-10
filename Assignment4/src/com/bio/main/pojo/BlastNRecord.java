package com.bio.main.pojo;

import java.util.List;

/**
 * POJO to hold the values read from the BlastN file output
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class BlastNRecord {

	/**
	 * Holds the whole raw record string
	 */
	private StringBuffer str = new StringBuffer();
	/**
	 * length of the query
	 */
	private Integer length;
	/**
	 * List of all the alignment lengths given for each query
	 */
	private List<Integer> alignmentLengths;
	/**
	 * Query string
	 */
	private String queryString;

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

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

}
