package com.bio.main.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.bio.main.pojo.MicrobiomeDB;
import com.bio.main.pojo.Query;
import com.bio.main.util.FileUtil;
import com.bio.main.util.MicrobiomeUtil;

public class TestFileProcessor {

	@Test
	public void testQueriesFromFile() {
		try {
			MicrobiomeDB db = FileUtil.getInstance().readQueries("1_Simple-Test-blastn-out-95");
			assertEquals(4, db.getQueries().size());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Something went wrong, while (reading from/writing into) the file!");
		}
	}

	@Test
	public void testQueriesFromFile_Header() {
		try {
			MicrobiomeDB db = FileUtil.getInstance().readQueries("1_Simple-Test-blastn-out-95");
			System.out.println(db.getHeader());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Something went wrong, while (reading from/writing into) the file!");
		}
	}

	@Test
	public void testFindQueryLength() {
		MicrobiomeDB db;
		try {
			db = FileUtil.getInstance().readQueries("2_Simple-Test-blastn-out-95");
			for (Query query : db.getQueries()) {
				MicrobiomeUtil.getInstance().findQueryLength(query);
			}
			assertEquals(Integer.valueOf(14972), db.getQueries().get(0).getLength());
			assertEquals(Integer.valueOf(1959), db.getQueries().get(1).getLength());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Something went wrong, while (reading from/writing into) the file!");
		}
	}

	@Test
	public void testFindAlignments() {
		MicrobiomeDB db;
		try {
			db = FileUtil.getInstance().readQueries("2_Simple-Test-blastn-out-95");
			for (Query query : db.getQueries()) {
				MicrobiomeUtil.getInstance().findFindAlignments(query);
			}
			assertNull(db.getQueries().get(0).getAlignmentLengths());
			assertEquals(4, db.getQueries().get(1).getAlignmentLengths().size());
			assertEquals(1959, db.getQueries().get(1).getAlignmentLengths().get(0).intValue());
			assertEquals(1958, db.getQueries().get(1).getAlignmentLengths().get(1).intValue());
			assertEquals(1963, db.getQueries().get(1).getAlignmentLengths().get(2).intValue());
			assertEquals(701, db.getQueries().get(1).getAlignmentLengths().get(3).intValue());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Something went wrong, while (reading from/writing into) the file!");
		}
	}

	@Test
	public void testIsQueryEligible() {
		MicrobiomeDB db;
		try {
			db = FileUtil.getInstance().readQueries("3_Simple-Test-blastn-out-95");
			for (Query query : db.getQueries()) {
				MicrobiomeUtil.getInstance().findQueryLength(query);
				MicrobiomeUtil.getInstance().findFindAlignments(query);
			}
			assertFalse(MicrobiomeUtil.getInstance().isQueryEligible(db.getQueries().get(0)));
			assertTrue(MicrobiomeUtil.getInstance().isQueryEligible(db.getQueries().get(1)));
			assertFalse(MicrobiomeUtil.getInstance().isQueryEligible(db.getQueries().get(2)));

		} catch (IOException e) {
			e.printStackTrace();
			fail("Something went wrong, while (reading from/writing into) the file!");
		}
	}
}
