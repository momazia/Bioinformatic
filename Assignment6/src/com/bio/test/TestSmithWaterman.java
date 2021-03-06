package com.bio.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.bio.pojo.AffineResult;
import com.bio.pojo.Cell;
import com.bio.pojo.Direction;
import com.bio.pojo.Sequence;
import com.bio.util.CabiosUtils;
import com.bio.util.FileUtils;
import com.bio.util.SmithWatermanUtils;

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
		AffineResult result = SmithWatermanUtils.getInstance().run(SEQ_2, SEQ_1, -4, 5, -3);
		for (int i = 0; i < result.getTable().length; i++) {
			Cell[] cells = result.getTable()[i];
			for (int j = 0; j < cells.length; j++) {
				assertEquals(scoreTable[j][i], cells[j].getScore());
			}
		}
	}

	@Test
	public void testDirectionTable() {
		AffineResult result = SmithWatermanUtils.getInstance().run(SEQ_3, SEQ_4, -1, 2, -1);
		assertEquals(Direction.DIAGONAL, result.getTable()[1][1].getDirection());
		assertEquals(Direction.TOP, result.getTable()[2][1].getDirection());
		assertEquals(Direction.LEFT, result.getTable()[3][2].getDirection());
		assertEquals(Direction.TOP, result.getTable()[7][7].getDirection());
	}

	@Test
	public void testGetScore() {
		assertEquals(4, SmithWatermanUtils.getInstance().getScore('A', 'A'));

		assertEquals(-4, SmithWatermanUtils.getInstance().getScore('C', 'E'));
		assertEquals(-4, SmithWatermanUtils.getInstance().getScore('E', 'C'));

		assertEquals(0, SmithWatermanUtils.getInstance().getScore('N', 'R'));
		assertEquals(0, SmithWatermanUtils.getInstance().getScore('R', 'N'));
	}

	@Test
	public void testAfineGap() {
		AffineResult result = SmithWatermanUtils.getInstance().run(TestFileUtils.SEQ_0, TestFileUtils.QUERY_STR);
		assertEquals(20, result.getMaxScore());

		result = SmithWatermanUtils.getInstance().run(TestFileUtils.SEQ_1, TestFileUtils.QUERY_STR);
		assertEquals(17, result.getMaxScore());
	}

	@Test
	public void testBackTrace2() {
		String db = "ATGCATCCCATGAC";
		String query = "TCTATATCCGT";
		AffineResult result = SmithWatermanUtils.getInstance().run(db, query, -2, 2, -3);
		assertEquals(8, result.getMaxScore());
		SmithWatermanUtils.getInstance().backTrace(db, query, result);
		assertEquals("ATCC", result.getSeqStr());
		assertEquals("ATCC", result.getQueryStr());
	}

	@Test
	public void testBackTrace1() {
		AffineResult result = SmithWatermanUtils.getInstance().run(SEQ_3, SEQ_4, -1, 2, -1);
		assertEquals(12, result.getMaxScore());
		SmithWatermanUtils.getInstance().backTrace(SEQ_3, SEQ_4, result);
		assertEquals("AGCACAC-A", result.getSeqStr());
		assertEquals("A-CACACTA", result.getQueryStr());
	}

	@Test
	public void testScore() {
		assertEquals(0, CabiosUtils.getInstance().getScore('A', 'T'));
		assertEquals(1, CabiosUtils.getInstance().getScore('V', 'L'));
		assertEquals(-3, CabiosUtils.getInstance().getScore('F', 'G'));
	}

	@Test
	public void testSpecialCase() throws IOException {
		// Reading the query file
		Sequence query = FileUtils.getInstance().readQuery(FileUtils.E_COLI_QUERY1_FA);
		// Reading the sequences from the file
		List<Sequence> seqs = FileUtils.getInstance().readSequences(FileUtils.SWISSPROT_100_FA);
		for (Sequence sequence : seqs) {
			AffineResult mine = SmithWatermanUtils.getInstance().run(sequence.getStr(), query.getStr());
			AffineResult cabios = CabiosUtils.getInstance().sw(sequence.getStr().toCharArray(), sequence.getStr().length(), query.getStr().toCharArray(), query.getStr().length(), 11, 1);
			StringBuffer str = new StringBuffer();
			if (!mine.equals(cabios)) {
				str.append(sequence.getName() + '\n');
				str.append("Length=" + sequence.getStr().length() + '\n');
				str.append("Mine: " + mine + '\n');
				str.append("Cabi: " + cabios + '\n');
				System.out.println(str.toString());
//				str.append("Table Mine:" + '\n');
//				for (int i = 0; i < mine.getTable().length; i++) {
//					str.append("{}");
//					for (int j = 0; j < mine.getTable()[i].length; j++) {
//						str.append(String.format("%2s, ", mine.getTable()[i][j].getScore()));
//					}
//					str.append('\n');
//				}

//				str.append("\nTable Cabios:" + '\n');
//				for (int i = 0; i < cabios.getTable().length; i++) {
//					str.append("{}");
//					for (int j = 0; j < cabios.getTable()[i].length; j++) {
//						str.append(String.format("%2s, ", cabios.getTable()[i][j].getScore()));
//					}
//					str.append('\n');
//				}

//				String fileName = "testFile" + Math.random() + ".txt";
//				FileUtils.getInstance().deleteIfExists(fileName);
//				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FileUtils.IO_PATH + fileName, true)));
//				out.println(str.toString());
//				out.close();
			}
		}
	}

}
