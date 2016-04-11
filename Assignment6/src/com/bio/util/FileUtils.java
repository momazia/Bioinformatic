package com.bio.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
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
	public static final String SWISSPROT_100_FA = "swissprot-100.fa";
	public static final String E_COLI_QUERY1_FA = "EColi-query1.fa";
	public static final String IO_PATH = "../Assignment6/io/";
	public static final String OUTPUT_TXT = "output.txt";
	private static FileUtils instance = null;

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
	 * @param length
	 */
	public void write(PrintWriter out, AffineResult affineResult, String name, int length) {
		out.println(name + " (len=" + length + ")");
		out.println("SW_score = " + affineResult.getMaxScore() + " (i=" + affineResult.getiIndex() + ", j=" + affineResult.getjIndex() + ")");
		out.println("Query\t" + affineResult.getQueryStr());
		out.println("Sbjct\t" + affineResult.getSeqStr());
		out.println();
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
