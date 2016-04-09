package com.bio.pojo;

/**
 * Cell object represents each of the cells in Smith Waterman algorithm table. It holds a score and the direction in which the result of that score
 * came from. The direction is later used for back tracing.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class Cell {

	private int score;
	private Direction direction;

	/**
	 * Constructor to set the values into the POJO.
	 * 
	 * @param score
	 * @param dir
	 */
	public Cell(int score, Direction dir) {
		this.score = score;
		this.direction = dir;
	}

	public Cell() {
		this.score = 0;
		this.direction = null;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

}
