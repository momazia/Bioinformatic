package com.bio.main.pojo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Database {

	private List<BlastNRecord> blastNRecords;
	private Set<String> duplicateQueries;

	public boolean addRedundantQuery(String arg0) {
		if (duplicateQueries == null) {
			duplicateQueries = new HashSet<>();
		}
		return duplicateQueries.add(arg0);
	}

	public Database(List<BlastNRecord> blastNRecords) {
		this.setBlastNRecords(blastNRecords);
	}

	public Set<String> getDuplicateQueries() {
		return duplicateQueries;
	}

	public void setDuplicateQueries(Set<String> duplicateQueries) {
		this.duplicateQueries = duplicateQueries;
	}

	public List<BlastNRecord> getBlastNRecords() {
		return blastNRecords;
	}

	public void setBlastNRecords(List<BlastNRecord> blastNRecords) {
		this.blastNRecords = blastNRecords;
	}
}
