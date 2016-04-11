package com.bio.util;

import org.apache.commons.lang3.StringUtils;

import com.bio.pojo.AffineResult;
import com.bio.pojo.Cell;
import com.bio.pojo.Direction;

/**
 * This class is the utility class for all the operations related to Smith Waterman.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class SmithWaterman {

	private static final char CHAR_DASH = '-';
	private static final int SCORE_GAP = -1;
	private static final int SCORE_GAP_OPEN = -11;
	private static SmithWaterman instance = null;
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
	 * Private constructor to avoid this class being initialized by the client.
	 */
	private SmithWaterman() {
		super();
	}

	/**
	 * Gets the static instance of this class.
	 * 
	 * @return
	 */
	public static SmithWaterman getInstance() {
		if (instance == null) {
			instance = new SmithWaterman();
		}
		return instance;
	}

	/**
	 * The main method to be invoked to run Smith Waterman logic for the given sequence and query strings. If the scores are passed, they will be
	 * used, otherwise it will use Affine gap scoring system.
	 * 
	 * @param sequence
	 * @param query
	 * @param gapScore
	 * @param matchScore
	 * @param misMatchScore
	 * @return
	 */
	public AffineResult run(String sequence, String query, Integer gapScore, Integer matchScore, Integer misMatchScore) {
		char[] seqChrs = sequence.toCharArray();
		char[] queryChrs = query.toCharArray();
		Cell[][] table = createEmptyTable(seqChrs, queryChrs);
		int maxScore = 0;
		int iIndex = 0;
		int jIndex = 0;
		for (int i = 1; i < seqChrs.length; i++) {
			for (int j = 1; j < queryChrs.length; j++) {
				int diagScore = table[i - 1][j - 1].getScore() + matchMisMatchScore(seqChrs[i], queryChrs[j], matchScore, misMatchScore);
				int horScore = table[i][j - 1].getScore() + getScoreGap(table, i, j - 1, gapScore);
				int verScore = table[i - 1][j].getScore() + getScoreGap(table, i - 1, j, gapScore);
				table[i][j] = populateCell(diagScore, horScore, verScore);
				if (maxScore < table[i][j].getScore()) {
					iIndex = i;
					jIndex = j;
					maxScore = table[i][j].getScore();
				}
			}
		}
		return new AffineResult(table, maxScore, iIndex, jIndex);
	}

	/**
	 * Gets the gap score for the position of i and j in table 2D array. If the gapScore is given, that will be used, otherwise it will check to see
	 * if the gap was opened before or not. If the gap was opened before, it will just return the score gap, otherwise it will add gap opening score
	 * together with a single gap score.
	 * 
	 * @param table
	 * @param i
	 * @param j
	 * @param gapScore
	 * @return
	 */
	private int getScoreGap(Cell[][] table, int i, int j, Integer gapScore) {
		if (gapScore != null) {
			return gapScore;
		}
		return (isGapOpened(table[i][j].getDirection()) ? SCORE_GAP : SCORE_GAP_OPEN + SCORE_GAP);
	}

	/**
	 * If the direction given is from LEFT or TOP, that means the gap was opened before.
	 * 
	 * @param direction
	 * @return
	 */
	private boolean isGapOpened(Direction direction) {
		return Direction.LEFT == direction || Direction.TOP == direction;
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
	 * Based on the maximum score coming from 3 different directions, it creates a cell which stores the direction the score was originated and the
	 * value of the score. It will look into Diagonal, Top and lastly Left scores in order.
	 * 
	 * @param diagScore
	 * @param horScore
	 * @param verScore
	 * @return
	 */
	private Cell populateCell(int diagScore, int horScore, int verScore) {
		int maxScore = 0;
		Direction dir = null;
		if (maxScore < diagScore) {
			maxScore = diagScore;
			dir = Direction.DIAGONAL;
		}
		if (maxScore < verScore) {
			maxScore = verScore;
			dir = Direction.TOP;
		}
		if (maxScore < horScore) {
			maxScore = horScore;
			dir = Direction.LEFT;
		}
		return new Cell(maxScore, dir);
	}

	/**
	 * The method instantiates empty cells with zero score for the table.
	 * 
	 * @param seqChrs
	 * @param queryChrs
	 * @return
	 */
	private Cell[][] createEmptyTable(char[] seqChrs, char[] queryChrs) {
		Cell[][] result = new Cell[seqChrs.length][queryChrs.length];
		for (Cell[] cells : result) {
			for (int j = 0; j < cells.length; j++) {
				cells[j] = new Cell();
			}
		}
		return result;
	}

	/**
	 * If both match and mismatch scores are given, they will be used to determine the score. If they are not provided (in case of Affine), it will
	 * get the score from the scoring table.
	 * 
	 * @param seq
	 * @param query
	 * @param matchScore
	 * @param misMatchScore
	 * @return
	 */
	private int matchMisMatchScore(char seq, char query, Integer matchScore, Integer misMatchScore) {
		if (matchScore != null && misMatchScore != null) {
			if (seq == query) {
				return matchScore;
			}
			return misMatchScore;
		}
		return getScore(seq, query);
	}

	/**
	 * The main method to be called for Smith Waterman if Affine gap is to be used.
	 * 
	 * @param sequence
	 * @param query
	 * @return
	 */
	public AffineResult run(String sequence, String query) {
		return run(sequence, query, null, null, null);
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
		trace(seq.toCharArray(), query.toCharArray(), affineResult.getTable(), affineResult.getiIndex(), affineResult.getjIndex(), seqStr, queryStr);
		affineResult.setSeqStr(StringUtils.reverse(seqStr.toString()));
		affineResult.setQueryStr(StringUtils.reverse(queryStr.toString()));
	}

	/**
	 * The main tracing back method which must be called recursively. The order of finding the single final result is diagonal, top and left. In case
	 * of insertion or deletion, dash is added.
	 * 
	 * @param seqChrs
	 * @param queryChrs
	 * @param table
	 * @param i
	 * @param j
	 * @param seqStr
	 * @param queryStr
	 */
	private void trace(char[] seqChrs, char[] queryChrs, Cell[][] table, int i, int j, StringBuffer seqStr, StringBuffer queryStr) {
		if (table[i][j].getScore() == 0) {
			return;
		}
		if (table[i][j].getDirection() == Direction.DIAGONAL) {
			seqStr.append(seqChrs[i]);
			queryStr.append(queryChrs[j]);
			trace(seqChrs, queryChrs, table, i - 1, j - 1, seqStr, queryStr);
		} else if (table[i][j].getDirection() == Direction.TOP) {
			seqStr.append(seqChrs[i]);
			queryStr.append(CHAR_DASH);
			trace(seqChrs, queryChrs, table, i - 1, j, seqStr, queryStr);
		} else if (table[i][j].getDirection() == Direction.LEFT) {
			seqStr.append(CHAR_DASH);
			queryStr.append(queryChrs[j]);
			trace(seqChrs, queryChrs, table, i, j - 1, seqStr, queryStr);
		}
	}
}
