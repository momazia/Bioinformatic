package com.bio.main.pojo;

import java.util.List;

/**
 * Main POJO which holds the data needed to be shown to the user according.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class Median {

	/**
	 * Median string
	 */
	private String str;
	/**
	 * Total distance of the median string.
	 */
	private int totalDistance;
	/**
	 * Consensus string
	 */
	private String consensusStr;
	/**
	 * Consensus score
	 */
	private int consensusScore;
	/**
	 * List of Motifs found.
	 */
	private List<Motif> motifs;

	public Median(String str, int totalDistance) {
		super();
		this.str = str;
		this.totalDistance = totalDistance;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public int getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(int totalDistance) {
		this.totalDistance = totalDistance;
	}

	public String getConsensusStr() {
		return consensusStr;
	}

	public void setConsensusStr(String consensusStr) {
		this.consensusStr = consensusStr;
	}

	public int getConsensusScore() {
		return consensusScore;
	}

	public void setConsensusScore(int consensusScore) {
		this.consensusScore = consensusScore;
	}

	public List<Motif> getMotifs() {
		return motifs;
	}

	public void setMotifs(List<Motif> motifs) {
		this.motifs = motifs;
	}
}
