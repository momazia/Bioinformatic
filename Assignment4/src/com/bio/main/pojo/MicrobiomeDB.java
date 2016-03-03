package com.bio.main.pojo;

import java.util.List;
import java.util.Objects;

public class MicrobiomeDB {

	private List<Query> queries;
	private String header;

	public MicrobiomeDB(List<Query> queries, StringBuffer header) {
		this.queries = queries;
		this.header = Objects.toString(header);
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public List<Query> getQueries() {
		return queries;
	}

	public void setQueries(List<Query> queries) {
		this.queries = queries;
	}
}
