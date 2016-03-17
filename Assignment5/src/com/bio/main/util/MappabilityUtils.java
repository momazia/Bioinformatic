package com.bio.main.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

import com.bio.pojo.Gene;
import com.bio.pojo.Output;

public class MappabilityUtils {
	private static final String SEPARATOR_DOT = ".";
	private static final String SEPARATOR_TAB = "\t";
	private static MappabilityUtils instance = null;

	private MappabilityUtils() {
		super();
	}

	public static MappabilityUtils getInstance() {
		if (instance == null) {
			instance = new MappabilityUtils();
		}
		return instance;
	}

	public void checkMappability(String btOutputFileName, int fileTileLength, String outputFileName, String readFileName) throws IOException {
		System.out.println("Creating [" + outputFileName + "]...");
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<String, Output> result = new HashMap<>();
		List<String> fileLines = FileUtils.getInstance().readFile(btOutputFileName);
		for (String line : fileLines) {
			String[] splittedLine = StringUtils.split(line, SEPARATOR_TAB);
			Integer tileStart = getTileStartIndex(splittedLine);
			Integer tileEnd = getTileEndIndex(splittedLine);
			Gene gene = getGeneId(splittedLine);
			String geneName = gene.getName();
			if (!result.containsKey(geneName)) {
				result.put(geneName, new Output(0, getTotalNumberOfTiles(geneName, readFileName)));
			}
			Output output = result.get(geneName);
			if (isTileWithinGene(tileStart, tileEnd, gene)) {
				output.addNumberOfMatches();
			}
			result.put(geneName, output);
		}
		FileUtils.getInstance().writeFile(result, outputFileName, fileTileLength);
		stopWatch.stop();
		System.out.println("[" + outputFileName + "] is done in [" + TimeUnit.MILLISECONDS.convert(stopWatch.getNanoTime(), TimeUnit.NANOSECONDS) + "] MILLISECONDS");
	}

	private Integer getTotalNumberOfTiles(String geneName, String readFileName) throws IOException {
		List<String> readFile = FileUtils.getInstance().readFile(readFileName);
		int counter = 0;
		for (String line : readFile) {
			if (line.contains(geneName)) {
				counter++;
			}
		}
		return counter;
	}

	private boolean isTileWithinGene(Integer tileStart, Integer tileEnd, Gene gene) {
		return gene.getStartIndex() <= tileStart && tileEnd <= gene.getEndIndex();
	}

	private Gene getGeneId(String[] splittedLine) {
		String[] geneSplittedStr = StringUtils.split(splittedLine[3], SEPARATOR_DOT);
		return new Gene(Integer.valueOf(geneSplittedStr[1]), Integer.valueOf(geneSplittedStr[2]), geneSplittedStr[3]);
	}

	private Integer getTileEndIndex(String[] splittedLine) {
		return Integer.valueOf(splittedLine[2]);
	}

	private Integer getTileStartIndex(String[] splittedLine) {
		return Integer.valueOf(splittedLine[1]);
	}
}
