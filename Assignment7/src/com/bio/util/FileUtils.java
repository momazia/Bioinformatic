package com.bio.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.input.BoundedInputStream;

public class FileUtils {

	public static final String IO_PATH = "../Assignment7/io/";
	private static FileUtils instance = null;
	public static final String HG19_REFSEQ_EXON_ANNOT = "HG19-refseq-exon-annot-chr1-2016";
	public static final String CHR1 = "chr1.fa";
	public static final String MASKED_CHR1 = "chr1_masked.fa";
	public static final String COLLAPSED_EXON = "collapsed_exon.txt";

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
	 * Reads the file name given in io folder and returns a list of strings representing each line in the file.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public List<String> readFile(String fileName) throws IOException {
		return Files.readAllLines(Paths.get(FileUtils.IO_PATH + fileName));
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

	public FileInputStream getFileInputStream(String fileName) throws FileNotFoundException {
		return new FileInputStream(FileUtils.IO_PATH + fileName);
	}

	public BufferedReader getBufferReader(FileInputStream fileName) {
		return new BufferedReader(new InputStreamReader(new BoundedInputStream(fileName)));
	}

	public PrintWriter getPrinterWriter(String fileName) throws IOException {
		return new PrintWriter(new BufferedWriter(new FileWriter(FileUtils.IO_PATH + fileName, true)));
	}

}