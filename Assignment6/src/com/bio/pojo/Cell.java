package com.bio.pojo;

public class Cell {

	private int score;
	private Direction direction;

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
