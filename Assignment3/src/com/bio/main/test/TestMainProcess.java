package com.bio.main.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.bio.main.BnBMainProcess;

public class TestMainProcess {

	@Test
	public void testNextChar() {
		BnBMainProcess mp = new BnBMainProcess();
		assertEquals('2', mp.nextCharacter('1'));
		assertEquals('3', mp.nextCharacter('2'));
		assertEquals('4', mp.nextCharacter('3'));
	}

	@Test
	public void testCreateInitialLmer() {
		char[] ans1 = new char[] { ' ', '1', '1' };
		BnBMainProcess mp = new BnBMainProcess();
		assertTrue(Arrays.equals(ans1, mp.createInitialLmer(2)));
	}

	@Test
	public void testGetActualText() {
		char[] chrs = new char[] { ' ', '1', '2', '3', '4' };
		BnBMainProcess mp = new BnBMainProcess();
		assertEquals("ACGT", mp.getActualText(chrs));
	}
}
