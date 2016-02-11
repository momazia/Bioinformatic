package com.bio.main.pojo;

/**
 * The class holds values of the result of running SmithWaterman algorithm. It
 * contains a maximum score and locations of the found score.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class SmithWatermanResult {

	private int iIndex;
	private int jIndex;
	private int score;

	@Override
	public String toString() {
		return "Score: [" + this.getScore() + "] iIndex: [" + this.getiIndex() + "] jIndex: [" + this.getjIndex() + "]";
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
