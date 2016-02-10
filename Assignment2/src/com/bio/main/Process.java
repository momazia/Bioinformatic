package com.bio.main;

import java.io.IOException;
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
			PerformanceMonitor readingChr1FilePm = new PerformanceMonitor("Reading [" + chrFilePath + "] file");
			String chr1 = FileProcessor.getInstance().readChromoseFile(chrFilePath);
			readingChr1FilePm.end();

			// Reading all the exonRefSeqs + strings to look up
			PerformanceMonitor readingExonAnnotationFilePm = new PerformanceMonitor("Parsing [" + exonAnnotFilePath + "] file");
			List<RefSeq> exonRefSeqs = FileProcessor.getInstance().readAnnorationFile(exonAnnotFilePath);
			readingExonAnnotationFilePm.end();

			// Looping through each of the Exons and running Smith-Waterman
			for (RefSeq refSeq : exonRefSeqs) {
				PerformanceMonitor smithWatermanResultPm = new PerformanceMonitor("Running smith - Waterman for [" + refSeq.getHeader() + "]");
				SmithWatermanResult optimumResult = smithWaterman(refSeq, chr1);
				System.out.println(optimumResult);
				smithWatermanResultPm.end();
			}

		} catch (IOException e) {
			System.out.println("Could not read a file: " + e.getMessage());
		}

	}

	private SmithWatermanResult smithWaterman(RefSeq refSeq, String chr1) {

		SmithWatermanResult optimumSmithWatermanResult = new SmithWatermanResult();
		char[] charArray = chr1.toCharArray();
		int chr1Size = charArray.length;

		int patternSize = refSeq.getStr().length();
		// Creating a vertical Table for the calculation.
		int[][] vTable = new int[patternSize][2];

		for (int jIndex = 1; jIndex < chr1Size; jIndex++) {

			for (int iIndex = 0; iIndex < patternSize; iIndex++) {

				int result = 0;

				if (iIndex == 0) {
					vTable[iIndex][1] = 0; // Initial value for [0][1]
				} else {
					// We are looking at the first row
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
			// Shifting the result to left column
			for (int iIndex = 0; iIndex < patternSize; iIndex++) {
				vTable[iIndex][0] = vTable[iIndex][1];
			}
		}

		return optimumSmithWatermanResult;
	}

	private int findMax(int[][] vTable, int iIndex, char charAtChr, char charAtPattern) {

		boolean isSameChar = charAtChr == charAtPattern;

		int left = vTable[iIndex][0] + SCORE_INDEL;
		int top = vTable[iIndex][1] + SCORE_INDEL;
		int diagonal = vTable[iIndex - 1][0] + (isSameChar ? SCORE_MATCH : SCORE_MISMATCH);
		return Math.max(Math.max(left, top), Math.max(diagonal, 0));
	}

}
