package com.bio.main;

import java.io.IOException;

import com.bio.main.util.FileUtils;

/**
 * Merges the given mappability files into one file which could be used by R application for analysis.All the files must be placed under
 * /Assignment5/io folder.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class MergeOutputApp {
	private static final String MAPPABILITY_OUTPUT_250 = "Mappability-output-250";

	/**
	 * The main method to be executed.
	 * 
	 * @param str
	 */
	public static void main(String[] str) {
		try {
			FileUtils.getInstance().merge(MAPPABILITY_OUTPUT_250, CheckMappabilityApp.FINAL_OUTPUT_50, CheckMappabilityApp.FINAL_OUTPUT_70, CheckMappabilityApp.FINAL_OUTPUT_100);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
