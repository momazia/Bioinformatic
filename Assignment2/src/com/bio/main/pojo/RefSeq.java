package com.bio.main.pojo;

/**
 * A POJO to keep RefSeq structure.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class RefSeq {

	/**
	 * Header holds the RefSeq annotation.
	 */
	private String header;

	/**
	 * The string to look for.
	 */
	private String str;

	public RefSeq(String header, String str) {
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
