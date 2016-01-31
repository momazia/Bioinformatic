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

public class Process {

	private static final char INTRON_N = 'N';
	private static final String IO_PATH = "../Assignment1/io/";
	private static final String EXON_ANNOT_FILE_PATH = IO_PATH + "HG19-refseq-exon-annot-chr1-2016";
	private static final String GENE_ANNOT_FILE_PATH = IO_PATH + "HG19-refseq-gene-annot-filtered";
	private static final String CHR1_FILE_PATH = IO_PATH + "chr1.fa";
	private static Process instance;

	private Process() {
	}

	public static Process getInstance() {
		if (instance == null) {
			instance = new Process();
		}
		return instance;
	}

	public void run() {

		try {

			System.out.println("Loading file [" + GENE_ANNOT_FILE_PATH + "] to read all the gene annotations");
			List<RefSeq> geneAnnots = FileProcessor.getInstance().readAnnorationFile(GENE_ANNOT_FILE_PATH, null);

			// The map contains the gene annotation as key and all the exon
			// annotations as value
			List<Gene> genes = new ArrayList<>();

			// We loop through each of the gene annotations.
			for (RefSeq geneAnnot : geneAnnots) {

				Gene gene = new Gene();
				gene.setGeneAnn(geneAnnot);

				// Reading exon annotations
				System.out.println("Loading file [" + EXON_ANNOT_FILE_PATH + "]" + geneAnnot.getId());
				gene.setExonAnns(
						FileProcessor.getInstance().readAnnorationFile(EXON_ANNOT_FILE_PATH, geneAnnot.getId()));

				// Extracting gene string
				gene.setStr(extractGene(geneAnnot));

				// Replacing the Introns with N
				replaceIntronsWithN(gene);

				// reverse-complemented the -ve stands
				reverseSequence(gene);
				genes.add(gene);
			}

			printGenes(genes);

		} catch (IOException e) {
			System.out.println("Cannot read the file:" + e.getMessage());
		}

	}

	public void reverseSequence(Gene gene) {
		// reversing the sequence if the gene's stand is -ve
		if (Strand.NEGATIVE.equals(gene.getGeneAnn().getStrand())) {
			StringBuilder stringBuilder = new StringBuilder(gene.getStr());

			for (int index = 0; index < gene.getStr().length(); index++) {
				stringBuilder.setCharAt(index, swapChar(stringBuilder.charAt(index)));
			}
			gene.setStr(stringBuilder.reverse().toString());
		}
	}

	private char swapChar(char chr) {
		if (chr == INTRON_N) {
			return INTRON_N;
		}
		if (Character.toLowerCase(chr) == 't') {
			return 'A';
		}
		if (Character.toLowerCase(chr) == 'c') {
			return 'G';
		}
		if (Character.toLowerCase(chr) == 'a') {
			return 'T';
		}
		if (Character.toLowerCase(chr) == 'g') {
			return 'C';
		}
		throw new RuntimeException("Found an unknown character [" + chr + "]");
	}

	private void printGenes(List<Gene> genes) throws FileNotFoundException {
		for (Gene gene : genes) {
			String fileName = IO_PATH + gene.getGeneAnn().getId() + ".txt";
			System.out.println("Exporting the FilteredChr1RefSeq to file [" + fileName + "]");

			try (PrintWriter out = new PrintWriter(fileName)) {
				out.println(gene.getStr());
			}
		}

	}

	private void replaceIntronsWithN(Gene gene) {

		String str = gene.getStr();

		for (int index = 0; index < str.length(); index++) {
			if (!isIndexWithinExtron(gene, index)) {
				// Replacing N if we are outside the Extron index (if it is
				// Intron)
				StringBuilder stringBuilder = new StringBuilder(str);
				stringBuilder.setCharAt(index, INTRON_N);
				str = stringBuilder.toString();
			}
		}
		gene.setStr(str);
	}

	private boolean isIndexWithinExtron(Gene gene, int index) {

		// We add the starting point of the gene to the current index to find
		// the real index.
		int actualIndex = index + gene.getGeneAnn().getStart();

		for (RefSeq refSeq : gene.getExonAnns()) {

			if (refSeq.getStart() <= actualIndex && refSeq.getEnd() >= actualIndex) {
				return true;
			}
		}
		return false;
	}

	private String extractGene(RefSeq refSeq) throws IOException, FileNotFoundException {
		System.out.println("Loading file [" + CHR1_FILE_PATH + "] to read [" + refSeq.getId() + "]");
		return FileProcessor.getInstance().readChromosomeFile(CHR1_FILE_PATH, refSeq);
	}

}
