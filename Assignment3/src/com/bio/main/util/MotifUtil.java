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

/**
 * A utility class for Motif finding.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
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

	/**
	 * Finds the motifs and put them into medians for the given sequences.
	 * 
	 * @param medians
	 * @param sequences
	 * @param lmer
	 */
	public void findMotifs(List<Median> medians, List<Sequence> sequences, int lmer) {

		for (Median median : medians) {

			List<Motif> result = new ArrayList<>();

			for (Sequence sequence : sequences) {
				Motif motif = findMotif(sequence.getStr().toCharArray(), median.getStr().toCharArray(), lmer);
				result.add(motif);
			}

			median.setMotifs(result);
		}
	}

	/**
	 * For the given median, it will try to find the best local motif based on its score.
	 * 
	 * @param seqChars
	 * @param median
	 * @param lmer
	 * @return
	 */
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

	/**
	 * Gets the score of two given arrays of characters, each matching character is counted as 1. The higher the score, the better the match.
	 * 
	 * @param sequenceChars
	 * @param median
	 * @return
	 */
	private int getScore(char[] sequenceChars, char[] median) {
		int score = 0;
		for (int i = 0; i < median.length; i++) {
			if (sequenceChars[i] == median[i]) {
				score++;
			}
		}
		return score;
	}

	/**
	 * Calculates the consensus string and score for each of the medians given.
	 * 
	 * @param medians
	 * @param lmer
	 */
	public void calculateConsensus(List<Median> medians, int lmer) {

		for (Median median : medians) {

			String result = "";
			int totalScore = 0;

			for (int i = 0; i < lmer; i++) {

				Map<TCGA, Integer> map = new HashMap<>();

				for (Motif motif : median.getMotifs()) {

					TCGA key = TCGA.valueOf(String.valueOf(motif.getStr().charAt(i)));
					Integer score = map.get(key);
					map.put(key, score == null ? 1 : score + 1); // If the score does not exist(is null), then we put 1, otherwise we add 1 to it.
				}

				MutableInt score = new MutableInt(0); // Using Mutable integer since Java is using pass by value, so that we don't need to create
														// another wrapper around it.
				TCGA tcga = findConsensus(map, score); // Finding which TCGA holds the biggest count number.
				result += tcga.toString();
				totalScore += score.getValue();
			}

			median.setConsensusStr(result);
			median.setConsensusScore(totalScore);
		}

	}

	/**
	 * For each of the TCGA keys in the map, it finds the one with the highest score.
	 * 
	 * @param map
	 * @param score
	 * @return
	 */
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
