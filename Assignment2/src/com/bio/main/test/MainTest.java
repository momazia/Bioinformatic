package com.bio.main.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.bio.main.PerformanceMonitor;
import com.bio.main.Process;
import com.bio.main.io.FileProcessor;

/**
 * Main class for running couple of tests, targeting the whole assignment 2.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class MainTest {

	private static final String EXON_ANNOT_FILE_PATH = FileProcessor.IO_PATH + "simple_Prog2-input-NM_032291-10exon-seqs.fa";
	private static final String chr_FILE_PATH = FileProcessor.IO_PATH + "simple_chr.fa";

	@Test
	public void testMainProcess() {
		PerformanceMonitor mainPm = new PerformanceMonitor("Main Process");
		Process.getInstance().run(EXON_ANNOT_FILE_PATH, chr_FILE_PATH);
		mainPm.end();
	}

	@Test
	public void testFindMax_diff_values_diag() {
		int[][] vTable = new int[][] { //
				{ 4, 1 }, //
				{ 2, 0 } }; //
		int iIndex = 1;
		char charAtChr = 'A';
		char charAtPattern = 'C';
		assertEquals(3, Process.getInstance().findMax(vTable, iIndex, charAtChr, charAtPattern));
	}

	@Test
	public void testFindMax_matching_values_diag() {
		int[][] vTable = new int[][] { //
				{ 4, 1 }, //
				{ 2, 0 } }; //
		int iIndex = 1;
		char charAtChr = 'A';
		char charAtPattern = 'A';
		assertEquals(6, Process.getInstance().findMax(vTable, iIndex, charAtChr, charAtPattern));
	}

	@Test
	public void testFindMax_left() {
		int[][] vTable = new int[][] { //
				{ 1, 1 }, //
				{ 4, 0 } }; //
		int iIndex = 1;
		char charAtChr = 'C';
		char charAtPattern = 'A';
		assertEquals(3, Process.getInstance().findMax(vTable, iIndex, charAtChr, charAtPattern));
	}

	@Test
	public void testFindMax_top() {
		int[][] vTable = new int[][] { //
				{ 1, 6 }, //
				{ 1, 0 } }; //
		int iIndex = 1;
		char charAtChr = 'C';
		char charAtPattern = 'A';
		assertEquals(5, Process.getInstance().findMax(vTable, iIndex, charAtChr, charAtPattern));
	}

}
