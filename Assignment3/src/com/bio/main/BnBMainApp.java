package com.bio.main;

import java.io.IOException;
import java.util.List;

import com.bio.main.io.FileProcessor;
import com.bio.main.pojo.Median;
import com.bio.main.pojo.Motif;
import com.bio.main.pojo.Sequence;
import com.bio.main.util.BnBMainUtil;
import com.bio.main.util.MotifUtil;

/**
 * The main application to be executed in order to find top best median strings, motif consensus strings and their scores, locations and each of the tiles in a given
 * DNA sequence and number of leafs visited in BnB approach and how many were skipped. <br>
 * In order to execute the program, put the DNA sequence file under "/Assignment3/io" and run this application. <br>
 * <b>Configurations:</b> <br>
 * 1. DNA_FILE = Name of the file in "/Assignment3/io" folder to read the DNA sequences from <br>
 * 2. K = 4 since there are 4 letters in TGCA.<br>
 * 3. L_MER = 6 in this assignment.
 * 
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class BnBMainApp {

	/**
	 * Location of DNA sequence file
	 */
	private static final String DNA_FILE = "HMP-617.fa";
	/**
	 * In case of DNA (TGCA), always 4
	 */
	private static final int K = 4;
	/**
	 * Lmer is 6 according to the assignment 3 requirements.
	 */
	private static final int L_MER = 6;

	/**
	 * Main method to run the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			// Reading the DNA sequences from the file
			List<Sequence> sequences = FileProcessor.getInstance().readSequences(DNA_FILE);

			// Finding medians search using BnB and keeping the top best medians.
			BnBMainUtil mp = new BnBMainUtil(sequences, L_MER, K);
			List<Median> medians = mp.medianSearch();

			// Finding the motifs in the sequences given for the medians found earlier.
			MotifUtil.getInstance().findMotifs(medians, sequences, L_MER);

			// Calculating consensus string and score
			MotifUtil.getInstance().calculateConsensus(medians, L_MER);

			// Prints the result
			print(mp, medians);

		} catch (IOException e) {
			System.out.println("Cannot read the file: " + e.getMessage());
		}

	}

	/**
	 * Prints the result according the format given in assignment 3.
	 * 
	 * @param mp
	 * @param medians
	 */
	private static void print(BnBMainUtil mp, List<Median> medians) {
		for (Median median : medians) {
			System.out.println("Median String: " + median.getStr() + " (tot_dist = " + median.getTotalDistance() + ")");
			System.out.println("Motif consensus string: " + median.getConsensusStr() + " (consensus_score = " + median.getConsensusScore() + ")");
			System.out.println("Motif positions/string s=(s1..st):");
			String motifs = "  ";
			for (Motif motif : median.getMotifs()) {
				motifs += motif.getLocation() + "(" + motif.getStr() + "), ";
			}
			System.out.println(motifs);
		}
		System.out.printf("Number of leafs visited: %d\n", mp.getNumOfLeafsVisited());
		System.out.printf("Number of leafs skipped: %d", Double.valueOf(Math.pow(K, L_MER) - mp.getNumOfLeafsVisited()).intValue());
	}
}
