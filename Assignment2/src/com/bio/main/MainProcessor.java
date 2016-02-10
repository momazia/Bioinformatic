package com.bio.main;

/**
 * Main class to run the program.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class MainProcessor {

	private static final String EXON_ANNOT_FILE_PATH = Process.IO_PATH + "Prog2-input-NM_032291-10exon-seqs.fa";
	private static final String CHR1_FILE_PATH = Process.IO_PATH + "chr1.fa";

	public static void main(String[] args) {
		PerformanceMonitor mainPm = new PerformanceMonitor("Main Process");
		Process.getInstance().run(EXON_ANNOT_FILE_PATH, CHR1_FILE_PATH);
		mainPm.end();
	}
}
