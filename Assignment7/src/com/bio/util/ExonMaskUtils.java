package com.bio.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.bio.pojo.RefSeq;

/**
 * The main utility class for Exon Mask.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class ExonMaskUtils {
	private static final char CHAR_N = 'N';
	private static ExonMaskUtils instance = null;

	/**
	 * Private constructor for Singleton design pattern purpose. Declared private so it is not accessible from outside.
	 */
	private ExonMaskUtils() {
	}

	/**
	 * Gets instance of this class. It will instantiate it if it is not done yet, once.
	 * 
	 * @return
	 */
	public static ExonMaskUtils getInstance() {
		if (instance == null) {
			instance = new ExonMaskUtils();
		}
		return instance;
	}

	/**
	 * The method reads the Exon annotation file, collapses the overlapping Exon regions into one and save it into collapsedExonFileName file. Later
	 * it will use the collapsed Exons to replace non Exon regions with N.
	 * 
	 * @param exonAnnotationFileName
	 * @param chr1FileName
	 * @param maskedChr1FileName
	 * @param collapsedExonFileName
	 * @throws IOException
	 */
	public void run(String exonAnnotationFileName, String chr1FileName, String maskedChr1FileName, String collapsedExonFileName) throws IOException {
		// Reading the Exon annotation file
		List<RefSeq> refSeqs = readRefSeqs(exonAnnotationFileName);
		// Collapsing the Exons
		System.out.println("Collapsing Exons...");
		List<RefSeq> collapsedExons = collapseExons(refSeqs);
		// Save collapsed Exons into a file
		System.out.println("Saving collapsed Exons into output file...");
		saveCollapsedExons(collapsedExonFileName, collapsedExons);
		// Creating masked Chr1 file
		System.out.println("Masking non-Exon regions and saving it into output file...");
		maskNonExons(collapsedExons, chr1FileName, maskedChr1FileName);
		System.out.println("Done!");
	}

	/**
	 * Reads the Exon annotation file name given and convert it into RefSeq.
	 * 
	 * @param exonAnnotationFileName
	 * @return
	 * @throws IOException
	 */
	public List<RefSeq> readRefSeqs(String exonAnnotationFileName) throws IOException {
		System.out.println("Reading the Exon annotation file...");
		List<String> exonAnnotLines = FileUtils.getInstance().readFile(exonAnnotationFileName);
		// Converting the file lines into RefSeq
		System.out.println("Converting Exon annotations...");
		return toRefSeq(exonAnnotLines);
	}

	/**
	 * Saves the list of given RefSeqs into a file using collapsedExonFileName file name.
	 * 
	 * @param collapsedExonFileName
	 * @param collapsedExons
	 * @throws IOException
	 */
	private void saveCollapsedExons(String collapsedExonFileName, List<RefSeq> collapsedExons) throws IOException {
		// Deleting the output file if it already exists
		FileUtils.getInstance().deleteIfExists(collapsedExonFileName);
		PrintWriter out = FileUtils.getInstance().getPrinterWriter(collapsedExonFileName);
		for (RefSeq refSeq : collapsedExons) {
			out.println(format(refSeq));
		}
		out.close();
	}

	/**
	 * Formats the RefSeq given by harcoding chr1 as prefix, start index, end index and the refseq ID. It will also add 0 and + at the end of the
	 * string. Each of the mentioned columns will be separated with a tab.
	 * 
	 * @param refSeq
	 * @return
	 */
	private String format(RefSeq refSeq) {
		return String.join(FileUtils.TAB, "chr1", Integer.toString(refSeq.getStartIndex()), Integer.toString(refSeq.getEndIndex()), refSeq.getId(), "0", "+");
	}

	/**
	 * The method uses the collapsedExons regions and those which are not within the region in chr1FileName will be replaced with N. The result will
	 * be saved in maskedChr1FileName method.
	 * 
	 * @param collapsedExons
	 * @param chr1FileName
	 * @param maskedChr1FileName
	 * @throws IOException
	 */
	public void maskNonExons(List<RefSeq> collapsedExons, String chr1FileName, String maskedChr1FileName) throws IOException {
		// Deleting the output file if it already exists
		FileUtils.getInstance().deleteIfExists(maskedChr1FileName);
		int chr1Index = -1; // Chr1 file index.
		List<String> chr1Lines = FileUtils.getInstance().readFile(chr1FileName);
		PrintWriter out = FileUtils.getInstance().getPrinterWriter(maskedChr1FileName);
		out.println(chr1Lines.get(0));
		StringBuffer resultLine = null;
		int collapsedExonsIndex = 0;
		int startIndex = collapsedExons.get(collapsedExonsIndex).getStartIndex();
		int endIndex = collapsedExons.get(collapsedExonsIndex).getEndIndex();
		for (int i = 1; i < chr1Lines.size(); i++) {
			resultLine = new StringBuffer();
			char[] chr1LineChars = chr1Lines.get(i).toCharArray();
			for (char ch : chr1LineChars) {
				chr1Index++;
				// If we are within the Exon start and end indexes, copy the same character, otherwise print N.
				if (startIndex <= chr1Index && chr1Index < endIndex) {
					resultLine.append(ch);
				} else {
					resultLine.append(CHAR_N);
				}
				// If we have reached the current collapsedExons end Index, then we get the next one in the list.
				if (collapsedExonsIndex < collapsedExons.size() - 1 && chr1Index >= endIndex) {
					collapsedExonsIndex++;
					startIndex = collapsedExons.get(collapsedExonsIndex).getStartIndex();
					endIndex = collapsedExons.get(collapsedExonsIndex).getEndIndex();
				}
			}
			out.println(resultLine.toString());
		}
		out.close();
	}

	/**
	 * Goes through the list of RefSeqs and combines those which are overlapping.
	 * 
	 * @param refSeqs
	 * @return
	 */
	public List<RefSeq> collapseExons(List<RefSeq> refSeqs) {
		// Sorting the refSeqs based on their startIndex
		sortRefSeqs(refSeqs);
		List<RefSeq> result = new ArrayList<>();
		// Setting the initial end and start indexes.
		int endIndex = refSeqs.get(0).getEndIndex();
		int startIndex = refSeqs.get(0).getStartIndex();
		for (int i = 1; i < refSeqs.size(); i++) {
			// Going through all the ones which are overlapping and finding the maximum end index.
			while (i < refSeqs.size() && endIndex >= refSeqs.get(i).getStartIndex()) {
				if (endIndex < refSeqs.get(i).getEndIndex()) {
					endIndex = refSeqs.get(i).getEndIndex();
				}
				i++;
			}
			result.add(new RefSeq(startIndex, endIndex, refSeqs.get(i - 1).getId()));
			// When we are on the last index, we don't want to set the end and start indexes because we are done!
			if (i < refSeqs.size()) {
				startIndex = refSeqs.get(i).getStartIndex();
				endIndex = refSeqs.get(i).getEndIndex();
			}
		}
		return result;
	}

	/**
	 * Sorts the given RefSeqs based on their start indexes.
	 * 
	 * @param refSeqs
	 */
	private void sortRefSeqs(List<RefSeq> refSeqs) {
		Collections.sort(refSeqs, new Comparator<RefSeq>() {
			@Override
			public int compare(RefSeq arg0, RefSeq arg1) {
				// Sorting two RefSeqs based on their start indexes.
				return Integer.compare(arg0.getStartIndex(), arg1.getStartIndex());
			}
		});
	}

	/**
	 * Converts the lines read from a file into Refseq, given each of the elements are separated by \t.
	 * 
	 * @param lines
	 * @return
	 */
	public List<RefSeq> toRefSeq(List<String> lines) {
		List<RefSeq> result = new ArrayList<>();
		for (String line : lines) {
			String[] columns = line.split(FileUtils.TAB);
			result.add(new RefSeq(Integer.valueOf(columns[1]), Integer.valueOf(columns[2]), columns[3]));
		}
		return result;
	}
}
