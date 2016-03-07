package com.bio.main.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.bio.main.pojo.BlastNRecord;
import com.bio.main.pojo.Database;
import com.bio.main.pojo.Query;
import com.bio.main.util.DatabaseUtil;
import com.bio.main.util.FileUtil;

public class TestFileProcessor {

	private static final String SIMPLE_RESULT_META_HIT_NR_HMP_FA = "Simple_Result_META_HIT_NR_HMP.FA";
	private static final String _2_SIMPLE_TEST_BLASTN_OUT_95 = "2_Simple-Test-blastn-out-95";
	private static final String _1_SIMPLE_TEST_BLASTN_OUT_95 = "1_Simple-Test-blastn-out-95";
	private static final String _1_SIMPLE_HMP_2000_FA = "1_Simple-HMP-2000.fa";
	private static final String _3_SIMPLE_TEST_BLASTN_OUT_95 = "3_Simple-Test-blastn-out-95";

	@Test
	public void testQueriesFromFile() {
		try {
			Database db = FileUtil.getInstance().readBlastNRecords(_1_SIMPLE_TEST_BLASTN_OUT_95);
			assertEquals(4, db.getBlastNRecords().size());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Something went wrong, while (reading from/writing into) the file!");
		}
	}

	@Test
	public void testFindQueryLength() {
		Database db;
		try {
			db = FileUtil.getInstance().readBlastNRecords(_2_SIMPLE_TEST_BLASTN_OUT_95);
			for (BlastNRecord record : db.getBlastNRecords()) {
				DatabaseUtil.getInstance().findQueryLength(record);
			}
			assertEquals(Integer.valueOf(14972), db.getBlastNRecords().get(0).getLength());
			assertEquals(Integer.valueOf(1959), db.getBlastNRecords().get(1).getLength());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Something went wrong, while (reading from/writing into) the file!");
		}
	}

	@Test
	public void testFindAlignments() {
		Database db;
		try {
			db = FileUtil.getInstance().readBlastNRecords(_2_SIMPLE_TEST_BLASTN_OUT_95);
			for (BlastNRecord record : db.getBlastNRecords()) {
				DatabaseUtil.getInstance().findFindAlignments(record);
			}
			assertNull(db.getBlastNRecords().get(0).getAlignmentLengths());
			assertEquals(4, db.getBlastNRecords().get(1).getAlignmentLengths().size());
			assertEquals(1959, db.getBlastNRecords().get(1).getAlignmentLengths().get(0).intValue());
			assertEquals(1958, db.getBlastNRecords().get(1).getAlignmentLengths().get(1).intValue());
			assertEquals(1963, db.getBlastNRecords().get(1).getAlignmentLengths().get(2).intValue());
			assertEquals(701, db.getBlastNRecords().get(1).getAlignmentLengths().get(3).intValue());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Something went wrong, while (reading from/writing into) the file!");
		}
	}

	@Test
	public void testIsRedundant() {
		Database db;
		try {
			db = FileUtil.getInstance().readBlastNRecords(_3_SIMPLE_TEST_BLASTN_OUT_95);
			for (BlastNRecord record : db.getBlastNRecords()) {
				DatabaseUtil.getInstance().findQueryLength(record);
				DatabaseUtil.getInstance().findFindAlignments(record);
			}
			assertFalse(DatabaseUtil.getInstance().isRedundant(db.getBlastNRecords().get(0)));
			assertTrue(DatabaseUtil.getInstance().isRedundant(db.getBlastNRecords().get(1)));
			assertFalse(DatabaseUtil.getInstance().isRedundant(db.getBlastNRecords().get(2)));

		} catch (IOException e) {
			e.printStackTrace();
			fail("Something went wrong, while (reading from/writing into) the file!");
		}
	}

	@Test
	public void testQueryString() {
		Database db;
		try {
			db = FileUtil.getInstance().readBlastNRecords(_3_SIMPLE_TEST_BLASTN_OUT_95);
			for (BlastNRecord record : db.getBlastNRecords()) {
				DatabaseUtil.getInstance().findQueryLength(record);
				DatabaseUtil.getInstance().findFindAlignments(record);
			}
			assertEquals("scaffold14_2_MH0001", db.getBlastNRecords().get(0).getQueryString());
			assertEquals("scaffold33_1_MH0001", db.getBlastNRecords().get(1).getQueryString());
			assertEquals("C799358_1_MH0001", db.getBlastNRecords().get(2).getQueryString());

		} catch (IOException e) {
			e.printStackTrace();
			fail("Something went wrong, while (reading from/writing into) the file!");
		}
	}

	@Test
	public void testRedundantBlastNRecords() {
		Database db;
		try {
			db = FileUtil.getInstance().readBlastNRecords(_3_SIMPLE_TEST_BLASTN_OUT_95);
			DatabaseUtil.getInstance().findDuplicateRecords(db);
			assertEquals(1, db.getDuplicateQueries().size());
			assertEquals("scaffold33_1_MH0001", db.getDuplicateQueries().iterator().next());

		} catch (IOException e) {
			e.printStackTrace();
			fail("Something went wrong, while (reading from/writing into) the file!");
		}
	}

	@Test
	public void testCopyFileExcludeRedundantQueries() {
		Database db;
		try {
			db = FileUtil.getInstance().readBlastNRecords(_3_SIMPLE_TEST_BLASTN_OUT_95);
			DatabaseUtil.getInstance().findDuplicateRecords(db);
			FileUtil.getInstance().copyFileExcludeRedundantQueries(SIMPLE_RESULT_META_HIT_NR_HMP_FA, _1_SIMPLE_HMP_2000_FA, db.getDuplicateQueries());

		} catch (IOException e) {
			e.printStackTrace();
			fail("Something went wrong, while (reading from/writing into) the file!");
		}
	}

	@Test
	public void testReadQueries() {
		try {
			List<Query> queries = FileUtil.getInstance().readQueries(_1_SIMPLE_HMP_2000_FA);

			assertEquals(8, queries.size());

			assertEquals("scaffold33_1_MH0001", queries.get(0).getName());
			assertEquals(">scaffold33_1_MH0001" + System.getProperty("line.separator") + "ATGCAGATGCTACCATCGATGATGATCAGATCATTGTACATGTAGATGTGA" + System.getProperty("line.separator"), queries.get(0).getStr());

			assertEquals("HMP.18203.DS483497  18203.DS483479-DS483503.nuc.gbk", queries.get(3).getName());
			assertTrue(queries.get(3).getStr().startsWith(">HMP.18203.DS483497  18203.DS483479-DS483503.nuc.gbk" + System.getProperty("line.separator") + "GTTGTGCATCTGCCCCTTTTTTACAATTTATACTGCTCCGTAGATGCCGT"));
			assertTrue(queries.get(3).getStr().endsWith("CGTGGGGCAGATCGGCGGCGTCAAAATAGCTCTGGGGCTGGGCAAGGTTG" + System.getProperty("line.separator")));

		} catch (IOException e) {
			e.printStackTrace();
			fail("Something went wrong, while (reading from/writing into) the file!");
		}
	}
}
