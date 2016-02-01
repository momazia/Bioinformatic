package com.bio.main;

/**
 * Main class to run the program.
 * 
 * @author Max
 *
 */
public class MainProcessor {

	private static final String EXON_ANNOT_FILE_PATH = Process.IO_PATH + "HG19-refseq-exon-annot-chr1-2016";
	private static final String GENE_ANNOT_FILE_PATH = Process.IO_PATH + "HG19-refseq-gene-annot-filtered";
	private static final String CHR1_FILE_PATH = Process.IO_PATH + "chr1.fa";

	public static void main(String[] args) {
		System.out.println("Starting the process...");
		Process.getInstance().run(GENE_ANNOT_FILE_PATH, EXON_ANNOT_FILE_PATH, CHR1_FILE_PATH);
		System.out.println("Process ended.");
	}
}
