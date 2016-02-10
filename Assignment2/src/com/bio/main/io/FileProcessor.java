package com.bio.main.io;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bio.main.pojo.RefSeq;

/**
 * The main class in charge of reading from and writing to a file.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class FileProcessor {

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
	 * Reads the file given and constructs a list of {@link RefSeq} which
	 * contains a header and string.
	 * 
	 * @param filePath
	 * @param geneId
	 * @return
	 * @throws IOException
	 */
	public List<RefSeq> readAnnorationFile(String filePath) throws IOException {
		List<RefSeq> result = new ArrayList<>();

		// Reading the whole file line by line
		List<String> lines = Files.readAllLines(Paths.get(filePath));
		Iterator<String> iterator = lines.iterator();

		while (iterator.hasNext()) {
			result.add(new RefSeq(iterator.next(), iterator.next()));
		}

		return result;
	}

	/**
	 * Reads the chromosome file given and puts it into a string.
	 * 
	 * @param chrFilePath
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public String readChromoseFile(String chrFilePath) throws UnsupportedEncodingException, IOException {
		return new String(Files.readAllBytes(Paths.get(chrFilePath)), "UTF-8");
	}

}
