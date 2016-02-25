package com.bio.main.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bio.main.pojo.Sequence;

/**
 * The main class in charge of reading from a file.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class FileProcessor {

	/**
	 * Default path to read the file from
	 */
	public static final String IO_PATH = "../Assignment3/io/";

	private static FileProcessor instance = null;

	/**
	 * Not accessible because of Singleton Design Pattern.
	 */
	private FileProcessor() {
		super();
	}

	public static FileProcessor getInstance() {
		if (instance == null) {
			instance = new FileProcessor();
		}
		return instance;
	}

	/**
	 * For the given file name, it will read the sequences headers and strings from the file and return them in a list.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 *             If file cannot be read
	 */
	public List<Sequence> readSequences(String fileName) throws IOException {

		List<Sequence> result = new ArrayList<>();

		// Reading the whole file line by line
		List<String> lines = Files.readAllLines(Paths.get(IO_PATH + fileName));
		Iterator<String> iterator = lines.iterator();

		while (iterator.hasNext()) {
			result.add(new Sequence(iterator.next(), iterator.next()));
		}

		return result;
	}
}
