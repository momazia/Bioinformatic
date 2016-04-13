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
public class SmithWatermanUtils {

	public static final char CHAR_DASH = '-';
	public static final String SPACE = " ";
	private static final int SCORE_GAP_EXT = -1;
	private static final int SCORE_GAP_OPEN = -11;
	private static SmithWatermanUtils instance = null;
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
	private SmithWatermanUtils() {
		super();
	}

	/**
	 * Gets the static instance of this class.
	 * 
	 * @return
	 */
	public static SmithWatermanUtils getInstance() {
		if (instance == null) {
			instance = new SmithWatermanUtils();
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

		sequence = addSpacePrefix(sequence);
		query = addSpacePrefix(query);
		char[] seqChrs = sequence.toCharArray();
		char[] queryChrs = query.toCharArray();
		Cell[][] table = createEmptyTable(seqChrs, queryChrs);
		int maxScore = 0;
		int iIndex = 0;
		int jIndex = 0;

		for (int i = 1; i < queryChrs.length; i++) {
			for (int j = 1; j < seqChrs.length; j++) {
				int diagScore = table[i - 1][j - 1].getScore() + matchMisMatchScore(seqChrs[j], queryChrs[i], matchScore, misMatchScore);
				int horScore = table[i][j - 1].getScore() + getScoreGap(table, i, j, Direction.LEFT, gapScore);
				int verScore = table[i - 1][j].getScore() + getScoreGap(table, i, j, Direction.TOP, gapScore);
				table[i][j] = populateCell(diagScore, horScore, verScore);
				if (maxScore <= table[i][j].getScore()) {
					iIndex = i;
					jIndex = j;
					maxScore = table[i][j].getScore();
				}
			}
		}
		return new AffineResult(table, maxScore, iIndex - 1, jIndex - 1); // Reducing the indexes by 1 because of the space added to the strings
	}

	/**
	 * Gets the gap score for the position of i and j in table 2D array. If the gapScore is given, that will be used, otherwise it will check to see
	 * if the gap was opened before or not. If the gap was opened before, it will just return the score gap, otherwise it will add gap opening score
	 * together with a single gap score.
	 * 
	 * @param table
	 * @param i
	 * @param j
	 * @param left
	 * @param gapScore
	 * @return
	 */
	private int getScoreGap(Cell[][] table, int i, int j, Direction direction, Integer gapScore) {
		if (gapScore != null) {
			return gapScore;
		}
		switch (direction) {
		case LEFT:
			return direction.equals(table[i][j - 1].getDirection()) ? SCORE_GAP_EXT : SCORE_GAP_OPEN + SCORE_GAP_EXT;
		case TOP:
			return direction.equals(table[i - 1][j].getDirection()) ? SCORE_GAP_EXT : SCORE_GAP_OPEN + SCORE_GAP_EXT;
		default:
			break;
		}
		return 0; // Cannot happen
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
	public Cell populateCell(int diagScore, int horScore, int verScore) {
		int maxScore = 0;
		Direction dir = null;
		if (maxScore <= diagScore) {
			maxScore = diagScore;
			dir = Direction.DIAGONAL;
		}
		if (maxScore <= verScore) {
			maxScore = verScore;
			dir = Direction.TOP;
		}
		if (maxScore <= horScore) {
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
	public Cell[][] createEmptyTable(char[] seqChrs, char[] queryChrs) {
		Cell[][] result = new Cell[queryChrs.length][seqChrs.length];
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
	 * Adds an empty character to the beginning of the string passed.
	 * 
	 * @param str
	 * @return
	 */
	private String addSpacePrefix(String str) {
		return SPACE + str;
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
		query = addSpacePrefix(query);
		seq = addSpacePrefix(seq);
		StringBuffer queryStr = new StringBuffer();
		trace(query.toCharArray(), seq.toCharArray(), affineResult.getTable(), affineResult.getiIndex()+1, affineResult.getjIndex()+1, seqStr, queryStr);
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
		if (table[i][j].getScore() == 0) {
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
}
