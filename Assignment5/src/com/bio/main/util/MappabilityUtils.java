package com.bio.main.util;

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
}
