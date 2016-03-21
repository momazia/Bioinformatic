package com.bio.pojo;

/**
 * A POJO to hold gene information like start and end indexes.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class Gene {
	private Integer startIndex;
	private Integer endIndex;
	private String name;

	/**
	 * The main constructor.
	 * 
	 * @param startIndex
	 * @param endIndex
	 * @param name
	 */
	public Gene(Integer startIndex, Integer endIndex, String name) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.name = name;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
