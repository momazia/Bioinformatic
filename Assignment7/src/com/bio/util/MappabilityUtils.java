package com.bio.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bio.pojo.RefSeq;

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

	public void run(String exonAnnotationFileName, String bowTieOutputFileName, String outputFileName) throws IOException {
		// Counting mapped reads on each Exon.
		Map<String, Integer> countMappedReadsOnExons = countMappedReadsOnExons(exonAnnotationFileName, bowTieOutputFileName);
		// Converting the count to gene level
		Map<String, Integer> countMappedReadsOnGenes = convertCounterToGeneLevel(countMappedReadsOnExons);
		// Writing the end result to output file.
		saveGeneExpressionResult(outputFileName, countMappedReadsOnGenes);
		System.out.println("Done!");
	}

	private void saveGeneExpressionResult(String outputFileName, Map<String, Integer> countMappedReadsOnGenes) throws IOException {
		System.out.println("Saving the final result...");
		PrintWriter out = FileUtils.getInstance().getPrinterWriter(outputFileName);
		for (String geneId : countMappedReadsOnGenes.keySet()) {
			out.println(String.format("%s\t%s", geneId, countMappedReadsOnGenes.get(geneId)));
		}
		out.close();
	}

	public Map<String, Integer> convertCounterToGeneLevel(Map<String, Integer> countMappedReadsOnExons) {
		System.out.println("Converting the counter to Gene level...");
		Map<String, Integer> result = new HashMap<>();
		// Initializing the gene counter
		for (String exonId : countMappedReadsOnExons.keySet()) {
			String[] columns = exonId.split(FileUtils.UNDER_SCORE);
			String geneId = columns[0] + FileUtils.UNDER_SCORE + columns[1];
			result.put(geneId, Integer.valueOf(0));
		}

		for (String geneId : result.keySet()) {
			for (String exonId : countMappedReadsOnExons.keySet()) {
				if (exonId.startsWith(geneId)) {
					result.put(geneId, result.get(geneId) + countMappedReadsOnExons.get(exonId));
				}
			}
		}

		return result;
	}

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
