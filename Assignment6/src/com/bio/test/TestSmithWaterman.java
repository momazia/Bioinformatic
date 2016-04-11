package com.bio.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.bio.pojo.AffineResult;
import com.bio.pojo.Cell;
import com.bio.pojo.Direction;
import com.bio.util.SmithWaterman;

public class TestSmithWaterman {

	private static final String SEQ_1 = "CGTGAATTCAT";
	private static final String SEQ_2 = "GACTTAC";
	private static final String SEQ_3 = "AGCACACA";
	private static final String SEQ_4 = "ACACACTA";
	private int[][] scoreTable = new int[][] { //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 5, 1, 5, 1, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 1, 2, 1, 10, 6, 2, 0, 0, 5, 1 }, //
			{ 0, 5, 1, 0, 0, 6, 7, 3, 0, 5, 1, 2 }, //
			{ 0, 1, 2, 6, 2, 2, 3, 12, 8, 4, 2, 6 }, //
			{ 0, 0, 0, 7, 3, 0, 0, 8, 17, 13, 9, 7 }, //
			{ 0, 0, 0, 3, 4, 8, 5, 4, 13, 14, 18, 14 }, //
			{ 0, 5, 1, 0, 0, 4, 5, 2, 9, 18, 14, 15 } };//

	@Test
	public void testScoreTable() {
		AffineResult result = SmithWaterman.getInstance().run(SEQ_2, SEQ_1, -4, 5, -3);
		for (int i = 0; i < result.getTable().length; i++) {
			Cell[] cells = result.getTable()[i];
			for (int j = 0; j < cells.length; j++) {
				assertEquals(scoreTable[i][j], cells[j].getScore());
			}
		}
	}

	@Test
	public void testDirectionTable() {
		AffineResult result = SmithWaterman.getInstance().run(SEQ_3, SEQ_4, -1, 2, -1);
		assertEquals(Direction.DIAGONAL, result.getTable()[1][1].getDirection());
		assertEquals(Direction.TOP, result.getTable()[2][1].getDirection());
		assertEquals(Direction.DIAGONAL, result.getTable()[3][2].getDirection());
		assertEquals(Direction.LEFT, result.getTable()[7][7].getDirection());
	}

	@Test
	public void testGetScore() {
		assertEquals(4, SmithWaterman.getInstance().getScore('A', 'A'));

		assertEquals(-4, SmithWaterman.getInstance().getScore('C', 'E'));
		assertEquals(-4, SmithWaterman.getInstance().getScore('E', 'C'));

		assertEquals(0, SmithWaterman.getInstance().getScore('N', 'R'));
		assertEquals(0, SmithWaterman.getInstance().getScore('R', 'N'));
	}

	@Test
	public void testAfineGap() {
		AffineResult result = SmithWaterman.getInstance().run(TestFileUtils.SEQ_0, TestFileUtils.QUERY_STR);
		assertEquals(20, result.getMaxScore());

		result = SmithWaterman.getInstance().run(TestFileUtils.SEQ_1, TestFileUtils.QUERY_STR);
		assertEquals(17, result.getMaxScore());
	}

	@Test
	public void testBackTrace() {
		AffineResult result = SmithWaterman.getInstance().run(SEQ_3, SEQ_4, -1, 2, -1);
		assertEquals(12, result.getMaxScore());
		SmithWaterman.getInstance().backTrace(SEQ_3, SEQ_4, result);
		assertEquals("A-CACACTA", result.getQueryStr());
		assertEquals("AGCACAC-A", result.getSeqStr());
	}

}
