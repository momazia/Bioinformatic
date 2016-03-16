package com.bio.main;

import java.io.IOException;

import com.bio.main.util.FileUtils;

public class GenerateReadsApp {

	private static final String CHR1_250_FA = "chr1-250.fa";

	private static final String READS_50_FA = "reads50.fa";
	private static final String READS_70_FA = "reads70.fa";
	private static final String READS_100_FA = "reads100.fa";
	private static final int READ_LENGTH_50 = 50;
	private static final int READ_LENGTH_70 = 70;
	private static final int READ_LENGTH_100 = 100;

	public static void main(String[] args) {
		try {
			FileUtils.getInstance().createReads(READ_LENGTH_50, READS_50_FA, CHR1_250_FA);
			FileUtils.getInstance().createReads(READ_LENGTH_70, READS_70_FA, CHR1_250_FA);
			FileUtils.getInstance().createReads(READ_LENGTH_100, READS_100_FA, CHR1_250_FA);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
