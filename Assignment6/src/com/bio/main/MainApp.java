package com.bio.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.bio.pojo.AffineResult;
import com.bio.pojo.Sequence;
import com.bio.util.FileUtils;
import com.bio.util.SmithWaterman;

/**
 * This is the main program to be executed in order to run Smith Waterman logic using Affine gap, on a DB file and a set of sequences. Both these
 * files must be placed under /Assignment6/io folder. The output file will be placed under the same folder.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class MainApp {

	/**
	 * Main method to be executed to run the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Reading the db file
			String db = FileUtils.getInstance().readDb(FileUtils.E_COLI_QUERY1_FA);
			// Reading the sequences from the file
			List<Sequence> seqs = FileUtils.getInstance().readSequences(FileUtils.SWISSPROT_100_FA);
			// Deleting the output file if it already exists
			FileUtils.getInstance().deleteIfExists(FileUtils.OUTPUT_TXT);
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FileUtils.IO_PATH + FileUtils.OUTPUT_TXT, true)));
			// Looping through each of the sequences and running SmithWaterman and printing the output into a file.
			for (Sequence sequence : seqs) {
				AffineResult affineResult = SmithWaterman.getInstance().run(sequence.getStr(), db);
				SmithWaterman.getInstance().backTrace(sequence.getStr(), db, affineResult);
				FileUtils.getInstance().write(out, affineResult, sequence.getName(), sequence.getStr().length());
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
