package com.bio.pojo;

/**
 * The main POJO to hold the output of the program.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class AffineResult {

	private Cell[][] table;
	private int maxScore = 0;
	private int iIndex;
	private int jIndex;
	private String queryStr;
	private String seqStr;

	/**
	 * Constructor which sets the values in the POJO.
	 * 
	 * @param table
	 * @param maxScore
	 * @param iIndex
	 * @param jIndex
	 */
	public AffineResult(Cell[][] table, int maxScore, int iIndex, int jIndex) {
		this.table = table;
		this.maxScore = maxScore;
		this.iIndex = iIndex;
		this.jIndex = jIndex;
	}

	public Cell[][] getTable() {
		return table;
	}

	public void setTable(Cell[][] table) {
		this.table = table;
	}

	public int getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

	public int getiIndex() {
		return iIndex;
	}

	public void setiIndex(int iIndex) {
		this.iIndex = iIndex;
	}

	public int getjIndex() {
		return jIndex;
	}

	public void setjIndex(int jIndex) {
		this.jIndex = jIndex;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

	public String getSeqStr() {
		return seqStr;
	}

	public void setSeqStr(String seqStr) {
		this.seqStr = seqStr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AffineResult) {
			return ((AffineResult) obj).getiIndex() == getiIndex() && ((AffineResult) obj).getjIndex() == getjIndex() && ((AffineResult) obj).getMaxScore() == getMaxScore();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("MaxScore: %s, i: %s, j: %s", getMaxScore(), getiIndex(), getjIndex());
	}
}
