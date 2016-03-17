package com.bio.pojo;

public class Output {

	private Integer numberOfMatches;
	private Integer numberOfTiles;

	public Output(Integer numberOfMatches, Integer numberOfTiles) {
		this.numberOfMatches = numberOfMatches;
		this.numberOfTiles = numberOfTiles;
	}

	public void addNumberOfMatches() {
		this.numberOfMatches++;
	}

	public Integer getNumberOfTiles() {
		return numberOfTiles;
	}

	public void setNumberOfTiles(Integer numberOfTiles) {
		this.numberOfTiles = numberOfTiles;
	}

	public Integer getNumberOfMatches() {
		return numberOfMatches;
	}

	public void setNumberOfMatches(Integer numberOfMatches) {
		this.numberOfMatches = numberOfMatches;
	}

	public String getMappability() {
		return String.format("%.2f", numberOfMatches.doubleValue() / numberOfTiles.doubleValue() * 100);
	}

}
