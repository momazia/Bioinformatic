package com.bio.main.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.bio.main.PerformanceMonitor;
import com.bio.main.Process;
import com.bio.main.pojo.Gene;
import com.bio.main.pojo.RefSeq;

public class MainTest {

	private static final String EXON_ANNOT_1 = "chr1	2	5	NM_032291_exon_0_0_chr1_66999639_f	0	+";
	private static final String GENE_ANNOT_2 = "chr1	0	12	NM_003689	0	-	19630718	19638618	0	7	1679,130,100,97,105,188,320,	0,3310,4294,4606,5450,5747,9119,";
	private static final String SAMPLE_1 = "AAccNNNNNtTggg";
	private static final String SAMPLE_2 = "aaccAAgttggg";
	private static final String GENE_ANNOT_1 = "chr1	1	14	NM_003689	0	-	19630718	19638618	0	7	1679,130,100,97,105,188,320,	0,3310,4294,4606,5450,5747,9119,";
	private static final String GENE_ANNOT_POS = "chr1	1	14	NM_003689	0	+	19630718	19638618	0	7	1679,130,100,97,105,188,320,	0,3310,4294,4606,5450,5747,9119,";

	private static final String EXON_ANNOT_FILE_PATH = Process.IO_PATH + "TestHG19-refseq-exon-annot-chr1-2016";
	private static final String GENE_ANNOT_FILE_PATH = Process.IO_PATH + "TestHG19-refseq-gene-annot-filtered";
	private static final String CHR1_FILE_PATH = Process.IO_PATH + "simplecha.fa";

	@Test
	public void testReverseSeq1() {
		Gene gene = new Gene();
		gene.setGeneAnn(new RefSeq(GENE_ANNOT_1));
		gene.setStr(SAMPLE_1);
		Process.getInstance().reverseSequence(gene);
		assertEquals("cccAaNNNNNggTT", gene.getStr());
	}

	@Test
	public void testReverseSeq2() {
		Gene gene = new Gene();
		gene.setGeneAnn(new RefSeq(GENE_ANNOT_POS));
		gene.setStr(SAMPLE_1);
		Process.getInstance().reverseSequence(gene);
		assertEquals(SAMPLE_1, gene.getStr());
	}

	@Test
	public void testReplaceIntronsWithN() {
		Gene gene = new Gene();
		gene.setStr(SAMPLE_2);
		List<RefSeq> exonAnns = new ArrayList<>();
		exonAnns.add(new RefSeq(EXON_ANNOT_1));
		gene.setExonAnns(exonAnns);
		gene.setGeneAnn(new RefSeq(GENE_ANNOT_2));
		Process.getInstance().replaceIntronsWithN(gene);
		assertEquals("NNccANNNNNNN", gene.getStr());
	}

	@Test
	public void testFinalResult() {
		PerformanceMonitor pm = new PerformanceMonitor();
		Process.getInstance().run(GENE_ANNOT_FILE_PATH, EXON_ANNOT_FILE_PATH, CHR1_FILE_PATH);
		pm.end();
		System.out.println("Take took to run the test: " + pm);
	}

}
