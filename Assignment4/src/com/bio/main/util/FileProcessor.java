package com.bio.main.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;

import com.bio.main.pojo.Query;

public class FileProcessor {

	private static final String QUERY = "Query= ";
	public static final String IO_PATH = "../Assignment4/io/";
	private static FileProcessor instance = null;

	private FileProcessor() {
		super();
	}

	public static FileProcessor getInstance() {
		if (instance == null) {
			instance = new FileProcessor();
		}
		return instance;
	}

	public List<Query> readQueries(String fileName) throws IOException {
		List<Query> result = new ArrayList<>();
		// Reading the file line by line
		List<String> fileLines = Files.readAllLines(Paths.get(IO_PATH + fileName));
		for (int fileIndex = 0; fileIndex < fileLines.size(); fileIndex++) {
			String line = fileLines.get(fileIndex);
			// If it is it contains query string
			if (isQueryStr(line)) {
				Query query = new Query();
				MutableInt mutableIndex = new MutableInt(fileIndex);
				findQuery(fileLines, mutableIndex, query);
				fileIndex = mutableIndex.getValue();
				result.add(query);
			}
		}
		return result;
	}

	private void findQuery(List<String> fileLines, MutableInt fileIndex, Query query) {
		// Adding the first line containing the query itself
		String line = fileLines.get(fileIndex.getValue());
		addLine(query, line, fileLines, fileIndex);
		fileIndex.increment();
		// Finding the rest of the query data from file.
		while (!endOfFile(fileLines, fileIndex.getValue())) {
			line = fileLines.get(fileIndex.getValue());
			if (!isQueryStr(line)) {
				addLine(query, line, fileLines, fileIndex);
				fileIndex.increment();
			} else {
				fileIndex.decrement();
				return;
			}
		}
	}

	private void addLine(Query query, String line, List<String> fileLines, MutableInt fileIndex) {
		query.getStr().append(line);
		// Will not add a new line break if the next line is a query string.
		if (!endOfFile(fileLines, fileIndex.getValue() + 1) && !isQueryStr(fileLines.get(fileIndex.getValue() + 1))) {
			query.getStr().append(System.getProperty("line.separator"));
		}
	}

	private boolean endOfFile(List<String> fileLines, int fileIndex) {
		return fileIndex >= fileLines.size();
	}

	private boolean isQueryStr(String line) {
		return StringUtils.contains(line, QUERY);
	}

	public void writeResult(String fileName, List<Query> queries) throws IOException {
		List<String> queryStrings = new ArrayList<>();
		for (Query query : queries) {
			queryStrings.add(query.getStr().toString());
		}
		FileUtils.writeLines(new File(IO_PATH + fileName), queryStrings);
	}
}
