package com.bio.main.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

import com.bio.pojo.Gene;
import com.bio.pojo.Mappability;

public class MappabilityUtils {
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

	public void checkMappability(String btOutputFileName, int fileTileLength, String outputFileName, String readFileName, List<String> chr1FileLines) throws IOException {
		System.out.println("Creating [" + outputFileName + "]...");
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		System.out.println("Reading [" + btOutputFileName + "]");
		List<String> btOutputFileLines = FileUtils.getInstance().readFile(btOutputFileName);
		System.out.println("Done reading [" + btOutputFileName + "]");
		System.out.println("Reading [" + readFileName + "]");
		List<String> readFileLines = FileUtils.getInstance().readFile(readFileName);
		System.out.println("Done reading [" + readFileName + "]");
		Map<String, Mappability> result = createMappability(btOutputFileLines, readFileLines, chr1FileLines);
		FileUtils.getInstance().writeFile(result, outputFileName, fileTileLength);
		stopWatch.stop();
		System.out.println("[" + outputFileName + "] is done in [" + TimeUnit.MILLISECONDS.convert(stopWatch.getNanoTime(), TimeUnit.NANOSECONDS) + "] MILLISECONDS");
	}

	private Map<String, Mappability> createMappability(List<String> btOutputFileLines, List<String> readFileLines, List<String> chr1FileLines) throws IOException {
		Map<String, Mappability> result = new HashMap<>();
		for (String btOutputLine : btOutputFileLines) {
			String[] splittedLine = StringUtils.split(btOutputLine, FileUtils.SEPARATOR_TAB);
			Integer tileStart = getTileStartIndex(splittedLine);
			Integer tileEnd = getTileEndIndex(splittedLine);
			Gene gene = getGeneId(splittedLine);
			String geneName = gene.getName();
			if (!result.containsKey(geneName)) {
				result.put(geneName, new Mappability(0, getTotalNumberOfTiles(geneName, readFileLines)));
			}
			Mappability output = result.get(geneName);
			if (isTileWithinGene(tileStart, tileEnd, gene)) {
				output.addNumberOfMatches();
			}
			result.put(geneName, output);
		}
		// Add the missing tiles from output
		for (int lineIndex = 0; lineIndex < chr1FileLines.size(); lineIndex++) {
			if (lineIndex % 2 == 0) {
				// Reading the first line
				String geneName = StringUtils.substringAfter(chr1FileLines.get(lineIndex), FileUtils.CHR_INDICATOR);
				if (!result.containsKey(geneName)) {
					result.put(geneName, null);
				}
			}
			chr1FileLines.get(lineIndex);
		}
		return result;
	}

	private Integer getTotalNumberOfTiles(String geneName, List<String> readFileLines) throws IOException {
		int counter = 0;
		for (String line : readFileLines) {
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
		String[] geneSplittedStr = StringUtils.split(splittedLine[3], FileUtils.SEPARATOR_DOT);
		return new Gene(Integer.valueOf(geneSplittedStr[1]), Integer.valueOf(geneSplittedStr[2]), StringUtils.substringBeforeLast(splittedLine[3], FileUtils.SEPARATOR_DOT));
	}

	private Integer getTileEndIndex(String[] splittedLine) {
		return Integer.valueOf(splittedLine[2]);
	}

	private Integer getTileStartIndex(String[] splittedLine) {
		return Integer.valueOf(splittedLine[1]);
	}
}
