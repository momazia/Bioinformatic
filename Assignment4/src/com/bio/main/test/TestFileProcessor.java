package com.bio.main.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.bio.main.pojo.Query;
import com.bio.main.util.FileProcessor;
import com.bio.main.util.Microbiome;

public class TestFileProcessor {

	@Test
	public void testQueriesFromFile() {
		try {
			List<Query> queries = FileProcessor.getInstance().readQueries("1_Simple-Test-blastn-out-95");
			FileProcessor.getInstance().writeResult("Result-Simple-Test-blastn-out-95", queries);
			assertEquals(4, queries.size());
			for (Query query : queries) {
				System.out.println(query.getStr());
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("Something went wrong, while (reading from/writing into) the file!");
		}
	}

	@Test
	public void testFindQueryLength() {
		List<Query> queries;
		try {
			queries = FileProcessor.getInstance().readQueries("2_Simple-Test-blastn-out-95");
			for (Query query : queries) {
				Microbiome.getInstance().findQueryLength(query);
			}
			assertEquals(Integer.valueOf(14972), queries.get(0).getLength());
			assertEquals(Integer.valueOf(1959), queries.get(1).getLength());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Something went wrong, while (reading from/writing into) the file!");
		}
	}

	@Test
	public void testFindAlignments() {
		List<Query> queries;
		try {
			queries = FileProcessor.getInstance().readQueries("2_Simple-Test-blastn-out-95");
			for (Query query : queries) {
				Microbiome.getInstance().findFindAlignments(query);
			}
			assertNull(queries.get(0).getAlignmentLengths());
			assertEquals(4, queries.get(1).getAlignmentLengths().size());
			assertEquals(1959, queries.get(1).getAlignmentLengths().get(0).intValue());
			assertEquals(1958, queries.get(1).getAlignmentLengths().get(1).intValue());
			assertEquals(1963, queries.get(1).getAlignmentLengths().get(2).intValue());
			assertEquals(701, queries.get(1).getAlignmentLengths().get(3).intValue());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Something went wrong, while (reading from/writing into) the file!");
		}
	}

	@Test
	public void testIsQueryEligible() {
		List<Query> queries;
		try {
			queries = FileProcessor.getInstance().readQueries("3_Simple-Test-blastn-out-95");
			for (Query query : queries) {
				Microbiome.getInstance().findQueryLength(query);
				Microbiome.getInstance().findFindAlignments(query);
			}
			assertFalse(Microbiome.getInstance().isQueryEligible(queries.get(0)));
			assertTrue(Microbiome.getInstance().isQueryEligible(queries.get(1)));
			assertFalse(Microbiome.getInstance().isQueryEligible(queries.get(2)));

		} catch (IOException e) {
			e.printStackTrace();
			fail("Something went wrong, while (reading from/writing into) the file!");
		}
	}
}
