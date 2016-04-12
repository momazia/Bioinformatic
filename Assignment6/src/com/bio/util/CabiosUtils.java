package com.bio.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.bio.pojo.AffineResult;
import com.bio.pojo.Cell;
import com.bio.pojo.Direction;

/**
 * A utility class for Cabios version of Smith Waterman.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class CabiosUtils {

	private static CabiosUtils instance = null;
	private static final char CHAR_DASH = '-';
	private static char[] ALPHABETS = new char[] { 'A', 'R', 'N', 'D', 'C', 'Q', 'E', 'G', 'H', 'I', 'L', 'K', 'M', 'F', 'P', 'S', 'T', 'W', 'Y', 'V', 'B', 'J', 'Z', 'X', '*' };
	public static int[][] SCORE_MATRIX = new int[][] { //
			{ 4, -1, -2, -2, 0, -1, -1, 0, -2, -1, -1, -1, -1, -2, -1, 1, 0, -3, -2, 0, -2, -1, -1, -1, -4 }, //
			{ -1, 5, 0, -2, -3, 1, 0, -2, 0, -3, -2, 2, -1, -3, -2, -1, -1, -3, -2, -3, -1, -2, 0, -1, -4 }, //
			{ -2, 0, 6, 1, -3, 0, 0, 0, 1, -3, -3, 0, -2, -3, -2, 1, 0, -4, -2, -3, 4, -3, 0, -1, -4 }, //
			{ -2, -2, 1, 6, -3, 0, 2, -1, -1, -3, -4, -1, -3, -3, -1, 0, -1, -4, -3, -3, 4, -3, 1, -1, -4 }, //
			{ 0, -3, -3, -3, 9, -3, -4, -3, -3, -1, -1, -3, -1, -2, -3, -1, -1, -2, -2, -1, -3, -1, -3, -1, -4 }, //
			{ -1, 1, 0, 0, -3, 5, 2, -2, 0, -3, -2, 1, 0, -3, -1, 0, -1, -2, -1, -2, 0, -2, 4, -1, -4 }, //
			{ -1, 0, 0, 2, -4, 2, 5, -2, 0, -3, -3, 1, -2, -3, -1, 0, -1, -3, -2, -2, 1, -3, 4, -1, -4 }, //
			{ 0, -2, 0, -1, -3, -2, -2, 6, -2, -4, -4, -2, -3, -3, -2, 0, -2, -2, -3, -3, -1, -4, -2, -1, -4 }, //
			{ -2, 0, 1, -1, -3, 0, 0, -2, 8, -3, -3, -1, -2, -1, -2, -1, -2, -2, 2, -3, 0, -3, 0, -1, -4 }, //
			{ -1, -3, -3, -3, -1, -3, -3, -4, -3, 4, 2, -3, 1, 0, -3, -2, -1, -3, -1, 3, -3, 3, -3, -1, -4 }, //
			{ -1, -2, -3, -4, -1, -2, -3, -4, -3, 2, 4, -2, 2, 0, -3, -2, -1, -2, -1, 1, -4, 3, -3, -1, -4 }, //
			{ -1, 2, 0, -1, -3, 1, 1, -2, -1, -3, -2, 5, -1, -3, -1, 0, -1, -3, -2, -2, 0, -3, 1, -1, -4 }, //
			{ -1, -1, -2, -3, -1, 0, -2, -3, -2, 1, 2, -1, 5, 0, -2, -1, -1, -1, -1, 1, -3, 2, -1, -1, -4 }, //
			{ -2, -3, -3, -3, -2, -3, -3, -3, -1, 0, 0, -3, 0, 6, -4, -2, -2, 1, 3, -1, -3, 0, -3, -1, -4 }, //
			{ -1, -2, -2, -1, -3, -1, -1, -2, -2, -3, -3, -1, -2, -4, 7, -1, -1, -4, -3, -2, -2, -3, -1, -1, -4 }, //
			{ 1, -1, 1, 0, -1, 0, 0, 0, -1, -2, -2, 0, -1, -2, -1, 4, 1, -3, -2, -2, 0, -2, 0, -1, -4 }, //
			{ 0, -1, 0, -1, -1, -1, -1, -2, -2, -1, -1, -1, -1, -2, -1, 1, 5, -2, -2, 0, -1, -1, -1, -1, -4 }, //
			{ -3, -3, -4, -4, -2, -2, -3, -2, -2, -3, -2, -3, -1, 1, -4, -3, -2, 11, 2, -3, -4, -2, -2, -1, -4 }, //
			{ -2, -2, -2, -3, -2, -1, -2, -3, 2, -1, -1, -2, -1, 3, -3, -2, -2, 2, 7, -1, -3, -1, -2, -1, -4 }, //
			{ 0, -3, -3, -3, -1, -2, -2, -3, -3, 3, 1, -2, 1, -1, -2, -2, 0, -3, -1, 4, -3, 2, -2, -1, -4 }, //
			{ -2, -1, 4, 4, -3, 0, 1, -1, 0, -3, -4, 0, -3, -3, -2, 0, -1, -4, -3, -3, 4, -3, 0, -1, -4 }, //
			{ -1, -2, -3, -3, -1, -2, -3, -4, -3, 3, 3, -3, 2, 0, -3, -2, -1, -2, -1, 2, -3, 3, -3, -1, -4 }, //
			{ -1, 0, 0, 1, -3, 4, 4, -2, 0, -3, -3, 1, -1, -3, -1, 0, -1, -2, -2, -2, 0, -3, 4, -1, -4 }, //
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -4 }, //
			{ -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, 1 } //
	};

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
	 * Back traces the final result by looking at maximum score i and j indexes. At the end, it will reverse the strings.
	 * 
	 * @param seq
	 * @param query
	 * @param affineResult
	 */
	public void backTrace(String seq, String query, AffineResult affineResult) {
		StringBuffer seqStr = new StringBuffer();
		StringBuffer queryStr = new StringBuffer();
		trace(query.toCharArray(), seq.toCharArray(), affineResult.getTable(), affineResult.getiIndex(), affineResult.getjIndex(), seqStr, queryStr);
		affineResult.setSeqStr(StringUtils.reverse(seqStr.toString()));
		affineResult.setQueryStr(StringUtils.reverse(queryStr.toString()));
	}

	/**
	 * The main tracing back method which must be called recursively. The order of finding the single final result is diagonal, top and left. In case
	 * of insertion or deletion, dash is added.
	 * 
	 * @param queryChrs
	 * @param seqChrs
	 * @param table
	 * @param i
	 * @param j
	 * @param seqStr
	 * @param queryStr
	 */
	private void trace(char[] queryChrs, char[] seqChrs, Cell[][] table, int i, int j, StringBuffer seqStr, StringBuffer queryStr) {
		if (table[i][j].getScore() == 0 || i <= 0 || j <= 0) {
			return;
		}
		if (table[i][j].getDirection() == Direction.DIAGONAL) {
			queryStr.append(queryChrs[i - 1]);
			seqStr.append(seqChrs[j - 1]);
			trace(queryChrs, seqChrs, table, i - 1, j - 1, seqStr, queryStr);
		} else if (table[i][j].getDirection() == Direction.TOP) {
			seqStr.append(CHAR_DASH);
			queryStr.append(queryChrs[i - 1]);
			trace(queryChrs, seqChrs, table, i - 1, j, seqStr, queryStr);
		} else if (table[i][j].getDirection() == Direction.LEFT) {
			seqStr.append(seqChrs[j - 1]);
			queryStr.append(CHAR_DASH);
			trace(queryChrs, seqChrs, table, i, j - 1, seqStr, queryStr);
		}
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
	public AffineResult sw(char[] seqA, int lena, char[] seqB, int lenb, int gap_open, int gap_ext) {
		int my_i = 0;
		int my_j = 0;
		int[] nogap = new int[lena];
		int[] b_gap = new int[lena];
		int last_nogap, prev_nogap;
		int score = 0;
		init_vect(lena, nogap, 0);
		init_vect(lena, b_gap, -gap_open);
		Cell[][] table = createEmptyTable(seqA, seqB);
		for (int i = 0; i < lenb; i++) {
			int a_gap;
			last_nogap = prev_nogap = 0;
			a_gap = -gap_open;
			for (int j = 0; j < lena; j++) {
				Direction dir = null;
				a_gap = NumberUtils.max((last_nogap - gap_open - gap_ext), (a_gap - gap_ext));
				b_gap[j] = NumberUtils.max((nogap[j] - gap_open - gap_ext), (b_gap[j] - gap_ext));
				int diagScore = (prev_nogap + getScore(seqA[j], seqB[i]));
				last_nogap = NumberUtils.max(diagScore, 0);
				if (diagScore > 0) {
					dir = Direction.DIAGONAL;
				}
				int horScore = a_gap;
				if (last_nogap < horScore) {
					dir = Direction.LEFT;
				}
				last_nogap = NumberUtils.max(last_nogap, a_gap);
				int verScore = b_gap[j];
				if (last_nogap < verScore) {
					dir = Direction.TOP;
				}
				last_nogap = NumberUtils.max(last_nogap, b_gap[j]);
				prev_nogap = nogap[j];
				nogap[j] = last_nogap;
				if (score <= last_nogap) {
					my_i = i;
					my_j = j;
				}
				score = NumberUtils.max(score, last_nogap);
				table[i][j] = new Cell(score, dir);
			}
		}
		return new AffineResult(table, score, my_i, my_j);
	}

	/**
	 * For 2 given characters, it will find the match/mismatch score in the scoring matrix.
	 * 
	 * @param ch1
	 * @param ch2
	 * @return
	 */
	public int getScore(char ch1, char ch2) {
		return SCORE_MATRIX[getAlphabetPosition(ch1)][getAlphabetPosition(ch2)];
	}

	/**
	 * Finds the position of the given character in alphabet array. If the character is not found, it will return the position of * (last index).
	 * 
	 * @param ch
	 * @return
	 */
	private int getAlphabetPosition(char ch) {
		for (int i = 0; i < ALPHABETS.length; i++) {
			if (ALPHABETS[i] == ch) {
				return i;
			}
		}
		return ALPHABETS.length - 1; // It will return the position of * character
	}

	/**
	 * The method instantiates empty cells with zero score for the table.
	 * 
	 * @param seqChrs
	 * @param queryChrs
	 * @return
	 */
	private Cell[][] createEmptyTable(char[] seqChrs, char[] queryChrs) {
		Cell[][] result = new Cell[queryChrs.length][seqChrs.length];
		for (Cell[] cells : result) {
			for (int j = 0; j < cells.length; j++) {
				cells[j] = new Cell();
			}
		}
		return result;
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
