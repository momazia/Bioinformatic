package com.bio.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.bio.pojo.AffineResult;
import com.bio.pojo.Sequence;

/**
 * This utility class is in charge of all the IO related operations needed.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class FileUtils {
	private static final String PLUS = "+";
	private static final String SPACE = " ";
	public static final String SWISSPROT_100_FA = "swissprot-100.fa";
	public static final String E_COLI_QUERY1_FA = "EColi-query1.fa";
	public static final String IO_PATH = "../Assignment6/io/";
	public static final String OUTPUT_TXT = "output.txt";
	private static FileUtils instance = null;
	private static final DecimalFormat df = new DecimalFormat("#");

	/**
	 * Private constructor for Singleton design pattern purpose. Declared private so it is not accessible from outside.
	 */
	private FileUtils() {
	}

	/**
	 * Gets instance of this class. It will instantiate it if it is not done yet, once.
	 * 
	 * @return
	 */
	public static FileUtils getInstance() {
		if (instance == null) {
			instance = new FileUtils();
		}
		return instance;
	}

	/**
	 * Read the query file and returns the query string by putting the whole sequence into one single line.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public Sequence readQuery(String fileName) throws IOException {
		StringBuffer str = new StringBuffer();
		List<String> lines = readFile(fileName);
		for (int i = 1; i < lines.size(); i++) {
			String line = lines.get(i);
			if (StringUtils.isNotBlank(line)) {
				str.append(line);
			}
		}
		return new Sequence(lines.get(0), str.toString());
	}

	/**
	 * Reads the file name given in io folder and returns a list of strings representing each line in the file.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private List<String> readFile(String fileName) throws IOException {
		return Files.readAllLines(Paths.get(FileUtils.IO_PATH + fileName));
	}

	/**
	 * Reads the sequences from the sequence file name given.
	 * 
	 * @param seqFileName
	 * @return
	 * @throws IOException
	 */
	public List<Sequence> readSequences(String seqFileName) throws IOException {
		List<Sequence> results = new ArrayList<>();
		String header = null;
		List<String> lines = readFile(seqFileName);
		for (int i = 0; i < lines.size(); i++) {
			if (i % 2 != 0) {
				results.add(new Sequence(header, lines.get(i)));
			} else {
				header = lines.get(i);
			}
		}
		return results;
	}

	/**
	 * Deletes the file placed in io folder if it already exists.
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public void deleteIfExists(String fileName) throws IOException {
		Files.deleteIfExists(Paths.get(FileUtils.IO_PATH + fileName));
	}

	/**
	 * Writes the final result to the PrintWriter object passed.
	 * 
	 * @param out
	 * @param affineResult
	 * @param name
	 * @param seqLength
	 */
	public void write(PrintWriter out, AffineResult affineResult, String name, int seqLength) {
		out.print(formatOutput(affineResult, name, seqLength));
	}

	/**
	 * Formats the output to look like BlastN output.
	 * 
	 * @param affineResult
	 * @param name
	 * @param seqLength
	 * @return
	 */
	public String formatOutput(AffineResult affineResult, String name, int seqLength) {
		String queryStr = affineResult.getQueryStr();
		int resultLength = queryStr.length();
		int iIndex = affineResult.getiIndex();
		int jIndex = affineResult.getjIndex();
		String seqStr = affineResult.getSeqStr();
		StringBuffer str = new StringBuffer();
		str.append(name + " (len=" + seqLength + ")\n");
		str.append("Score = " + affineResult.getMaxScore() + " (i=" + iIndex + ", j=" + jIndex + ")\n");
		String similarityString = getSimilarityString(queryStr.toCharArray(), seqStr.toCharArray());
		int identity = getIdentity(similarityString);
		int positives = getPositive(similarityString);
		int queryStrLength = queryStr.length();
		int gaps = getGaps(queryStr, seqStr);
		str.append(String.format("Identities = %s/%s (%s%%), Positives = %s/%s (%s%%), Gaps = %s/%s (%s%%)\n", identity, queryStrLength, getPerc(identity, queryStrLength), positives, queryStrLength, getPerc(positives, queryStrLength), gaps,
				queryStrLength, getPerc(gaps, queryStrLength)));
		str.append(String.format("Query: %5s %s %s\n", iIndex - resultLength + 1, queryStr, iIndex));
		str.append(String.format("\t\t\t %s\n", similarityString));
		str.append(String.format("Sbjct: %5s %s %s\n\n", jIndex - resultLength + 1, seqStr, jIndex));
		return str.toString();
	}

	/**
	 * Counts number of gaps in both query and DB sequence
	 * 
	 * @param queryStr
	 * @param seqStr
	 * @return
	 */
	private int getGaps(String queryStr, String seqStr) {
		int counter = 0;
		for (int i = 0; i < queryStr.length(); i++) {
			if (queryStr.charAt(i) == '-') {
				counter++;
			}
		}
		for (int i = 0; i < seqStr.length(); i++) {
			if (seqStr.charAt(i) == '-') {
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Returns number of characters in similarity String which are not spaces.
	 * 
	 * @param str
	 * @return
	 */
	private int getPositive(String str) {
		int counter = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != ' ') {
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Calculates percentage by following formula: (val1/val2) * 100
	 * 
	 * @param val1
	 * @param val2
	 * @return
	 */
	private String getPerc(int val1, int val2) {
		return df.format((val1 / Double.valueOf(val2)) * 100.0);
	}

	/**
	 * Returns identity by looking at the similarity String and counting all the characters that are not space or plus.
	 * 
	 * @param str
	 * @return
	 */
	private int getIdentity(String str) {
		int counter = 0;
		for (int i = 0; i < str.length(); i++) {
			if (!(str.charAt(i) == '+' || str.charAt(i) == ' ')) {
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Generates similarity string. If the characters are the same, it will be printed as it is. Otherwise, it looks up into scoring matrix and if the
	 * score is zero or a positive number, it will print +, otherwise a space will be used.
	 * 
	 * @param chr1s
	 * @param chr2s
	 * @return
	 */
	private String getSimilarityString(char[] chr1s, char[] chr2s) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < chr1s.length; i++) {
			if (chr1s[i] == chr2s[i]) {
				str.append(chr1s[i]);
			} else {
				str.append(CabiosUtils.getInstance().getScore(chr1s[i], chr2s[i]) >= 0 ? PLUS : SPACE);
			}
		}
		return str.toString();
	}

	/**
	 * Writes the header by printing the query name and string into the given output file name. If the file already exists, it will delete it first.
	 * The method returns PrintWriter pointing the output file.
	 * 
	 * @param headerSequence
	 * @param outputFileName
	 * @return
	 * @throws IOException
	 */
	public PrintWriter writeHeader(Sequence headerSequence, String outputFileName) throws IOException {
		// Deleting the output file if it already exists
		deleteIfExists(outputFileName);
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FileUtils.IO_PATH + outputFileName, true)));
		out.println(headerSequence.getName());
		out.println(headerSequence.getStr());
		out.println();
		return out;
	}
}
