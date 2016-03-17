package com.bio.main;

import java.io.IOException;

import com.bio.main.util.MappabilityUtils;

public class CheckMappabilityApp {

	private static final int TILE_LENGTH_50 = 50;
	private static final String FINAL_OUTPUT_50 = "final-output-50";
	private static final String FINAL_B_TOUT50_V2_M1 = "final-BTout50-v2-m1";

	private static final int TILE_LENGTH_70 = 70;
	private static final String FINAL_OUTPUT_70 = "final-output-70";
	private static final String FINAL_B_TOUT70_V2_M1 = "final-BTout70-v2-m1";

	private static final int TILE_LENGTH_100 = 100;
	private static final String FINAL_OUTPUT_100 = "final-output-100";
	private static final String FINAL_B_TOUT100_V2_M1 = "final-BTout100-v2-m1";

	public static void main(String[] args) {
		try {
			MappabilityUtils.getInstance().checkMappability(FINAL_B_TOUT50_V2_M1, TILE_LENGTH_50, FINAL_OUTPUT_50, GenerateReadsApp.READS_50_FA);
			MappabilityUtils.getInstance().checkMappability(FINAL_B_TOUT70_V2_M1, TILE_LENGTH_70, FINAL_OUTPUT_70, GenerateReadsApp.READS_70_FA);
			MappabilityUtils.getInstance().checkMappability(FINAL_B_TOUT100_V2_M1, TILE_LENGTH_100, FINAL_OUTPUT_100, GenerateReadsApp.READS_100_FA);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
