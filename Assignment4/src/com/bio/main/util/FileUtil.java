package com.bio.main.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;

import com.bio.main.pojo.Database;
import com.bio.main.pojo.BlastNRecord;

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

	public Database readBlastNRecords(String fileName) throws IOException {
		// Reading the file line by line
		List<BlastNRecord> records = new ArrayList<>();
		List<String> fileLines = Files.readAllLines(Paths.get(IO_PATH + fileName));
		for (int fileIndex = 0; fileIndex < fileLines.size(); fileIndex++) {
			String line = fileLines.get(fileIndex);
			// If it is it contains query string
			if (isQueryString(line)) {
				BlastNRecord record = new BlastNRecord();
				MutableInt mutableIndex = new MutableInt(fileIndex);
				findQuery(fileLines, mutableIndex, record);
				fileIndex = mutableIndex.getValue();
				records.add(record);
			}
		}
		return new Database(records);
	}

	private void findQuery(List<String> fileLines, MutableInt fileIndex, BlastNRecord record) {
		// Adding the first line containing the query itself
		String line = fileLines.get(fileIndex.getValue());
		addLine(record, line, fileLines, fileIndex, true);
		fileIndex.increment();
		// Finding the rest of the record data from file.
		while (!endOfFile(fileLines, fileIndex.getValue())) {
			line = fileLines.get(fileIndex.getValue());
			if (!isQueryString(line)) {
				addLine(record, line, fileLines, fileIndex, false);
				fileIndex.increment();
			} else {
				fileIndex.decrement();
				return;
			}
		}
	}

	private void addLine(BlastNRecord record, String line, List<String> fileLines, MutableInt fileIndex, boolean isQueryString) {
		if (isQueryString) {
			record.setQueryString(StringUtils.substringAfter(line, QUERY));
		}
		record.getStr().append(line);
		record.getStr().append(System.getProperty("line.separator"));
	}

	private boolean endOfFile(List<String> fileLines, int fileIndex) {
		return fileIndex >= fileLines.size();
	}

	private boolean isQueryString(String line) {
		return StringUtils.contains(line, QUERY);
	}

	public void copyFileExcludeRedundantQueries(String targetFilePath, String sourceFilePath, Set<String> duplicateQueries) throws IOException {
		// Reading the source file line by line.
		List<String> sourceFileLines = Files.readAllLines(Paths.get(IO_PATH + sourceFilePath));
		for (int fileIndex = 0; fileIndex < sourceFileLines.size();) {
			String queryFromFile = getQueryString(sourceFileLines, fileIndex);
			while (hasMoreQueries(sourceFileLines, fileIndex)) {
				if (!duplicateQueries.contains(queryFromFile)) {
					FileUtils.write(new File(IO_PATH + targetFilePath), sourceFileLines.get(fileIndex) + System.getProperty("line.separator"), true);
				}
				fileIndex++;
			}
		}
	}

	private boolean hasMoreQueries(List<String> sourceFileLines, int fileIndex) {
		return !endOfFile(sourceFileLines, fileIndex) && StringUtils.isNotBlank(getQueryString(sourceFileLines, fileIndex));
	}

	private String getQueryString(List<String> sourceFileLines, int fileIndex) {
		if (fileIndex >= sourceFileLines.size()) {
			return null;
		}
		return StringUtils.substringAfter(sourceFileLines.get(fileIndex), ">");
	}

}
