package com.bio.main;

import java.io.IOException;

import com.bio.util.FileUtils;
import com.bio.util.MappabilityUtils;

public class MappabilityApp {
	public static void main(String[] args) {
		try {
			MappabilityUtils.getInstance().run(FileUtils.HG19_REFSEQ_EXON_ANNOT, FileUtils.BOW_TIE_OUTPUT, FileUtils.GENE_EXPRESSION_COUNT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
