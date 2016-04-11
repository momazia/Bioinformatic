package com.bio.main;

import java.io.IOException;
import java.util.List;

import com.bio.pojo.Sequence;
import com.bio.util.CabiosUtils;
import com.bio.util.FileUtils;

/**
 * Main application to be executed for Cabios version of local alignment.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class CabiosApp {

	public static void main(String[] args) {
		try {
			// Reading the query file
			Sequence query = FileUtils.getInstance().readQuery(FileUtils.E_COLI_QUERY1_FA);
			// Reading the sequences from the file
			List<Sequence> seqs = FileUtils.getInstance().readSequences(FileUtils.SWISSPROT_100_FA);
			for (Sequence sequence : seqs) {
				System.out.println(sequence.getName());
				int score = CabiosUtils.getInstance().sw(sequence.getStr().toCharArray(), sequence.getStr().length(), query.getStr().toCharArray(), query.getStr().length(), 11, 1);
				System.out.println(score);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
