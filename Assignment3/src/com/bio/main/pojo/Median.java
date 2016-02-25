package com.bio.main.pojo;

import java.util.List;

public class Median {

	private String str;
	private String consensusStr;
	private int consensusScore;
	private int totalDistance;
	List<Motif> sequences;

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
}
