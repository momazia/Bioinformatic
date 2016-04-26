package com.bio.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bio.pojo.RefSeq;

/**
 * The main utility class related to mappability.
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
		super();
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
	 * The method counts the number of reads on Exons and then convert the count to gene level. Later it will save the result into outputFileName
	 * file.
	 * 
	 * @param exonAnnotationFileName
	 * @param bowTieOutputFileName
	 * @param outputFileName
	 * @throws IOException
	 */
	public void run(String exonAnnotationFileName, String bowTieOutputFileName, String outputFileName) throws IOException {
		// Counting mapped reads on each Exon.
		Map<String, Integer> countMappedReadsOnExons = countMappedReadsOnExons(exonAnnotationFileName, bowTieOutputFileName);
		// Converting the count to gene level
		Map<String, Integer> countMappedReadsOnGenes = convertCounterToGeneLevel(countMappedReadsOnExons);
		// Writing the end result to output file.
		saveGeneExpressionResult(outputFileName, countMappedReadsOnGenes);
		System.out.println("Done!");
	}

	/**
	 * Saves the gene expression result into outputFileName for all the given counters.
	 * 
	 * @param outputFileName
	 * @param countMappedReadsOnGenes
	 * @throws IOException
	 */
	private void saveGeneExpressionResult(String outputFileName, Map<String, Integer> countMappedReadsOnGenes) throws IOException {
		System.out.println("Saving the final result...");
		PrintWriter out = FileUtils.getInstance().getPrinterWriter(outputFileName);
		for (String geneId : countMappedReadsOnGenes.keySet()) {
			out.println(String.format("%s\t%s", geneId, countMappedReadsOnGenes.get(geneId)));
		}
		out.close();
	}

	/**
	 * Convers the Exon level counter into gene level counter by checking the prefix of the Exons.
	 * 
	 * @param countMappedReadsOnExons
	 * @return
	 */
	public Map<String, Integer> convertCounterToGeneLevel(Map<String, Integer> countMappedReadsOnExons) {
		System.out.println("Converting the counter to Gene level...");
		Map<String, Integer> result = new HashMap<>();
		// Initializing the gene counter
		for (String exonId : countMappedReadsOnExons.keySet()) {
			String[] columns = exonId.split(FileUtils.UNDER_SCORE);
			String geneId = columns[0] + FileUtils.UNDER_SCORE + columns[1];
			result.put(geneId, Integer.valueOf(0));
		}
		// Counting by looking at the prefix of the exonId to see if it is the same geneId.
		for (String geneId : result.keySet()) {
			for (String exonId : countMappedReadsOnExons.keySet()) {
				if (exonId.startsWith(geneId)) {
					result.put(geneId, result.get(geneId) + countMappedReadsOnExons.get(exonId));
				}
			}
		}
		return result;
	}

	/**
	 * Counts the number of reads mapped on Exons using the bow tie output and the Exon annotation file.
	 * 
	 * @param exonAnnotationFileName
	 * @param bowTieOutputFileName
	 * @return
	 * @throws IOException
	 */
	public Map<String, Integer> countMappedReadsOnExons(String exonAnnotationFileName, String bowTieOutputFileName) throws IOException {
		List<RefSeq> refSeqs = ExonMaskUtils.getInstance().readRefSeqs(exonAnnotationFileName);
		List<RefSeq> bowTieRefSeqs = readBowTieOutput(bowTieOutputFileName);
		Map<String, Integer> countMap = new HashMap<>();
		// Initializing the counter map
		System.out.println("Initializing the counter map...");
		for (RefSeq refSeq : refSeqs) {
			countMap.put(refSeq.getId(), Integer.valueOf(0));
		}
		// Counting ...
		System.out.println("Counting the mapped reads on each exons...");
		for (RefSeq btSeq : bowTieRefSeqs) {
			for (RefSeq refSeq : refSeqs) {
				if (refSeq.getStartIndex() <= btSeq.getStartIndex() && btSeq.getEndIndex() <= refSeq.getEndIndex()) {
					String id = refSeq.getId();
					countMap.put(id, countMap.get(id) + 1);
				}
			}
		}
		return countMap;
	}

	/**
	 * Reads the bow tie output file and converts the result into a list of RefSeqs.
	 * 
	 * @param bowTieOutputFileName
	 * @return
	 * @throws IOException
	 */
	private List<RefSeq> readBowTieOutput(String bowTieOutputFileName) throws IOException {
		List<String> btOutputLines = FileUtils.getInstance().readFile(bowTieOutputFileName);
		List<RefSeq> result = new ArrayList<>();
		for (String line : btOutputLines) {
			String[] columns = line.split(FileUtils.TAB);
			result.add(new RefSeq(Integer.valueOf(columns[1]), Integer.valueOf(columns[2]), columns[3]));
		}
		return result;
	}
}
