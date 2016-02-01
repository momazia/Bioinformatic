package com.bio.main.pojo;

import java.util.List;

public class Gene {

	private RefSeq geneAnn;
	private List<RefSeq> exonAnns;
	private String str; // The actual data gene sequence

	public RefSeq getGeneAnn() {
		return geneAnn;
	}

	public void setGeneAnn(RefSeq geneAnn) {
		this.geneAnn = geneAnn;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public List<RefSeq> getExonAnns() {
		return exonAnns;
	}

	public void setExonAnns(List<RefSeq> exonAnns) {
		this.exonAnns = exonAnns;
	}
}