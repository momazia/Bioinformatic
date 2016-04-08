package com.bio.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.bio.pojo.AffineResult;
import com.bio.pojo.Sequence;

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

	public String readDb(String fileName) throws IOException {
		StringBuffer str = new StringBuffer();
		List<String> lines = readFile(fileName);
		for (int i = 1; i < lines.size(); i++) {
			String line = lines.get(i);
			if (StringUtils.isNotBlank(line)) {
				str.append(line);
			}
		}
		return str.toString();
	}

	private List<String> readFile(String fileName) throws IOException {
		return Files.readAllLines(Paths.get(FileUtils.IO_PATH + fileName));
	}

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

	public void deleteIfExists(String fileName) throws IOException {
		Files.deleteIfExists(Paths.get(FileUtils.IO_PATH + fileName));
	}

	public void write(PrintWriter out, AffineResult affineResult, String name, int length) {
		out.println(name.substring(0, 50) + " (len=" + length + ")");
		out.println("SW_score = " + affineResult.getMaxScore() + " (i=" + affineResult.getiIndex() + ", j=" + affineResult.getjIndex() + ")");
		out.println("Query: " + affineResult.getSeqStr());
		out.println("DB: 	" + affineResult.getDbStr());
		out.println();
	}
}
