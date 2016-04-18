package com.bio.pojo;

/**
 * A POJO to hold the sequence name and its string value.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class Sequence {

	private String name;
	private String str;

	/**
	 * Default constructor for Sequence object.
	 * 
	 * @param name
	 * @param str
	 */
	public Sequence(String name, String str) {
		this.name = name;
		this.str = str;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}
}
