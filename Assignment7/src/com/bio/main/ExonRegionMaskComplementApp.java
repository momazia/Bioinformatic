package com.bio.main;

import java.io.IOException;

import com.bio.util.ExonMaskUtils;
import com.bio.util.FileUtils;

public class ExonRegionMaskComplementApp {
	public static void main(String[] args) {
		try {
			ExonMaskUtils.getInstance().run(FileUtils.HG19_REFSEQ_EXON_ANNOT, FileUtils.CHR1, FileUtils.MASKED_CHR1, FileUtils.COLLAPSED_EXON);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
