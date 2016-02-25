package com.bio.main;

import java.io.IOException;
import java.util.List;

import com.bio.main.io.FileProcessor;
import com.bio.main.pojo.Median;
import com.bio.main.pojo.Sequence;

public class MainApp {

	public static void main(String[] args) {

		try {
			List<Sequence> sequences = FileProcessor.getInstance().readSequences("Sample-HMP-617.fa");
			MainProcess mp = new MainProcess(sequences, 6, 4);
			List<Median> medians = mp.medianSearch();
			for (Median median : medians) {
				System.out.println("Median String: " + median.getStr() + " (tot_dist = " + median.getTotalDistance() + ")");
				
			}

		} catch (IOException e) {
			System.out.println("Cannot read the file: " + e.getMessage());
		}

	}
}
