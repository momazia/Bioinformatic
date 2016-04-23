package com.bio.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.bio.pojo.RefSeq;
import com.bio.util.ExonMaskUtil;
import com.bio.util.FileUtils;

public class TestExonMaskUtil {

	@Test
	public void testToRefSeq() {

		List<String> lines = new ArrayList<>();
		lines.add("chr1	66999638	67000051	NM_032291_exon_0_0_chr1_66999639_f	0	+");
		lines.add("chr1	67091529	67091593	NM_032291_exon_1_0_chr1_67091530_f	0	+");
		lines.add("chr1	66999251	66999355	NM_001308203_exon_0_0_chr1_66999252_f	0	+");

		List<RefSeq> refSeqs = ExonMaskUtil.getInstance().toRefSeq(lines);

		assertEquals(3, refSeqs.size());

		assertEquals(66999638, refSeqs.get(0).getStartIndex());
		assertEquals(67000051, refSeqs.get(0).getEndIndex());
		assertEquals("NM_032291_exon_0_0_chr1_66999639_f", refSeqs.get(0).getId());

		assertEquals(66999251, refSeqs.get(2).getStartIndex());
		assertEquals(66999355, refSeqs.get(2).getEndIndex());
		assertEquals("NM_001308203_exon_0_0_chr1_66999252_f", refSeqs.get(2).getId());
	}

	@Test
	public void testRun() throws IOException {
		ExonMaskUtil.getInstance().run(FileUtils.HG19_REFSEQ_EXON_ANNOT);
	}

	@Test
	public void testCollapseExons() {
		List<RefSeq> refSeqs = new ArrayList<>();

		refSeqs.add(new RefSeq(56, 60, "+"));
		refSeqs.add(new RefSeq(25, 44, "1"));
		refSeqs.add(new RefSeq(30, 45, "2"));
		refSeqs.add(new RefSeq(10, 20, "3"));
		refSeqs.add(new RefSeq(2, 4, "B"));
		refSeqs.add(new RefSeq(0, 5, "A"));
		refSeqs.add(new RefSeq(15, 40, "4"));
		refSeqs.add(new RefSeq(45, 50, "5"));
		refSeqs.add(new RefSeq(55, 58, "-"));
		refSeqs.add(new RefSeq(41, 43, "6"));
		refSeqs.add(new RefSeq(25, 40, "7"));
		refSeqs.add(new RefSeq(59, 70, "/"));

		List<RefSeq> collapsedExons = ExonMaskUtil.getInstance().collapseExons(refSeqs);

		assertEquals(3, collapsedExons.size());

		assertEquals(0, collapsedExons.get(0).getStartIndex());
		assertEquals(5, collapsedExons.get(0).getEndIndex());

		assertEquals(10, collapsedExons.get(1).getStartIndex());
		assertEquals(50, collapsedExons.get(1).getEndIndex());

		assertEquals(55, collapsedExons.get(2).getStartIndex());
		assertEquals(70, collapsedExons.get(2).getEndIndex());
	}

}
