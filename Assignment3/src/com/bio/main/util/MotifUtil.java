package com.bio.main.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;

import com.bio.main.pojo.Median;
import com.bio.main.pojo.Motif;
import com.bio.main.pojo.Sequence;
import com.bio.main.pojo.TCGA;

public class MotifUtil {

	private static MotifUtil instance = null;

	private MotifUtil() {
		super();
	}

	public static MotifUtil getInstance() {
		if (instance == null) {
			instance = new MotifUtil();
		}
		return instance;
	}

	public void calculateMotifs(List<Median> medians, List<Sequence> sequences, int lmer) {

		for (Median median : medians) {

			List<Motif> result = new ArrayList<>();

			for (Sequence sequence : sequences) {
				Motif motif = findMotif(sequence.getStr().toCharArray(), median.getStr().toCharArray(), lmer);
				result.add(motif);
			}

			median.setMotifs(result);
		}
	}

	public Motif findMotif(char[] seqChars, char[] median, int lmer) {

		int bestScore = 0;
		String motifStr = null;
		int location = 0;

		for (int seqCharIndex = 0; seqCharIndex < seqChars.length - median.length + 1; seqCharIndex++) {

			char[] sequenceChars = Arrays.copyOfRange(seqChars, seqCharIndex, seqCharIndex + median.length);
			int score = getScore(sequenceChars, median);

			if (score > bestScore) {
				bestScore = score;
				motifStr = String.copyValueOf(sequenceChars);
				location = seqCharIndex;
			}
		}

		return new Motif(motifStr, location);
	}

	private int getScore(char[] sequenceChars, char[] median) {
		int score = 0;
		for (int i = 0; i < median.length; i++) {
			if (sequenceChars[i] == median[i]) {
				score++;
			}
		}
		return score;
	}

	public void calculateConsensus(List<Median> medians, int lmer) {

		for (Median median : medians) {
			String result = "";
			int totalScore = 0;

			for (int i = 0; i < lmer; i++) {

				Map<TCGA, Integer> map = new HashMap<>();

				for (Motif motif : median.getMotifs()) {

					TCGA key = TCGA.valueOf(String.valueOf(motif.getStr().charAt(i)));
					Integer score = map.get(key);
					map.put(key, score == null ? 1 : score + 1);
				}

				MutableInt score = new MutableInt(0);
				TCGA tcga = findConsensus(map, score);
				result += tcga.toString();
				totalScore += score.getValue();
			}

			median.setConsensusStr(result);
			median.setConsensusScore(totalScore);
		}

	}

	private TCGA findConsensus(Map<TCGA, Integer> map, MutableInt score) {
		TCGA tcga = null;
		for (TCGA key : map.keySet()) {
			if (score.getValue() < map.get(key)) {
				score.setValue(map.get(key));
				tcga = key;
			}
		}
		return tcga;
	}
}
