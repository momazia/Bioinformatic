package com.bio.main;

import com.bio.main.io.FileProcessor;

/**
 * The application runs Smith-Waterman algorithm for given Exon annotations in a
 * chromosome file. Due to the large size of the files, these files are not in
 * repository. Please create the following folder structure for the application
 * to work: <b>/Assignment2/io <b> and include the files needed. The default
 * file names are {@link MainProcessor#EXON_ANNOT_FILE_PATH} and
 * {@link MainProcessor#EXON_ANNOT_FILE_PATH}.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class MainProcessor {

	private static final String EXON_ANNOT_FILE_PATH = FileProcessor.IO_PATH + "Prog2-input-NM_032291-10exon-seqs.fa";
	private static final String chr_FILE_PATH = FileProcessor.IO_PATH + "chr1.fa";

	public static void main(String[] args) {
		PerformanceMonitor mainPm = new PerformanceMonitor("Main Process");
		Process.getInstance().run(EXON_ANNOT_FILE_PATH, chr_FILE_PATH);
		mainPm.end();
	}
}
