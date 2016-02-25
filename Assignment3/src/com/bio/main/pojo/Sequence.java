package com.bio.main.pojo;

/**
 * DNA Sequence which holds header of each DNA and the string itself.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class Sequence {

	/**
	 * Header of the sequence
	 */
	private String header;
	/**
	 * The contain of the sequence
	 */
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
