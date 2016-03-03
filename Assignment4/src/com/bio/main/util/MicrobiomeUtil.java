package com.bio.main.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.bio.main.pojo.Query;

public class MicrobiomeUtil {

	private static final int LOWER_BOUND_PERCENTAGE = 90;
	private static final int UPPER_BOUND_PERCENTAGE = 110;
	private static MicrobiomeUtil instance = null;

	private MicrobiomeUtil() {
		super();
	}

	public static MicrobiomeUtil getInstance() {
		if (instance == null) {
			instance = new MicrobiomeUtil();
		}
		return instance;
	}

	public void findQueryLength(Query query) {
		String lengthStr = StringUtils.substringBetween(query.getStr().toString(), "Length=", System.getProperty("line.separator"));
		query.setLength(Integer.valueOf(lengthStr));
	}

	public void findFindAlignments(Query query) {
		String[] alignmentLengths = StringUtils.substringsBetween(query.getStr().toString(), " Identities = ", " (");
		if (alignmentLengths != null) {
			List<Integer> alignments = new ArrayList<>();
			for (String alignmentLength : alignmentLengths) {
				alignments.add(Integer.valueOf(StringUtils.substringAfter(alignmentLength, "/")));
			}
			query.setAlignmentLengths(alignments);
		}
	}

	public boolean isQueryEligible(Query query) {
		if (query.getAlignmentLengths() != null) {
			for (Integer alginmentLength : query.getAlignmentLengths()) {
				Double percentage = alginmentLength.doubleValue() / query.getLength().doubleValue() * 100;
				if (LOWER_BOUND_PERCENTAGE <= percentage && percentage <= UPPER_BOUND_PERCENTAGE) {
					return true;
				}
			}
		}
		return false;
	}
}
