package com.bio.main;

import java.io.IOException;

import com.bio.util.FileUtils;

public class MainApp {

	public static void main(String[] args) {
		try {
			FileUtils.getInstance().readDb(FileUtils.E_COLI_QUERY1_FA);
			FileUtils.getInstance().readSequences(FileUtils.SWISSPROT_100_FA);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
