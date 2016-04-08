package com.bio.pojo;

public class AffineResult {

	private Cell[][] table;
	private int maxScore = 0;
	private int iIndex;
	private int jIndex;

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

}
