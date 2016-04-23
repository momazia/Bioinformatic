package com.bio.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.bio.pojo.RefSeq;

public class ExonMaskUtil {

	private static final String TAB = "\t";
	private static ExonMaskUtil instance = null;

	private ExonMaskUtil() {
	}

	public static ExonMaskUtil getInstance() {
		if (instance == null) {
			instance = new ExonMaskUtil();
		}
		return instance;
	}

	public void run(String fileName) throws IOException {
		// Reading the file
		List<String> lines = FileUtils.getInstance().readFile(fileName);
		// Converting the file lines into RefSeq
		List<RefSeq> refSeqs = toRefSeq(lines);
		// Collapsing the Exons
		List<RefSeq> collapsedExons = collapseExons(refSeqs);
	}

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

	private void sortRefSeqs(List<RefSeq> refSeqs) {
		Collections.sort(refSeqs, new Comparator<RefSeq>() {
			@Override
			public int compare(RefSeq arg0, RefSeq arg1) {
				// Sorting two RefSeqs based on their start indexes.
				return Integer.compare(arg0.getStartIndex(), arg1.getStartIndex());
			}
		});
	}

	public List<RefSeq> toRefSeq(List<String> lines) {
		List<RefSeq> result = new ArrayList<>();
		for (String line : lines) {
			String[] columns = line.split(TAB);
			result.add(new RefSeq(Integer.valueOf(columns[1]), Integer.valueOf(columns[2]), columns[3]));
		}
		return result;
	}
}
