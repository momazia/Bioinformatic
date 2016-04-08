package com.bio.util;

import org.apache.commons.lang3.StringUtils;

import com.bio.pojo.AffineResult;
import com.bio.pojo.Cell;
import com.bio.pojo.Direction;

public class SmithWaterman {

	private static final char CHAR_DASH = '-';
	private static final String SPACE = " ";
	private static final int SCORE_GAP = -1;
	private static final int SCORE_GAP_OPEN = -11;
	private static SmithWaterman instance = null;
	private static char[] ALPHABETS = new char[] { 'A', 'R', 'N', 'D', 'C', 'Q', 'E', 'G', 'H', 'I', 'L', 'K', 'M', 'F', 'P', 'S', 'T', 'W', 'Y', 'V', 'B', 'J', 'Z', 'X', '*' };
	private static int[][] SCORE_MATRIX = new int[][] { //
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

	private SmithWaterman() {
		super();
	}

	public static SmithWaterman getInstance() {
		if (instance == null) {
			instance = new SmithWaterman();
		}
		return instance;
	}

	public AffineResult run(String query, String db, Integer gapScore, Integer matchScore, Integer misMatchScore) {

		query = addSpacePrefix(query);
		db = addSpacePrefix(db);
		char[] queryChrs = query.toCharArray();
		char[] dbChrs = db.toCharArray();
		Cell[][] table = createEmptyTable(queryChrs, dbChrs);
		int maxScore = 0;
		int iIndex = 0;
		int jIndex = 0;

		for (int i = 1; i < queryChrs.length; i++) {
			for (int j = 1; j < dbChrs.length; j++) {
				int diagScore = table[i - 1][j - 1].getScore() + matchMisMatchScore(queryChrs[i], dbChrs[j], matchScore, misMatchScore);
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

	private int getScoreGap(Cell[][] table, int i, int j, Integer gapScore) {
		if (gapScore != null) {
			return gapScore;
		}
		return (isGapOpened(table[i][j].getDirection()) ? SCORE_GAP : SCORE_GAP_OPEN + SCORE_GAP);
	}

	private boolean isGapOpened(Direction direction) {
		return Direction.LEFT == direction || Direction.TOP == direction;
	}

	public int getScore(char ch1, char ch2) {
		return SCORE_MATRIX[getAlphabetPosition(ch1)][getAlphabetPosition(ch2)];
	}

	private int getAlphabetPosition(char ch) {
		for (int i = 0; i < ALPHABETS.length; i++) {
			if (ALPHABETS[i] == ch) {
				return i;
			}
		}
		return ALPHABETS.length - 1; // It will return the position of * character
	}

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

	private Cell[][] createEmptyTable(char[] queryChrs, char[] dbChrs) {
		Cell[][] result = new Cell[queryChrs.length][dbChrs.length];
		for (Cell[] cells : result) {
			for (int j = 0; j < cells.length; j++) {
				cells[j] = new Cell();
			}
		}
		return result;
	}

	private int matchMisMatchScore(char seq, char db, Integer matchScore, Integer misMatchScore) {
		if (matchScore != null && misMatchScore != null) {
			if (seq == db) {
				return matchScore;
			}
			return misMatchScore;
		}
		return getScore(seq, db);
	}

	private String addSpacePrefix(String query) {
		return SPACE + query;
	}

	public AffineResult run(String query, String db) {
		return run(query, db, null, null, null);
	}

	public void backTrace(String seq, String db, AffineResult affineResult) {
		StringBuffer seqStr = new StringBuffer();
		StringBuffer dbStr = new StringBuffer();
		trace(addSpacePrefix(seq).toCharArray(), addSpacePrefix(db).toCharArray(), affineResult.getTable(), affineResult.getiIndex(), affineResult.getjIndex(), seqStr, dbStr);
		affineResult.setSeqStr(StringUtils.reverse(seqStr.toString()));
		affineResult.setDbStr(StringUtils.reverse(dbStr.toString()));
	}

	private void trace(char[] seqChrs, char[] dbChrs, Cell[][] table, int i, int j, StringBuffer seqStr, StringBuffer dbStr) {
		if (table[i][j].getScore() == 0) {
			return;
		}
		if (table[i][j].getDirection() == Direction.DIAGONAL) {
			seqStr.append(seqChrs[i]);
			dbStr.append(dbChrs[j]);
			trace(seqChrs, dbChrs, table, i - 1, j - 1, seqStr, dbStr);
		} else if (table[i][j].getDirection() == Direction.TOP) {
			seqStr.append(seqChrs[i]);
			dbStr.append(CHAR_DASH);
			trace(seqChrs, dbChrs, table, i - 1, j, seqStr, dbStr);
		} else if (table[i][j].getDirection() == Direction.LEFT) {
			seqStr.append(CHAR_DASH);
			dbStr.append(dbChrs[j]);
			trace(seqChrs, dbChrs, table, i, j - 1, seqStr, dbStr);
		}

	}
}
