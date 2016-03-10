package com.bio.main.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.bio.main.pojo.Database;
import com.bio.main.pojo.BlastNRecord;

/**
 * Database utility class (Singleton) which is responsible for processing the output of BlastN records.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class DatabaseUtil {

	/**
	 * List of constants
	 */
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

	/**
	 * Populates the length property of {@link BlastNRecord} by finding it in the record's str.
	 * 
	 * @param record
	 */
	public void findQueryLength(BlastNRecord record) {
		String lengthStr = StringUtils.substringBetween(record.getStr().toString(), "Length=", System.getProperty("line.separator"));
		record.setLength(Integer.valueOf(lengthStr));
	}

	/**
	 * Populates the alignment lengths for the given record in its str.
	 * 
	 * @param record
	 */
	public void findAlignmentLengths(BlastNRecord record) {
		String[] alignmentLengths = StringUtils.substringsBetween(record.getStr().toString(), " Identities = ", " (");
		if (alignmentLengths != null) {
			List<Integer> alignments = new ArrayList<>();
			for (String alignmentLength : alignmentLengths) {
				alignments.add(Integer.valueOf(StringUtils.substringAfter(alignmentLength, "/")));
			}
			record.setAlignmentLengths(alignments);
		}
	}

	/**
	 * Checks to see if the record given is duplicate by running 90% <= alignment_length/query_length <= 110% validation against all the alignment
	 * records and as long as one of them fits into the logic, it is considered duplicate. If there is no alignment length, it will return false.
	 * 
	 * @param record
	 * @return
	 */
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

	/**
	 * Runs the main logic in which it finds the duplicate queries for the given BlastN Records.
	 * 
	 * @param db
	 */
	public void findDuplicateRecords(Database db) {
		for (BlastNRecord record : db.getBlastNRecords()) {
			findQueryLength(record);
			findAlignmentLengths(record);
			if (isRedundant(record)) {
				System.out.println("Redundant query: [" + record.getQueryString() + "]");
				db.addRedundantQuery(record.getQueryString());
			}
		}
		System.out.println("Number of Duplicate Records found: [" + db.getDuplicateQueries().size() + "]");
	}
}
