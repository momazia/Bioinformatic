package com.bio.main.util;

import java.io.IOException;

public class MappabilityUtils {
	private static MappabilityUtils instance = null;

	private MappabilityUtils() {
		super();
	}

	public static MappabilityUtils getInstance() {
		if (instance == null) {
			instance = new MappabilityUtils();
		}
		return instance;
	}

	public void checkMappability(String btOutputFileName, int tileLength, String outputFileName) throws IOException {
		FileUtils.getInstance().readFile(btOutputFileName);
	}
}
