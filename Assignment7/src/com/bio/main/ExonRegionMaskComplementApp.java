package com.bio.main;

import java.io.IOException;

import com.bio.util.ExonMaskUtils;
import com.bio.util.FileUtils;

/**
 * The main application to be executed for the task 1 of the assignment in which it will replace N for all the non Exon parts by reading the Exon
 * annotation file, chromosome 1 file. The result of the process will be saved in io folder, namely MASKED_CHR1 and the list of collapsed Exons will
 * be saved in another file name under the same folder, called value of COLLAPSED_EXON.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class ExonRegionMaskComplementApp {

	/**
	 * The main method to be executed in the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ExonMaskUtils.getInstance().run(FileUtils.HG19_REFSEQ_EXON_ANNOT, FileUtils.CHR1, FileUtils.MASKED_CHR1, FileUtils.COLLAPSED_EXON);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
