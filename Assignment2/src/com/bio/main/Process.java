package com.bio.main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.bio.main.io.FileProcessor;
import com.bio.main.pojo.RefSeq;
import com.bio.main.pojo.SmithWatermanResult;

/**
 * The main class in charge of the whole process.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class Process {

	public static final String IO_PATH = "../Assignment2/io/";

	private static Process instance;

	// Constants to for scoring purposes.
	private static final int SCORE_INDEL = -1;
	private static final int SCORE_MATCH = 2;
	private static final int SCORE_MISMATCH = -1;

	private Process() {
	}

	public static Process getInstance() {
		if (instance == null) {
			instance = new Process();
		}
		return instance;
	}

	/**
	 * The main method to be invoked to print the final result.
	 * 
	 * @param exonAnnotFilePath
	 * @param chrFilePath
	 */
	public void run(String exonAnnotFilePath, String chrFilePath) {

		try {
			// Read the db file.
			String chr = extractChromosome(chrFilePath);

			// Reading all the exonRefSeqs + strings to look up
			List<RefSeq> exonRefSeqs = extractRefSeq(exonAnnotFilePath);

			// Looping through each of the Exons and running Smith-Waterman
			for (RefSeq refSeq : exonRefSeqs) {
				runSmithWaterman(chr, refSeq);
			}

		} catch (IOException e) {
			System.out.println("Could not read a file: " + e.getMessage());
		}

	}

	/**
	 * Executes SmithWaterman for the given RefSeq on chromosome string.
	 * 
	 * @param chr
	 * @param refSeq
	 */
	private void runSmithWaterman(String chr, RefSeq refSeq) {
		PerformanceMonitor smithWatermanResultPm = new PerformanceMonitor("Running smith - Waterman for [" + refSeq.getHeader() + "]");
		SmithWatermanResult optimumResult = smithWaterman(refSeq, chr);
		System.out.println(optimumResult);
		smithWatermanResultPm.end();
	}

	/**
	 * Returns a list of ReqSeqs by reading the Exon annotation file path given.
	 * 
	 * @param exonAnnotFilePath
	 * @return
	 * @throws IOException
	 */
	private List<RefSeq> extractRefSeq(String exonAnnotFilePath) throws IOException {
		PerformanceMonitor readingExonAnnotationFilePm = new PerformanceMonitor("Parsing [" + exonAnnotFilePath + "] file");
		List<RefSeq> exonRefSeqs = FileProcessor.getInstance().readAnnorationFile(exonAnnotFilePath);
		readingExonAnnotationFilePm.end();
		return exonRefSeqs;
	}

	/**
	 * Extracts the Chromosome file path given and returns its string.
	 * 
	 * @param chrFilePath
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private String extractChromosome(String chrFilePath) throws UnsupportedEncodingException, IOException {
		PerformanceMonitor readingchrFilePm = new PerformanceMonitor("Reading [" + chrFilePath + "] file");
		String chr = FileProcessor.getInstance().readChromoseFile(chrFilePath);
		readingchrFilePm.end();
		return chr;
	}

	/**
	 * The method runs Smith Waterman algorithm on chr string using the RefSeq's
	 * string.
	 * 
	 * @param refSeq
	 * @param chr
	 * @return returns the optimum score together with the position of it in
	 *         both strings in terms of i and j indexes.
	 */
	private SmithWatermanResult smithWaterman(RefSeq refSeq, String chr) {

		SmithWatermanResult optimumSmithWatermanResult = new SmithWatermanResult();

		char[] charArray = chr.toCharArray();

		int chrSize = charArray.length;

		int patternSize = refSeq.getStr().length();
		// Creating a vertical Table for the calculation.
		int[][] vTable = new int[patternSize][2];

		// Printing the progress
		int progress = 0;
		System.out.print("Progress: ");

		for (int jIndex = 1; jIndex < chrSize; jIndex++) {

			// Printing the progress to make sure it is going.
			if (jIndex % (chrSize / 100) == 0) {
				System.out.printf(" ");
				progress++;
				System.out.printf(" %2d", progress);
			}

			for (int iIndex = 0; iIndex < patternSize; iIndex++) {

				int result = 0;

				if (iIndex == 0) {
					vTable[iIndex][1] = 0; // Initial value for [0][1]
				} else {
					result = findMax(vTable, iIndex, charArray[jIndex], refSeq.getStr().charAt(iIndex));
					vTable[iIndex][1] = result;
				}

				// Keeping the biggest score if found one.
				if (vTable[iIndex][1] >= optimumSmithWatermanResult.getScore()) {
					optimumSmithWatermanResult.setScore(vTable[iIndex][1]);
					optimumSmithWatermanResult.setiIndex(iIndex);
					optimumSmithWatermanResult.setjIndex(jIndex);
				}
			}
			// Shifting the result to left column prior to repeating for the
			// next charater in chr.
			for (int iIndex = 0; iIndex < patternSize; iIndex++) {
				vTable[iIndex][0] = vTable[iIndex][1];
			}
		}

		return optimumSmithWatermanResult;
	}

	/**
	 * For a given vTable, it applies Smith-Waterman calculation and finds the
	 * maximum score among left, top and diagonal items in the table. The result
	 * cannot be lower than zero.
	 * 
	 * @param vTable
	 * @param iIndex
	 * @param charAtChr
	 * @param charAtPattern
	 * @return
	 */
	public int findMax(int[][] vTable, int iIndex, char charAtChr, char charAtPattern) {

		boolean isSameChar = charAtChr == charAtPattern;

		int left = vTable[iIndex][0] + SCORE_INDEL;
		int top = vTable[iIndex - 1][1] + SCORE_INDEL;
		int diagonal = vTable[iIndex - 1][0] + (isSameChar ? SCORE_MATCH : SCORE_MISMATCH);
		return Math.max(Math.max(left, top), Math.max(diagonal, 0));
	}

}
