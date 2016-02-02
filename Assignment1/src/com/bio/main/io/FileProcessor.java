package com.bio.main.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.input.BoundedInputStream;

import com.bio.main.pojo.RefSeq;

/**
 * The main class in charge of reading from and writing to a file.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class FileProcessor {

	private static final int CHR1_FA_STARTING_BYTES = 6;
	private static final int CHR1_FA_COLUMNS = 50;
	private static FileProcessor instance;

	private FileProcessor() {
	}

	public static FileProcessor getInstance() {
		if (instance == null) {
			instance = new FileProcessor();
		}
		return instance;
	}

	/**
	 * For a given Gene ID, a list of RefSeq will be returned by looking at the
	 * line and check if the geneID exists anywhere. If the geneID is not given,
	 * the method will read the whole file.
	 * 
	 * @param filePath
	 * @param geneId
	 * @return
	 * @throws IOException
	 */
	public List<RefSeq> readAnnorationFile(String filePath, String geneId) throws IOException {
		List<RefSeq> result = new ArrayList<>();

		for (String line : Files.readAllLines(Paths.get(filePath))) {
			// Only add those which we are interested in by filtering using the
			// geneId given. If the geneIn is null, we include all.
			if (geneId == null || (geneId != null && line.contains(geneId))) {
				result.add(new RefSeq(line));
			}
		}
		return result;
	}

	/**
	 * Reads chromosome file based on the RefReq starting and ending indexes.
	 * 
	 * @param filePath
	 * @param refSeq
	 * @return
	 * @throws IOException
	 */
	public String readChromosomeFile(String filePath, RefSeq refSeq) throws IOException {

		String result = ""; // Final result will be stored in this string.

		int start = refSeq.getStart();
		int end = refSeq.getEnd();

		int calculatedStartIndex = CHR1_FA_STARTING_BYTES + start + getNumberOfNewLines(start) + 1;// Excluding
																									// the
																									// first
																									// index
																									// by
																									// adding
																									// 1

		int length = end - start;

		FileInputStream file = new FileInputStream(filePath);
		// Since the file given is big, that is why we want to skip the
		// unnecessary part of the file instead of reading the whole file.
		file.skip(calculatedStartIndex);

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new BoundedInputStream(file)))) {
			int charCount = 0;

			int fileChar;
			StringBuilder response = new StringBuilder();

			while ((fileChar = br.read()) != -1) {

				// We want to stop the loop if we have read enough
				if (charCount >= length) {
					break;
				}

				// Making sure to only put the letters in the string. This is to
				// exclude '\n' (New lines)
				if (Character.isLetter(fileChar)) {
					response.append((char) fileChar);
					charCount++;
				}
			}
			result = response.toString();

		}
		return result;
	}

	/**
	 * Since Chromosome file contains "\n" (new lines) and it counts as one
	 * character, this method helps finding the number of new lines for a given
	 * index.
	 * 
	 * @param index
	 * @return
	 */
	private Integer getNumberOfNewLines(int index) {
		return index / CHR1_FA_COLUMNS;
	}

}
