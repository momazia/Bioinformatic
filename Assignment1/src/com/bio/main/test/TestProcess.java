package com.bio.main.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bio.main.Process;
import com.bio.main.pojo.Gene;
import com.bio.main.pojo.RefSeq;

public class TestProcess {

	private static final String CHR1_ = "chr1	19629201	19638640	NM_003689	0	-	19630718	19638618	0	7	1679,130,100,97,105,188,320,	0,3310,4294,4606,5450,5747,9119,";
	private static final String CHR1_POS = "chr1	19629201	19638640	NM_003689	0	+	19630718	19638618	0	7	1679,130,100,97,105,188,320,	0,3310,4294,4606,5450,5747,9119,";

	@Test
	public void testReverseSeq1() {
		Gene gene = new Gene();
		gene.setGeneAnn(new RefSeq(CHR1_));
		gene.setStr("aaccNNNNNttggg");
		Process.getInstance().reverseSequence(gene);
		assertTrue("cccaaNNNNNggtt".equalsIgnoreCase(gene.getStr()));
	}

	@Test
	public void testReverseSeq2() {
		Gene gene = new Gene();
		gene.setGeneAnn(new RefSeq(CHR1_POS));
		gene.setStr("aaccNNNNNttggg");
		Process.getInstance().reverseSequence(gene);
		assertTrue("aaccNNNNNttggg".equalsIgnoreCase(gene.getStr()));
	}

}
