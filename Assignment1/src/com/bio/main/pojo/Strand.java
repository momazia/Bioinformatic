package com.bio.main.pojo;

/**
 * An enumeration to hold the possible values for Strand
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public enum Strand {

	POSITIVE("+"), NEGATIVE("-");

	private String value;

	Strand(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static Strand value(String str) {

		for (Strand strand : Strand.values()) {
			if (strand.value.equals(str)) {
				return strand;
			}
		}
		return null;
	}
}
