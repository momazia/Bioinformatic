package com.bio.main;

import java.io.IOException;

import com.bio.util.FileUtils;
import com.bio.util.MappabilityUtils;

/**
 * The main application to be executed in order to completed part 2 of the assignment. All the input and output files must be placed under io folder.
 * The application reads the Exon annotations file and the bow tie output and finally, it counts the number of hits at gene level and saves it into an
 * output file.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class MappabilityApp {

	/**
	 * The main method to be executed.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MappabilityUtils.getInstance().run(FileUtils.HG19_REFSEQ_EXON_ANNOT, FileUtils.BOW_TIE_OUTPUT, FileUtils.GENE_EXPRESSION_COUNT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
