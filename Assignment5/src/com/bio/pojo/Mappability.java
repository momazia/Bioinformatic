package com.bio.pojo;

import java.text.DecimalFormat;

/**
 * A POJO to hold the data related to the mappability. It contains the number of matches and the tiles.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class Mappability {
	private Integer numberOfMatches;
	private Integer numberOfTiles;
	private static final DecimalFormat df = new DecimalFormat("#0.00##");

	/**
	 * The main constructor.
	 * 
	 * @param numberOfMatches
	 * @param numberOfTiles
	 */
	public Mappability(Integer numberOfMatches, Integer numberOfTiles) {
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

	/**
	 * Calculates the mappability percentage by dividing numberOfMatches over numberOfTiles * 100. It is formatted using #0.00## formatter.
	 * 
	 * @return
	 */
	public String getMappability() {
		return df.format(numberOfMatches.doubleValue() / numberOfTiles.doubleValue() * 100);
	}

}
