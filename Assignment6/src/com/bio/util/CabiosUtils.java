package com.bio.util;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * A utility class for Cabios version of Smith Waterman.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class CabiosUtils {

	private static CabiosUtils instance = null;

	/**
	 * Private constructor for Singleton design pattern purpose. Declared private so it is not accessible from outside.
	 */
	private CabiosUtils() {
	}

	/**
	 * Gets instance of this class. It will instantiate it if it is not done yet, once.
	 * 
	 * @return
	 */
	public static CabiosUtils getInstance() {
		if (instance == null) {
			instance = new CabiosUtils();
		}
		return instance;
	}

	/**
	 * Main method to be executed in order to run Smith Waterman logic.
	 * 
	 * @param seqA
	 * @param lena
	 * @param seqB
	 * @param lenb
	 * @param gap_open
	 * @param gap_ext
	 * @return
	 */
	public void sw(char[] seqA, int lena, char[] seqB, int lenb, int gap_open, int gap_ext) {
		int my_i = 0;
		int my_j = 0;
		int[] nogap = new int[lena];
		int[] b_gap = new int[lena];
		int last_nogap, prev_nogap;
		int score = 0;
		init_vect(lena, nogap, 0);
		init_vect(lena, b_gap, -gap_open);
		for (int i = 0; i < lenb; i++) {
			int a_gap;
			last_nogap = prev_nogap = 0;
			a_gap = -gap_open;
			for (int j = 0; j < lena; j++) {
				a_gap = NumberUtils.max((last_nogap - gap_open - gap_ext), (a_gap - gap_ext));
				b_gap[j] = NumberUtils.max((nogap[j] - gap_open - gap_ext), (b_gap[j] - gap_ext));
				last_nogap = NumberUtils.max((prev_nogap + SmithWatermanUtils.getInstance().getScore(seqA[j], seqB[i])), 0);
				last_nogap = NumberUtils.max(last_nogap, a_gap);
				last_nogap = NumberUtils.max(last_nogap, b_gap[j]);
				prev_nogap = nogap[j];
				nogap[j] = last_nogap;
				if (score <= last_nogap) {
					my_i = i;
					my_j = j;
				}
				score = NumberUtils.max(score, last_nogap);
			}
		}
		System.out.println("score: " + score + " i: " + my_i + " j: " + my_j);
	}

	/**
	 * Puts the value given into all the elements of the array.
	 * 
	 * @param lena
	 * @param array
	 * @param val
	 */
	private void init_vect(int lena, int[] array, int val) {
		for (int i = 0; i < lena; i++) {
			array[i] = val;
		}
	}

}
