package com.bio.main;

import java.io.IOException;

import com.bio.main.util.FileUtils;

public class MergeOutputApp {

	private static final String MAPPABILITY_OUTPUT_250 = "Mappability-output-250";

	public static void main(String[] str) {
		try {
			FileUtils.getInstance().merge(MAPPABILITY_OUTPUT_250, CheckMappabilityApp.FINAL_OUTPUT_50, CheckMappabilityApp.FINAL_OUTPUT_70, CheckMappabilityApp.FINAL_OUTPUT_100);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
