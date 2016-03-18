package com.bio.main.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;

import com.bio.pojo.Output;

public class FileUtils {
	private static final String DOT = ".";
	private static FileUtils instance = null;
	public static final String IO_PATH = "../Assignment5/io/";

	private FileUtils() {
		super();
	}

	public static FileUtils getInstance() {
		if (instance == null) {
			instance = new FileUtils();
		}
		return instance;
	}

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

	private void writeReadsToFile(String header, List<String> tiles, PrintWriter out) throws IOException {
		int counter = 0;
		for (String tile : tiles) {
			// Writing the header
			StringBuffer buffer = new StringBuffer();
			buffer.append(header);
			buffer.append(DOT);
			buffer.append(counter++);
			out.println(buffer.toString());
			out.println(tile);
		}
	}

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

	public List<String> readFile(String fileName) throws IOException {
		return Files.readAllLines(Paths.get(FileUtils.IO_PATH + fileName));
	}

	public void writeFile(Map<String, Output> result, String outputFileName, int fileTileLength) throws IOException {
		String path = FileUtils.IO_PATH + outputFileName;
		Files.deleteIfExists(Paths.get(path));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));
		// Printing the header
		out.println(String.join("\t", "geneID", "readLen" + fileTileLength));
		for (String geneName : result.keySet()) {
			Output output = result.get(geneName);
			out.println(String.join("\t", geneName, output.getMappability()));
		}
		out.close();
	}

}
