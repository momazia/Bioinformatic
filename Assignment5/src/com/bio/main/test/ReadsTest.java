package com.bio.main.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.bio.main.util.FileUtils;

public class ReadsTest {

	@Test
	public void testCreateTiles_bigger() {
		List<String> tiles = FileUtils.getInstance().createTiles(3, "ACTGAAC");
		assertEquals(5, tiles.size());
		assertEquals("ACT", tiles.get(0));
		assertEquals("AAC", tiles.get(4));
	}

	@Test
	public void testCreateTiles_smaller() {
		List<String> tiles = FileUtils.getInstance().createTiles(10, "ACTGAAC");
		assertEquals(1, tiles.size());
		assertEquals("ACTGAAC", tiles.get(0));
	}

	@Test
	public void testCreateTiles_same() {
		List<String> tiles = FileUtils.getInstance().createTiles(7, "ACTGAAC");
		assertEquals(1, tiles.size());
		assertEquals("ACTGAAC", tiles.get(0));
	}

	@Test
	public void testCreateReads() {
		try {
			FileUtils.getInstance().createReads(5, "Test_Reads5.fa", "Test_chr1-2.fa");
			List<String> lines = FileUtils.getInstance().readFile("Test_Reads5.fa");
			assertEquals(80, lines.size());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Could not read the file");
		}
	}

}
