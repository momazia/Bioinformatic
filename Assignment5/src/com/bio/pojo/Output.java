package com.bio.pojo;

import java.text.DecimalFormat;

public class Output {

	private Integer numberOfMatches;
	private Integer numberOfTiles;
	private static final DecimalFormat df = new DecimalFormat("000.0000");

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
		return df.format(numberOfMatches.doubleValue() / numberOfTiles.doubleValue() * 100);
	}

}
