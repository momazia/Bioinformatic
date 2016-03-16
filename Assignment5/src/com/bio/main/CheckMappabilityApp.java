package com.bio.main;

import java.io.IOException;

import com.bio.main.util.MappabilityUtils;

public class CheckMappabilityApp {

	private static final int TILE_LENGTH_50 = 50;
	private static final String FINAL_OUTPUT_50 = "final-output-50";
	private static final String FINAL_B_TOUT50_V2_M1 = "final-BTout50-v2-m1";

	public static void main(String[] args) {
		try {
			MappabilityUtils.getInstance().checkMappability(FINAL_B_TOUT50_V2_M1, TILE_LENGTH_50, FINAL_OUTPUT_50);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
