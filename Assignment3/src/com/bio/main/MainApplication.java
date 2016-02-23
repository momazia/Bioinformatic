package com.bio.main;

import java.io.IOException;
import java.util.List;

import com.bio.main.io.FileProcessor;
import com.bio.main.pojo.MedianStr;
import com.bio.main.pojo.Motif;
import com.bio.main.pojo.Sequence;

public class MainApplication {

	public static void main(String[] args) {

		try {
			List<Sequence> sequences = FileProcessor.getInstance().readSequences("HMP-617.fa");

			List<MedianStr> bestMedianStrs = BranchAndBound.getInstance().findBestMedianStrs(sequences);

			for (MedianStr medianStr : bestMedianStrs) {
				System.out.println("Median String: " + medianStr.getLeaf() + " (tot_dist = " + medianStr.getTotalDistance() + ")");
				System.out.println("Motif consensus string: " + medianStr.getMotifStr(BranchAndBound.L_MER) + " (consensus_score = " + medianStr.getConsensusScore() + ")");
				System.out.println("Motif positions/string s=(s1..st):");
				String sequence = "  ";
				for (Motif motif : medianStr.getMotifs()) {
					sequence += motif.getLocation() + "(" + motif.getStr() + "), ";
				}
				System.out.println(sequence);
			}

		} catch (IOException e) {
			System.out.println("Cannot read the file: " + e.getMessage());
		}

	}

}
