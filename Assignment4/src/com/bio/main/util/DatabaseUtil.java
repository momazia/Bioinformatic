package com.bio.main.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.bio.main.pojo.Database;
import com.bio.main.pojo.BlastNRecord;

public class DatabaseUtil {

	private static final int LOWER_BOUND_PERCENTAGE = 90;
	private static final int UPPER_BOUND_PERCENTAGE = 110;
	private static DatabaseUtil instance = null;

	private DatabaseUtil() {
		super();
	}

	public static DatabaseUtil getInstance() {
		if (instance == null) {
			instance = new DatabaseUtil();
		}
		return instance;
	}

	public void findQueryLength(BlastNRecord record) {
		String lengthStr = StringUtils.substringBetween(record.getStr().toString(), "Length=", System.getProperty("line.separator"));
		record.setLength(Integer.valueOf(lengthStr));
	}

	public void findFindAlignments(BlastNRecord record) {
		String[] alignmentLengths = StringUtils.substringsBetween(record.getStr().toString(), " Identities = ", " (");
		if (alignmentLengths != null) {
			List<Integer> alignments = new ArrayList<>();
			for (String alignmentLength : alignmentLengths) {
				alignments.add(Integer.valueOf(StringUtils.substringAfter(alignmentLength, "/")));
			}
			record.setAlignmentLengths(alignments);
		}
	}

	public boolean isRedundant(BlastNRecord record) {
		if (record.getAlignmentLengths() != null) {
			for (Integer alginmentLength : record.getAlignmentLengths()) {
				Double percentage = alginmentLength.doubleValue() / record.getLength().doubleValue() * 100;
				if (LOWER_BOUND_PERCENTAGE <= percentage && percentage <= UPPER_BOUND_PERCENTAGE) {
					return true;
				}
			}
		}
		return false;
	}

	public void findDuplicateRecords(Database db) {
		for (BlastNRecord record : db.getBlastNRecords()) {
			findQueryLength(record);
			findFindAlignments(record);
			if (isRedundant(record)) {
				System.out.println("Redundant query: [" + record.getQueryString() + "]");
				db.addRedundantQuery(record.getQueryString());
			}
		}
		System.out.println("Number of Duplicate Records found: [" + db.getDuplicateQueries().size() + "]");
	}
}
