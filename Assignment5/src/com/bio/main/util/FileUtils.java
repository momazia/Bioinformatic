package com.bio.main.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

import com.bio.pojo.Mappability;

/**
 * The main utility class which is in charge of all the file related operations
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class FileUtils {
	// Constants used for reading from and writing to the files.
	private static final String ZERO = "0.00";
	private static final String GENE_ID_HEADER = "geneID";
	public static final String SEPARATOR_DOT = ".";
	public static final String SEPARATOR_TAB = "\t";
	public static final String CHR_INDICATOR = ">";
	public static final String IO_PATH = "../Assignment5/io/";
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

	/**
	 * Generates Reads for the given read length and save it into the output file name passed to this method. If the file output exists, it will
	 * overwrite it.
	 * 
	 * @param readLength
	 *            The read length
	 * @param outputFileName
	 *            The output file name to save the reads to
	 * @param chr1FileName
	 *            The chromosome source file to generate the reads from
	 * @throws IOException
	 */
	public void createReads(int readLength, String outputFileName, String chr1FileName) throws IOException {
		System.out.println("Creating [" + outputFileName + "]...");
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		String outputPath = FileUtils.IO_PATH + outputFileName;
		Files.deleteIfExists(Paths.get(outputPath));
		List<String> lines = readFile(chr1FileName);
		String header = null;
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputPath, true)));
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (i % 2 == 0) {
				// Reading the gene name
				header = line;
			} else {
				// Reading the sequence
				List<String> tiles = createTiles(readLength, line);
				writeReadsToFile(header, tiles, out);
			}
		}
		out.close();
		stopWatch.stop();
		System.out.println("[" + outputFileName + "] is done in [" + TimeUnit.MILLISECONDS.convert(stopWatch.getNanoTime(), TimeUnit.NANOSECONDS) + "] MILLISECONDS");
	}

	/**
	 * Writes the tiles given into the output file by adding a counter at the end of the gene name.
	 * 
	 * @param header
	 *            Gene name
	 * @param tiles
	 *            The list of tiles generated for a given gene sequence
	 * @param out
	 *            the output file PrintWriter
	 * @throws IOException
	 */
	private void writeReadsToFile(String header, List<String> tiles, PrintWriter out) throws IOException {
		int counter = 0;
		for (String tile : tiles) {
			// Writing the header
			StringBuffer buffer = new StringBuffer();
			buffer.append(header);
			buffer.append(SEPARATOR_DOT);
			buffer.append(counter++);
			out.println(buffer.toString());
			out.println(tile);
		}
	}

	/**
	 * Given a gene sequence and a read length, it generates all the possible tiles.
	 * 
	 * @param readLength
	 *            Desired read length
	 * @param line
	 *            The main string to create the tiles from
	 * @return The list of tiles
	 */
	public List<String> createTiles(int readLength, String line) {
		List<String> result = new ArrayList<>();
		if (readLength < line.length()) {
			for (int index = 0; index < line.length() - readLength + 1; index++) {
				result.add(line.substring(index, index + readLength));
			}
		} else {
			result.add(line);
		}
		return result;
	}

	/**
	 * Given the file name, it reads all its content and put it into a list.
	 * 
	 * @param fileName
	 *            File name to read
	 * @return
	 * @throws IOException
	 */
	public List<String> readFile(String fileName) throws IOException {
		return Files.readAllLines(Paths.get(FileUtils.IO_PATH + fileName));
	}

	/**
	 * Writes the given map of mappabilities into the output file.
	 * 
	 * @param result
	 *            A map which the key is the gene names and the value is the mappability related to that gene.
	 * @param outputFileName
	 *            The name of the file to save the result into.
	 * @param fileTileLength
	 *            The length of the tile, this is used to generate the header
	 * @throws IOException
	 */
	public void writeFile(Map<String, Mappability> result, String outputFileName, int fileTileLength) throws IOException {
		String path = FileUtils.IO_PATH + outputFileName;
		Files.deleteIfExists(Paths.get(path));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));
		// Printing the header
		out.println(String.join(SEPARATOR_TAB, GENE_ID_HEADER, "readLen" + fileTileLength));
		for (String geneName : result.keySet()) {
			Mappability output = result.get(geneName);
			if (output == null) {
				out.println(String.join(SEPARATOR_TAB, geneName, ZERO));
			} else {
				out.println(String.join(SEPARATOR_TAB, geneName, output.getMappability()));
			}
		}
		out.close();
	}

	/**
	 * Merges any given number of files into one. It overwrites if the output already exists.
	 * 
	 * @param outputFileName
	 *            The output file name.
	 * @param inputFileNames
	 *            The list of input File names
	 * @throws IOException
	 */
	public void merge(String outputFileName, String... inputFileNames) throws IOException {
		Map<String, List<String>> file = readFileIntoMap(inputFileNames);
		String path = FileUtils.IO_PATH + outputFileName;
		Files.deleteIfExists(Paths.get(path));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));
		// Printing the header
		StringBuffer stringBuffer = createLine(file, GENE_ID_HEADER);
		out.println(stringBuffer);
		file.remove(GENE_ID_HEADER);
		// Printing the body
		for (String geneId : file.keySet()) {
			stringBuffer = createLine(file, geneId);
			out.println(stringBuffer);
		}
		out.close();
	}

	/**
	 * It constructs a line in merging process for each mappability type.
	 * 
	 * @param file
	 *            The content of the whole mappabilities.
	 * @param geneId
	 *            the Desired geneId which the line must be created for.
	 * @return
	 */
	private StringBuffer createLine(Map<String, List<String>> file, String geneId) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(geneId);
		stringBuffer.append(SEPARATOR_TAB);
		stringBuffer.append(String.join(SEPARATOR_TAB, file.get(geneId)));
		return stringBuffer;
	}

	/**
	 * For the given file names, it reads the files and put them into a map in which the key is the gene names and the value is the mappability
	 * related to that gene.
	 * 
	 * @param inputFileNames
	 * @return
	 * @throws IOException
	 */
	private Map<String, List<String>> readFileIntoMap(String[] inputFileNames) throws IOException {
		Map<String, List<String>> result = new HashMap<>();
		for (String fileName : inputFileNames) {
			List<String> lines = readFile(fileName);
			for (String line : lines) {
				String[] splittedLine = StringUtils.splitByWholeSeparator(line, SEPARATOR_TAB);
				String geneId = splittedLine[0];
				if (!result.containsKey(geneId)) {
					result.put(geneId, new ArrayList<>());
				}
				List<String> existingMappabilities = result.get(geneId);
				existingMappabilities.add(splittedLine[1]);
				result.put(geneId, existingMappabilities);
			}
		}
		return result;
	}

}
