package com.bio.main.pojo;

/**
 * Motif holds the data related to the each of the tiles found. It contains a string and its location in the sequence.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class Motif {

	/**
	 * Motif string
	 */
	private String str;
	/**
	 * Motif location in the sequence
	 */
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
