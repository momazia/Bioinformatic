package com.bio.pojo;

/**
 * A POJO to hold the data related to RefSeq.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class RefSeq {

	private int startIndex;
	private int endIndex;
	private String id;

	/**
	 * Default constructor setting all the attributes of the class.
	 * 
	 * @param startIndex
	 * @param endIndex
	 * @param id
	 */
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
