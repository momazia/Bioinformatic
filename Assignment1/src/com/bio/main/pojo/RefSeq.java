package com.bio.main.pojo;

import java.util.StringTokenizer;

/**
 * A POJO to keep RefSeq structure.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class RefSeq {

	private String chromosome;
	private Integer start;
	private Integer end;
	private String id;
	private Strand strand;

	/**
	 * Populates the fields in the POJO using the string given which is contains
	 * the data separated with tab.
	 * 
	 * @param line
	 */
	public RefSeq(String line) {
		StringTokenizer st = new StringTokenizer(line, "\t");

		this.chromosome = st.nextToken();
		this.start = Integer.valueOf(st.nextToken());
		this.end = Integer.valueOf(st.nextToken());
		this.id = st.nextToken();
		st.nextToken(); // Skipping one column
		this.strand = Strand.value(st.nextToken());
	}

	public String getChromosome() {
		return chromosome;
	}

	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Strand getStrand() {
		return strand;
	}

	public void setStrand(Strand strand) {
		this.strand = strand;
	}

}
