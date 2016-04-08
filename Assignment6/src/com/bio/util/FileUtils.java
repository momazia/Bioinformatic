package com.bio.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class FileUtils {
	public static final String SWISSPROT_100_FA = "swissprot-100.fa";
	public static final String E_COLI_QUERY1_FA = "EColi-query1.fa";
	private static FileUtils instance = null;
	public static final String IO_PATH = "../Assignment6/io/";

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

	public List<String> readSequences(String seqFileName) throws IOException {
		List<String> lines = readFile(seqFileName);
		List<String> results = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++) {
			if (i % 2 != 0) {
				results.add(lines.get(i));
			}
		}
		return results;
	}
}
