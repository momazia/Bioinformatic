package com.bio.main.test;

import org.junit.Test;

import com.bio.main.PerformanceMonitor;
import com.bio.main.Process;

public class MainTest {

	private static final String EXON_ANNOT_FILE_PATH = Process.IO_PATH + "simple_Prog2-input-NM_032291-10exon-seqs.fa";
	private static final String CHR1_FILE_PATH = Process.IO_PATH + "simple_chr1.fa";
	
	@Test
	public void testMainProcess() {
		PerformanceMonitor mainPm = new PerformanceMonitor("Main Process");
		Process.getInstance().run(EXON_ANNOT_FILE_PATH, CHR1_FILE_PATH);
		mainPm.end();
	}

}
