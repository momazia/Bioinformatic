package com.bio.main.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.input.BoundedInputStream;

import com.bio.main.pojo.RefSeq;

/**
 * The main class in charge of reading from and writing to a file.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class FileProcessor {

	private static FileProcessor instance;
	private static final int chr_FA_STARTING_BYTES = 6; // Includes '\n' at the
														// end of the first
														// line.

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
	 * Reads the chromosome file given and puts it into a string. It skips the
	 * first 6 bytes containing text ">chr" and replaces it with an empty space.
	 * 
	 * @param chrFilePath
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public String readChromoseFile(String chrFilePath) throws UnsupportedEncodingException, IOException {

		FileInputStream file = new FileInputStream(chrFilePath);
		// Since the file given is big, that is why we want to skip the
		// unnecessary part of the file instead of reading the whole file.
		file.skip(chr_FA_STARTING_BYTES);

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new BoundedInputStream(file)))) {

			int fileChar;
			StringBuilder response = new StringBuilder();

			// Adding an empty character to the beginning of the string.
			response.append(' ');

			while ((fileChar = br.read()) != -1) {
				// Making sure to only put the letters in the string. This is to
				// exclude '\n' (New lines)
				if (Character.isLetter(fileChar)) {
					response.append((char) fileChar);
				}
			}
			return response.toString();
		}
	}

}
