package com.bio.main.pojo;

/**
 * POJO to hold information about the queries read from the files.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class Query {

	private String name;
	private String str;

	public Query(String name, String str) {
		super();
		this.setName(name);
		this.setStr(str);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

}
