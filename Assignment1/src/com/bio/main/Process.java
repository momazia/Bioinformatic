package com.bio.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.bio.main.io.FileProcessor;
import com.bio.main.pojo.Gene;
import com.bio.main.pojo.RefSeq;
import com.bio.main.pojo.Strand;

/**
 * The main class in charge of the whole process.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class Process {

	private static final String SEPARATOR_DOT = ".";
	private static final char INTRON_N = 'N';
	public static final String IO_PATH = "../Assignment1/io/";

	private static Process instance;

	private Process() {
	}

	public static Process getInstance() {
		if (instance == null) {
			instance = new Process();
		}
		return instance;
	}

	/**
	 * The main method to be invoked to print the final result.
	 * 
	 * @param geneAnnotFilePath
	 * @param exonAnnotFilePath
	 * @param chrFilePath
	 */
	public void run(String geneAnnotFilePath, String exonAnnotFilePath, String chrFilePath) {

		try {

			System.out.println("Loading file [" + geneAnnotFilePath + "] to read all the gene annotations");
			List<RefSeq> geneAnnots = FileProcessor.getInstance().readAnnorationFile(geneAnnotFilePath, null);

			// The map contains the gene annotation as key and all the Exon
			// annotations as value
			List<Gene> genes = new ArrayList<>();

			// We loop through each of the gene annotations.
			for (RefSeq geneAnnot : geneAnnots) {

				Gene gene = new Gene();
				gene.setGeneAnn(geneAnnot);

				// Reading Exon annotations
				System.out.println("Loading file [" + exonAnnotFilePath + "] for geneID [" + geneAnnot.getId() + "]");
				gene.setExonAnns(FileProcessor.getInstance().readAnnorationFile(exonAnnotFilePath, geneAnnot.getId()));

				// Extracting gene string
				gene.setStr(extractGene(geneAnnot, chrFilePath));

				// Replacing the Introns with N
				replaceIntronsWithN(gene);

				// reverse-complemented the negative strands
				reverseSequence(gene);
				genes.add(gene);
			}

			printGenes(genes);

		} catch (IOException e) {
			System.out.println("Cannot read the file:" + e.getMessage());
		}

	}

	/**
	 * For a given gene, the method checks its annotation and if the strand is
	 * negative, it will apply reverse-complemented.
	 * 
	 * @param gene
	 */
	public void reverseSequence(Gene gene) {
		// reversing the sequence if the gene's strand is negative
		if (Strand.NEGATIVE.equals(gene.getGeneAnn().getStrand())) {

			System.out.println("Reverse-complement process for geneID [" + gene.getGeneAnn().getId() + "] starts ...");
			PerformanceMonitor performanceMonitor = new PerformanceMonitor();

			StringBuilder stringBuilder = new StringBuilder(gene.getStr());

			for (int index = 0; index < gene.getStr().length(); index++) {
				stringBuilder.setCharAt(index, swapChar(stringBuilder.charAt(index)));
			}
			gene.setStr(stringBuilder.reverse().toString());
			performanceMonitor.end();
			System.out.println("Reverse-complement process ended in " + performanceMonitor);
		}

	}

	/**
	 * Swaps characters in a sequence, with respect to the letter cases. It will
	 * ignore N character. If unknown character is passed, it will throw a run
	 * time exception.
	 * 
	 * @param chr
	 * @return
	 */
	private char swapChar(char chr) {
		if (chr == INTRON_N) {
			return INTRON_N;
		}
		if (Character.toLowerCase(chr) == 't') {
			return Character.isLowerCase(chr) ? 'a' : 'A';
		}
		if (Character.toLowerCase(chr) == 'c') {
			return Character.isLowerCase(chr) ? 'g' : 'G';
		}
		if (Character.toLowerCase(chr) == 'a') {
			return Character.isLowerCase(chr) ? 't' : 'T';
		}
		if (Character.toLowerCase(chr) == 'g') {
			return Character.isLowerCase(chr) ? 'c' : 'C';
		}
		throw new RuntimeException("Found an unknown character [" + chr + "]");
	}

	/**
	 * Prints the genes given into the following path: {@link Process#IO_PATH} +
	 * Result.fa with the following format: >chr.start.end.gene_ID.strand
	 * 
	 * @param genes
	 * @throws FileNotFoundException
	 */
	private void printGenes(List<Gene> genes) throws FileNotFoundException {
		String fileName = IO_PATH + "Result.fa";
		System.out.println("Exporting the final result to file [" + fileName + "]");

		try (PrintWriter out = new PrintWriter(fileName)) {
			for (Gene gene : genes) {
				String header = ">" + gene.getGeneAnn().getChromosome() + SEPARATOR_DOT + gene.getGeneAnn().getStart()
						+ SEPARATOR_DOT + gene.getGeneAnn().getEnd() + SEPARATOR_DOT + gene.getGeneAnn().getId()
						+ SEPARATOR_DOT + gene.getGeneAnn().getStrand().getValue();
				System.out.println("Printing the header: [" + header + "]");
				out.println(header);
				out.println(gene.getStr());
			}
		}

	}

	/**
	 * Replaces Introns by looking at the Exon annotations given. If the gene
	 * sequence is not within the Exon ranges defined, it will be replaced by N
	 * (as it is an Intron)
	 * 
	 * @param gene
	 */
	public void replaceIntronsWithN(Gene gene) {
		System.out.println("Relacing Intros with N for geneID [" + gene.getGeneAnn().getId() + "] starts ...");
		PerformanceMonitor pm = new PerformanceMonitor();

		char[] charArray = gene.getStr().toCharArray();
		for (int index = 0; index < gene.getStr().length(); index++) {
			if (!isIndexWithinExon(gene, index)) {
				// Replacing N if we are outside the Exon index (if it is
				// Intron)
				charArray[index] = INTRON_N;
			}
		}

		gene.setStr(new String(charArray));
		pm.end();
		System.out.println("Replacing Introns with N was done in " + pm);
	}

	/**
	 * Returns true if the index given is within any of the Exon genes ranges in
	 * annotation file.
	 * 
	 * @param gene
	 * @param index
	 * @return
	 */
	private boolean isIndexWithinExon(Gene gene, int index) {
		// We add the starting point of the gene to the current index to find
		// the real index.
		int actualIndex = index + gene.getGeneAnn().getStart();

		for (RefSeq refSeq : gene.getExonAnns()) {

			if (refSeq.getStart() <= actualIndex && refSeq.getEnd() > actualIndex) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Reads Gene string using the RefSeq (gene annotation) id.
	 * 
	 * @param refSeq
	 * @param chrFilePath
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private String extractGene(RefSeq refSeq, String chrFilePath) throws IOException, FileNotFoundException {
		System.out.println("Loading file [" + chrFilePath + "] to extract [" + refSeq.getId() + "] gene ...");

		PerformanceMonitor extractGenePm = new PerformanceMonitor();
		String gene = FileProcessor.getInstance().readChromosomeFile(chrFilePath, refSeq);
		extractGenePm.end();

		System.out.println("Gene extraction was done in " + extractGenePm);
		return gene;
	}

}
