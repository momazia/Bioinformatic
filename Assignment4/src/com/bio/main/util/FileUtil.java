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

import com.bio.main.pojo.BlastNRecord;
import com.bio.main.pojo.Database;
import com.bio.main.pojo.Query;

/**
 * This is a utility class in charge of the operations related to reading/saving to files.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class FileUtil {
	/**
	 * List of constants
	 */
	public static final String SEPARATOR = ">";
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

	/**
	 * Creates a database by reading the BlastN output file name given.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public Database readBlastNRecords(String fileName) throws IOException {
		System.out.println("Reading file [" + fileName + "]...");
		// Reading the file line by line
		List<BlastNRecord> records = new ArrayList<>();
		List<String> fileLines = Files.readAllLines(Paths.get(IO_PATH + fileName));
		for (int fileIndex = 0; fileIndex < fileLines.size(); fileIndex++) {
			String line = fileLines.get(fileIndex);
			// If it contains query string
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

	/**
	 * Populates the record object passed. It also changes the fileIndex as it reads through the file lines passed.
	 * 
	 * @param fileLines
	 * @param fileIndex
	 * @param record
	 */
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

	/**
	 * Populates str property of the given BlastN record. If it is a query string line we are looking at, it will also populate Query string property
	 * of BlastN record.
	 * 
	 * @param record
	 * @param line
	 * @param fileLines
	 * @param fileIndex
	 * @param isQueryString
	 */
	private void addLine(BlastNRecord record, String line, List<String> fileLines, MutableInt fileIndex, boolean isQueryString) {
		if (isQueryString) {
			record.setQueryString(StringUtils.substringAfter(line, QUERY));
		}
		record.getStr().append(line);
		record.getStr().append(System.getProperty("line.separator"));
	}

	/**
	 * Checks to see if we are looking at the last line of the list given.
	 * 
	 * @param fileLines
	 * @param fileIndex
	 * @return
	 */
	private boolean endOfFile(List<String> fileLines, int fileIndex) {
		return fileIndex >= fileLines.size();
	}

	/**
	 * Checks to see if constant {@link FileUtil#QUERY} exists in the line passed.
	 * 
	 * @param line
	 * @return
	 */
	private boolean isQueryString(String line) {
		return StringUtils.contains(line, QUERY);
	}

	/**
	 * Reads the query list from the source file path given and for each of them, it writes them into the target file path if they are not registered
	 * as a duplicate query.
	 * 
	 * @param targetFilePath
	 * @param sourceFilePath
	 * @param duplicateQueries
	 * @throws IOException
	 */
	public void copyFileExcludeRedundantQueries(String targetFilePath, String sourceFilePath, Set<String> duplicateQueries) throws IOException {
		System.out.println("Copying the unique queries into [" + targetFilePath + "]..");
		List<Query> queries = readQueries(sourceFilePath);
		for (Query query : queries) {
			if (!duplicateQueries.contains(query.getName())) {
				// FileUtils.writeStringToFile(new File(IO_PATH + targetFilePath), query.getStr(), true);
				StringBuffer strBuffer = new StringBuffer();
				strBuffer.append(SEPARATOR);
				strBuffer.append(query.getName());
				strBuffer.append(System.getProperty("line.separator"));
				FileUtils.writeStringToFile(new File(IO_PATH + targetFilePath), strBuffer.toString(), true);
			}
		}
	}

	/**
	 * Creates a list of queries from the source file path given.
	 * 
	 * @param sourceFilePath
	 * @return
	 * @throws IOException
	 */
	public List<Query> readQueries(String sourceFilePath) throws IOException {
		List<Query> result = new ArrayList<>();
		String fileContent = new String(Files.readAllBytes(Paths.get(IO_PATH + sourceFilePath)));
		// Splitting the file content using ">"
		String[] splitStrs = StringUtils.split(fileContent, SEPARATOR);
		for (String str : splitStrs) {
			String queryName = StringUtils.substringBefore(str, "\n");
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append(SEPARATOR);
			strBuffer.append(str);
			String queryStr = strBuffer.toString();
			result.add(new Query(queryName, queryStr));
		}
		return result;
	}
}
