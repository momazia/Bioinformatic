package com.bio.main;

/**
 * Main class to run the program.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class MainProcessor {

	private static final String EXON_ANNOT_FILE_PATH = Process.IO_PATH + "HG19-refseq-exon-annot-chr-2016";
	private static final String GENE_ANNOT_FILE_PATH = Process.IO_PATH + "HG19-refseq-gene-annot-filtered";
	private static final String chr_FILE_PATH = Process.IO_PATH + "chr.fa";

	public static void main(String[] args) {
		System.out.println("Starting the process...");
		PerformanceMonitor mainPm = new PerformanceMonitor();
		Process.getInstance().run(GENE_ANNOT_FILE_PATH, EXON_ANNOT_FILE_PATH, chr_FILE_PATH);
		mainPm.end();
		System.out.println("Process ended in " + mainPm);
	}
}
