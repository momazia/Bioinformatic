package com.bio.main;

import java.io.IOException;
import java.util.List;

import com.bio.main.util.FileUtils;
import com.bio.main.util.MappabilityUtils;

/**
 * The main application to create different mappability files for the given BowTie outputs. All the files must be placed under /Assignment5/io folder.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class CheckMappabilityApp {
	// Constants to the file names
	private static final String CHR1_250_FA = "chr1-250.fa";
	private static final int TILE_LENGTH_50 = 50;
	public static final String FINAL_OUTPUT_50 = "final-output-50";
	private static final String FINAL_B_TOUT50_V2_M1 = "final-BTout50-v2-m1";
	private static final int TILE_LENGTH_70 = 70;
	public static final String FINAL_OUTPUT_70 = "final-output-70";
	private static final String FINAL_B_TOUT70_V2_M1 = "final-BTout70-v2-m1";
	private static final int TILE_LENGTH_100 = 100;
	public static final String FINAL_OUTPUT_100 = "final-output-100";
	private static final String FINAL_B_TOUT100_V2_M1 = "final-BTout100-v2-m1";

	/**
	 * The main method to be executed.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Reading the 250 chromosomes file.
			List<String> chr1FileLines = FileUtils.getInstance().readFile(CHR1_250_FA);
			MappabilityUtils.getInstance().checkMappability(FINAL_B_TOUT50_V2_M1, TILE_LENGTH_50, FINAL_OUTPUT_50, GenerateReadsApp.READS_50_FA, chr1FileLines);
			MappabilityUtils.getInstance().checkMappability(FINAL_B_TOUT70_V2_M1, TILE_LENGTH_70, FINAL_OUTPUT_70, GenerateReadsApp.READS_70_FA, chr1FileLines);
			MappabilityUtils.getInstance().checkMappability(FINAL_B_TOUT100_V2_M1, TILE_LENGTH_100, FINAL_OUTPUT_100, GenerateReadsApp.READS_100_FA, chr1FileLines);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
