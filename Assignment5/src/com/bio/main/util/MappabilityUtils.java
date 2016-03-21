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

/**
 * The main utility class which is in charge of mappability related operations.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class MappabilityUtils {
	private static MappabilityUtils instance = null;

	/**
	 * Private constructor for Singleton design pattern purpose. Declared private so it is not accessible from outside.
	 */
	private MappabilityUtils() {
	}

	/**
	 * Gets instance of this class. It will instantiate it if it is not done yet, once.
	 * 
	 * @return
	 */
	public static MappabilityUtils getInstance() {
		if (instance == null) {
			instance = new MappabilityUtils();
		}
		return instance;
	}

	/**
	 * The main method to check the mappabilities of the given read file names.
	 * 
	 * @param btOutputFileName
	 *            BowTie output file name
	 * @param fileTileLength
	 *            Tile length
	 * @param outputFileName
	 *            Output file name which the result will be saved into
	 * @param readFileName
	 *            Reads file name
	 * @param chr1FileLines
	 *            chromosome file name
	 * @throws IOException
	 */
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

	/**
	 * Creating a map which its key is a gene name and its value is the mappability. It also calculates the number of tiles which are within the gene.
	 * 
	 * @param btOutputFileLines
	 *            BowTie output file name
	 * @param readFileLines
	 *            Reads file content in a list
	 * @param chr1FileLines
	 *            chromosome file name in a list
	 * @return
	 * @throws IOException
	 */
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

	/**
	 * Calculates the total number of tiles by counting the occurance of the gene name in the reads file content list passed.
	 * 
	 * @param geneName
	 * @param readFileLines
	 * @return
	 * @throws IOException
	 */
	private Integer getTotalNumberOfTiles(String geneName, List<String> readFileLines) throws IOException {
		int counter = 0;
		for (String line : readFileLines) {
			if (line.contains(geneName)) {
				counter++;
			}
		}
		return counter;
	}

	/**
	 * A tile is within a gene if its start and end indexes are within the gene's index range.
	 * 
	 * @param tileStart
	 * @param tileEnd
	 * @param gene
	 * @return
	 */
	private boolean isTileWithinGene(Integer tileStart, Integer tileEnd, Gene gene) {
		return gene.getStartIndex() <= tileStart && tileEnd <= gene.getEndIndex();
	}

	/**
	 * Extracts the geneID out of the splitted file line.
	 * 
	 * @param splittedLine
	 * @return
	 */
	private Gene getGeneId(String[] splittedLine) {
		String[] geneSplittedStr = StringUtils.split(splittedLine[3], FileUtils.SEPARATOR_DOT);
		return new Gene(Integer.valueOf(geneSplittedStr[1]), Integer.valueOf(geneSplittedStr[2]), StringUtils.substringBeforeLast(splittedLine[3], FileUtils.SEPARATOR_DOT));
	}

	/**
	 * Gets the tile end index by looking at the 3rd element in the given splitted line
	 * 
	 * @param splittedLine
	 * @return
	 */
	private Integer getTileEndIndex(String[] splittedLine) {
		return Integer.valueOf(splittedLine[2]);
	}

	/**
	 * Gets the tile end index by looking at the 2nd element in the given splitted line
	 * 
	 * @param splittedLine
	 * @return
	 */
	private Integer getTileStartIndex(String[] splittedLine) {
		return Integer.valueOf(splittedLine[1]);
	}
}
