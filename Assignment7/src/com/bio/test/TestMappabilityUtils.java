package com.bio.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.bio.util.FileUtils;
import com.bio.util.MappabilityUtils;

public class TestMappabilityUtils {

	@Test
	public void testRun() throws IOException {
		MappabilityUtils.getInstance().run(FileUtils.HG19_REFSEQ_EXON_ANNOT, FileUtils.BOW_TIE_OUTPUT, FileUtils.GENE_EXPRESSION_COUNT);
	}

	@Test
	public void testSimpleCountMapReadsOnExons() throws IOException {
		Map<String, Integer> countMapReadsOnExons = MappabilityUtils.getInstance().countMappedReadsOnExons("simple_annot.txt", "simple_bowtie.txt");
		assertEquals(6, countMapReadsOnExons.size());
		assertEquals(Integer.valueOf(2), countMapReadsOnExons.get("NM_032291_exon_0_0_chr1_66999639_f"));
		assertEquals(Integer.valueOf(0), countMapReadsOnExons.get("NM_032292_exon_0_0_chr1_67091530_f"));
		assertEquals(Integer.valueOf(1), countMapReadsOnExons.get("NM_032292_exon_1_0_chr1_67098753_f"));
		assertEquals(Integer.valueOf(0), countMapReadsOnExons.get("NM_032293_exon_0_0_chr1_67101627_f"));
		assertEquals(Integer.valueOf(0), countMapReadsOnExons.get("NM_032293_exon_1_0_chr1_67105460_f"));
		assertEquals(Integer.valueOf(0), countMapReadsOnExons.get("NM_032294_exon_0_0_chr1_67108493_f"));
	}

	@Test
	public void testConvertCounterToGeneLevel() {
		Map<String, Integer> countMappedReadsOnExons = new HashMap<>();
		countMappedReadsOnExons.put("NM_032291_exon_0_0_chr1_66999639_f", 2);
		countMappedReadsOnExons.put("NM_032292_exon_0_0_chr1_67091530_f", 3);
		countMappedReadsOnExons.put("NM_032292_exon_1_0_chr1_67098753_f", 1);
		countMappedReadsOnExons.put("NM_032293_exon_0_0_chr1_67101627_f", 0);
		countMappedReadsOnExons.put("NM_032293_exon_1_0_chr1_67105460_f", 0);
		Map<String, Integer> geneLevelCounter = MappabilityUtils.getInstance().convertCounterToGeneLevel(countMappedReadsOnExons);
		assertEquals(3, geneLevelCounter.size());
		assertEquals(Integer.valueOf(2), geneLevelCounter.get("NM_032291"));
		assertEquals(Integer.valueOf(4), geneLevelCounter.get("NM_032292"));
		assertEquals(Integer.valueOf(0), geneLevelCounter.get("NM_032293"));
	}
}
