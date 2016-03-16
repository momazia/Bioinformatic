package com.bio.main.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.bio.main.util.MappabilityUtils;

public class MappabilityTest {

	@Test
	public void testCheckMappability() {
		try {
			MappabilityUtils.getInstance().checkMappability("Test_BTout-5", 5, "Test_final-output-5");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to load/read a file: " + e.getMessage());
		}
	}

}
