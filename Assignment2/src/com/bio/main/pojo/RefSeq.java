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

	/**
	 * The only constructor of the POJO.
	 * 
	 * @param header
	 * @param str
	 */
	public RefSeq(String header, String str) {
		this.header = header;
		this.str = str;
	}

	public String getHeader() {
		return header;
	}

	/**
	 * Returns the string attached to this RefSeq by prefixing it with an empty
	 * space first.
	 * 
	 * @return
	 */
	public String getStr() {
		return ' ' + str;
	}

}
