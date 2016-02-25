package com.bio.main;

import java.io.IOException;
import java.util.List;

import com.bio.main.io.FileProcessor;
import com.bio.main.pojo.Median;
import com.bio.main.pojo.Motif;
import com.bio.main.pojo.Sequence;

public class BnBMainApp {

	private static final int K = 4;
	private static final int L_MER = 6;

	public static void main(String[] args) {

		try {
			List<Sequence> sequences = FileProcessor.getInstance().readSequences("Sample-HMP-617.fa");
			BnBMainProcess mp = new BnBMainProcess(sequences, L_MER, K);
			List<Median> medians = mp.medianSearch();
			MotifProcess.getInstance().calculateMotifs(medians, sequences, L_MER);
			MotifProcess.getInstance().calculateConsensus(medians, L_MER);

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

		} catch (IOException e) {
			System.out.println("Cannot read the file: " + e.getMessage());
		}

	}
}
