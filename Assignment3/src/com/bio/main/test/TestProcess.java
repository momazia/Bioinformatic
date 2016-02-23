package com.bio.main.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.bio.main.BranchAndBound;
import com.bio.main.io.FileProcessor;
import com.bio.main.pojo.MedianStr;
import com.bio.main.pojo.Sequence;

public class TestProcess {

	@Test
	public void testConstructLeafNodes() {
		List<String> constructLeafNodes = BranchAndBound.getInstance().constructLeafNodes(2);
		// Checking the size
		assertEquals(16, constructLeafNodes.size());
		// Checking randomly
		assertEquals("TT", constructLeafNodes.get(0));
		assertEquals("CT", constructLeafNodes.get(4));
		assertEquals("AA", constructLeafNodes.get(15));
	}

	@Test
	public void testReadSequences() throws IOException {
		List<Sequence> sequences = FileProcessor.getInstance().readSequences("HMP-617.fa");
		// Checking the size
		assertEquals(617, sequences.size());
		// Checking randomly
		assertEquals(">ECSE_0294;coding;x	HMP.18057.AP009240.325077.325307.+", sequences.get(616).getHeader());
		assertTrue(sequences.get(616).getStr().startsWith("ATGCCTGAACTCACTGAACTACCAGAGGGACCGTTTTCGCGCCAG"));
		assertEquals(231, sequences.get(616).getStr().length());
	}

	@Test
	public void findBestMedianStrs1() throws IOException {
		List<Sequence> sequences = new ArrayList<>();
		sequences.add(new Sequence(">ECSE_0294", "aatca"));
		sequences.add(new Sequence(">ECSE_0295", "aaaac"));
		List<MedianStr> findBestMedianStrs = BranchAndBound.getInstance().findBestMedianStrs(sequences, 3);

		assertEquals(5, findBestMedianStrs.size());

		assertEquals("ATC", findBestMedianStrs.get(0).getLeaf());
		assertEquals(1, findBestMedianStrs.get(0).getTotalDistance());

		assertEquals("AAA", findBestMedianStrs.get(1).getLeaf());
		assertEquals(1, findBestMedianStrs.get(1).getTotalDistance());

		assertEquals("AAT", findBestMedianStrs.get(2).getLeaf());
		assertEquals(1, findBestMedianStrs.get(2).getTotalDistance());

		assertEquals("AAC", findBestMedianStrs.get(3).getLeaf());
		assertEquals(1, findBestMedianStrs.get(3).getTotalDistance());

		assertEquals("TAA", findBestMedianStrs.get(4).getLeaf());
		assertEquals(2, findBestMedianStrs.get(4).getTotalDistance());
	}

	@Test
	public void findBestMedianStrs2() throws IOException {
		List<Sequence> sequences = new ArrayList<>();
		sequences.add(new Sequence(">ECSE_0294", "AATCCCGAGA"));
		sequences.add(new Sequence(">ECSE_0295", "AAACCGACGC"));
		sequences.add(new Sequence(">ECSE_0296", "CAACCGACGC"));
		int lmer = 3;
		List<MedianStr> medianStrs = BranchAndBound.getInstance().findBestMedianStrs(sequences, lmer);

		assertEquals(5, medianStrs.size());
		for (MedianStr medianStr : medianStrs) {
			System.out.println(medianStr.getMotifStr(lmer) + " " + medianStr.getLeaf());
		}
	}

	@Test
	public void testFindBestDistance() throws IOException {

		assertEquals(0, BranchAndBound.getInstance().createMotif("A".toCharArray(), "A".toCharArray(), BranchAndBound.L_MER).getDistance());
		assertEquals(0, BranchAndBound.getInstance().createMotif("A".toCharArray(), "a".toCharArray(), BranchAndBound.L_MER).getDistance());

		assertEquals(1, BranchAndBound.getInstance().createMotif("A".toCharArray(), "C".toCharArray(), BranchAndBound.L_MER).getDistance());
		assertEquals(1, BranchAndBound.getInstance().createMotif("c".toCharArray(), "A".toCharArray(), BranchAndBound.L_MER).getDistance());

		assertEquals(1, BranchAndBound.getInstance().createMotif("cTg".toCharArray(), "cAg".toCharArray(), BranchAndBound.L_MER).getDistance());
		assertEquals(1, BranchAndBound.getInstance().createMotif("cTg".toCharArray(), "cAg".toCharArray(), BranchAndBound.L_MER).getDistance());

		assertEquals(0, BranchAndBound.getInstance().createMotif("ctg".toCharArray(), "ctg".toCharArray(), BranchAndBound.L_MER).getDistance());
		assertEquals(0, BranchAndBound.getInstance().createMotif("ctg".toCharArray(), "ctg".toCharArray(), BranchAndBound.L_MER).getDistance());

		assertEquals(3, BranchAndBound.getInstance().createMotif("tgc".toCharArray(), "ctg".toCharArray(), BranchAndBound.L_MER).getDistance());

		assertEquals(1, BranchAndBound.getInstance().createMotif("tgcagaaa".toCharArray(), "ctg".toCharArray(), BranchAndBound.L_MER).getDistance());
		assertEquals(0, BranchAndBound.getInstance().createMotif("ctgagaaa".toCharArray(), "ctg".toCharArray(), BranchAndBound.L_MER).getDistance());
		assertEquals(0, BranchAndBound.getInstance().createMotif("ctgagaaa".toCharArray(), "aaa".toCharArray(), BranchAndBound.L_MER).getDistance());
		assertEquals(1, BranchAndBound.getInstance().createMotif("ctgagaat".toCharArray(), "aaa".toCharArray(), BranchAndBound.L_MER).getDistance());
	}
}
