package com.bio.main.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bio.main.BrutForceSearch;

public class MedianStr {

	private String leaf;
	private List<MotifBrutForce> motifs;

	public MedianStr(String leaf, List<MotifBrutForce> motifs) {
		this.leaf = leaf;
		this.motifs = motifs;
	}

	public int getTotalDistance() {
		int total = 0;
		for (MotifBrutForce motif : motifs) {
			total += motif.getDistance();
		}
		return total;
	}

	public String getLeaf() {
		return leaf;
	}

	public List<MotifBrutForce> getMotifs() {
		return motifs;
	}

	public String getMotifStr(Integer lmer) {

		String result = "";

		for (int i = 0; i < lmer; i++) {

			Map<TCGA, Integer> map = new HashMap<>();

			for (MotifBrutForce motif : motifs) {
				TCGA key = TCGA.valueOf(String.valueOf(motif.getStr().charAt(i)));
				Integer score = map.get(key);
				map.put(key, score == null ? 1 : score + 1);
			}

			Character consChar = findConsensus(map);
			result += consChar;
		}

		return result;
	}

	private Character findConsensus(Map<TCGA, Integer> map) {
		int maxScore = 0;
		Character consChar = null;
		for (TCGA key : map.keySet()) {
			if (maxScore < map.get(key)) {
				maxScore = map.get(key);
				consChar = key.toString().charAt(0);
			}
		}
		return consChar;
	}

	public Integer getConsensusScore() {
		return this.motifs.size() * BrutForceSearch.L_MER - getTotalDistance();
	}

}
