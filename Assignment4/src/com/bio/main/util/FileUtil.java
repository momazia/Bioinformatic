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

import com.bio.main.pojo.MicrobiomeDB;
import com.bio.main.pojo.Query;

public class FileUtil {

	private static final String QUERY = "Query= ";
	public static final String IO_PATH = "../Assignment4/io/";
	private static FileUtil instance = null;

	private FileUtil() {
		super();
	}

	public static FileUtil getInstance() {
		if (instance == null) {
			instance = new FileUtil();
		}
		return instance;
	}

	public MicrobiomeDB readQueries(String fileName) throws IOException {
		// Reading the file line by line
		List<Query> queries = new ArrayList<>();
		List<String> fileLines = Files.readAllLines(Paths.get(IO_PATH + fileName));
		boolean isHeader = true;
		StringBuffer header = new StringBuffer();
		for (int fileIndex = 0; fileIndex < fileLines.size(); fileIndex++) {
			String line = fileLines.get(fileIndex);
			// If it is it contains query string
			if (isQueryStr(line)) {
				Query query = new Query();
				MutableInt mutableIndex = new MutableInt(fileIndex);
				findQuery(fileLines, mutableIndex, query);
				fileIndex = mutableIndex.getValue();
				queries.add(query);
				isHeader = false;
			}
			// Including the header of the file
			if (isHeader == true) {
				header.append(line);
				header.append(System.getProperty("line.separator"));
			}
		}
		return new MicrobiomeDB(queries, header);
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
		query.getStr().append(System.getProperty("line.separator"));
	}

	private boolean endOfFile(List<String> fileLines, int fileIndex) {
		return fileIndex >= fileLines.size();
	}

	private boolean isQueryStr(String line) {
		return StringUtils.contains(line, QUERY);
	}

	public void saveQuery(String fileName, Query query) throws IOException {
		FileUtils.write(new File(IO_PATH + fileName), query.getStr(), true);
	}

	public void saveHeaderFile(String fileName, String header) throws IOException {
		FileUtils.write(new File(IO_PATH + fileName), header);
	}
}
